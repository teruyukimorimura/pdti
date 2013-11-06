PDTI - Provider Directory Test Implementation
=============================================

Last Updated October 23, 2013 - by Alan Viars @aviars

PDTI is a collection of Java applications designed to test "Provider Directories" using multiple flavors of Provider Direcotry specifications including ModSpec, HPD Plus, and IHE. In a nutshell, the provider directy exists in LDAP, but it is wrapped in a SOAP service.  There is a seperate WSDL for each specification flavor.

This code was developed by ESAC, Inc for Office of the National Coordinator of Health IT (ONC).  Its maintainence is now the task of Adacious Inquiry (AI).


The following set of instructions serve as installation notes supplementing the complilation directions here http://modularspecs.siframework.org/Provider+Directories+Installation+Guide.  This is still a work in progress and should be considered a rough draft.


Overview
========

You need to have a few services running, including tomcat-7, and LDAP server.  You will also need to install Apche Directory Studio to start ApacheDS, the LDAP server. You could theoreticly use another LDAP server, but these instructions are based on what the development team used.  To my knowledge this software has only been tested with ApacheDS.  You will also need soap-ui to run the soap tests.



Dependencies
===========

    Name                      Minimum Version
    Mercurial client          2.2
    Java JDK + JRE            7 (Oracle or OpenJDK)
    Apache Maven              3.04
    Apache Tomcat             7.0
    Apache Directory Studio   1.5.x
    ApacheDS				  2.0


Server Configuration
====================

This outlines how to install all needed components on Ubuntu 12.04 LTS 64-bit.

Install prerequisites:

    sudo apt-get install maven tomcat7 mercurial

Setup Tomcat 7
==============

Now that tomcat is installed and running, Lets change this so it is using OpenJRE-7 instead o OpenJRE7

    sudo apt-get -y install openjdk-7-jre
    sudo update-java-alternatives -s java-1.7.0-openjdk-amd64
    unlink /usr/lib/jvm/default-java
    ln -s /usr/lib/jvm/java-1.7.0-openjdk-amd64 /usr/lib/jvm/default-java
    service tomcat7 restart

The previous command  will make Java 7 the default system-wide.


Now let's setup an admin user in Tomcat7.

    sudo nano /var/lib/tomcat7/conf/tomcat-users.xml

Add the following uncommentend text within the "tomcat-users" tags.

    <role rolename="manager"/>
    <role rolename="admin"/>
    <user username="admin" password="admin" roles="admin,manager"/>


Restart Tomcat

    sudo /etc/init.d/tomcat7 restart

Edit the maven settings file

    sudo nano /etc/maven/settings.xml


Add the following code in /etc/maven/settings.xml somewhere between the "profile" tags.

    <profile>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <id>tomcat</id>
            <properties>
                <tomcat.manager.url>http://localhost:8080/manager/text</tomcat.manager.url>
                <tomcat.manager.username>manager</tomcat.manager.username>
                <tomcat.manager.password>managerpass</tomcat.manager.password>
                <tomcat.manager.2.url>http://localhost:8080/manager/text</tomcat.manager.2.url>
                <tomcat.manager.2.username>manager</tomcat.manager.2.username>
                <tomcat.manager.2.password>managerpass</tomcat.manager.2.password>
            </properties>
    </profile>

Download the Source Code & Compile
==================================

Fetch the software from GitHub

    git clone https://github.com/siteadmin/pdti.git
    cd pdti
    mvn -Dtomcat.manager.password=admin clean install tomcat7:deploy

This step take approximatley 10 minutes.

Setup LDAP with ApacheDS or Apache Directory Studio
====================================================


ApacheDirectory Studio contains an embedded version of ApacheDS, so you only need one or the 
other. attempting to run both can result in port conflicts.  You may find that Apache directory Studio is easier in development.

ApacheDS
--------


Install ApacheDS from http://directory.apache.org/apacheds/

    wget http://apache.osuosl.org//directory/apacheds/dist/2.0.0-M15/apacheds-2.0.0-M15-amd64.deb
    sudo dpkg -i apacheds-2.0.0-M15-amd64.deb


Run ApacheDS

    sudo /etc/init.d/apacheds-2.0.0-M15-default start

Check the status of ApacheDS

    sudo /etc/init.d/apacheds-2.0.0-M15-default status

You should see a message that ApcheDS is indeed running.

Apache Directory Studio
-----------------------


Install Apache Directory Studio

    wget http://download.nextag.com/apache//directory/studio/dist/2.0.0.v20130628/ApacheDirectoryStudio-linux-x86_64-2.0.0.v20130628.tar.gz
    tar zxvf ApacheDirectoryStudio-linux-x86_64-2.0.0.v20130628.tar.gz

After this file is decompressed with tar then you can start the executable

    ./ApacheDirectoryStudio-linux-x86_64-2.0.0.v20130628/ApacheDirectoryStudio &

You now need to create a server. Here is a quick video overview that introduces how to do that.

http://www.youtube.com/watch?v=xJr1hJVo2So


Create a partition with the suffix o=dev.provider-directories.com,dc=hpd. In Apache directory Studio, open up the server's configuration, click on the partition tab.  You should make yours look like this. <img src="http://oncsiteadmin.s3.amazonaws.com/partitions.png">. Changee "example.com" to "provider-directories.com".  To make things easi





After creating the partition, start the server, by right clicking the icon in the bottom left corner and click "Run". After it starts, right click again and click "Create a Connection". this will create a connection to the LDAP server you just created. In the top left part of the screen you will see the connection and a globe icon that says "o=dev.provider-directories.com,dc=hpd". Right click on this and select "Import" and then "Import LDIF" files to get to the LDIF Import dialoge.  Be sure to check the "Update existing entries" and "Continue on error" as shown below.
<img src="http://oncsiteadmin.s3.amazonaws.com/LDIFimport.png">


import the LDAP schemas as LDIF files in this order:

* pdti/pdti-ldap/src/main/resources/META-INF/ldap/schema/hc.ldif
* pdti/pdti-ldap/src/main/resources/META-INF/ldap/schema/pkcs9.ldif
* pdti/pdti-ldap/src/main/resources/META-INF/ldap/schema/hpd_plus.ldif
* pdti/pdti-ldap/src/test/resources/META-INF/ldap/data/hpd_plus_test_data_1.ldif


SOAP-UI Tests
-------------

The soap-ui project files can be found in  `pdti/pdti-server/src/test/resources/`.The project `soapui-project_hpdplus.xml` covers the Mod Specs WSDL-based on ModSpec (a.k.a. HPD Plus) and the IHE version is found in `soapui-project.xml`.



Websites of Importance:
-----------------------

Tha main SITE site is  http://sitenv.org.  It is anticiated that the functionailty contained in PDTI will be incorprated into this site.


This a a working version of the PDTI client and server.: http://prod.provider-directories.com/pdti-client/


Confluence: http://confluence.siframework.org


JIRA: http://jira.oncprojectracking.org




