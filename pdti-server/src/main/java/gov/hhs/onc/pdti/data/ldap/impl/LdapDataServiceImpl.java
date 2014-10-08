package gov.hhs.onc.pdti.data.ldap.impl;

import gov.hhs.onc.pdti.data.DirectoryDataException;
import gov.hhs.onc.pdti.data.dsml.DirectoryDsmlService;
import gov.hhs.onc.pdti.data.impl.AbstractDataService;
import gov.hhs.onc.pdti.data.ldap.DirectoryLdapException;
import gov.hhs.onc.pdti.data.ldap.LdapDataService;
import gov.hhs.onc.pdti.data.ldap.LdapDataSource;
import gov.hhs.onc.pdti.data.ldap.LdapServerType;
import gov.hhs.onc.pdti.util.DirectoryUtils;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.hhs.onc.pdti.ws.api.DsmlMessage;
import gov.hhs.onc.pdti.ws.api.ErrorResponse;
import gov.hhs.onc.pdti.ws.api.SearchResponse;
import org.apache.directory.api.dsmlv2.engine.Dsmlv2Engine;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBElement;

@Qualifier("ldap")
@Scope("singleton")
@Service("dataService")
public class LdapDataServiceImpl extends AbstractDataService<LdapDataSource> implements LdapDataService {

    private final static Logger LOGGER = Logger.getLogger(LdapDataServiceImpl.class);

    @Autowired
    private DirectoryDsmlService dsmlService;

    @Override
    public BatchResponse processData(LdapDataSource dataSource, BatchRequest batchReq) throws DirectoryDataException {
        LdapConnectionConfig ldapConnConfig = dataSource.toConfig();
        LdapConnection ldapConn = null;
        boolean useTransReqId = useTransactionalRequestId(dataSource);

        try {
            ldapConn = new LdapNetworkConnection(ldapConnConfig);

            connectAndBind(dataSource, ldapConn);

            if (useTransReqId) {
                setTransactionalRequestId(batchReq);
            }

            BatchResponse batchResp = this.dsmlService.processDsml(new Dsmlv2Engine(ldapConn, ldapConnConfig.getName(), ldapConnConfig.getCredentials()),
                    batchReq);

            if (useTransReqId) {
                restoreTransactionalRequestId(batchReq, batchResp);
            }

            return batchResp;
        } finally {
            if (ldapConn != null) {
                unbindAndDisconnect(dataSource, ldapConn);
            }
        }
    }

    private static void setTransactionalRequestId(BatchRequest batchReq) {
		// replace request IDs in the request to integers
		batchReq.setRequestId(toInteger(batchReq.getRequestId()));

		for (DsmlMessage batchReqMsg : batchReq.getBatchRequests()) {
			batchReqMsg.setRequestId(toInteger(batchReqMsg.getRequestId()));
		}
    }

    private static void restoreTransactionalRequestId(BatchRequest batchReq, BatchResponse batchResp) {

		// restore request IDs in the request
		batchReq.setRequestId(toUUID(batchReq.getRequestId()));
		for (DsmlMessage batchReqMsg : batchReq.getBatchRequests()) {
			batchReqMsg.setRequestId(toUUID(batchReqMsg.getRequestId()));
		}

		// ApacheDS DSMLv2Engine doesn't return BatchResponse with requestID attributes in BatchRequest,
		// so requestID attributes in BatchResponse is set based on BatchRequest
		batchResp.setRequestId(batchReq.getRequestId());
		int index = 0;
		for (DsmlMessage batchReqMsg : batchReq.getBatchRequests()) {
			if (index >= batchResp.getBatchResponses().size())
				break;

			//
			JAXBElement<?> batchRespItem = batchResp.getBatchResponses().get(index);
			String requestId = batchReqMsg.getRequestId();

			//
			if (DsmlMessage.class.isAssignableFrom(batchRespItem.getDeclaredType())) {
				((DsmlMessage) batchRespItem.getValue()).setRequestId(requestId);
			} else if (ErrorResponse.class.isAssignableFrom(batchRespItem.getDeclaredType())) {
				((ErrorResponse) batchRespItem.getValue()).setRequestId(requestId);
			} else if (SearchResponse.class.isAssignableFrom(batchRespItem.getDeclaredType())) {
				SearchResponse searchResponse = (SearchResponse) batchRespItem.getValue();
				searchResponse.setRequestId(requestId);
				if (searchResponse.getSearchResultDone() != null) {
					searchResponse.getSearchResultDone().setRequestId(requestId);
				}
			}
			++index;
		}

    }

	private static ThreadLocal<Map<String, String>> REQUEST_ID_MAP = new ThreadLocal<Map<String, String>>() {
		@Override
		protected Map<String, String> initialValue() {
			return new HashMap<String, String>();
		}
	};

	private static String toInteger(String uuid) {
		if (uuid != null) {
			String id = String.valueOf(Math.abs(uuid.hashCode()));
			LOGGER.debug("toInteger, UUID:id=" + uuid + ":" + id);
			REQUEST_ID_MAP.get().put(id, uuid);
			return id;
		}
		return null;
	}

	private static String toUUID(String id) {
		if (id != null && REQUEST_ID_MAP.get().containsKey(id)) {
			String uuid = REQUEST_ID_MAP.get().get(id);
			LOGGER.debug("toUUID, UUID:id=" + uuid + ":" + id);
			return uuid;
		}
		return id;
	}

    private static boolean useTransactionalRequestId(LdapDataSource dataSource) {
        return dataSource.getType() == LdapServerType.APACHEDS;
    }

    private static boolean connectAndBind(LdapDataSource dataSource, LdapConnection ldapConn) throws DirectoryLdapException {
        try {
            ldapConn.connect();
        } catch (LdapException e) {
            throw new DirectoryLdapException("Unable to connect to LDAP data source (" + dataSource + ").", e);
        }

        try {
            if (dataSource.getCredentials().isAnonymous()) {
                LOGGER.debug("Anonymously binding to LDAP data source (" + dataSource + ") ...");

                ldapConn.anonymousBind();
            } else {
                LOGGER.debug("Binding to LDAP data source (" + dataSource + ").");

                ldapConn.bind();
            }
        } catch (LdapException e) {
            throw new DirectoryLdapException("Unable to bind to LDAP data source (" + dataSource + ").", e);
        }

        return ldapConn.isAuthenticated();
    }

    private static boolean unbindAndDisconnect(LdapDataSource dataSource, LdapConnection ldapConn) throws DirectoryLdapException {
        if (ldapConn.isAuthenticated()) {
            try {
                ldapConn.unBind();
            } catch (LdapException e) {
                throw new DirectoryLdapException("Unable to unbind from LDAP data source (" + dataSource + ").", e);
            }
        }

        if (ldapConn.isConnected()) {
            try {
                ldapConn.close();
            } catch (IOException e) {
                throw new DirectoryLdapException("Unable to disconnect from LDAP data source (" + dataSource + ").", e);
            }
        }

        return !ldapConn.isConnected();
    }

    @Autowired(required = false)
    @Override
    protected void setDataSources(List<LdapDataSource> dataSources) {
        this.dataSources = dataSources;
    }

	/**
	 * Set DirectoryDsmlService
	 * @param dsmlService
	 */
	public void setDsmlService (DirectoryDsmlService dsmlService) {
		this.dsmlService = dsmlService;
	}

}
