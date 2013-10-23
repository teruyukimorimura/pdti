package gov.hhs.onc.pdti.interceptor;


import gov.hhs.onc.pdti.data.DirectoryDescriptor;

public interface DirectoryResponseInterceptor<T, U> extends DirectoryInterceptor<T, U> {
    public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, T queryReq, U queryResp) throws DirectoryInterceptorException;
}
