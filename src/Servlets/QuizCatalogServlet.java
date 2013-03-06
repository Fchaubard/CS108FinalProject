package Servlets;

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
			rs = stmt.executeQuery("Select quiz_id, name from quiz");
			response.setContentType("text/html");
	    	PrintWriter out = response.getWriter();
			out.println("<ul>");
			while (rs.next()) {
				String name = rs.getString("name");
				int id = rs.getInt("quiz_id");
				out.println("<li><a href= \"QuizTitleServlet?id="+id+"\">"+name+"</a></li>");
			}
			out.println("</ul>");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("oops!");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}