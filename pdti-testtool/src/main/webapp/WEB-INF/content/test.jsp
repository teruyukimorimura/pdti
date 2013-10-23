<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="head_common.jsp"%>
        <!-- workaround for bootstrap conflict with jquery that prevents dialog close icon from appearing (see: http://stackoverflow.com/questions/8681707/jqueryui-modal-dialog-does-not-show-close-button-x) -->
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js" type="text/javascript"></script>
        <script src="<s:url value="/js/test.js"/>" type="text/javascript"></script>
        <link href="<s:url value="/css/test.css"/>" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <%@ include file="header.jsp"%>
        <div class="container">
            <br /> <br />
            <h2 id="header">
                <s:text name="test.header" />
            </h2>
            <hr />
            <div id="validationerrordiv" class="validationerror">* <s:text name="required" /></div>
            <form id="testform">
                <table>
                    <tr>
                        <td><label id="typeOfDirectoryLabel" for="typeOfDirectory"><s:text name="typeOfDirectory" /></label></td>
                        <td><a href="#" id="wsdl_type-dialog-link" class="helplink"><i
                                class="icon-question-sign"></i></a></td>
                        <td><s:select id="typeOfDirectory" key="typeOfDirectory"
                                list="@gov.hhs.onc.pdti.testtool.DirectoryTypes@values()"
                                value="@gov.hhs.onc.pdti.testtool.DirectoryTypes@MSPD.toString()"
                                theme="simple" /></td>
                    </tr>
                    <tr>
                        <td><label id="wsdlUrlLabel" for="wsdlUrl"><s:text name="wsdlUrl" /></label></td>
                        <td><a href="#" id="wsdl_url-dialog-link" class="helplink"><i
                                class="icon-question-sign"></i></a></td>
                        <td><s:textfield id="wsdlUrl" key="wsdlUrl" theme="simple" /></td>
                    </tr>
                    <tr>
                        <td><label id="baseDnLabel" for="baseDn"><s:text name="baseDn" /></label></td>
                        <td><a href="#" id="basedn-dialog-link" class="helplink"><i
                                class="icon-question-sign"></i></a></td>
                        <td><s:textfield id="baseDn" key="baseDn" theme="simple" /></td>
                    </tr>
                    <tr>
                        <td>
                            <button type="submit" class="btn">
                                <s:text name="submit" />
                            </button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <div id="wsdl_type-dialog" title="<s:text name='typeOfDirectory'/>">
            <s:text name="wsdl.type.dialog.html" />
        </div>
        <div id="wsdl_url-dialog" title="<s:text name='wsdlUrl'/>">
            <s:text name="wsdl.url.dialog.html" />
        </div>
        <div id="basedn-dialog" title="<s:text name='baseDn'/>">
            <s:text name="basedn.dialog.html" />
        </div>
        <div id="indicatordiv">
            <p>
                <img src="<s:url value="/img/ajax-loader.gif"/>"
                    alt="Running tests..." />
                <s:text name="tests.running.text" />
                <span id="wsdlUrl1" class="wsdlUrl"></span>
            </p>
        </div>
        <div id="infodiv">
            <p>
                <s:text name="tests.done.text" />
                <span id="wsdlUrl2" class="wsdlUrl"></span>
            </p>
        </div>
        <div id="testResults">
            <div id="accordion"></div>
        </div>
    </body>
</html>