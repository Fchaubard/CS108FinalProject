<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>compose message</title>
</head>
<body>
<%= helpers.HTMLHelper.printHeader() %>
<form action="MailManagementServlet" method="post">
<% String from = ((Accounts.Account) request.getSession().getAttribute("account")).getName(); 
	String to = (String) request.getParameter("to");
	String quiz = (String) request.getParameter("quiz");
	String sub = "";
	if (to == null) to = "";
	if (quiz == null) quiz = "";
	if (request.getParameter("sub") != null) sub = "RE:" + (String) request.getParameter("sub");
out.println("Sender:" + from);%>
<br>
<input type="hidden" name="sender" value = "<% out.print(from); %>">
Recipient: <input type="text" name="recipient" value = "<% out.println(to); %>"><br>
subject: <input type="text" name="subject" value = "<% out.println(sub); %>"><br>
(optional) challenge ID: <input type="text" name="challenge" value = "<% out.println(quiz); %>"><br>
message: <textarea cols = "50" rows = "5" name = "body"></textarea>
<input type="submit" value="Send">
</form>
</body>
</html>