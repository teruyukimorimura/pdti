package gov.hhs.onc.pdti.interceptor.impl;


import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorNoOpException;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.DsmlMessage;
import gov.hhs.onc.pdti.ws.api.SearchRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("policyInterceptor")
@DirectoryStandard(DirectoryStandardId.IHE)
@Order(201)
@Scope("singleton")
public class PolicyInterceptorImpl extends AbstractDirectoryInterceptor<BatchRequest, BatchResponse> implements
        DirectoryRequestInterceptor<BatchRequest, BatchResponse> {
    private final static Class<? extends DsmlMessage>[] VALID_REQ_MSG_CLASSES = ArrayUtils.toArray(SearchRequest.class);

    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq, BatchResponse batchResp)
            throws DirectoryInterceptorException {
        Class<? extends DsmlMessage> batchReqMsgClass;

        for (DsmlMessage batchReqMsg : batchReq.getBatchRequests()) {
            batchReqMsgClass = batchReqMsg.getClass();

			boolean valid = false;
			for (Class<?> validReqMsgClass : VALID_REQ_MSG_CLASSES) {
				if (ClassUtils.isAssignable(batchReqMsgClass, validReqMsgClass)) {
					valid = true;
					break;
				}
			}
			if (!valid) {
                throw new DirectoryInterceptorNoOpException("Invalid DSML batch request message (directoryId=" + dirDesc.getDirectoryId() + ", requestId="
                        + reqId + ", class=" + batchReqMsgClass.getName() + ").");
            }
        }
    }
}
