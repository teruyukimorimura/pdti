//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.04 at 03:02:10 PM PST 
//


package gov.hhs.onc.pdti.server.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FederatedSearchResponseData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FederatedSearchResponseData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="federatedResponseStatus" type="{}federatedResponseStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FederatedSearchResponseData", propOrder = {
    "federatedResponseStatus"
})
public class FederatedSearchResponseData {

    protected FederatedResponseStatus federatedResponseStatus;

    /**
     * Gets the value of the federatedResponseStatus property.
     * 
     * @return
     *     possible object is
     *     {@link FederatedResponseStatus }
     *     
     */
    public FederatedResponseStatus getFederatedResponseStatus() {
        return federatedResponseStatus;
    }

    /**
     * Sets the value of the federatedResponseStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link FederatedResponseStatus }
     *     
     */
    public void setFederatedResponseStatus(FederatedResponseStatus value) {
        this.federatedResponseStatus = value;
    }

}
