package gov.hhs.onc.pdti.cache.impl;


import gov.hhs.onc.pdti.cache.DirectoryCacheDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DirectoryCacheDescriptorImpl implements DirectoryCacheDescriptor {
    private static class CacheBuilderToStringStyle extends ToStringStyle {
        public final static CacheBuilderToStringStyle INSTANCE = new CacheBuilderToStringStyle();

        protected CacheBuilderToStringStyle() {
            super();

            this.setContentStart(StringUtils.EMPTY);
            this.setContentEnd(StringUtils.EMPTY);
            this.setUseClassName(false);
            this.setUseIdentityHashCode(false);
        }

        @Override
        public void append(StringBuffer buffer, String fieldName, boolean value) {
            String fieldNameValueSep = this.getFieldNameValueSeparator();
            this.setFieldNameValueSeparator(StringUtils.EMPTY);

            this.appendFieldStart(buffer, fieldName);
            this.appendFieldEnd(buffer, fieldName);

            this.setFieldNameValueSeparator(fieldNameValueSep);
        }
    }

    private int initialCapacity = -1;
    private int concurrencyLevel = -1;
    private int maximumSize = -1;
    private int maximumWeight = -1;
    private String expireAfterWrite;
    private String expireAfterAccess;
    private boolean weakKeys;
    private boolean softValues;
    private boolean weakValues;
    private String refreshAfterWrite;
    private String refreshInterval;
    private long cleanUpInterval = -1;

    @Override
    public String toCacheBuilderSpecString() {
        ToStringBuilder toStrBuilder = new ToStringBuilder(this, CacheBuilderToStringStyle.INSTANCE);

        if (this.initialCapacity >= 0) {
            toStrBuilder.append("initialCapacity", this.initialCapacity);
        }

        if (this.concurrencyLevel >= 0) {
            toStrBuilder.append("concurrencyLevel", this.concurrencyLevel);
        }

        if (this.maximumSize >= 0) {
            toStrBuilder.append("maximumSize", this.maximumSize);
        }

        if (this.maximumWeight >= 0) {
            toStrBuilder.append("maximumWeight", this.maximumWeight);
        }

        if (!StringUtils.isBlank(this.expireAfterWrite)) {
            toStrBuilder.append("expireAfterWrite", this.expireAfterWrite);
        }

        if (!StringUtils.isBlank(this.expireAfterAccess)) {
            toStrBuilder.append("expireAfterAccess", this.expireAfterAccess);
        }

        if (this.weakKeys) {
            toStrBuilder.append("weakKeys", this.weakKeys);
        }

        if (this.softValues) {
            toStrBuilder.append("softValues", this.softValues);
        }

        if (this.weakValues) {
            toStrBuilder.append("weakValues", this.weakValues);
        }

        if (!StringUtils.isBlank(this.refreshAfterWrite)) {
            toStrBuilder.append("refreshAfterWrite", this.refreshAfterWrite);
        }

        if (!StringUtils.isBlank(this.refreshInterval)) {
            toStrBuilder.append("refreshInterval", this.refreshInterval);
        }

        return toStrBuilder.build();
    }

    @Override
    public long getCleanUpInterval() {
        return this.cleanUpInterval;
    }

    @Override
    public void setCleanUpInterval(long cleanUpInterval) {
        this.cleanUpInterval = cleanUpInterval;
    }

    @Override
    public int getConcurrencyLevel() {
        return this.concurrencyLevel;
    }

    @Override
    public void setConcurrencyLevel(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
    }

    @Override
    public String getExpireAfterAccess() {
        return this.expireAfterAccess;
    }

    @Override
    public void setExpireAfterAccess(String expireAfterAccess) {
        this.expireAfterAccess = expireAfterAccess;
    }

    @Override
    public String getExpireAfterWrite() {
        return this.expireAfterWrite;
    }

    @Override
    public void setExpireAfterWrite(String expireAfterWrite) {
        this.expireAfterWrite = expireAfterWrite;
    }

    @Override
    public int getInitialCapacity() {
        return this.initialCapacity;
    }

    @Override
    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    @Override
    public int getMaximumSize() {
        return this.maximumSize;
    }

    @Override
    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    @Override
    public int getMaximumWeight() {
        return this.maximumWeight;
    }

    @Override
    public void setMaximumWeight(int maximumWeight) {
        this.maximumWeight = maximumWeight;
    }

    @Override
    public String getRefreshAfterWrite() {
        return this.refreshAfterWrite;
    }

    @Override
    public void setRefreshAfterWrite(String refreshAfterWrite) {
        this.refreshAfterWrite = refreshAfterWrite;
    }

    @Override
    public String getRefreshInterval() {
        return this.refreshInterval;
    }

    @Override
    public void setRefreshInterval(String refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    @Override
    public boolean isSoftValues() {
        return this.softValues;
    }

    @Override
    public void setSoftValues(boolean softValues) {
        this.softValues = softValues;
    }

    @Override
    public boolean isWeakKeys() {
        return this.weakKeys;
    }

    @Override
    public void setWeakKeys(boolean weakKeys) {
        this.weakKeys = weakKeys;
    }

    @Override
    public boolean isWeakValues() {
        return this.weakValues;
    }

    @Override
    public void setWeakValues(boolean weakValues) {
        this.weakValues = weakValues;
    }
}
