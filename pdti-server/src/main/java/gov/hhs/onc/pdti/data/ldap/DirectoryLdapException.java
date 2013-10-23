package gov.hhs.onc.pdti.data.ldap;

import gov.hhs.onc.pdti.data.DirectoryDataException;

public class DirectoryLdapException extends DirectoryDataException {
    public DirectoryLdapException() {
        super();
    }

    public DirectoryLdapException(String str) {
        super(str);
    }

    public DirectoryLdapException(String str, Throwable throwable) {
        super(str, throwable);
    }

    public DirectoryLdapException(Throwable throwable) {
        super(throwable);
    }
}
