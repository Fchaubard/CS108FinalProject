<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>compose message</title>
</head>
<body>
<form action="MailManagementServlet" method="post">
<% String user = (String) request.getParameter("user"); 
out.println("Sender:" + user);%>
<br>
<input type="hidden" name="sender" value = "<% out.print(user); %>">
Recipient: <input type="text" name="recipient"><br>
subject: <input type="text" name="subject"><br>
message: <textarea cols = "50" rows = "5" name = "body"></textarea>
<input type="submit" value="Send">
</form>
</body>
</html>