package Servlets;

import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Quiz;
import model.QuizAttempts;

import Accounts.Account;
import Accounts.AccountManager;

/**
 * Servlet implementation class HistoryServlet
 */
@WebServlet("/HistoryServlet")
public class HistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HistoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
    	PrintWriter out = response.getWriter();
    	out.println("<head>");
    	out.println(HTMLHelper.printCSSLink());
    	out.println("</head>");
    	
    	out.println(HTMLHelper.printHeader((Account)request.getSession().getAttribute("account")));
    	
    	out.println(HTMLHelper.contentStart());
		AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
		int userID = -1;
		try {
		userID = Integer.parseInt(request.getParameter("user"));
		} catch (NumberFormatException e) {}
		int quizID = -1;
		try {
			quizID = Integer.parseInt(request.getParameter("quiz"));
		} catch (NumberFormatException e) {}
		ArrayList<QuizAttempts> history = am.getHistory(userID, quizID);
		out.println("<table border = \"0\">");
		out.println("<tr><b><td>User</td><td>Quiz</td><td>Score</td><td>Time</td><td>Date</td></b></tr>");
		for (QuizAttempts qa : history) {
			try {
				Quiz q = new Quiz(qa.getQuizID(), am.getCon());
				Account a = am.getAccount(qa.getUserID());
				out.println("<tr><td><a href = \"ProfileServlet?user="+qa.getUserID()+"\">" + a.getName()+"</a>");
				out.println("</td><td><a href = \"QuizTitleServlet?id="+qa.getQuizID()+"\">" + q.getQuizName());
				out.println("</td><td>" + qa.getScore());
				out.println("</td><td>" + qa.getTime()/1000.0);
				out.println("</td><td>" + qa.getDate());
				out.println("</td></tr>");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		out.println("</table>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}