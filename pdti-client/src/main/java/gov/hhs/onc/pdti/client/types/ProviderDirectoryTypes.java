package gov.hhs.onc.pdti.client.types;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

public enum ProviderDirectoryTypes {
    
    DSML_WSDL("DSML-Based WSDL"), HPDPlus("HPDPlus WSDL");

    private String name;

    private static final Map<String, String> urlLookup = new HashMap<String, String>();
    static {
        Properties baseProperties = new Properties();
        Properties properties = new Properties();
        try {
            baseProperties.load(ProviderDirectoryTypes.class.getClassLoader().getResourceAsStream("pdti-client.properties"));
            properties.load(ProviderDirectoryTypes.class.getClassLoader().getResourceAsStream("provider-directory-types.properties"));
        }
        catch (Exception e) {
            Logger LOGGER = Logger.getLogger(ProviderDirectoryTypes.class);
            LOGGER.debug(e);
        }

        for (ProviderDirectoryTypes pdt : ProviderDirectoryTypes.values()) {
            urlLookup.put(pdt.toString(), properties.getProperty(pdt.name()));
        }
    }

    private ProviderDirectoryTypes(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
    
    public static String getUrl(String name) {
        return ProviderDirectoryTypes.urlLookup.get(name);
    }
}
