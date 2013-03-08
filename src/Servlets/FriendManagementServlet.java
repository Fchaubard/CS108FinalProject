package Servlets;

    	import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

    	import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

    	import Accounts.Account;
import Accounts.AccountManager;

    	/**
    	 * Servlet implementation class AcctManagementServlet
    	 */
    	@WebServlet("/FriendManagementServlet")
    	public class FriendManagementServlet extends HttpServlet {
    		private static final long serialVersionUID = 1L;
    	    
    	    /**
    	     * @see HttpServlet#HttpServlet()
    	     */
    	    public FriendManagementServlet() {
    	        super();
    	    }

    	    /**
    	     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
    	     */
    	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	    	Account user = (Account) request.getSession().getAttribute("account");
    	    	AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
    	    	ArrayList<Account> requests = am.getFriendRequests(user.getId());
    	    	ArrayList<Account> friends = am.getFriends(user.getId());
    	    	response.setContentType("text/html");
    	    	PrintWriter out = response.getWriter();
    	    	out.println("<head>");
    	    	out.println(HTMLHelper.printCSSLink());
    	    	out.println("/<head>");
    	    	out.println(HTMLHelper.printHeader());
    	    	out.println("<h3>Requests</h3>");
    	    	out.println("<ul>");
    	    	for (Account a : requests) {
    	    		out.println("<li><form action=\"FriendManagementServlet\" method=\"post\">");
    	    		out.println("<a href = \"ProfileServlet?user="+a.getName()+"\">"+a.getName()+"</a>");
    	    		out.println("<input type=\"hidden\" name=\"ID\" value=\""+a.getId()+"\">");
    	    		out.println("<input type=\"submit\" name=\"action\" value=\"delete\">");
    	    		out.println("<input type=\"submit\" name=\"action\" value=\"add\">");
    	    		out.println("</form></li>");
    	    	}
    	    	out.println("</ul>");
    	    	out.println("<h3>Friends</h3>");
    	    	out.println("<ul>");
    	    	for (Account a : friends) {
       	    		out.println("<li><form action=\"FriendManagementServlet\" method=\"post\">");
    	    		out.println("<a href = \"ProfileServlet?user="+a.getName()+"\">"+a.getName()+"</a>");
    	    		out.println("<input type=\"hidden\" name=\"ID\" value=\""+a.getId()+"\">");
    	    		out.println("<input type=\"submit\" name=\"action\" value=\"delete\">");
    	    		out.println("</form></li>");
    	    	}
    	    	out.println("</ul>");
    	    }

    	    /**
    	     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    	     */
    	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	    	String action = request.getParameter("action");
    	    	int friendID = Integer.parseInt(request.getParameter("ID"));
    	    	AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
    	    	int myID = ((Account) request.getSession().getAttribute("account")).getId();
    	    	if (action.equals("delete")) {
    	    		am.deleteFriend(myID, friendID);
    	    	} else if (action.equals("add")) {
    	    		System.out.println(friendID + " " + myID);
    	    		am.makeFriend(myID, friendID);
    	    	}
    	    	request.getRequestDispatcher("/UserHome.jsp").forward(request, response);
    	    }
}