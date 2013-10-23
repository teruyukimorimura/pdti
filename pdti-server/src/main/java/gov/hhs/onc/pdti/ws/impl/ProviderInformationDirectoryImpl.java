package gov.hhs.onc.pdti.ws.impl;

import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.service.DirectoryService;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.ProviderInformationDirectoryPortType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@DirectoryStandard(DirectoryStandardId.IHE)
@Scope("singleton")
@Service("providerInfoDir")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@WebService(portName = "ProviderInformationDirectory_Port_Soap", serviceName = "ProviderInformationDirectory_Service", targetNamespace = "urn:ihe:iti:hpd:2010")
public class ProviderInformationDirectoryImpl extends AbstractProviderInformationDirectory<BatchRequest, BatchResponse> implements
        ProviderInformationDirectoryPortType {
    @Override
    @WebMethod(operationName = "ProviderInformationQueryRequest", action = "urn:ihe:iti:hpd:2010:ProviderInformationQueryRequest")
    @WebResult(name = "batchResponse", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "queryResponse")
    public BatchResponse providerInformationQueryRequest(
            @WebParam(name = "batchRequest", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core", partName = "queryRequest") BatchRequest queryRequest) {
        return this.dirService.processRequest(queryRequest);
    }

    @Autowired
    @DirectoryStandard(DirectoryStandardId.IHE)
    @Override
    protected void setDirectoryService(DirectoryService<BatchRequest, BatchResponse> dirService) {
        this.dirService = dirService;
    }
}
