package gov.hhs.onc.pdti.interceptor.impl;


import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("hpdPlusUriInterceptor")
@DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
@Order(102)
@Scope("singleton")
public class HpdPlusUriInterceptorImpl extends AbstractDirectoryInterceptor<HpdPlusRequest, HpdPlusResponse> implements
        DirectoryResponseInterceptor<HpdPlusRequest, HpdPlusResponse> {
    @Override
    public void interceptResponse(DirectoryDescriptor dirDesc, String reqId, HpdPlusRequest hpdPlusReq, HpdPlusResponse hpdPlusResp)
            throws DirectoryInterceptorException {
        if (dirDesc.isShouldExposeDirectoryUri()) {
            hpdPlusResp.setDirectoryUri(dirDesc.getWsdlLocation().toExternalForm());
        } else {
            removeDirectoryUris(hpdPlusResp);
        }
    }

    private void removeDirectoryUris(HpdPlusResponse hpdPlusResp) {
        hpdPlusResp.setDirectoryUri(null);
        List<Object> responseItems = hpdPlusResp.getResponseItems();
        for (Object object : responseItems) {
            if (object instanceof HpdPlusResponse) {
                removeDirectoryUris((HpdPlusResponse) object);
            }
        }
    }
}
