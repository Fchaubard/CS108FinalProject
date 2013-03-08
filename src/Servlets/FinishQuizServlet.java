package Servlets;

import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.FillInTheBlank;
import model.MultipleChoice;
import model.PictureResponse;
import model.Question;
import model.QuestionResponse;
import model.Quiz;

/**
 * Servlet implementation class FinishQuizServlet
 */
@WebServlet("/FinishQuizServlet")
public class FinishQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FinishQuizServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Quiz quiz = (Quiz) request.getSession().getAttribute("Quiz");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
				      + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		out.println("<html xmlns='http://www.w3.org/1999/xhtml'>");
		out.println("<head>");
		out.println(HTMLHelper.printCSSLink());
		out.println("<title>"+quiz.getQuizName()+"</title>");
		out.println("</head>");
		out.println("<body>");
		out.println(HTMLHelper.printHeader());
		out.println("<h1>"+quiz.getQuizName()+"</h1>");
		
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
		
		try {
			quiz.finishAndStoreQuizInDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.getSession().removeAttribute("Quiz");
		
		out.println("<form action=\"QuizCatalogServlet\" method=\"get\">");
		out.println("<br /><br />Click to Save Quiz");
		out.println("<br /><input type=\"submit\" value=\"Save Quiz\"/>");
		out.println("</form>");
		
		out.println("</body>");
		out.println("</html>");
	}

}
