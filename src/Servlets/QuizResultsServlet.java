package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

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
 * Servlet implementation class QuizResultsServlet
 */
@WebServlet("/QuizResultsServlet")
public class QuizResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Quiz quiz;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuizResultsServlet() {
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
		
		String quizID = (String)session.getAttribute("quizID");
		
		try {
			
			
			//means the cart hasnt been initialized
			if(session.getAttribute("quiz_"+quizID) == null){
				ServletContext sc = request.getServletContext();
				AccountManager am = (AccountManager) sc.getAttribute("accounts");
				quiz = new Quiz(Integer.parseInt(quizID), am.getCon());
				session.setAttribute("quiz_"+quizID, quiz);
			}
			else{
				quiz = (Quiz) session.getAttribute("quiz_"+quizID);
			}
			QuizAttempts qa = (QuizAttempts) session.getAttribute("qa");
			
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
			out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
					      + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			out.println("<html xmlns='http://www.w3.org/1999/xhtml'>");
			out.println("<head>");
			
			out.println("<title>"+quiz.getQuizName()+"</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>"+quiz.getQuizName()+"</h1><br />");
			out.println("<h3>Score: "+qa.score+"</h3><br />");
			out.println("<h3>Time: "+(qa.time/1000)+" s" +"</h3><br />");
			
			out.println("<h3>Top Scorers</h3>");
	    	out.println("<ol>");
	    	//currently prints ALL the scores. Can switch to a for (0-4) but table needs to be sorted first.
	    	for (QuizAttempts attempt : quiz.getHistory()) {
	    		out.println("<li>" + attempt.printAttemt((AccountManager)request.getServletContext().getAttribute("accounts")) + "</li>");
	    	}
	    	out.println("</ol><br>");
			
	    	out.println("<h3>Correct Answers</h3>");
	    	out.println("<ol>");
			for(Question q : quiz.getQuestions()) {
				out.println("<li>" + q.getCorrectAnswers() + " (Your Answers: " + q.getUserAnswers() + ")");
			}
			out.println("</ol><br>");
			
			out.println("<form action=\"LoginServlet\" method=\"post\">");
			out.println("<br /><input type=\"submit\" value=\"Go Home\"/>");
			out.println("</form>");
			
			/*for (int j = 0; j < quiz.getQuestions().size(); j++) {
				// output the questions and the answers
				out.println("<h3>Question "+ (j+1) +"</h3>");
				out.println(quiz.getNextQuestion().getCorrectAnswers()); // all the ids in the input fields must be unique
			}*/
			out.println("</body>");
			out.println("</html>");
			
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


