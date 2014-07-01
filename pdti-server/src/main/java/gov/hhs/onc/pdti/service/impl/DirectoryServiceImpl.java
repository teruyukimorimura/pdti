package gov.hhs.onc.pdti.service.impl;


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
import gov.hhs.onc.pdti.statistics.entity.PDTIStatisticsEntity;
import gov.hhs.onc.pdti.statistics.service.PdtiAuditLog;
import gov.hhs.onc.pdti.statistics.service.impl.PdtiAuditLogImpl;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.ErrorType;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

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
        PDTIStatisticsEntity entity = new PDTIStatisticsEntity();
        entity.setBaseDn(dirId);
        entity.setCreationDate(new Date());
        entity.setPdRequestType("BatchRequest");        
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
        entity.setStatus("Success");
        PdtiAuditLog pdtiAuditLogService = PdtiAuditLogImpl.getInstance();
        pdtiAuditLogService.save(entity);
        return batchResp;
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
