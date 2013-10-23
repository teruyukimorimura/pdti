package gov.hhs.onc.pdti.data.ldap.impl;

import gov.hhs.onc.pdti.data.ldap.LdapCredentials;
import gov.hhs.onc.pdti.data.ldap.LdapDataSource;
import gov.hhs.onc.pdti.data.ldap.LdapServerType;
import gov.hhs.onc.pdti.data.ldap.LdapSslProtocol;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public class LdapDataSourceImpl implements LdapDataSource {
    private boolean enabled = true;
    private LdapServerType type = LdapServerType.OTHER;
    private String host;
    private int port;
    private LdapCredentials credentials;
    private LdapSslProtocol ssl;

    @Override
    public LdapConnectionConfig toConfig() {
        LdapConnectionConfig ldapConnConfig = new LdapConnectionConfig();
        ldapConnConfig.setLdapHost(this.getHost());
        ldapConnConfig.setLdapPort(this.getPort());

        LdapSslProtocol ssl = this.getSsl();

        ldapConnConfig.setUseSsl(ssl.isSsl());
        ldapConnConfig.setSslProtocol(ssl.getProtocol());

        LdapCredentials credentials = this.getCredentials();

        ldapConnConfig.setName(credentials.getDnString());
        ldapConnConfig.setCredentials(credentials.getPassword());

        return ldapConnConfig;
    }

    @Override
    public String toString() {
        StrBuilder builder = new StrBuilder();
        builder.appendWithSeparators(
                ArrayUtils.toArray("host=" + this.getHost(), "port=" + this.getPort(), "ssl={" + this.getSsl() + "}", "credentials={" + this.getCredentials()
                        + "}"), ",");

        return builder.toString();
    }

    @Override
    public LdapCredentials getCredentials() {
        return (this.credentials = ObjectUtils.defaultIfNull(this.credentials, new LdapCredentials()));
    }

    @Override
    public void setCredentials(LdapCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getHost() {
        return StringUtils.defaultIfBlank(this.host, LdapConnectionConfig.DEFAULT_LDAP_HOST);
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return (this.port > 0) ? this.port : (this.getSsl().isSsl() ? LdapConnectionConfig.DEFAULT_LDAPS_PORT : LdapConnectionConfig.DEFAULT_LDAP_PORT);
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public LdapServerType getType() {
        return this.type;
    }

    @Override
    public void setType(LdapServerType type) {
        this.type = type;
    }

    @Override
    public LdapSslProtocol getSsl() {
        return (this.ssl = ObjectUtils.defaultIfNull(this.ssl, LdapSslProtocol.NONE));
    }

    @Override
    public void setSsl(LdapSslProtocol ssl) {
        this.ssl = ssl;
    }
}
