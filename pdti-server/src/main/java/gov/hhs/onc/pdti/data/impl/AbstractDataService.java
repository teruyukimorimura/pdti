package gov.hhs.onc.pdti.data.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.data.DirectoryDataException;
import gov.hhs.onc.pdti.data.DirectoryDataService;
import gov.hhs.onc.pdti.data.DirectoryDataSource;
import gov.hhs.onc.pdti.error.DirectoryErrorBuilder;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.ErrorType;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDataService<T extends DirectoryDataSource> implements DirectoryDataService<T> {
    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    protected ObjectFactory objectFactory;

    @Autowired
    protected DirectoryErrorBuilder errBuilder;

    protected List<T> dataSources;

    private final static Logger LOGGER = Logger.getLogger(AbstractDataService.class);

    @Override
    public List<BatchResponse> processData(BatchRequest batchReq) throws DirectoryDataException {
        String reqId = batchReq.getRequestId();
        List<BatchResponse> batchResps = new ArrayList<>();
        BatchResponse batchResp;

        if (this.dataSources != null) {
            for (T dataSource : this.dataSources) {
                if (!dataSource.isEnabled()) {
                    LOGGER.trace("Skipping disabled data source (requestId=" + reqId + ", dataSourceClass=" + dataSource.getClass().getName() + ").");
                    continue;
                }

                batchResp = this.objectFactory.createBatchResponse();

                try {
                    batchResp = this.processData(dataSource, batchReq);
                } catch (Throwable th) {
                    // TODO: improve error handling
                    batchResp.getBatchResponses().add(
                            this.objectFactory.createBatchResponseErrorResponse(this.errBuilder.buildErrorResponse(reqId, ErrorType.OTHER, th)));
                }

                batchResps.add(batchResp);
            }
        }

        return batchResps;
    }

    protected abstract void setDataSources(List<T> dataSources);
}
