package Servlets;

import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Accounts.AccountManager;

import model.Question;
import model.Quiz;

import model.QuizAttempts;
/**
 * Servlet implementation class SolveServlet
 */
@WebServlet("/SolveServlet")
public class SolveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Quiz quiz;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SolveServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
			HttpSession session = request.getSession(true);
			String quizID = (String) session.getAttribute("quizID");
			
			//means the cart hasnt been initialized
			if(session.getAttribute("quiz_"+quizID) == null){

				try {
					ServletContext sc = request.getServletContext();
					AccountManager am = (AccountManager) sc.getAttribute("accounts");
					quiz = new Quiz(Integer.parseInt(quizID), am.getCon());
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				session.setAttribute("quiz_"+quizID, quiz);
			}
			else{
				quiz = (Quiz) session.getAttribute("quiz_"+quizID);
			}
			
			Integer score = 0;
			// Solve the exam
			for (Question q : quiz.getQuestions()) {
				if(q.getType()!=5 || q.getType()!=6){
					ArrayList<String> answersArrayList = new ArrayList<String>();
					String paramterString = Integer.toString(q.getType())+"_"+Integer.toString(q.getqID());
					String string = (String) request.getParameter(paramterString);
					answersArrayList.add(string);
					score+=q.solve(answersArrayList);
					q.setUserAnswers(answersArrayList);
				}
			}
			
			score = (int)(((double)score/quiz.totalScore()) * 100);
			
			String timer = (String) request.getParameter("startTime");
			int time = (int)(-Long.parseLong(timer) + (long)System.currentTimeMillis());
			
			QuizAttempts qa = new QuizAttempts(new Integer(1), (Integer) Integer.parseInt(quizID), score, (java.util.Date) new Date(), (int)time);
			
			if(!quiz.isPracticeMode()) {
				try {
					qa.pushAttemptToDB((Connection)request.getServletContext().getAttribute("connect"));
					quiz.addToHistory(qa);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (session.getAttribute("qa") != null) {
				session.removeAttribute("qa");
			}
			
			session.setAttribute("qa", qa);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<head>");
			out.println(HTMLHelper.printCSSLink());
			out.println("</head>");
			out.println(HTMLHelper.printHeader());
			out.println("<form action=\"QuizResultsServlet\" method=\"post\">");
			out.println("<br /><input type=\"submit\" value=\"Exam is Scored...Click to see results!\"/>");
			out.println("</form>");
			
			//RequestDispatcher rd = request.getRequestDispatcher("QuizResultsServlet");
			//rd.forward(request, response);
	}
}


