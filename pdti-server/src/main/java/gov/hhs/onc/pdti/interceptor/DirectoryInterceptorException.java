package gov.hhs.onc.pdti.interceptor;

import gov.hhs.onc.pdti.DirectoryException;

public class DirectoryInterceptorException extends DirectoryException {
    public DirectoryInterceptorException() {
        super();
    }

    public DirectoryInterceptorException(String str) {
        super(str);
    }

    public DirectoryInterceptorException(String str, Throwable throwable) {
        super(str, throwable);
    }

    public DirectoryInterceptorException(Throwable throwable) {
        super(throwable);
    }
}
