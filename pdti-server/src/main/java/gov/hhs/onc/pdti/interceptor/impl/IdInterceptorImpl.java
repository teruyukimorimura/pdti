package gov.hhs.onc.pdti.interceptor.impl;


import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("idInterceptor")
@DirectoryStandard(DirectoryStandardId.IHE)
@Order(100)
@Scope("singleton")
public class IdInterceptorImpl extends AbstractDirectoryInterceptor<BatchRequest, BatchResponse> implements
        DirectoryRequestInterceptor<BatchRequest, BatchResponse>, DirectoryResponseInterceptor<BatchRequest, BatchResponse> {
    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq, BatchResponse batchResp)
            throws DirectoryInterceptorException {
        DirectoryUtils.setRequestId(batchReq, reqId);
    }

    @Override
    public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq, BatchResponse batchResp)
            throws DirectoryInterceptorException {
        DirectoryUtils.setRequestId(batchResp, reqId);
    }
}
