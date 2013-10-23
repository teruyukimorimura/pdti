<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- workaround for bootstrap conflict with jquery that prevents dialog close icon from appearing (see: http://stackoverflow.com/questions/8681707/jqueryui-modal-dialog-does-not-show-close-button-x) -->
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js" type="text/javascript"></script>
<div id="navbar" class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <div class="brand">
	            <img src="<s:url value="/img/pdti-logo.png"/>" id="title-img"/>
	            <a class="greylink" href="<s:url value="/"/>"><s:text name="title"/></a>
            </div>
            <div class="pull-right">
	            <ul class="nav">
	                <li id="home"><a href="<s:url value="/"/>"><i class="icon-home icon-white"></i> <s:text name="home"/></a></li>
	                <li id="test"><a href="<s:url action="test"/>"><i class="icon-play icon-white"></i> <s:text name="test"/></a></li>
	                <li id="help-menu" class="dropdown">
	                    <a class="dropdown-toggle" data-toggle="dropdown"><i class="icon-question-sign icon-white"></i> <s:text name="help"/> <b class="caret"></b></a>
	                    <ul class="dropdown-menu">
	                        <li>
	                            <a href="http://modularspecs.siframework.org/Provider+Directories+Installation+Guide" target="_blank"><s:text name="installation.guide"/></a>
	                        </li>
	                        <li class="divider"></li>
	                        <li>
	                            <a href="http://modularspecs.siframework.org/+Provider+Directories+FAQ" target="_blank"><s:text name="faq"/></a>
	                        </li>
                            <li class="divider"></li>
	                        <li>
	                           <a href="#" id="version-dialog-link" class="helplink"><s:text name="version.link"/></i></a>
	                        </li>
	                    </ul>
	                </li>
	            </ul>
	            <ul class="nav">
	                <li id="project-menu" class="dropdown">
	                    <a class="dropdown-toggle" data-toggle="dropdown"><i class="icon-wrench icon-white"></i> <s:text name="about.project"/> <b class="caret"></b></a>
	                    <ul class="dropdown-menu">
	                        <li>
	                            <a href="https://code.google.com/p/pdti/" target="_blank"><s:text name="project"/></a>
	                        </li>
	                        <li class="divider"></li>
	                        <li>
	                            <a href="https://groups.google.com/forum/?fromgroups#!forum/provider-directories-test-implementation" target="_blank"><s:text name="groups"/></a>
	                        </li>
	                        <li class="divider"></li>
	                        <li>
	                            <a href="http://jira.siframework.org/browse/MSPDTI" target="_blank"><s:text name="jira"/></a>
	                        </li>
	                    </ul>
	                </li>
	            </ul>
            </div>
        </div>
    </div>
</div>

<div id="version-dialog" title="<s:text name='version.title'/>">
    <s:text name="version"/>
</div>

<script type="text/javascript">
$(document).ready(function () {
	
	// specify dialog
	$("#version-dialog").dialog({
        autoOpen: false,
        width: 600,
        buttons: [
            {
                text: "OK",
                click: function() {
                    $( this ).dialog( "close");
                }
            }
        ]
    });
    
    // specify link to open the dialog
    $("#version-dialog-link").click(function( event ) {
        $("#version-dialog").dialog( "open");
        $('.ui-dialog :button').blur();
        event.preventDefault();
    });
    
});
</script>