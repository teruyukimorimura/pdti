package gov.hhs.onc.pdti.interceptor.impl;


import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.DirectoryType;
import gov.hhs.onc.pdti.DirectoryTypeId;
import gov.hhs.onc.pdti.cache.DirectoryCacheRequestIdException;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("hpdPlusCacheInterceptor")
@DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
@DirectoryType(DirectoryTypeId.MAIN)
@Order(200)
@Scope("singleton")
public class HpdPlusCacheInterceptorImpl extends AbstractCacheInterceptor<HpdPlusRequest, HpdPlusResponse> {
    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq, HpdPlusResponse hpdPlusResp)
            throws DirectoryInterceptorException {
        this.cacheRequest(reqId, hpdPlusReq, hpdPlusResp);
    }

    @Override
    protected void addRequestIdError(String reqId, HpdPlusRequest hpdPlusReq, HpdPlusResponse hpdPlusResp, DirectoryCacheRequestIdException reqIdException) {
        hpdPlusResp.getErrors().add(this.dirErrBuilder.buildError(hpdPlusReq.getDirectoryId(), reqId, HpdPlusErrorType.DUPLICATE_REQUEST_ID, reqIdException));
    }
}
