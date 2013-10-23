package gov.hhs.onc.pdti.client.types;

public enum AttributeTypes {
    
    GIVEN_NAME("givenName"), HC_IDENTIFIER("HcIdentifier"),
        HC_SPECIALIZATION("HcSpecialization");

    private String name;
    
    private AttributeTypes(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

}
