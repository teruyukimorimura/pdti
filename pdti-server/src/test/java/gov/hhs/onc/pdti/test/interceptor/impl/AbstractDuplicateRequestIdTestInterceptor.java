package gov.hhs.onc.pdti.test.interceptor.impl;


import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.interceptor.DirectoryResponseInterceptor;
import gov.hhs.onc.pdti.interceptor.impl.AbstractDirectoryInterceptor;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public abstract class AbstractDuplicateRequestIdTestInterceptor<T, U> extends AbstractDirectoryInterceptor<T, U> implements DirectoryRequestInterceptor<T, U>,
        DirectoryResponseInterceptor<T, U> {
    protected final static Pattern DUP_REQ_ID_TEST_REQ_ID_PATTERN = Pattern.compile("^.+_dup_req_id_federation_loop_test_.+$");

    protected final static Logger LOGGER = Logger.getLogger(AbstractDuplicateRequestIdTestInterceptor.class);

    protected List<DirectoryDescriptor> dupReqIdTestDirs;

    protected void setDuplicateRequestIdTest(String reqId, boolean dupReqIdTestEnabled) {
        if (isDuplicateRequestIdTestRequestId(reqId) && (this.dupReqIdTestDirs != null)) {
            for (DirectoryDescriptor dupReqIdTestDir : this.dupReqIdTestDirs) {
                dupReqIdTestDir.setEnabled(dupReqIdTestEnabled);

                LOGGER.info("Duplicate request ID test (requestId=" + reqId + ") federated directory (directoryId=" + dupReqIdTestDir.getDirectoryId()
                        + ") toggled: " + dupReqIdTestEnabled);
            }
        }
    }

    protected static boolean isDuplicateRequestIdTestRequestId(String reqId) {
        return DUP_REQ_ID_TEST_REQ_ID_PATTERN.matcher(reqId).matches();
    }

    protected abstract void setDuplicateRequestIdTestDirectories(List<DirectoryDescriptor> dupReqIdTestDirs);
}
