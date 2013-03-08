<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
    <head>
    <link rel="stylesheet" type="text/css" href="mufasa.css">
        <title>SO question 4112686</title>
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
       
    </head>
    <body>
    <%= helpers.HTMLHelper.printHeader() %>
        <button id="somebutton">press here</button>
        <div id="somediv"></div>
         <button id="somebutton1">press here1</button>
        <div id="somediv1"></div>
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
         <script>
            $(document).ready(function() {                        // When the HTML DOM is ready loading, then execute the following function...
                $('#somebutton').click(function() {    
                									// Locate HTML DOM element with ID "somebutton" and assign the following function to its "click" event...
                    $.get('StudentInfo?id=1', function(responseText) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
                    	var value = $("#txt_name").val(); 
                    	if ( responseText.indexOf(value) > -1 ) {
                    		$('#somediv').text("correct!"); 
                    	}
                    	else{
                    		$('#somediv').text(responseText); 
                    	}
                    	// Locate HTML DOM element with ID "somediv" and set its text content with the response text.
                   
                    });
                });
            });
        </script>
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
          <script>
            $(document).ready(function() {                        // When the HTML DOM is ready loading, then execute the following function...
                $('#somebutton1').click(function() {    
                									// Locate HTML DOM element with ID "somebutton" and assign the following function to its "click" event...
                    $.get('StudentInfo?id=2', function(responseText) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
                        $('#somediv1').text(responseText);         // Locate HTML DOM element with ID "somediv" and set its text content with the response text.
                    });
                });
            });
        </script>
    </body>
</html>