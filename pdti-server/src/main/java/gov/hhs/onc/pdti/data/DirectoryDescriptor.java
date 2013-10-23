package gov.hhs.onc.pdti.data;

import java.net.URL;

import org.apache.directory.api.ldap.model.name.Dn;

public interface DirectoryDescriptor {
    public boolean isEnabled();

    public void setEnabled(boolean enabled);

    public String getDirectoryId();

    public void setDirectoryId(String dirId);

    public URL getWsdlLocation();

    public void setWsdlLocation(URL wsdlLoc);

    public Dn getBaseDn();

    public void setBaseDn(Dn baseDn);
    
    public boolean isShouldExposeDirectoryUri();
    
    public void setShouldExposeDirectoryUri(boolean shouldExposeDirectoryUri);
}
