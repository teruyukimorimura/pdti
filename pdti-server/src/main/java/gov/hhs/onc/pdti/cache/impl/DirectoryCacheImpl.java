package gov.hhs.onc.pdti.cache.impl;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.cache.RemovalNotification;
import gov.hhs.onc.pdti.DirectoryType;
import gov.hhs.onc.pdti.DirectoryTypeId;
import gov.hhs.onc.pdti.cache.DirectoryCache;
import gov.hhs.onc.pdti.cache.DirectoryCacheDescriptor;
import gov.hhs.onc.pdti.cache.DirectoryCacheException;
import gov.hhs.onc.pdti.cache.DirectoryCacheRequestIdException;
import java.util.Date;
import java.util.concurrent.Semaphore;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("dirCache")
@Scope("singleton")
public class DirectoryCacheImpl implements DirectoryCache {
    private final static Semaphore CACHE_LOCK = new Semaphore(1, true);

    private final static Logger LOGGER = Logger.getLogger(DirectoryCacheImpl.class);

    private static Cache<String, Date> cache;
    private static CacheStats cacheStats;

    @Autowired
    @DirectoryType(DirectoryTypeId.MAIN)
    private DirectoryCacheDescriptor dirCacheDesc;

    @Override
    public void registerRequest(String reqId) throws DirectoryCacheException {
        if (StringUtils.isBlank(reqId)) {
            return;
        }

        this.prepareCache();

        try {
            this.lockCache();

            Date reqDate;

            if ((reqDate = cache.getIfPresent(reqId)) != null) {
                throw new DirectoryCacheRequestIdException("Duplicate directory request (requestId=" + reqId + ") detected - already registered at: " + reqDate);
            }

            reqDate = new Date();

            cache.put(reqId, reqDate);

            LOGGER.trace("Registered directory request (requestId=" + reqId + ", requestDate=" + reqDate + ").");
        } finally {
            this.unlockCache();
        }
    }

    @Override
    public void releaseRequest(String reqId) {
        if (StringUtils.isBlank(reqId)) {
            return;
        }

        this.prepareCache();

        try {
            this.lockCache();

            cache.invalidate(reqId);
        } finally {
            this.unlockCache();
        }
    }

    @Override
    public void cleanUp() {
        this.prepareCache();

        try {
            this.lockCache();

            cache.cleanUp();

            CacheStats cacheStatsCleaned = cache.stats();

            if (cacheStats == null) {
                cacheStats = cacheStatsCleaned;
            }

            if (cacheStats.evictionCount() > 0) {
                LOGGER.trace("Cleaned up directory cache: " + cacheStats);
            }

            // Resetting directory cache statistics
            cacheStats = cacheStats.minus(cacheStats);
        } finally {
            this.unlockCache();
        }
    }

    @Override
    public void onRemoval(RemovalNotification<String, Date> notification) {
        LOGGER.trace("Released directory request (requestId=" + notification.getKey() + ", requestDate=" + notification.getValue() + ") from registry: cause="
                + notification.getCause());
    }

    private synchronized void prepareCache() {
        try {
            this.lockCache();

            if (cache == null) {
                this.createCache();
            }
        } finally {
            this.unlockCache();
        }
    }

    private synchronized void lockCache() {
        try {
            CACHE_LOCK.acquire();
        } catch (InterruptedException e) {
            LOGGER.error("Directory request handler thread interrupted.", e);

            Thread.currentThread().interrupt();
        }
    }

    private synchronized void unlockCache() {
        CACHE_LOCK.release();
    }

    private synchronized void createCache() {
        String dirCacheDescStr = this.dirCacheDesc.toCacheBuilderSpecString();

        cache = CacheBuilder.from(dirCacheDescStr).recordStats().removalListener(this).build();

        LOGGER.debug("Directory cache initialized: " + dirCacheDescStr);
    }
}
