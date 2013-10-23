package gov.hhs.onc.pdti.interceptor.impl;


import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusError;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("hpdPlusIdInterceptor")
@DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
@Order(100)
@Scope("singleton")
public class HpdPlusIdInterceptorImpl extends AbstractDirectoryInterceptor<HpdPlusRequest, HpdPlusResponse> implements
        DirectoryRequestInterceptor<HpdPlusRequest, HpdPlusResponse>, DirectoryResponseInterceptor<HpdPlusRequest, HpdPlusResponse> {
    @Autowired
    private IdInterceptorImpl idInterceptor;

    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq, HpdPlusResponse hpdPlusResp)
            throws DirectoryInterceptorException {
        String dirId = dirDesc.getDirectoryId();

        hpdPlusReq.setDirectoryId(dirId);

        this.idInterceptor.interceptRequest(dirDesc, reqId, hpdPlusReq.getBatchRequest(), null);
    }

    @Override
    public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq, HpdPlusResponse hpdPlusResp)
            throws DirectoryInterceptorException {
        String dirId = dirDesc.getDirectoryId();

        hpdPlusResp.setDirectoryId(dirId);
        hpdPlusResp.setRequestId(reqId);

        if (hpdPlusResp.isSetErrors()) {
            for (HpdPlusError hpdPlusErr : hpdPlusResp.getErrors()) {
                hpdPlusErr.setDirectoryId(dirId);
                hpdPlusErr.setRequestId(reqId);
            }
        }

        if (hpdPlusResp.isSetResponseItems()) {
            List<Object> respItems = hpdPlusResp.getResponseItems();

            for (BatchResponse batchResp : (Collection<BatchResponse>) CollectionUtils.select(respItems,
                    PredicateUtils.instanceofPredicate(BatchResponse.class))) {
                this.idInterceptor.interceptResponse(dirDesc, reqId, hpdPlusReq.getBatchRequest(), batchResp);
            }
        }
    }
}
