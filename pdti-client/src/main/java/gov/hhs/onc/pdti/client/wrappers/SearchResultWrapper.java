package gov.hhs.onc.pdti.client.wrappers;

import java.util.List;
import java.util.Map;

public class SearchResultWrapper {
    
    private String dn;
    private String directoryId;
    private String directoryUri;
    private Map<String, List<String>> attributes;
    
    public String getDn() {
        return dn;
    }
    
    public Map<String, List<String>> getAttributes() {
        return attributes;
    }
    
    public void setDn(String dn) {
        this.dn = dn;
    }
    
    public void setAttributes(Map<String, List<String>> attributes) {
        this.attributes = attributes;
    }

    public String getDirectoryUri() {
        return directoryUri;
    }

    public void setDirectoryUri(String directoryUri) {
        this.directoryUri = directoryUri;
    }

    public String getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(String directoryId) {
        this.directoryId = directoryId;
    }
    
}