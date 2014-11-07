package gov.hhs.onc.pdti.jaxb.impl;

import gov.hhs.onc.pdti.jaxb.FederationJaxb2Marshaller;
import gov.hhs.onc.pdti.server.xml.FederatedRequestData;
import org.apache.log4j.Logger;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;

public class FederationJaxb2MarshallerImpl extends Jaxb2Marshaller implements FederationJaxb2Marshaller {

	private final static Logger LOGGER = Logger.getLogger(FederationJaxb2MarshallerImpl.class);

	@Override
	public FederatedRequestData unmarshalFederatedRequestData(byte[] xml) {
		if (xml == null || xml.length == 0)  {
			return null;
		}

		//
		XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
		XMLStreamReader xmlStreamReader = null;
		try {
			StreamSource streamSource = new StreamSource(new ByteArrayInputStream(xml));
			xmlStreamReader = xmlInputFactory.createXMLStreamReader(streamSource);
			xmlStreamReader.nextTag();
			while (!xmlStreamReader.getLocalName().equals("FederatedRequestData")) {
				xmlStreamReader.nextTag();
			}
			Unmarshaller unmarshaller = super.createUnmarshaller();
			JAXBElement<FederatedRequestData> federatedRequestData = unmarshaller.unmarshal
					(xmlStreamReader, FederatedRequestData.class);
			if (federatedRequestData != null) {
				return federatedRequestData.getValue();
			}
		} catch (JAXBException e) {
			LOGGER.error("Failed to parse XML data.", e);
		} catch (XMLStreamException e) {
			LOGGER.error("Failed to read XML data.", e);
		} finally {
			if (xmlStreamReader != null) {
				try {
					xmlStreamReader.close();
				} catch (XMLStreamException e) {
					LOGGER.error("Failed to close XMLStreamReader.", e);
				}
			}
		}

		return null;
	}
}
