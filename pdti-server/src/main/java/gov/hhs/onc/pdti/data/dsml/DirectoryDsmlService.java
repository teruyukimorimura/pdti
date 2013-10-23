package gov.hhs.onc.pdti.data.dsml;

import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import org.apache.directory.api.dsmlv2.engine.Dsmlv2Engine;

public interface DirectoryDsmlService {
    public BatchResponse processDsml(Dsmlv2Engine dsmlEngine, BatchRequest batchReq) throws DirectoryDsmlException;
}
