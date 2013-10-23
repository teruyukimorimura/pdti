<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <%@ include file="head.jsp"%>
    <body>
        <div class="container">
            <h3><s:text name="searchErrorLegend" /></h3>
            <table class="table">
                <s:iterator value="searchErrorMessages">
                    <tr class="error"><td><s:property/></td></tr>
                </s:iterator>
            </table>
            <a href="<s:url value="/"/>"><s:text name="back" /></a>
        </div>
    </body>
</html>
