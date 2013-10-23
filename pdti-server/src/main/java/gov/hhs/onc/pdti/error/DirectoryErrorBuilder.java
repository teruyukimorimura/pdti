package gov.hhs.onc.pdti.error;

import gov.hhs.onc.pdti.ws.api.ErrorResponse;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.ErrorType;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusError;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;

public interface DirectoryErrorBuilder {
    public HpdPlusError buildError(String dirId, String reqId, HpdPlusErrorType errType, Throwable th);

    public ErrorResponse buildErrorResponse(String reqId, ErrorType errType, Throwable th);
}
