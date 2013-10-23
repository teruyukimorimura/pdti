PDTI - Provider Directory Test Implementation
=============================================

Last Updated October 23, 2013 - by Alan Viars @aviars

PDTI is a collection of Java applications designed to test "Provider Directories" using multiple flavors of Provider Direcotry specifications including ModSpec, HPD Plus, and IHE. In a nutshell, the provider directy exists in LDAP, but it is wrapped in a SOAP service.  There is a seperate WSDL for each specification flavor.

This code was developed by ESAC, Inc for Office of the National Coordinator of Health IT (ONC).  Its maintainence is now the task of Adacious Inquiry (AI).


The following set of instructions serve as installation notes supplementing the complilation directions here http://modularspecs.siframework.org/Provider+Directories+Installation+Guide.  This is still a work in progress and should be considered a rough draft.


Overview
========

You need to have a few services running, including tomcat-7, and LDAP server.  You will also need to install Apche Directory Studio to start ApacheDS, the LDAP server. You could theoreticly use another LDAP server, but these instructions are based on what the development team used.  To my knowledge this software has only been tested with ApacheDS.  You will also need soap-ui to run the soap tests.  The soap-ui project files can be found in  `pdti/pdti-server/src/test/resources/`.The project `soapui-project_hpdplus.xml` covers the Mod Specs WSDL-based on ModSpec (a.k.a. HPD Plus) and the IHE version is found in `soapui-project.xml`.



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

Setup LDAP with ApacheDS and Apache Directory Studio
====================================================


Install ApacheDS from http://directory.apache.org/apacheds/

    wget http://apache.osuosl.org//directory/apacheds/dist/2.0.0-M15/apacheds-2.0.0-M15-amd64.deb
    sudo dpkg -i apacheds-2.0.0-M15-amd64.deb


Run ApacheDS

    sudo /etc/init.d/apacheds-2.0.0-M15-default start

Check the status of ApacheDS

    sudo /etc/init.d/apacheds-2.0.0-M15-default status

You should see a message that ApcheDS is indeed running.



Install Apache Directory Studio

    wget http://download.nextag.com/apache//directory/studio/dist/2.0.0.v20130628/ApacheDirectoryStudio-linux-x86_64-2.0.0.v20130628.tar.gz
    tar zxvf ApacheDirectoryStudio-linux-x86_64-2.0.0.v20130628.tar.gz

After this file is decompressed with tar then you can start the executable

    ./ApacheDirectoryStudio-linux-x86_64-2.0.0.v20130628/ApacheDirectoryStudio &

You now need to create a server. Here is a quick video overview that introduces how to do that.  

<iframe width="560" height="315" src="//www.youtube.com/embed/xJr1hJVo2So" frameborder="0" allowfullscreen></iframe>


Be sure and follow the steps outlined in http://modularspecs.siframework.org/Provider+Directories+Installation+Guide to import data into your LDAP server.


Download Source, Build, and Deploy
----------------------------------

Clone the GitHub Repository

    git clone https://github.com/meaningfuluse/pdti


Build from source code and deploy on Tomcat7.

In the next step, we are assuming the Tomcat manager password is "admin".

	cd pdti
	mvn -Dtomcat.manager.password=<Tomcat manager password> clean install tomcat7:deploy

This will build and deploy the software on localhost. This step may take some time to complete.  It took about 15 minutes on an Tntel i5 dual core processor (circa 2011).


Websites of Importance:
-----------------------

Tha main SITE site is  http://sitenv.org.  It is anticiated that the functionailty contained in PDTI will be incorprated into this site.


This a a working version of the PDTI client and server.: http://prod.provider-directories.com/pdti-client/


Confluence: http://confluence.siframework.org


JIRA: http://jira.oncprojectracking.org




