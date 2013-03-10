package Servlets;

import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import Accounts.AccountManager;
import Accounts.MailManager;
import Accounts.Message;

/**
 * Servlet implementation class AcctManagementServlet
 */
@WebServlet("/SiteStatsServlet")
public class SiteStatsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SiteStatsServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Connection con = (Connection) request.getServletContext().getAttribute("connect");
    	try {
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs;
			response.setContentType("text/html");
	    	PrintWriter out = response.getWriter();
	    	out.println("<head>");
	    	out.println(HTMLHelper.printCSSLink());
	    	out.println("</head");
	    	out.println("<body>");
	    	out.println(HTMLHelper.printHeader());
	    	out.println(HTMLHelper.contentStart());
	    	out.println("<h3>Site Statistics</h3>");
	    	out.println("<a href = \"ProfileCatalogServlet\">Registered users</a>");
	    	rs = stmt.executeQuery("select count(*) from user");
	    	rs.next();
	    	out.println(": " + rs.getInt("count(*)") + "<br>");
	    	out.println("<a href = \"ProfileCatalogServlet\">Banned users</a>");
	    	rs = stmt.executeQuery("select count(*) from user where banned = true");
	    	rs.next();
	    	out.println(": " + rs.getInt("count(*)") + "<br>");
	    	out.println("<a href = \"ProfileCatalogServlet\">Administrators</a>");
	    	rs = stmt.executeQuery("select count(*) from user where admin = true");
	    	rs.next();
	    	out.println(": " + rs.getInt("count(*)") + "<br>");
	    	out.println("<a href = \"QuizCatalogServlet\">Quizes</a>");
	    	rs = stmt.executeQuery("select count(*) from quiz");
	    	rs.next();
	    	out.println(": " + rs.getInt("count(*)") + "<br>");
	    	out.println("Quizes Taken");
	    	rs = stmt.executeQuery("select count(*) from history");
	    	rs.next();
	    	out.println(": " + rs.getInt("count(*)") + "<br>");
	    	out.println("Messages Sent");
	    	rs = stmt.executeQuery("select count(*) from message");
	    	rs.next();
	    	out.println(": " + rs.getInt("count(*)") + "<br>");
	    	out.println("Challenges Sent");
	    	rs = stmt.executeQuery("select count(*) from message where quiz_id > 0");
	    	rs.next();
	    	out.println(": " + rs.getInt("count(*)") + "<br>");
	    	out.println(HTMLHelper.contentEnd());
	    	out.println("</body>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    }
}
