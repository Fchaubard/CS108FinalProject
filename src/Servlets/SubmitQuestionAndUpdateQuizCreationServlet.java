package Servlets;

import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.FillInTheBlank;
import model.Matching;
import model.MultipleChoice;
import model.PictureResponse;
import model.Question;
import model.QuestionResponse;
import model.Quiz;


/**
 * Servlet implementation class SubmitQuestionAndUpdateQuizCreationServlet
 */
@WebServlet("/SubmitQuestionAndUpdateQuizCreationServlet")
public class SubmitQuestionAndUpdateQuizCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Quiz quiz;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SubmitQuestionAndUpdateQuizCreationServlet() {
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
			//error
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
			out.println(HTMLHelper.printCSSLink());
			out.println("<title>"+quiz.getQuizName()+"</title>");
			out.println("</head>");
			out.println("<body>");
			out.println(HTMLHelper.printHeader());
			out.println(HTMLHelper.contentStart());
			out.println("<h1>"+quiz.getQuizName()+"</h1>");
			out.println(HTMLHelper.contentEnd());
			
			switch(Integer.parseInt((String)request.getParameter("questionType"))) {
			case 1: 
				String statementString = (String)request.getParameter("statement");
				HashSet<String> hashSet = new HashSet<String>();
				hashSet.add((String)request.getParameter("answer1"));
				if (!((String)request.getParameter("answer2")).equals("")) {
					hashSet.add((String)request.getParameter("answer2"));
					
				}
				if (!((String)request.getParameter("answer3")).equals("")) {
					hashSet.add((String)request.getParameter("answer3"));
					
				}
				
				question = new QuestionResponse(statementString, hashSet);
				quiz.addQuestion(question);
				break;
			
			case 2:
				String statementStringBefore = (String)request.getParameter("statementBefore");
				String statementStringAfter = (String)request.getParameter("statementAfter");
				statementString = statementStringBefore+ "__________" +statementStringAfter ;
				hashSet = new HashSet<String>();
				hashSet.add((String)request.getParameter("answer1"));
				if (!((String)request.getParameter("answer2")).equals("")) {
					hashSet.add((String)request.getParameter("answer2"));
					
				}
				if (!((String)request.getParameter("answer3")).equals("")) {
					hashSet.add((String)request.getParameter("answer3"));
					
				}
				
				question = new FillInTheBlank(statementString, hashSet);
				quiz.addQuestion(question);
				break;
				
			case 3:
				statementString = (String)request.getParameter("statement");
				hashSet = new HashSet<String>();
				hashSet.add((String)request.getParameter("wrongAnswer1"));
				hashSet.add((String)request.getParameter("wrongAnswer2"));
				hashSet.add((String)request.getParameter("wrongAnswer3"));
				
				String answer = (String)request.getParameter("answer1");
				question = new MultipleChoice(statementString, hashSet, answer);
				quiz.addQuestion(question);
				break;
				
			case 4:
				statementString = (String)request.getParameter("url");
				hashSet = new HashSet<String>();
				hashSet.add((String)request.getParameter("answer1"));
				if (!((String)request.getParameter("answer2")).equals("")) {
					hashSet.add((String)request.getParameter("answer2"));
					
				}
				if (!((String)request.getParameter("answer3")).equals("")) {
					hashSet.add((String)request.getParameter("answer3"));
					
				}
				
				question = new PictureResponse(statementString, hashSet);
				quiz.addQuestion(question);
				break;
				
			case 5:
				//question = MultipleAnswer.getHTMLInputString();
				break;
				
			case 6:
				//question = MultipleChoiceMultipleAnswer.getHTMLInputString();
				break;
				
			case 7:
				Matching matching_question = (Matching)session.getAttribute("matching_question");
				
				quiz.addQuestion(matching_question);
				session.removeAttribute("matching_question");
				
				
				break;
			
				
		}
			session.removeAttribute("Quiz");
			session.setAttribute("Quiz", quiz);
			out.println(HTMLHelper.contentStart());
			out.println("<br />Quiz Name: "+quiz.getQuizName()+"");
			out.println("<br />Quiz Description: " +quiz.getDescription()+"");
			out.println("<br />Quiz Category: " +quiz.getCategory()+"<br />");
			out.println(HTMLHelper.contentEnd());
			int counter =1;
			for (Question q : quiz.getQuestions()) {
				out.println(HTMLHelper.contentStart());
				out.println("<br /><br />Question "+counter +": <br />");
				out.println(q.toHTMLString());
				if (q.getType()!=7) { // doesnt make sense to print out answers because its implicit
					out.println("<br />Answer "+counter +": <br />");					
				}
				out.println(q.getCorrectAnswers());
				counter++;
				out.println(HTMLHelper.contentEnd());
			}
			out.println(HTMLHelper.contentStart());
			out.println("<br /><form  action=\"AddQuestion\" method=\"post\">");
			out.println("<br /><h2>Add another question?</h2>");
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
			out.println("<br /><input type=\"submit\" value=\"Add another question\"/>");
			out.println("</form>");
			out.println(HTMLHelper.contentEnd());
			out.println(HTMLHelper.contentStart());
			out.println("<form action=\"FinishQuizServlet\" method=\"post\">");
			out.println("<br /><br />Done Creating Quiz");
			out.println("<br /><input type=\"submit\" value=\"Done Creating Quiz\"/>");
			out.println("</form>");
			out.println(HTMLHelper.contentEnd());
			
			out.println("</body>");
			out.println("</html>");
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}


