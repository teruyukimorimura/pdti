package gov.hhs.onc.pdti.test.ldap;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.JMX;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

public class ApacheDsInstanceRunner {
    private final static String CMD_START = "start";
    private final static String CMD_STOP = "stop";

    private final static int EXIT_SUCCESS = 0;
    private final static int EXIT_ERR = 1;

    private final static int THREAD_SLEEP_MS = 1000;

    private final static String PDTI_TEST_APACHEDS_JMX_PORT_PROP_NAME = "pdti.test.apacheds.jmx.port";

    private final static String JMX_SERVICE_URL_PREFIX = "service:jmx:rmi:///jndi/rmi://:";
    private final static String JMX_SERVICE_URL_SUFFIX = "/jmxrmi";

    private final static Logger LOGGER = Logger.getLogger(ApacheDsInstance.class);

    private static ObjectName mbeanName;

    private static JMXServiceURL jmxServiceUrl;

    private JMXConnector jmxConn;

    static {
        try {
            mbeanName = new ObjectName("gov.hhs.onc.pdti.test.ldap:type=ApacheDsInstance");
        } catch (MalformedObjectNameException e) {
            // TODO: improve error handling
            LOGGER.error(e);

            System.exit(EXIT_ERR);
        }

        try {
            jmxServiceUrl = new JMXServiceURL(JMX_SERVICE_URL_PREFIX + System.getProperty(PDTI_TEST_APACHEDS_JMX_PORT_PROP_NAME) + JMX_SERVICE_URL_SUFFIX);
        } catch (MalformedURLException e) {
            // TODO: improve error handling
            LOGGER.error(e);

            System.exit(EXIT_ERR);
        }
    }

    private ApacheDsInstanceRunner() {
    }

    public static void main(String... args) {
        if (ArrayUtils.isEmpty(args)) {
            LOGGER.error("A command must be specified.");

            System.exit(EXIT_ERR);
        }

        String cmd = args[0].trim().toLowerCase();

        try {
            switch (cmd) {
                case CMD_START:
                    new ApacheDsInstanceRunner().start();
                    break;

                case CMD_STOP:
                    new ApacheDsInstanceRunner().stop();
                    break;

                default:
                    LOGGER.error("Unknown command: " + cmd);

                    System.exit(EXIT_ERR);
                    break;
            }

            System.exit(EXIT_SUCCESS);
        } catch (InstanceAlreadyExistsException | IOException | MBeanRegistrationException | NotCompliantMBeanException e) {
            // TODO: improve error handling
            LOGGER.error(e);

            System.exit(EXIT_ERR);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.jmxConn != null) {
            this.jmxConn.close();
        }

        super.finalize();
    }

    private void start() throws InstanceAlreadyExistsException, IOException, MBeanRegistrationException, NotCompliantMBeanException {
        ApacheDsInstanceMBean mbean = new ApacheDsInstance(Thread.currentThread());

        ManagementFactory.getPlatformMBeanServer().registerMBean(mbean, mbeanName);

        this.getMbeanProxy().start();

        while(!Thread.interrupted()) {
            try {
                Thread.sleep(THREAD_SLEEP_MS);

                LOGGER.trace("ApacheDS instance thread slept ...");
            } catch (InterruptedException e) {
                LOGGER.debug("ApacheDS instance thread interrupted.");

                Thread.currentThread().interrupt();
            }
        }
    }

    private void stop() throws IOException {
        this.getMbeanProxy().stop();
    }

    private ApacheDsInstanceMBean getMbeanProxy() throws IOException {
        return JMX.newMBeanProxy((this.jmxConn = JMXConnectorFactory.connect(jmxServiceUrl, null)).getMBeanServerConnection(), mbeanName,
                ApacheDsInstanceMBean.class, true);
    }
}
