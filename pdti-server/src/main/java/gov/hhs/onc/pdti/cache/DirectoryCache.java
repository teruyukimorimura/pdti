package gov.hhs.onc.pdti.cache;


import com.google.common.cache.RemovalListener;
import java.util.Date;

public interface DirectoryCache extends RemovalListener<String, Date> {
    public void registerRequest(String reqId) throws DirectoryCacheException;

    public void releaseRequest(String reqId);

    public void cleanUp();
}
