package gov.hhs.onc.pdti.interceptor;


import gov.hhs.onc.pdti.data.DirectoryDescriptor;

public interface DirectoryRequestInterceptor<T, U> extends DirectoryInterceptor<T, U> {
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, T queryReq, U queryResp) throws DirectoryInterceptorException;
}
