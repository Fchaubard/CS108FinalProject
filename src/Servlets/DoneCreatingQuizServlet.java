package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sun.font.TrueTypeFont;

import model.FillInTheBlank;
import model.Matching;
import model.MultipleAnswer;
import model.MultipleChoice;
import model.MultipleChoiceMultipleAnswer;
import model.PictureResponse;
import model.Question;
import model.QuestionResponse;
import model.Quiz;


/**
 * Servlet implementation class DoneCreatingQuizServlet
 */
@WebServlet("/DoneCreatingQuizServlet")
public class DoneCreatingQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Quiz quiz;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DoneCreatingQuizServlet() {
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
		//public Quiz(ArrayList<Question> q, boolean random, boolean onePage, boolean immediateCorrect, boolean practice, int userID, String quizName, String description, String category){
		Quiz quiz;
		if (session.getAttribute("Quiz")==null) {
			return;
		}
		else{
			quiz = (Quiz) session.getAttribute("Quiz");
		}
		
		Question question;
		try {
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
		
			session.removeAttribute("Quiz");

			try {
				quiz.finishAndStoreQuizInDatabase();
				out.println("<br /><h1>Quiz was submitted successfully!</h1>");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				out.println("<br /><h1>Quiz failed to submit</h1>");
				e.printStackTrace();
			}

			
			out.println("<br />Quiz Name: "+quiz.getQuizName()+"");
			out.println("<br />Quiz Description: " +quiz.getDescription()+"");
			out.println("<br />Quiz Category: " +quiz.getCategory()+"<br />");
			int counter =1;
			for (Question q : quiz.getQuestions()) {
				out.println("<br /><br />Question "+counter +": <br />");
				out.println(q.toHTMLString());
				out.println("<br />Answer "+counter +": <br />");
				out.println(q.getCorrectAnswers());
				counter++;
			}
			
				
			out.println("<form action=\"HomePage\" method=\"post\">");
			out.println("<br /><br />Done Creating Quiz");
			out.println("<br /><input type=\"submit\" value=\"Go Home\"/>");
			out.println("</form>");
			
			out.println("</body>");
			out.println("</html>");
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}


