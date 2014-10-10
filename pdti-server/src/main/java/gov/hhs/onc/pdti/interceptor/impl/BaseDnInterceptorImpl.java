package gov.hhs.onc.pdti.interceptor.impl;


import gov.hhs.onc.pdti.DirectoryStandard;
import gov.hhs.onc.pdti.DirectoryStandardId;
import gov.hhs.onc.pdti.data.DirectoryDescriptor;
import gov.hhs.onc.pdti.interceptor.DirectoryInterceptorException;
import gov.hhs.onc.pdti.interceptor.DirectoryRequestInterceptor;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.SearchRequest;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("baseDnInterceptor")
@DirectoryStandard(DirectoryStandardId.IHE)
@Order(101)
@Scope("singleton")
public class BaseDnInterceptorImpl extends AbstractDirectoryInterceptor<BatchRequest, BatchResponse> implements
        DirectoryRequestInterceptor<BatchRequest, BatchResponse> {
    @Override
    public void interceptRequest(DirectoryDescriptor dirDesc, String reqId, BatchRequest batchReq, BatchResponse batchResp)
            throws DirectoryInterceptorException {
        Dn dirBaseDn = dirDesc.getBaseDn();

        for (SearchRequest searchReqMsg : (Collection<SearchRequest>) CollectionUtils.select(batchReq.getBatchRequests(),
                PredicateUtils.instanceofPredicate(SearchRequest.class))) {

            try {
				if (!DirectoryUtils.hasValidBaseDn(dirBaseDn, new Dn(searchReqMsg.getDn()))) {
					throw new DirectoryInterceptorException("Invalid DN in DSML request at directory (id="	+
							dirDesc .getDirectoryId() + ") Distinguished Name: " + dirBaseDn.getName());
				}
            } catch (LdapInvalidDnException e) {
                throw new DirectoryInterceptorException("Unable to target DSML search request at directory (id=" + dirDesc.getDirectoryId()
                        + ") Distinguished Name: " + dirBaseDn.getName(), e);
            }
        }
    }
}
