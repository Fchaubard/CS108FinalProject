<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ page import="Accounts.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login</title>
</head>
<body>
<div style= "font-size: large"><b>Account System Test</b></div>

<% Account acct = (Account) request.getSession().getAttribute("account"); %>

Logged in as <% if (acct != null) out.println(acct.getName()); else out.println("guest"); %>

<br>

<%if (acct == null) {
	//actual homepage'll probably use a servlet or methods; this is just to check if the account system works.
	out.println("please log in.");
	out.println("<form action=\"LoginServlet\" method=\"post\">");
	out.println("Username: <input type=\"text\" name=\"user\"><br>");
	out.println("Password: <input type=\"text\" name=\"pass\">");
	out.println("<input type=\"submit\" value=\"Login\">");
	out.println("</form>");
	out.println("<a href = \"newAccount.html\">create new account</a>");
} else {
	out.println("<form action=\"AcctManagementServlet\" method=\"post\">");
	out.println("<input type=\"hidden\" name =\"Action\" value=\"Logout\">");
	out.println("<input type=\"submit\" value=\"Logout\">");
	out.println("</form>");
	out.println("<a href = \"accountDebug.jsp\">Profile</a>");
}
%>
</body>
</html>