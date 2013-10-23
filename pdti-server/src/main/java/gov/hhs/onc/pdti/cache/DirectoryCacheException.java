package gov.hhs.onc.pdti.cache;


import gov.hhs.onc.pdti.DirectoryException;

public class DirectoryCacheException extends DirectoryException {
    public DirectoryCacheException() {
        super();
    }

    public DirectoryCacheException(Throwable cause) {
        super(cause);
    }

    public DirectoryCacheException(String msg) {
        super(msg);
    }

    public DirectoryCacheException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
