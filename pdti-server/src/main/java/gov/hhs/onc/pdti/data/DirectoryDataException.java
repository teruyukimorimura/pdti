package gov.hhs.onc.pdti.data;

import gov.hhs.onc.pdti.DirectoryException;

public class DirectoryDataException extends DirectoryException {
    public DirectoryDataException() {
        super();
    }

    public DirectoryDataException(String str) {
        super(str);
    }

    public DirectoryDataException(String str, Throwable throwable) {
        super(str, throwable);
    }

    public DirectoryDataException(Throwable throwable) {
        super(throwable);
    }
}
