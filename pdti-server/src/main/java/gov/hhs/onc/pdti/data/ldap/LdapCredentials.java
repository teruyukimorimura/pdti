package gov.hhs.onc.pdti.data.ldap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("ldapCredentials")
@Scope("prototype")
public class LdapCredentials {
    private Dn dn;
    private String password;

    public boolean isAnonymous() {
        return !this.hasDn() || !this.hasPassword();
    }

    public String getDnString() {
        return (this.dn != null) ? this.dn.getName() : null;
    }

    public void setDnString(String dnStr) throws LdapInvalidDnException {
		if (StringUtils.isNotBlank(dnStr)) {
			this.dn = new Dn(dnStr);
		}
    }

    @Override
    public String toString() {
        StrBuilder strBuilder = new StrBuilder();

        if (this.hasDn()) {
            strBuilder.append("dn=");
            strBuilder.append(this.getDnString());
        }

        return strBuilder.toString();
    }

    public boolean hasDn() {
        return this.dn != null;
    }

    public Dn getDn() {
        return this.dn;
    }

    public void setDn(Dn dn) {
        this.dn = dn;
    }

    public boolean hasPassword() {
        return !StringUtils.isBlank(this.password);
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
