package gov.hhs.onc.pdti.data.ldap;

import gov.hhs.onc.pdti.data.DirectoryDataSource;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public interface LdapDataSource extends DirectoryDataSource {
    public LdapConnectionConfig toConfig();

    public LdapServerType getType();

    public void setType(LdapServerType type);

    public String getHost();

    public void setHost(String host);

    public int getPort();

    public void setPort(int port);

    public LdapSslProtocol getSsl();

    public void setSsl(LdapSslProtocol ssl);

    public LdapCredentials getCredentials();

    public void setCredentials(LdapCredentials credentials);
}
