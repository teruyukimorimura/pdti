@XmlSchema(namespace = "urn:oasis:names:tc:DSML:2:0:core", elementFormDefault = XmlNsForm.QUALIFIED, xmlns = {
        @XmlNs(prefix = "dsml", namespaceURI = "urn:oasis:names:tc:DSML:2:0:core"), @XmlNs(prefix = "hpd", namespaceURI = "urn:ihe:iti:hpd:2010") })
@XmlSchemaTypes({ @XmlSchemaType(name = "string", type = String.class) })
package gov.hhs.onc.pdti.ws.api;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSchemaTypes;
