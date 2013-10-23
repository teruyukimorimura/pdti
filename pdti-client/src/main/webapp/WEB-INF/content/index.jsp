<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
	<%@ include file="head.jsp"%>
	<body>
        <div class="container">
	        <h3><s:text name="searchLegend" /></h3>
	        <hr/>
			<s:form action="search" validate="true" class="form-inline">
                <fieldset>
		           <div style="display:block; text-align:center">
                       <h5><s:text name="defaultUrl"/>: <s:text name="provider.directory.wsdl.url"/></h5>
	                   <s:textfield style="width: 400px;" key="url"/>
	                   <s:hidden name="providerDirectoryType" value="HPDPlus WSDL"/>
	                   <s:textfield style="width: 400px;" key="requestId"/>
					   <s:select style="width: 415px;" key="typeToSearch" list="@gov.hhs.onc.pdti.client.types.SearchTypes@values()"/>
					   <s:textfield style="width: 400px;" key="searchAttribute"/>
				       <s:textfield style="width: 400px;" key="searchString" />
				       <s:submit />
                   </div>
			    </fieldset>
		    </s:form>
        </div>
    </body>
</html>