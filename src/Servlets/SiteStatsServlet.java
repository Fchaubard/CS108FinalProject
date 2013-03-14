package Servlets;

import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import Accounts.*;

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
	    	out.println(HTMLHelper.printHeader((Account)request.getSession().getAttribute("account")));
	    	
	    	AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
			if(request.getSession().getAttribute("account") != null) out.println(HTMLHelper.printNewsFeed(am.getAnnouncements()));
	    	
	    	out.println(HTMLHelper.contentStart());
	    	String title = "Site Statistics";
	    	ArrayList<String> actions = new ArrayList<String>();
	    	
	    	String dummyString = "";
	    	dummyString = dummyString + ("<a href = \"ProfileCatalogServlet\">Registered users</a>");
	    	rs = stmt.executeQuery("select count(*) from user");
	    	rs.next();
	    	dummyString = dummyString + (": " + rs.getInt("count(*)") + "");
	    	actions.add(dummyString);
	    	
	    	
	    	dummyString = ("<a href = \"ProfileCatalogServlet?special=banned\">Banned users</a>");
	    	rs = stmt.executeQuery("select count(*) from user where banned = true");
	    	rs.next();
	    	dummyString = dummyString + (": " + rs.getInt("count(*)") + "");
	    	actions.add(dummyString);
	    	
	    	dummyString = ("<a href = \"ProfileCatalogServlet?special=admin\">Administrators</a>");
	    	rs = stmt.executeQuery("select count(*) from user where admin = true");
	    	rs.next();
	    	dummyString = dummyString + (": " + rs.getInt("count(*)") + "");
	    	actions.add(dummyString);
	    	
	    	
	    	dummyString = ("<a href = \"QuizCatalogServlet\">Quizes</a>");
	    	rs = stmt.executeQuery("select count(*) from quiz");
	    	rs.next();
	    	dummyString = dummyString + (": " + rs.getInt("count(*)") + "");
	    	actions.add(dummyString);
	    	
	    	
	    	dummyString = ("Quizes Taken");
	    	rs = stmt.executeQuery("select count(*) from history");
	    	rs.next();
	    	dummyString = dummyString + (": " + rs.getInt("count(*)") + "");
	    	actions.add(dummyString);
	    	
	    	dummyString = ("Messages Sent");
	    	rs = stmt.executeQuery("select count(*) from message");
	    	rs.next();
	    	dummyString = dummyString + (": " + rs.getInt("count(*)") + "");
	    	actions.add(dummyString);
	    	
	    	
	    	dummyString = ("Challenges Sent");
	    	rs = stmt.executeQuery("select count(*) from message where quiz_id > 0");
	    	rs.next();
	    	dummyString = dummyString + (": " + rs.getInt("count(*)") + "");
	    	actions.add(dummyString);
	    	
	    	out.println(HTMLHelper.printActionList(HTMLHelper.STATISTICS_ICON, title, actions));
	    	out.println("<form action=\"SiteStatsServlet\" method=\"post\">");
	    	out.println("<input type=\"text\" name=\"message\">");
	    	out.println("<input type=\"submit\" value=\"Send Announcement\">");
	    	out.println("</form>");
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
    	String message = request.getParameter("message");
    	AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
   		am.storeAnnouncement(message);
    	doGet(request, response);
    }
}
