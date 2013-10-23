package gov.hhs.onc.pdti.data;

import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import java.util.List;

public interface DirectoryDataService<T extends DirectoryDataSource> {
    public List<BatchResponse> processData(BatchRequest batchReq) throws DirectoryDataException;

    public BatchResponse processData(T dataSrc, BatchRequest batchReq) throws DirectoryDataException;
}
