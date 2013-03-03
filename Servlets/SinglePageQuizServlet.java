package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String quizID = "1";// request.getParameter("quizID");
		
		try {
			quiz = new Quiz(Integer.parseInt(quizID));
			
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
			out.println("<form action=\"SolveServlet\" method=\"post\">");
			out.println("<br /><input id=\"startTime\" type=\"hidden\" value=\"" + System.currentTimeMillis()+"\"/>");
			for (int j = 0; j < quiz.getQuestions().size(); j++) {
				out.println("<h3>Question "+ (j+1) +"</h3>");
				out.println(quiz.getNextQuestion().toHTMLString()); // all the ids in the input fields must be unique
			}
			out.println("<br /><input type=\"submit\" value=\"Score Exam\"/>");
			out.println("</form>");
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}


