package gov.hhs.onc.pdti.jaxb;

import gov.hhs.onc.pdti.server.xml.FederatedRequestData;
import org.springframework.oxm.XmlMappingException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

public interface FederationJaxb2Marshaller {

	/**
	 * See org.springframework.oxm.jaxb.Jaxb2Marshaller.marshal method.
	 *
	 * @param graph
	 * @param result
	 * @throws XmlMappingException
	 */
	public void marshal(Object graph, Result result) throws XmlMappingException;

	/**
	 * See org.springframework.oxm.jaxb.Jaxb2Marshaller.unmarshal method.
	 *
	 * @param source
	 * @return
	 * @throws XmlMappingException
	 */
	public Object unmarshal(Source source) throws XmlMappingException;

	/**
	 * Unmarshall FederatedRequestData XML.
	 *
	 * @param xml
	 * @return FederatedRequestData
	 */
	public FederatedRequestData unmarshalFederatedRequestData(byte[] xml);

}
