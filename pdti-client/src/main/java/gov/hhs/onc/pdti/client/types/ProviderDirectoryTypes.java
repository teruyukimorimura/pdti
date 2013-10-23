package gov.hhs.onc.pdti.client.types;

public enum ProviderDirectoryTypes {
    
    DSML_WSDL("DSML-Based WSDL"), HPDPlus("HPDPlus WSDL");

    private String name;
    
    private ProviderDirectoryTypes(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

}
