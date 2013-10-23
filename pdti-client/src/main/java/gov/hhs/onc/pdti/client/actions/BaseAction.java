package gov.hhs.onc.pdti.client.actions;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;

import com.opensymphony.xwork2.ActionSupport;

@InterceptorRefs({ @InterceptorRef("defaultStack") })
public abstract class BaseAction extends ActionSupport {

    private static final long serialVersionUID = -1751487419797613300L;

}