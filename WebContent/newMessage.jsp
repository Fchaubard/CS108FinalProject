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
Account user = (Account) request.getSession().getAttribute("account");
if(request.getSession().getAttribute("account") != null) out.println(HTMLHelper.printNewsFeed(am.getAnnouncements(),am.getNews(user.getId())));
%>
<%=HTMLHelper.contentStart() %>
<form action="MailManagementServlet" method="post">
<% String from = ((Accounts.Account) request.getSession().getAttribute("account")).getName(); 
	boolean errSet = false;
	String to = (String) request.getParameter("to");
	String quiz = (String) request.getParameter("quiz");
	String lockSubject = "";
	String sub = (String) request.getParameter("subject");
	String body = (String) request.getParameter("body");
	if (to != null && to.equals("error")) {
		out.println("<b>Recpient not found in system</b><br>");
		errSet = true;
	} else if (to == null)  {
		to = "";
	}
	if (sub == null) sub = "";
	if (body == null) body = "";
	if (quiz == null)  {
		quiz = "";
	} else {
		sub = "I challenge you!";
		lockSubject = "readonly";
		if (!errSet) out.println("<b>Enter a user and optional message.</b><br>");
		errSet = true;
	}
	//if (!errSet) out.println("<b>Enter a user and optional message.</b><br>");%>
<br>
<input type="hidden" name="sender" value = "<% out.print(from); %>">
<input type="hidden" name="quiz" value = "<% out.print(quiz); %>">
<table border = 0>
<tr><td>Sender:</td><td><% out.println(from);%></td></tr>
<%if (quiz.length() > 0) out.println("<tr><td>Challenge ID:</td><td>"+ quiz +"</td></tr>");%>
<!-- <tr><td>challenge ID:</td><td><input type="text" name="challenge" value = "<% out.println(quiz); %>"></td></tr>-->
<tr><td>Recipient:</td><td><input style="width:100%" type="text" name="recipient" value = "<% out.println(to); %>"></td></tr>
<tr><td>subject:</td><td><input style="width:100%" type="text" name="subject" value = "<% out.println(sub); %>" <% out.println(lockSubject); %>></td></tr>
<tr><td>message:</td><td><textarea style="width:100%" cols = "50" rows = "5" name = "body"><% out.println(body); %></textarea></td></tr>
</table>
<input type="submit" value="Send">
</form>
<%=HTMLHelper.contentEnd() %>
</body>
</html>