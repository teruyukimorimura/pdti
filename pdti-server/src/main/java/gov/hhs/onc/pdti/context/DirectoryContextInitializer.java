package gov.hhs.onc.pdti.context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.commons.collections.EnumerationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ConfigurableWebApplicationContext;

@Component("contextInit")
@Scope("singleton")
public class DirectoryContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    /**
     * @see org.springframework.web.servlet.FrameworkServlet.INIT_PARAM_DELIMITERS
     */
    @SuppressWarnings("JavadocReference")
    private static final String CONFIG_LOC_ITEM_DELIMS = ",; \t\n";
    private static final String CONFIG_LOC_ITEM_DELIM_DISPLAY = ",";

    private final static Map<Pair<String, String>, String> CONTEXT_DATA_CONFIG_LOCS = new LinkedHashMap<>();

    private final static Logger LOGGER = Logger.getLogger(DirectoryContextInitializer.class);

    static {
        CONTEXT_DATA_CONFIG_LOCS.put(new ImmutablePair<>("pdti.context.config.data.directory", "pdtiContextConfigDataDirectory"),
                "/META-INF/data/directory/*.xml");
        CONTEXT_DATA_CONFIG_LOCS.put(new ImmutablePair<>("pdti.context.config.data.ldap", "pdtiContextConfigDataLdap"), "/META-INF/data/ldap/*.xml");
        CONTEXT_DATA_CONFIG_LOCS
                .put(new ImmutablePair<>("pdti.context.config.data.federation", "pdtiContextDataFederation"), "/META-INF/data/federation/*.xml");
        CONTEXT_DATA_CONFIG_LOCS.put(new ImmutablePair<>("pdti.context.config.ws.service", "pdtiContextWsService"), "/META-INF/ws/service/*.xml");
    }

    @Override
    public void initialize(ConfigurableApplicationContext appContext) {
        ConfigurableWebApplicationContext webAppContext = (ConfigurableWebApplicationContext) appContext;
        ServletContext servletContext = webAppContext.getServletContext();
        Properties sysProps = System.getProperties();
        Set<String> contextParamNames = new HashSet((List<String>) EnumerationUtils.toList(servletContext.getInitParameterNames()));
        Set<String> configLocs = new LinkedHashSet<>(Arrays.asList(webAppContext.getConfigLocations()));
        String[] configLocItems;
        String configSysPropName, configContextParamName;

        for (Pair<String, String> configKeys : CONTEXT_DATA_CONFIG_LOCS.keySet()) {
            if (((configSysPropName = configKeys.getLeft()) != null) && sysProps.containsKey(configSysPropName)) {
                configLocItems = getConfigLocationItems(sysProps.getProperty(configSysPropName));

                LOGGER.debug("Using system property (name=" + configSysPropName + ") for context data configuration location(s): ["
                        + StringUtils.join(configLocItems, CONFIG_LOC_ITEM_DELIM_DISPLAY) + "]");
            } else if (((configContextParamName = configKeys.getRight()) != null) && contextParamNames.contains(configContextParamName)) {
                configLocItems = getConfigLocationItems(servletContext.getInitParameter(configContextParamName));

                LOGGER.debug("Using servlet context parameter (name=" + configContextParamName + ") for context data configuration location(s): ["
                        + StringUtils.join(configLocItems, CONFIG_LOC_ITEM_DELIM_DISPLAY) + "]");
            } else {
                configLocItems = getConfigLocationItems(CONTEXT_DATA_CONFIG_LOCS.get(configKeys));

                LOGGER.debug("Using default value for context data configuration location(s): "
                        + StringUtils.join(configLocItems, CONFIG_LOC_ITEM_DELIM_DISPLAY) + "]");
            }

            configLocs.addAll(Arrays.asList(configLocItems));
        }

        String[] configLocsArr = configLocs.toArray(new String[configLocs.size()]);

        LOGGER.trace("Setting Spring web application context configuration locations: [" + StringUtils.join(configLocsArr, ",") + "]");

        webAppContext.setConfigLocations(configLocsArr);
    }

    private static String[] getConfigLocationItems(String configLoc) {
        return org.springframework.util.StringUtils.tokenizeToStringArray(configLoc, CONFIG_LOC_ITEM_DELIMS);
    }
}
