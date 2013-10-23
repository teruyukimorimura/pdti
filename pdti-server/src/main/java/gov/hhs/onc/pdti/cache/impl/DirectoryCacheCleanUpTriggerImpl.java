package gov.hhs.onc.pdti.cache.impl;


import gov.hhs.onc.pdti.DirectoryType;
import gov.hhs.onc.pdti.DirectoryTypeId;
import gov.hhs.onc.pdti.cache.DirectoryCacheCleanUpTrigger;
import gov.hhs.onc.pdti.cache.DirectoryCacheDescriptor;
import java.util.Date;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Component;

@Component("dirCacheCleanUpTrigger")
@Scope("singleton")
public class DirectoryCacheCleanUpTriggerImpl implements DirectoryCacheCleanUpTrigger {
    @Autowired
    @DirectoryType(DirectoryTypeId.MAIN)
    private DirectoryCacheDescriptor dirCacheDesc;

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        long dirCacheCleanUpInterval = this.dirCacheDesc.getCleanUpInterval();

        return (dirCacheCleanUpInterval > 0) ? new Date(ObjectUtils.defaultIfNull(triggerContext.lastScheduledExecutionTime(), new Date()).getTime()
                + dirCacheCleanUpInterval) : null;
    }
}
