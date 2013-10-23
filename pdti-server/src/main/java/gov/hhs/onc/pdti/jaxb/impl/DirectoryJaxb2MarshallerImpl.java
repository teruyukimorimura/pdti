package gov.hhs.onc.pdti.jaxb.impl;

import gov.hhs.onc.pdti.jaxb.DirectoryJaxb2Marshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

public class DirectoryJaxb2MarshallerImpl extends Jaxb2Marshaller implements DirectoryJaxb2Marshaller {
    @Override
    public String marshal(Object obj) throws XmlMappingException {
        StringResult strResult = new StringResult();

        this.marshal(obj, strResult);

        return strResult.toString();
    }

    @Override
    public Object unmarshal(String str) throws XmlMappingException {
        return this.unmarshal(new StringSource(str));
    }

    @Override
    public void setContextPackages(Package... contextPackages) {
        String[] contextPackageNames = new String[contextPackages.length];

        for (int a = 0; a < contextPackages.length; a++) {
            contextPackageNames[a] = contextPackages[a].getName();
        }

        this.setContextPaths(contextPackageNames);
    }
}
