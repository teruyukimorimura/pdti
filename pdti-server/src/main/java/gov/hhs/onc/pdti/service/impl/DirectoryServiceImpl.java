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
import gov.hhs.onc.pdti.jaxb.FederationJaxb2Marshaller;
import gov.hhs.onc.pdti.server.xml.FederatedRequestData;
import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.statistics.entity.PDTIStatisticsEntity;
import gov.hhs.onc.pdti.statistics.service.PdtiAuditLog;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.Control;
import gov.hhs.onc.pdti.ws.api.DsmlMessage;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.ErrorType;
import gov.hhs.onc.pdti.ws.api.LDAPResult;
import gov.hhs.onc.pdti.ws.api.LDAPResultCode;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import gov.hhs.onc.pdti.ws.api.SearchResponse;
import gov.hhs.onc.pdti.server.xml.FederatedResponseStatus;
import gov.hhs.onc.pdti.server.xml.FederatedSearchResponseData;
import gov.hhs.onc.pdti.server.xml.SearchResultEntryMetadata;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import gov.hhs.onc.pdti.ws.api.SearchResultEntry;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;

import javax.xml.bind.JAXBElement;

@DirectoryStandard(DirectoryStandardId.IHE)
@Scope("singleton")
@Service("dirService")
public class DirectoryServiceImpl extends AbstractDirectoryService<BatchRequest, BatchResponse> implements DirectoryService<BatchRequest, BatchResponse> {

    private final static Logger LOGGER = Logger.getLogger(DirectoryServiceImpl.class);

	@Value("${ihefederationoid}")
	String iheoid;

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    private ObjectFactory objectFactory;
    private static String dirStaticId = "";

	@Autowired
	PdtiAuditLog pdtiAuditLogService;

	@Autowired
	private FederationJaxb2Marshaller federationJaxb2Marshaller;

