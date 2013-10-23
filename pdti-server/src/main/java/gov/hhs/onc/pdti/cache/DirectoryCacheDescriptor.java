package gov.hhs.onc.pdti.cache;

public interface DirectoryCacheDescriptor {
    public String toCacheBuilderSpecString();

    public long getCleanUpInterval();

    public void setCleanUpInterval(long cleanUpInterval);

    public int getConcurrencyLevel();

    public void setConcurrencyLevel(int concurrencyLevel);

    public String getExpireAfterAccess();

    public void setExpireAfterAccess(String expireAfterAccess);

    public String getExpireAfterWrite();

    public void setExpireAfterWrite(String expireAfterWrite);

    public int getInitialCapacity();

    public void setInitialCapacity(int initialCapacity);

    public int getMaximumSize();

    public void setMaximumSize(int maximumSize);

    public int getMaximumWeight();

    public void setMaximumWeight(int maximumWeight);

    public String getRefreshAfterWrite();

    public void setRefreshAfterWrite(String refreshAfterWrite);

    public String getRefreshInterval();

    public void setRefreshInterval(String refreshInterval);

    public boolean isSoftValues();

    public void setSoftValues(boolean softValues);

    public boolean isWeakKeys();

    public void setWeakKeys(boolean weakKeys);

    public boolean isWeakValues();

    public void setWeakValues(boolean weakValues);
}
