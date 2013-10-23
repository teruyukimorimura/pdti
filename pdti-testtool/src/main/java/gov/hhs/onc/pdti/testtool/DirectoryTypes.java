package gov.hhs.onc.pdti.testtool;

public enum DirectoryTypes {

    MSPD("MSPD WSDL"), IHE("IHE WSDL");
    
    private String name;
    
    private DirectoryTypes(final String name) {
        this.name = name;
    }
    
    public String toString() {
      return name;
  }
    
}