	@Override
    public BatchResponse processRequest(BatchRequest batchReq) {
        DirectoryInterceptorNoOpException noOpException = null;
        boolean isError = false;
        String dirId = this.dirDesc.getDirectoryId();
        dirStaticId = dirId;
        String reqId = DirectoryUtils.defaultRequestId(batchReq.getRequestId());
        BatchResponse batchResp = this.objectFactory.createBatchResponse();
        PDTIStatisticsEntity entity = new PDTIStatisticsEntity();
        entity.setBaseDn(dirId);
        entity.setCreationDate(new Date());
        entity.setPdRequestType("BatchRequest");
        String batchReqStr = null;
        try {
            try {
                this.interceptRequests(dirDesc, dirId, reqId, batchReq, batchResp);
				if (LOGGER.isTraceEnabled()) {
					batchReqStr = this.dirJaxb2Marshaller.marshal(this.objectFactory.createBatchRequest(batchReq));
				}
            } catch (DirectoryInterceptorNoOpException e) {
                noOpException = e;
            } catch (DirectoryInterceptorException e) {
                isError = true;
                this.addError(dirId, reqId, batchResp, e);
            } catch (Throwable th) {
                isError = true;
                this.addError(dirId, reqId, batchResp, th);
            } finally {
            }

            if (noOpException != null) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Skipping processing of DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + "):\n" + batchReqStr,
                            noOpException);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Skipping processing of DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ").", noOpException);
                }
            } else if (!isError) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Processing DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + "):\n" + batchReqStr);
                } else if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Processing DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ").");
                }
                //If Federation is enabled, then a local ldap call and Federation both should happen.. 
                //In the else part only local Directory will be searched.
                if (isFederatedRequest(batchReq)) {
                    if (this.dataServices != null) {
                        for (DirectoryDataService<?> dataService : this.dataServices) {
                            try {
                                combineFederatedBatchResponses(batchReq, batchResp, dataService.processData(batchReq));
                            } catch (Throwable th) {
                                this.addError(dirId, reqId, batchResp, th);
                            }
                        }
                    }

                    try {
						combineBatchResponses(batchResp, this.fedService.federate(batchReq));
                    } catch (Throwable th) {
                        isError = true;
                        this.addError(dirId, reqId, batchResp, th);
                    }
                } else {
                    // Call Local LDAP Directory...
                    LOGGER.info("Inside Local Directory Call...");
                    if (this.dataServices != null) {
                        for (DirectoryDataService<?> dataService : this.dataServices) {
                            try {
                                combineBatchResponses(batchResp, dataService.processData(batchReq));
                            } catch (Throwable th) {
                                this.addError(dirId, reqId, batchResp, th);
                            }
                        }
                    }
                }
            }
        } catch (XmlMappingException e) {
			LOGGER.error("Failed to process a request.", e);
            this.addError(dirId, reqId, batchResp, e);
        }
        try {
            this.interceptResponses(dirDesc, dirId, reqId, batchReq, batchResp);
        } catch (DirectoryInterceptorException e) {
            isError = true;
            this.addError(dirId, reqId, batchResp, e);
        }

		if (LOGGER.isTraceEnabled()) {
			try {
				String batchRespStr = this.dirJaxb2Marshaller.marshal(this.objectFactory.createBatchResponse(batchResp));
				LOGGER.trace("Processed DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ") into DSML batch response:\n" + batchRespStr);
			} catch (XmlMappingException e) {
				isError = true;
				this.addError(dirId, reqId, batchResp, e);
			}
		} else if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Processed DSML batch request (directoryId=" + dirId + ", requestId=" + reqId + ") into DSML batch response.");
		}
        if (isError) {
            entity.setStatus("Error");
        } else {
            entity.setStatus("Success");
        }
        pdtiAuditLogService.save(entity);
        return batchResp;
    }


	/**
	 *
	 * @param batchResp
	 * @param batchRequest
	 */
	private void combineFederatedBatchResponses(BatchRequest batchRequest, BatchResponse batchResp, List<BatchResponse> batchRespCombine) {

		if (batchRespCombine == null || batchRespCombine.size() == 0) {
			return;
		}

		// mapping between requestId and DsmlMessage
		Map<String, DsmlMessage> requestMap = new HashMap<>();

		// create a map
		Iterator<DsmlMessage> dsmlMessageIterator = batchRequest.getBatchRequests().iterator();
		int requestIndex = 0;
		while (dsmlMessageIterator.hasNext()) {
			DsmlMessage dsmlMessage = dsmlMessageIterator.next();
			if (StringUtils.isNotEmpty(dsmlMessage.getRequestId())) {
				LOGGER.debug("Mapping key=" + dsmlMessage.getRequestId());
				requestMap.put(dsmlMessage.getRequestId(), dsmlMessage);
			}
			// use index number if requestId is not available
			LOGGER.debug("Mapping key=" + requestIndex);
			requestMap.put(Integer.toString(requestIndex), dsmlMessage);
			++requestIndex;
		}

		// walk through each SearchResponse
		Iterator<BatchResponse> batchResponseIterator = batchRespCombine.iterator();
		while (batchResponseIterator.hasNext()) {
			BatchResponse batchResponse = batchResponseIterator.next();

			//
			Iterator<JAXBElement<?>> jaxbElementIterator = batchResponse.getBatchResponses().iterator();
			int index = 0;
			while (jaxbElementIterator.hasNext()) {
				JAXBElement<?> jaxbElement = jaxbElementIterator.next();

				// w
				if (jaxbElement.getValue() instanceof SearchResponse) {

					//
					SearchResponse searchResponse = (SearchResponse) jaxbElement.getValue();

					DsmlMessage dsmlMessage = null;
					if (StringUtils.isNotEmpty(searchResponse.getRequestId()) && requestMap.containsKey(searchResponse.getRequestId())) {
						LOGGER.debug("Getting DsmlMessage, key=" + searchResponse.getRequestId());
						dsmlMessage = requestMap.get(searchResponse.getRequestId());
					}
					else {
						LOGGER.debug("Getting DsmlMessage, key=" + index);
						dsmlMessage = requestMap.get(Integer.toString(index));
					}

					// DsmlMessage is found, set Controls
					if (dsmlMessage != null) {

						//
						List<Control> searchResultEntryCtrlList = buildSearchResultEntryMetadaCtrlList(dsmlMessage);
						List<Control> federatedResponseDataCtrlList = buildFederatedResponseDataCtrlList(dsmlMessage,
								searchResponse.getSearchResultDone());

						// set SearchResultDone/Control
						searchResponse.getSearchResultDone().getControl()
								.addAll(federatedResponseDataCtrlList);

						// set SearchResultEntry/Control
						Iterator<SearchResultEntry> searchResultEntryIterator =
								searchResponse.getSearchResultEntry().iterator();
						while (searchResultEntryIterator.hasNext()) {
							SearchResultEntry searchResultEntry = searchResultEntryIterator.next();
							searchResultEntry.getControl().addAll(searchResultEntryCtrlList);
						}
					}
					else {
						LOGGER.debug("Not found DsmlMessage.");
					}
				}

				// increment index for searching DsmlMessage if requestId is not available.
				++index;
			}
		}

		//
		combineBatchResponses(batchResp, batchRespCombine);
	}

    /**
     *
     * @param batchReq
     * @return boolean
     */
    private boolean isFederatedRequest(BatchRequest batchReq) {
        boolean isFederatedRequest = false;
        if (null != batchReq) {
			Iterator<DsmlMessage> dsmlMessageIterator = batchReq.getBatchRequests().iterator();
			while (dsmlMessageIterator.hasNext()) {
				DsmlMessage dsml = dsmlMessageIterator.next();
				Iterator<Control> controlIterator = dsml.getControl().iterator();
				while (controlIterator.hasNext()) {
					Control ctrl = controlIterator.next();
					if (ctrl.getControlValue() != null && StringUtils.equals(iheoid, ctrl.getType())) {
						isFederatedRequest = true;
					}

					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace(ctrl.getType());
						LOGGER.trace("isFederatedRequest = " + isFederatedRequest);
					} else if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(ctrl.getType());
						LOGGER.trace("isFederatedRequest = " + isFederatedRequest);
					}
				}
			}
        }
        return isFederatedRequest;
    }

    @Override
    protected void addError(String dirId, String reqId, BatchResponse batchResp, Throwable th) {
        // TODO: improve error handling
        batchResp.getBatchResponses().add(this.objectFactory.createBatchResponseErrorResponse(this.errBuilder.buildErrorResponse(reqId, ErrorType.OTHER, th)));
    }

    private void combineBatchResponses(BatchResponse batchResp, List<BatchResponse> batchRespCombine) {
        for (BatchResponse batchRespCombineItem : batchRespCombine) {
            batchResp.getBatchResponses().addAll(batchRespCombineItem.getBatchResponses());
        }
    }

    /**
     *
     * @param dsmlMessage
     * @return List<Control>
     */
    private List<Control> buildFederatedResponseDataCtrlList(DsmlMessage dsmlMessage, LDAPResult ldapResult) {
		List<Control> controlList = new ArrayList<>();

		//
		Iterator<Control> iterator = dsmlMessage.getControl().iterator();
		while (iterator.hasNext()) {

			// parse FederatedRequestData
			Control control = iterator.next();
			if (control.getControlValue() != null) {
				LOGGER.debug("FederatedRequestData XML=" + new String((byte[]) control.getControlValue()));
			}
			FederatedRequestData federatedRequestData =
					federationJaxb2Marshaller.unmarshalFederatedRequestData((byte[]) control.getControlValue());

			//
			if (federatedRequestData != null) {

				Control ctrl = new Control();

				//
				ctrl.setType(iheoid);
				ctrl.setCriticality(false);

				FederatedSearchResponseData oFederatedSearchResponseData = new FederatedSearchResponseData();
				FederatedResponseStatus oStatus = new FederatedResponseStatus();
				oStatus.setDirectoryId(dirStaticId);
				oStatus.setFederatedRequestId(federatedRequestData.getFederatedRequestId());

				//
				if (ldapResult != null && ldapResult.getResultCode() != null && ldapResult.getResultCode().getDescr()
						!= null) {
					oStatus.setResultCode(ldapResult.getResultCode().getDescr());
					oStatus.setResultMessage(ldapResult.getResultCode().getDescr().toString());
				} else {
					oStatus.setResultCode(LDAPResultCode.SUCCESS);
					oStatus.setResultMessage(LDAPResultCode.SUCCESS.toString());
				}

				//
				oFederatedSearchResponseData.setFederatedResponseStatus(oStatus);

				// marshall
				ctrl.setControlValue(convertToBytes(oFederatedSearchResponseData));

				//
				controlList.add(ctrl);
			}
		}

        return controlList;
    }

    /**
     *
     * @param dsmlMessage
     * @return List<Control>
     */
    private List<Control> buildSearchResultEntryMetadaCtrlList(DsmlMessage dsmlMessage) {
		List<Control> controlList = new ArrayList<>();

		//
		Iterator<Control> iterator = dsmlMessage.getControl().iterator();
		while (iterator.hasNext()) {

			// parse FederatedRequestData
			Control control = iterator.next();
			FederatedRequestData federatedRequestData =
					federationJaxb2Marshaller.unmarshalFederatedRequestData((byte[]) control.getControlValue());

			//
			if (federatedRequestData != null) {

				Control ctrl = new Control();

				//
				ctrl.setType(iheoid);
				ctrl.setCriticality(false);

				//
				SearchResultEntryMetadata oSearchResultEntryMetadata = new SearchResultEntryMetadata();
				oSearchResultEntryMetadata.setDirectoryId(dirStaticId);

				// marshall
				ctrl.setControlValue(convertToBytes(oSearchResultEntryMetadata));

				//
				controlList.add(ctrl);
			}
		}

		return controlList;
    }

    /**
     *
     * @param resData
     * @return byte
     */
    private byte[] convertToBytes(FederatedSearchResponseData resData) {
		gov.hhs.onc.pdti.server.xml.ObjectFactory factory = new gov.hhs.onc.pdti.server.xml.ObjectFactory();
		StringResult result = new StringResult();
		federationJaxb2Marshaller.marshal(factory.createFederatedSearchResponseData(resData), result);
		return result.toString().getBytes();
    }

    /**
     *
     * @param resData
     * @return byte
     */
    private byte[] convertToBytes(SearchResultEntryMetadata resData) {
		gov.hhs.onc.pdti.server.xml.ObjectFactory factory = new gov.hhs.onc.pdti.server.xml.ObjectFactory();
		StringResult result = new StringResult();
		federationJaxb2Marshaller.marshal(factory.createSearchResultEntryMetadata(resData), result);
		return result.toString().getBytes();
    }

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    @DirectoryType(DirectoryTypeId.MAIN)
    @Override
    public void setDirectoryDescriptor(DirectoryDescriptor dirDesc) {
        this.dirDesc = dirDesc;
    }

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    @Override
    public void setFederationService(FederationService<BatchRequest, BatchResponse> fedService) {
        this.fedService = fedService;
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.IHE)
    @Override
    public void setRequestInterceptors(SortedSet<DirectoryRequestInterceptor<BatchRequest, BatchResponse>> reqInterceptors) {
        this.reqInterceptors = reqInterceptors;
    }

    @Autowired(required = false)
    @DirectoryStandard(DirectoryStandardId.IHE)
    @Override
    public void setResponseInterceptors(SortedSet<DirectoryResponseInterceptor<BatchRequest, BatchResponse>> respInterceptors) {
        this.respInterceptors = respInterceptors;
    }
}
