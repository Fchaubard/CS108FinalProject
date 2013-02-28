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
<form action="AcctManagementServlet" method="post">
<input type="hidden" name ="Action" value="Delete">
<% out.print("<input type=\"hidden\" name =\"User\" value=\"" + name + "\">");%>
<input type="submit" value="Delete Account">
</form>
<a href = "homeDebug.jsp">Home</a>
<a href = "newMessage.jsp?user=<%out.print(name);%>">Compose Mail</a>
<a href = "MailManagementServlet?&index=inbox&user=<%out.print(name);%>">inbox</a>
<a href = "MailManagementServlet?&index=outbox&user=<%out.print(name);%>">outbox</a>
</body>
</html>