package gov.hhs.onc.pdti.service.impl;

import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.messaging.saaj.packaging.mime.internet.MimeUtility;
import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.DirectoryType;
import gov.hhs.onc.pdti.DirectoryTypeId;
import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.data.federation.FederationService;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorNoOpException;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.Control;
import gov.hhs.onc.pdti.ws.api.DsmlMessage;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.ErrorType;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import ihe.FederatedRequestData;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.SortedSet;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

@DirectoryStandard(DirectoryStandardId.IHE)
@Scope("singleton")
@Service("dirService")
public class DirectoryServiceImpl extends AbstractDirectoryService<BatchRequest, BatchResponse> implements DirectoryService<BatchRequest, BatchResponse> {

    private final static Logger LOGGER = Logger.getLogger(DirectoryServiceImpl.class);

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    private ObjectFactory objectFactory;

    @Override
    public BatchResponse processRequest(BatchRequest batchReq) {
        String dirId = this.dirDesc.getDirectoryId(), reqId = DirectoryUtils.defaultRequestId(batchReq.getRequestId());
        BatchResponse batchResp = this.objectFactory.createBatchResponse();
        DirectoryInterceptorNoOpException noOpException = null;
        String federatedRequestId = getFederatedRequestId(batchReq);
        if (null != federatedRequestId && federatedRequestId.length() > 0) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Federation Enabled...");
            } else if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Federation Enabled...");
            }
        } else {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Federation Not Enabled...");
            } else if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Federation Not Enabled...");
            }
        }

        try {
            this.interceptRequests(dirDesc, dirId, reqId, batchReq, batchResp);
        } catch (DirectoryInterceptorNoOpException e) {
            noOpException = e;
        } catch (DirectoryInterceptorException e) {
            this.addError(dirId, reqId, batchResp, e);
        }

        try {
            String batchReqStr = this.dirJaxb2Marshaller.marshal(this.objectFactory.createBatchRequest(batchReq));

            if (noOpException != null) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Skipping processing of DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + "):\n" + batchReqStr,
                            noOpException);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Skipping processing of DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ").", noOpException);
                }
            } else {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Processing DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + "):\n" + batchReqStr);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Processing DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ").");
                }

                if (this.dataServices != null) {
                    for (DirectoryDataService<?> dataService : this.dataServices) {
                        try {
                            combineBatchResponses(batchResp, dataService.processData(batchReq));
                        } catch (Throwable th) {
                            this.addError(dirId, reqId, batchResp, th);
                        }
                    }
                }

                try {
                    combineBatchResponses(batchResp, this.fedService.federate(batchReq));
                } catch (Throwable th) {
                    this.addError(dirId, reqId, batchResp, th);
                }
            }
        } catch (XmlMappingException e) {
            this.addError(dirId, reqId, batchResp, e);
        }

        try {
            this.interceptResponses(dirDesc, dirId, reqId, batchReq, batchResp);
        } catch (DirectoryInterceptorException e) {
            this.addError(dirId, reqId, batchResp, e);
        }

        try {
            String batchRespStr = this.dirJaxb2Marshaller.marshal(this.objectFactory.createBatchResponse(batchResp));

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Processed DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ") into DSML batch response:\n" + batchRespStr);
            } else if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Processed DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ") into DSML batch response.");
            }
        } catch (XmlMappingException e) {
            this.addError(dirId, reqId, batchResp, e);
        }

        return batchResp;
    }

    /**
     *
     * @param batchReq
     * @return String
     */
    private String getFederatedRequestId(BatchRequest batchReq) {
        String federatedRequestId = "";
        if (null != batchReq && null != batchReq.getBatchRequests() && batchReq.getBatchRequests().size() > 0) {
            DsmlMessage dsml = batchReq.getBatchRequests().get(0);
            if (null != dsml && null != dsml.getControl() && dsml.getControl().size() > 0) {
                Control ctrl = dsml.getControl().get(0);
                byte[] data = (byte[]) dsml.getControl().get(0).getControlValue();
                FederatedRequestData fredReqData = (FederatedRequestData) decodeBase64(data);
                if (null != fredReqData && null != fredReqData.getFederatedRequestId()) {
                    federatedRequestId = fredReqData.getFederatedRequestId();
                }
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(ctrl.getType());
                    LOGGER.trace(federatedRequestId);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(ctrl.getType());
                    LOGGER.debug(federatedRequestId);
                }
            }
        }
        return federatedRequestId;
    }

    /**
     *
     * @param b
     * @return Object
     */
    private Object decodeBase64(byte[] b) {
        Object obj = null;
        ObjectInputStream in;
        if (null != b && b.length > 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(b);
            try {
                in = new ObjectInputStream(MimeUtility.decode(bis, "base64"));
                obj = in.readObject();
            } catch (Exception ex) {
                LOGGER.error(ex);
            }
        }
        return obj;
    }

    @Override
    protected void addError(String dirId, String reqId, BatchResponse batchResp, Throwable th) {
        // TODO: improve error handling
        batchResp.getBatchResponses().add(this.objectFactory.createBatchResponseErrorResponse(this.errBuilder.buildErrorResponse(reqId, ErrorType.OTHER, th)));
    }

    private static void combineBatchResponses(BatchResponse batchResp, List<BatchResponse> batchRespCombine) {
        for (BatchResponse batchRespCombineItem : batchRespCombine) {
            batchResp.getBatchResponses().addAll(batchRespCombineItem.getBatchResponses());
        }
    }

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    @DirectoryType(DirectoryTypeId.MAIN)
    @Override
    protected void setDirectoryDescriptor(DirectoryDescriptor dirDesc) {
        this.dirDesc = dirDesc;
    }

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    @Override
    protected void setFederationService(FederationService<BatchRequest, BatchResponse> fedService) {
        this.fedService = fedService;
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.IHE)
    @Override
    protected void setRequestInterceptors(SortedSet<DirectoryRequestInterceptor<BatchRequest, BatchResponse>> reqInterceptors) {
        this.reqInterceptors = reqInterceptors;
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.IHE)
    @Override
    protected void setResponseInterceptors(SortedSet<DirectoryResponseInterceptor<BatchRequest, BatchResponse>> respInterceptors) {
        this.respInterceptors = respInterceptors;
    }
}
