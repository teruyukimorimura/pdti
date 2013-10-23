package gov.hhs.onc.pdti.jaxb;

import javax.xml.bind.JAXBContext;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

public interface DirectoryJaxb2Marshaller extends Marshaller, Unmarshaller {
    public String marshal(Object obj) throws XmlMappingException;

    public Object unmarshal(String str) throws XmlMappingException;

    public JAXBContext getJaxbContext();

    public void setContextPackages(Package... contextPackages);
}
