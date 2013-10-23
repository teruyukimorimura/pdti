package gov.hhs.onc.pdti.error.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.error.DirectoryErrorBuilder;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.ErrorResponse;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.Detail;
import gov.hhs.onc.pdti.ws.api.ErrorResponse.ErrorType;
import gov.hhs.onc.pdti.ws.api.ObjectFactory;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusError;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorDetail;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusErrorType;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("errBuilder")
@Scope("singleton")
public class DirectoryErrorBuilderImpl implements DirectoryErrorBuilder {
    private final static String PDTI_DEBUG_PROP_NAME = "pdti.debug";

    private final static boolean PDTI_DEBUG_DEFAULT = false;

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    private ObjectFactory objectFactory;

    @Autowired
    @DirectoryStandard(DirectoryStandardId.HPD_PLUS_PROPOSED)
    private gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory hpdPlusObjectFactory;

    @Override
    public HpdPlusError buildError(String dirId, String reqId, HpdPlusErrorType errType, Throwable th) {
        HpdPlusError err = this.hpdPlusObjectFactory.createHpdPlusError();
        err.setDirectoryId(dirId);
        err.setRequestId(reqId);
        err.setType(errType);
        err.setMessage(th.getMessage());

        if (setStackTraceDetail()) {
            HpdPlusErrorDetail errDetail = this.hpdPlusObjectFactory.createHpdPlusErrorDetail();
            errDetail.setAny(DirectoryUtils.getStackTraceJaxbElement(th));
            err.setDetail(errDetail);
        }

        return err;
    }

    @Override
    public ErrorResponse buildErrorResponse(String reqId, ErrorType errType, Throwable th) {
        ErrorResponse errResp = this.objectFactory.createErrorResponse();
        errResp.setRequestId(reqId);
        errResp.setType(errType);
        errResp.setMessage(th.getMessage());

        if (setStackTraceDetail()) {
            Detail errRespDetail = this.objectFactory.createErrorResponseDetail();
            errRespDetail.setAny(DirectoryUtils.getStackTraceJaxbElement(th));
            errResp.setDetail(errRespDetail);
        }

        return errResp;
    }

    private static boolean setStackTraceDetail() {
        return ObjectUtils.defaultIfNull(BooleanUtils.toBoolean(System.getProperty(PDTI_DEBUG_PROP_NAME)), PDTI_DEBUG_DEFAULT);
    }
}
