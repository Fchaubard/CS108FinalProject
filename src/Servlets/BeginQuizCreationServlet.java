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
import javax.servlet.http.HttpSession;

import model.Question;
import model.Quiz;


/**
 * Servlet implementation class BeginQuizCreationServlet
 */
@WebServlet("/BeginQuizCreationServlet")
public class BeginQuizCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Quiz quiz;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BeginQuizCreationServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		try {
			
			
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
			out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
					      + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			out.println("<html xmlns='http://www.w3.org/1999/xhtml'>");
			out.println("<head>");
			out.println(HTMLHelper.printCSSLink());
			out.println("<title>Create a Quiz</title>");
			out.println("</head>");
			out.println("<body>");
			out.println(HTMLHelper.printHeader());
			out.println(HTMLHelper.contentStart());
			out.println("<h1>Create a Quiz</h1>");
			out.println("<form action=\"AddQuestion\" method=\"post\">");
			out.println("<br />Quiz Name: <input name=\"quizName\" type=\"text\" />");
			out.println("<br />Quiz Description: <textarea name = \"quizDescription\" rows=\"4\" cols=\"50\"> </textarea>");
			//out.println("<br />Quiz Description: <input name=\"quizDescription\" type=\"text\" rows=\"4\" cols=\"50\" />");
			out.println("<br />Quiz Category: <input name=\"quizCategory\" type=\"text\" />");
			out.println("<br /><input type=\"checkbox\" name=\"random\" value=\"true1\"> Random Question Ordering?");
			out.println("<br /><input type=\"checkbox\" name=\"multipage\" value=\"true2\"> MultiPage Question Display?");
			out.println("<br /><input type=\"checkbox\" name=\"immediateCorrection\" value=\"true3\"> Immediate Correction?");
			out.println("<br />Question Type:<select name=\"questionType\">");
			out.println("<option value=\"1\">Question Response</option>");
			out.println("<option value=\"2\">Fill in the blank</option>");
			out.println("<option value=\"3\">Multiple Choice</option>");
			out.println("<option value=\"4\">Picture Response</option>");
			out.println("<option value=\"5\">Multiple Answer</option>");
			out.println("<option value=\"6\">Multiple Choice with Multiple Answer</option>");
			out.println("<option value=\"7\">Matching</option>");
			out.println("<option value=\"8\">Random Multiple Choice from Wikipedia</option>");
			out.println("</select>");
			out.println("<br /><input type=\"submit\" value=\"Add first question\"/>");
			out.println("</form>");
			out.println(HTMLHelper.contentEnd());
			out.println("</body>");
			out.println("</html>");
			
		} catch (NumberFormatException e) {
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


