<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ page import = "helpers.*" %>
     <%@ page import = "Accounts.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="mufasa.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>compose message</title>
</head>
<body>
<%= helpers.HTMLHelper.printHeader((Accounts.Account)request.getSession().getAttribute("account")) %>
<%AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
if(request.getSession().getAttribute("account") != null) out.println(HTMLHelper.printNewsFeed(am.getAnnouncements()));%>
<form action="MailManagementServlet" method="post">
<% String from = ((Accounts.Account) request.getSession().getAttribute("account")).getName(); 
	String to = (String) request.getParameter("to");
	String quiz = (String) request.getParameter("quiz");
	String sub = (String) request.getParameter("subject");
	String body = (String) request.getParameter("body");
	if (to.equals("error")) out.println("<b>Recpient not found in system</b>");
	if (quiz == null) quiz = "";
	if (sub == null) sub = "";
	if (body == null) body = "";%>
<br>
<input type="hidden" name="sender" value = "<% out.print(from); %>">
<input type="hidden" name="quiz" value = "<% out.print(quiz); %>">
<table border = 0>
<tr><td>Sender:</td><td><% out.println(from);%></td></tr>
<%if (quiz.length() > 0) out.println("<tr><td>Challenge ID:</td><td>"+ quiz +"</td></tr>");%>
<!-- <tr><td>challenge ID:</td><td><input type="text" name="challenge" value = "<% out.println(quiz); %>"></td></tr>-->
<tr><td>Recipient:</td><td><input type="text" name="recipient value = "<% out.println(to); %>"></td></tr>
<tr><td>subject:</td><td><input type="text" name="subject" value = "<% out.println(sub); %>"></td></tr>
<tr><td>message:</td><td><textarea cols = "50" rows = "5" name = "body"><% out.println(body); %></textarea></td></tr>
</table>
<input type="submit" value="Send">
</form>
</body>
</html>