package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.*;

/**
 * Servlet implementation class AddMultipleChoiceMultipleAnswerServlet
 */
@WebServlet("/AddMultipleChoiceMultipleAnswerServlet")
public class AddMultipleChoiceMultipleAnswerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMultipleChoiceMultipleAnswerServlet() {
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
		HttpSession session = request.getSession(true);
		Quiz quiz;
		if (session.getAttribute("Quiz")==null) {
			return; // this should never happen
			
		}
		else{
			quiz = (Quiz) session.getAttribute("Quiz");
		}
		
		String type = request.getParameter("questionType");
		
		String question = (String)request.getAttribute("question");
		String choices = (String)request.getAttribute("choices");
		Set<String> options = new HashSet<String>();
		
		String[] strings = choices.split(Pattern.quote("\r\n"));
		for (String s : strings) {
			options.add(s);
		}
		
		session.setAttribute("MultipleChoiceMultipleAnswerOptions", options);
		session.setAttribute("MultipleChoiceMultipleAnswerQuestion", question);
		
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
			out.println("<h3>Input Question "+(quiz.getQuestions().size()+1)+"</h3>");
			
			out.println("<form action=\"SubmitQuestionAndUpdateQuizCreationServlet\" method=\"post\">");
			out.println("<input name=\"questionType\" type=\"hidden\" value=\"" +type+"\"/>");
			out.println("<br />Please select all the correct answers for the following question: ");
			out.println("<br /><br />" + question);
			for(String s : options) {
				out.println("<input type=\"checkbox\" name=\"options\" value=\"" + s + "\">" + s + "<br>");
			}
			
			out.println("<br /><input type=\"submit\" value=\"Save Multiple-Choice-Multiple-Answer Question\"/>");
			out.println("</form>");
			out.println("</body>");
			out.println("</html>");
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
