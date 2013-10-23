package gov.hhs.onc.pdti.interceptor;

public class DirectoryInterceptorNoOpException extends DirectoryInterceptorException {
    public DirectoryInterceptorNoOpException() {
        super();
    }

    public DirectoryInterceptorNoOpException(String str) {
        super(str);
    }

    public DirectoryInterceptorNoOpException(String str, Throwable throwable) {
        super(str, throwable);
    }

    public DirectoryInterceptorNoOpException(Throwable throwable) {
        super(throwable);
    }
}
