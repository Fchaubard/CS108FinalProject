<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html5/strict.dtd">
<html>
<% String name = (String) getServletContext().getAttribute("user");%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><% out.println("Welcome " + name); %> </title>
</head>
<body>
<div style= "font-size: large"><b><% out.println("Hello " + name); %></b></div>
<br>
<a href = "login.html">The exit's that-a way.</a>
</body>
</html>