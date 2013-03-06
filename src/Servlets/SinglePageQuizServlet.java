package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Accounts.AccountManager;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import model.Question;
import model.Quiz;


/**
 * Servlet implementation class SinglePageQuizServlet
 */
@WebServlet("/SinglePageQuizServlet")
public class SinglePageQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Quiz quiz;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SinglePageQuizServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



		

		try {

			if (quiz ==null) {
					
				
				String quizID = request.getParameter("id");
				HttpSession session = request.getSession(true);
				session.setAttribute("quizID", quizID);
				
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
			}
			if( request.getParameter("ajax_id")==null){
				quiz.randomizeQuestions();

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
				out.println("<h1>"+quiz.getQuizName()+"</h1>");
				out.println("<form action=\"SolveServlet\" method=\"post\">");
				out.println("<br /><input name=\"startTime\" type=\"hidden\" value=\"" + System.currentTimeMillis()+"\"/>");
				out.println("<br /><input name=\"quizID\" type=\"hidden\" value=\"" +quiz.getQuiz_id()+"\"/>");
				Question q;
				for (int j = 0; j < quiz.getQuestions().size(); j++) {
					out.println("<h3>Question "+ (j+1) +"</h3>");
					q = quiz.getNextQuestion();
					out.println(q.toHTMLString()); // all the ids in the input fields must be unique
					if (quiz.isImmediateCorrection()) {
						String stringID = q.getType()+"_"+q.getqID();
						out.println(Quiz.ajaxHTMLText(j,stringID));
					}
					
				}
				out.println("<br /><input type=\"submit\" value=\"Score Exam\"/>");
				out.println("</form>");
				out.println("</body>");
				out.println("</html>");

			}
			else{
				int i = Integer.parseInt(request.getParameter("ajax_id"));

				String text = quiz.getQuestions().get(i).getCorrectAnswers();

				response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
				response.setCharacterEncoding("UTF-8"); // You want world domination, huh?
				response.getWriter().write(text);       // Write response body.

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
}


