package gov.hhs.onc.pdti.data.ldap;

import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("ldapSslProtocol")
@Scope("prototype")
public enum LdapSslProtocol {
    NONE("none"), LDAPS("LDAPS", "SSL"), STARTTLS("StartTLS", "TLS");

    private String name;
    private String protocol;

    LdapSslProtocol(String name) {
        this(name, null);
    }

    LdapSslProtocol(String name, String protocol) {
        this.name = name;
        this.protocol = protocol;
    }

    public boolean isSsl() {
        return this != NONE;
    }

    @Override
    public String toString() {
        StrBuilder strBuilder = new StrBuilder();
        strBuilder.append("name=");
        strBuilder.append(this.name);

        if (this.protocol != null) {
            strBuilder.appendSeparator(",");
            strBuilder.append("protocol=");
            strBuilder.append(this.protocol);
        }

        return strBuilder.toString();
    }

    public String getName() {
        return this.name;
    }

    public String getProtocol() {
        return this.protocol;
    }
}
