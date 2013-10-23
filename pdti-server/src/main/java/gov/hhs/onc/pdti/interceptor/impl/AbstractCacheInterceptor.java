package gov.hhs.onc.pdti.interceptor.impl;


import gov.hhs.onc.pdti.cache.DirectoryCache;
import gov.hhs.onc.pdti.cache.DirectoryCacheException;
import gov.hhs.onc.pdti.cache.DirectoryCacheRequestIdException;
import gov.hhs.onc.pdti.error.DirectoryErrorBuilder;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorNoOpException;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCacheInterceptor<T, U> extends AbstractDirectoryInterceptor<T, U> implements DirectoryRequestInterceptor<T, U> {
    @Autowired
    protected DirectoryErrorBuilder dirErrBuilder;

    @Autowired
    protected DirectoryCache dirCache;

    protected void cacheRequest(String reqId, T queryReq, U queryResp) throws DirectoryInterceptorException {
        try {
            this.dirCache.registerRequest(reqId);
        } catch (DirectoryCacheRequestIdException e) {
            this.addRequestIdError(reqId, queryReq, queryResp, e);

            throw new DirectoryInterceptorNoOpException("Unable to cache duplicate directory request (requestId=" + reqId + ").", e);
        } catch (DirectoryCacheException e) {
            throw new DirectoryInterceptorException("Unable to cache directory request (requestId=" + reqId + ").", e);
        }
    }

    protected abstract void addRequestIdError(String reqId, T queryReq, U queryResp, DirectoryCacheRequestIdException reqIdException);
}
