package gov.hhs.onc.pdti;

public class DirectoryException extends Exception {
    public DirectoryException() {
        super();
    }

    public DirectoryException(String msg) {
        super(msg);
    }

    public DirectoryException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DirectoryException(Throwable cause) {
        super(cause);
    }
}
