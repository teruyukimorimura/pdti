package gov.hhs.onc.pdti.data.impl;

import gov.hhs.onc.pdti.data.DirectoryDescriptor;

import java.net.URL;

import org.apache.directory.api.ldap.model.name.Dn;

public class DirectoryDescriptorImpl implements DirectoryDescriptor {
    private boolean enabled = true;
    private String dirId;
    private URL wsdlLoc;
    private Dn baseDn;
    private boolean shouldExposeDirectoryUri = true;

    @Override
    public Dn getBaseDn() {
        return this.baseDn;
    }

    @Override
    public void setBaseDn(Dn baseDn) {
        this.baseDn = baseDn;
    }

    @Override
    public String getDirectoryId() {
        return this.dirId;
    }

    @Override
    public void setDirectoryId(String dirId) {
        this.dirId = dirId;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public URL getWsdlLocation() {
        return this.wsdlLoc;
    }

    @Override
    public void setWsdlLocation(URL wsdlLoc) {
        this.wsdlLoc = wsdlLoc;
    }

    public boolean isShouldExposeDirectoryUri() {
        return shouldExposeDirectoryUri;
    }

    public void setShouldExposeDirectoryUri(boolean shouldExposeDirectoryUri) {
        this.shouldExposeDirectoryUri = shouldExposeDirectoryUri;
    }
}
