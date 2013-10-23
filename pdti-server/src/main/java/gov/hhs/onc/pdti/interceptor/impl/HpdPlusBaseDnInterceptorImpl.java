package gov.hhs.onc.pdti.interceptor.impl;


import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("hpdPlusBaseDnInterceptor")
@DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
@Order(101)
@Scope("singleton")
public class HpdPlusBaseDnInterceptorImpl extends AbstractDirectoryInterceptor<HpdPlusRequest, HpdPlusResponse> implements
        DirectoryRequestInterceptor<HpdPlusRequest, HpdPlusResponse> {
    @Autowired
    private BaseDnInterceptorImpl baseDnInterceptor;

    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq, HpdPlusResponse hpdPlusResp)
            throws DirectoryInterceptorException {
        this.baseDnInterceptor.interceptRequest(dirDesc, reqId, hpdPlusReq.getBatchRequest(), null);
    }
}
