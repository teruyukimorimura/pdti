package gov.hhs.onc.pdti.data.ldap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("ldapServerType")
@Scope("prototype")
public enum LdapServerType {
    APACHEDS("apacheds"), OTHER("other");

    private String type;

    LdapServerType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public String getType() {
        return this.type;
    }
}
