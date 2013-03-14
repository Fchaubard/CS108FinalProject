<%@page import="helpers.HTMLHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ page import="Accounts.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href="mufasa.css">
<title>Moses-Foe Quiz Project</title>
</head>
<body>
<%=HTMLHelper.printHeader((Account)request.getSession().getAttribute("account")) %>
<%AccountManager am = (AccountManager) request.getSession().getAttribute("accounts");
if(request.getAttribute("account") != null) out.println(HTMLHelper.printNewsFeed(am.getAnnouncements())); %>
<%=HTMLHelper.contentStart() %>
<div style= "font-size: large"><b>Login</b></div>

<br>
<% String err = request.getParameter("err");
	if (err == null) {
		out.println("You are currently visiting as a guest. Log in to save quiz results.");
	} else if (err.equals("badLogin")) {
		out.println("Invalid username or password");
	} else if (err.equals("banned")) {
		out.println("Account has been banned from the server");
	}%>
<form action="LoginServlet" method="post">
Username: <input type="text" name="user"><br>
Password: <input type="password" name="pass">
<input type="submit" value="Login">
</form>
<a href = "newAccount.html">create new account</a>
<%=HTMLHelper.contentEnd() %>
</body>
</html>