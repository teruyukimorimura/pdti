package gov.hhs.onc.pdti.data.dsml.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.data.dsml.DirectoryDsmlException;
import gov.hhs.onc.pdti.data.dsml.DirectoryDsmlService;
import gov.hhs.onc.pdti.jaxb.DirectoryJaxb2Marshaller;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import javax.xml.bind.JAXBElement;

import org.apache.directory.api.dsmlv2.engine.Dsmlv2Engine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

@Scope("singleton")
@Service("dsmlService")
public class DirectoryDsmlServiceImpl implements DirectoryDsmlService {
    private final static Logger LOGGER = Logger.getLogger(DirectoryDsmlServiceImpl.class);

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    private ObjectFactory objectFactory;

    @Autowired
    private DirectoryJaxb2Marshaller dirJaxb2Marshaller;

    @Override
    public BatchResponse processDsml(Dsmlv2Engine dsmlEngine, BatchRequest batchReq) throws DirectoryDsmlException {
		String processResponse = null;
        try {
            String batchReqStr = this.dirJaxb2Marshaller.marshal(this.objectFactory.createBatchRequest(batchReq));

            LOGGER.debug("Processing DSML batch request (id=" + batchReq.getRequestId() + ").");

			// processDSML
			processResponse = dsmlEngine.processDSML(batchReqStr);

			// unmarshal processResponse XML String
			JAXBElement<BatchResponse> batchRespElement = (JAXBElement<BatchResponse>) this.dirJaxb2Marshaller.unmarshal(processResponse);
            BatchResponse batchResp = batchRespElement.getValue();

            LOGGER.debug("Processed DSML batch request (id=" + batchReq.getRequestId() + ") into DSML batch response (num="
                    + batchResp.getBatchResponses().size() + ").");

            return batchResp;
        } catch (XmlMappingException | XmlPullParserException e) {
			LOGGER.error("Unable to process DSML batch transaction. response=" + processResponse, e);
            throw new DirectoryDsmlException("Unable to process DSML batch transaction.", e);
        }
    }
}
