package gov.hhs.onc.pdti.interceptor.impl;


import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.DirectoryType;
import gov.hhs.onc.pdti.DirectoryTypeId;
import gov.hhs.onc.pdti.cache.DirectoryCacheRequestIdException;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.ErrorType;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("cacheInterceptor")
@DirectoryStandard(DirectoryStandardId.IHE)
@DirectoryType(DirectoryTypeId.MAIN)
@Order(200)
@Scope("singleton")
public class CacheInterceptorImpl extends AbstractCacheInterceptor<BatchRequest, BatchResponse> {
    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    private ObjectFactory objectFactory;

    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq, BatchResponse batchResp)
            throws DirectoryInterceptorException {
        this.cacheRequest(reqId, batchReq, batchResp);
    }

    @Override
    protected void addRequestIdError(String reqId, BatchRequest batchReq, BatchResponse batchResp, DirectoryCacheRequestIdException reqIdException) {
        batchResp.getBatchResponses().add(
                this.objectFactory.createBatchResponseErrorResponse(this.dirErrBuilder.buildErrorResponse(reqId, ErrorType.OTHER, reqIdException)));
    }
}
