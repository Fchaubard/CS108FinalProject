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
<%=HTMLHelper.printHeader() %>
<div class=content>
<div style= "font-size: large"><b>Account System Test</b></div>

<br>

You are currently visiting as a guest. Log in to save quiz results.
<form action="LoginServlet" method="post">
Username: <input type="text" name="user"><br>
Password: <input type="password" name="pass">
<input type="submit" value="Login">
</form>
<a href = "newAccount.html">create new account</a>
</div>
</body>
</html>