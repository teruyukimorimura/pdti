package gov.hhs.onc.pdti.cache;

public class DirectoryCacheRequestIdException extends DirectoryCacheException {
    public DirectoryCacheRequestIdException() {
        super();
    }

    public DirectoryCacheRequestIdException(Throwable cause) {
        super(cause);
    }

    public DirectoryCacheRequestIdException(String msg) {
        super(msg);
    }

    public DirectoryCacheRequestIdException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
