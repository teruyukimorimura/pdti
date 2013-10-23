$(document).ready(function () {
    function clearErrors() {
        $("#wsdlUrlLabel").removeClass("validationerror");
        $("#wsdlUrl").removeClass("validationerrorinput");
        $("#baseDnLabel").removeClass("validationerror");
        $("#baseDn").removeClass("validationerrorinput");
    }
    
    function validate(wsdlUrlValue, baseDnValue) {
        if("" == wsdlUrlValue || "" == baseDnValue) {
            $("#validationerrordiv").show();
            if("" == wsdlUrlValue) {
                $("#wsdlUrlLabel").addClass("validationerror");
                $("#wsdlUrl").addClass("validationerrorinput");
            }
            if("" == baseDnValue) {
                $("#baseDnLabel").addClass("validationerror");
                $("#baseDn").addClass("validationerrorinput");
            }
            return false;
        } else {
            $("#validationerrordiv").hide();
            return true;
        }
    }
    
    $("#validationerrordiv").hide();
    
    $("#indicatordiv").hide();
    
    $("#infodiv").hide();
    
    $("#wsdl_type-dialog").dialog({
        autoOpen: false,
        width: 800,
        buttons: [
            {
                text: "OK",
                click: function() {
                    $( this ).dialog( "close");
                }
            }
        ]
    });
    
    // Link to open the dialog
    $("#wsdl_type-dialog-link").click(function( event ) {
        $("#wsdl_type-dialog").dialog( "open");
        $("#problemlink").blur();
        $('.ui-dialog :button').blur();
        event.preventDefault();
    });
    
    $("#wsdl_url-dialog").dialog({
        autoOpen: false,
        width: 400,
        buttons: [
            {
                text: "OK",
                click: function() {
                    $( this ).dialog( "close");
                }
            }
        ]
    });
    
    // Link to open the dialog
    $("#wsdl_url-dialog-link").click(function( event ) {
        $("#wsdl_url-dialog").dialog( "open");
        $('.ui-dialog :button').blur();
        event.preventDefault();
    });
    
    $("#basedn-dialog").dialog({
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
    
    // Link to open the dialog
    $("#basedn-dialog-link").click(function( event ) {
        $("#basedn-dialog").dialog( "open");
        $('.ui-dialog :button').blur();
        event.preventDefault();
    });
    
    $("#testform").submit(function(){
        clearErrors();
        var typeOfDirectoryValue = $("#typeOfDirectory").val();
        var wsdlUrlValue = $("#wsdlUrl").val();
        var baseDnValue = $("#baseDn").val();
        if(!validate(wsdlUrlValue, baseDnValue)) {
            return false;
        }
        $("#testform").hide();
        $("#header").html("Test Results For Your Healthcare Provider Directory Plus Instance");
        
        $("#wsdlUrl1").html(wsdlUrlValue);
        $("#wsdlUrl2").html(wsdlUrlValue);
        $("#indicatordiv").show();
        var formInput=$(this).serialize();
        $.getJSON('ajax/testResultsJson.action', formInput, function(data) {
            var messagesArray = data.testResultsJson.testResults;
            for (var i = 0; i < messagesArray.length; i++) {
                var message = messagesArray[i];
                var testSuiteName = message[0];
                var testCaseName = message[1];
                var testStatus = message[2];
                var testCaseDescription = message[3];
                var testCaseMessages = message[4];
                var request = message[5];
                var response = message[6];
                var color = "#FF1919";
                if("PASSED" == testStatus) {
                    color = "#00CC00";
                }
                $("#accordion").append("<h4 style=\"background:none;background-color:" + color + ";color:#ffffff;\">" + testSuiteName + "." + testCaseName + "<div class=\"pull-right\">" + testStatus + "</div></h4>");
                var divHtml = "<div>" +
                    "<em>Test Case Description:</em><br/>" +
                    "<p>" + testCaseDescription + "</p><hr/>" +
                    "<em>Test Case Messages:</em><br/>" +
                    "<p>" + testCaseMessages + "</p><hr/>" +
                    "<em>Request Content:</em><br/>" +
                    "<pre class=\"sh_xml\">" + request.replace(/</g, "&lt;").replace(/>/g, "&gt;") + "</pre>" +
                    "<em>Response Content:</em><br/>" +
                    "<pre class=\"sh_xml\">" + response.replace(/</g, "&lt;").replace(/>/g, "&gt;") + "</pre>" +
                    "</div>";
                $("#accordion").append(divHtml);
            }
            $("#indicatordiv").hide();
            $("#infodiv").show();
            
            $("#accordion").accordion({ header: "h4", collapsible: true, active: false, heightStyle: "content" });
        });
        return false;
    });
    
    sh_highlightDocument();
});
