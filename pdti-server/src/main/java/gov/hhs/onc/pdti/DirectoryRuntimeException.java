package gov.hhs.onc.pdti;

public class DirectoryRuntimeException extends RuntimeException {
    public DirectoryRuntimeException() {
        super();
    }

    public DirectoryRuntimeException(Throwable cause) {
        super(cause);
    }

    public DirectoryRuntimeException(String msg) {
        super(msg);
    }

    public DirectoryRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
