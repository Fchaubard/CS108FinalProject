<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="mufasa.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%=helpers.HTMLHelper.printHeader() %>
<%= helpers.HTMLHelper.contentStart() %>
<% out.println(((Accounts.Account)request.getSession().getAttribute("account")).getName()); %>
<form action="AcctManagementServlet" method="post">
	<input type="hidden" name ="Action" value="Logout">
	<input type="submit" value="Logout">
	</form>
	<a href = "ProfileServlet?user=<%out.println(((Accounts.Account)request.getSession().getAttribute("account")).getName()); %>">Profile</a>
	<a href = "QuizCatalogServlet">Take a Quiz!</a>
	<a href = "BeginQuizCreationServlet">Create a Quiz!</a>
<%= helpers.HTMLHelper.contentEnd() %>
</body>
</html>