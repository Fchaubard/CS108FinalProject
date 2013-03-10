package Servlets;

import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import Accounts.Account;

/**
 * Servlet implementation class QuizCatalogServlet
 */
@WebServlet("/QuizCatalogServlet")
public class QuizCatalogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuizCatalogServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = (Connection) request.getServletContext().getAttribute("connect");
		Statement stmt;
		ResultSet rs;
		try {
			stmt = (Statement) con.createStatement();
			String searchQuizes = "Select quiz_id, name from quiz";
			if (request.getParameter("search") != null && request.getParameter("search").length() > 0) {
				searchQuizes += " where name like \"%"+request.getParameter("search")+"%\"";
			}
			rs = stmt.executeQuery(searchQuizes);
			response.setContentType("text/html");
	    	PrintWriter out = response.getWriter();
	    	out.println("<head>");
	    	out.println(HTMLHelper.printCSSLink());
	    	out.println("</head");
	    	out.println("<body>");
	    	out.println(HTMLHelper.printHeader());
	    	out.println(HTMLHelper.contentStart());
	  
	    	out.println("<form action=\"QuizCatalogServlet\" method=\"get\">");
	    	out.println("Search Quizes: <input type=\"text\" name=\"search\"/>");
	    	out.println("<input type=\"submit\" value=\"Search\"/>");
			out.println("</form>");
			out.println("<ul>");
			while (rs.next()) {
				String name = rs.getString("name");
				int id = rs.getInt("quiz_id");
				out.println("<li><a href= \"QuizTitleServlet?id="+id+"\">"+name+"</a></li>");
			}
			out.println("</ul>");
			out.println(HTMLHelper.contentEnd());
			out.println("</body>");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("oops!");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		//TODO:
		//Delete quiz -> get questions from qqm-> delete questions->delete qqm entry
		//Connection con = (Connection) request.getServletContext().getAttribute("connect");
		//try {
		//	Statement stmt = (Statement) con.createStatement();
		//	stmt.executeUpdate("delete from quiz where quiz_id = "+id);
		//} catch (SQLException e) {
		//}
		System.out.println("Quiz deletion not implemented");
		doGet(request, response);
	}
}