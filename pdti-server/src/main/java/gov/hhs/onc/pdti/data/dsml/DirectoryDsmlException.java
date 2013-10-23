package gov.hhs.onc.pdti.data.dsml;

import gov.hhs.onc.pdti.data.DirectoryDataException;

public class DirectoryDsmlException extends DirectoryDataException {
    public DirectoryDsmlException() {
        super();
    }

    public DirectoryDsmlException(String str) {
        super(str);
    }

    public DirectoryDsmlException(String str, Throwable throwable) {
        super(str, throwable);
    }

    public DirectoryDsmlException(Throwable throwable) {
        super(throwable);
    }
}
