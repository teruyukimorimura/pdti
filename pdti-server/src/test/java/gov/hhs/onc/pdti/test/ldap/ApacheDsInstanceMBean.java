package gov.hhs.onc.pdti.test.ldap;

public interface ApacheDsInstanceMBean {
    public void start();

    public void stop();

    public boolean isRunning();

    public boolean isStarted();

    public String getName();
}
