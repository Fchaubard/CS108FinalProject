package Servlets;

import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Question;
import model.Quiz;
import Accounts.Account;
import Accounts.AccountManager;

/**
 * Servlet implementation class QuizEditServlet
 */
@WebServlet("/QuizEditServlet")
public class QuizEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Quiz quiz;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String quizID;

		if (request.getParameter("id")==null) {
			quizID = (String) session.getAttribute("quizID");
		}else{
			quizID = request.getParameter("id");
		}
		System.out.println(quizID);

		try {
				session.setAttribute("quizID", quizID);
				//means the quiz hasn't been initialized
				if(session.getAttribute("quiz_"+quizID) == null){
					ServletContext sc = request.getServletContext();
					AccountManager am = (AccountManager) sc.getAttribute("accounts");
					quiz = new Quiz(Integer.parseInt(quizID), am.getCon());
					session.setAttribute("quiz_"+quizID, quiz);
				}
				else{
					quiz = (Quiz) session.getAttribute("quiz_"+quizID);
				}
				quiz.setPracticeMode(false);
			System.out.println(quiz.getQuizName());
			
			

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
				out.println(HTMLHelper.printHeader((Account)request.getSession().getAttribute("account")));
				
				AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
				Account user = (Account) request.getSession().getAttribute("account");
				if(request.getSession().getAttribute("account") != null) out.println(HTMLHelper.printNewsFeed(am.getAnnouncements(),am.getNews(user.getId())));
				
				out.println(HTMLHelper.contentStart());
				out.println("<h1>Editing Quiz: "+quiz.getQuizName()+"</h1>");
				
				out.println(HTMLHelper.contentEnd());
				out.println(HTMLHelper.contentStart());
				out.println("<form action=\"QuizUpdateServlet\" method=\"post\">");
				out.println("<input name=\"quizID\" type=\"hidden\" value=\"" +quiz.getQuiz_id()+"\"/>");
				out.println("<br />Quiz Name: <input name=\"quizName\" type=\"text\" value=\"" +quiz.getQuizName()+"\"/>");
				out.println("<br />Quiz Description: <textarea name = \"quizDescription\" value=\"" +quiz.getDescription()+"\" rows=\"4\" cols=\"50\">" +quiz.getDescription()+"</textarea>");
				out.println("<br />Quiz Category: <input name=\"quizCategory\" type=\"text\" value=\"" +quiz.getCategory()+"\"/>");
				if (quiz.isRandom()) {
					out.println("<br /><input type=\"checkbox\" name=\"random\" value=\"randomTrue\" checked> Random Question Ordering?");
					
				}else{
					out.println("<br /><input type=\"checkbox\" name=\"random\" value=\"randomTrue\"> Random Question Ordering?");
					
				}
				if (quiz.isOnePageMultiPage()) {
					out.println("<br /><input type=\"checkbox\" name=\"multipage\" value=\"multipageTrue\" checked> MultiPage Question Display?");
					
				}else{
					out.println("<br /><input type=\"checkbox\" name=\"multipage\" value=\"multipageTrue\"> MultiPage Question Display?");
					
				}
				if (quiz.isImmediateCorrection()) {
					out.println("<br /><input type=\"checkbox\" name=\"immediateCorrection\" value=\"immediateCorrectionTrue\" checked> Immediate Correction?");
					
				}else{
					out.println("<br /><input type=\"checkbox\" name=\"immediateCorrection\" value=\"immediateCorrectionTrue\"> Immediate Correction?");
					
				}
				
				out.println("<br /><input type=\"checkbox\" name=\"clearHistory\" value=\"clearHistoryTrue\"> Clear Quiz History after this edit?");
				out.println(HTMLHelper.contentEnd());
				Question q;
				for (int j = 0; j < quiz.getQuestions().size(); j++) {
					out.println(HTMLHelper.contentStart());
					out.println("<h3>Question "+ (j+1) +"</h3>");
					q = quiz.getNextQuestion();
					out.println("<br /><input type=\"checkbox\" name=\"delete_"+q.getType()+"_"+q.getqID()+"\" value=\"delete_"+q.getType()+"_"+q.getqID()+"_true\"> Delete this question?");
					out.println(q.getEditQuizString()); // all the ids in the input fields must be unique
					out.println(HTMLHelper.contentEnd());
				}
				
				out.println(HTMLHelper.contentStart());
				out.println("<br /><input type=\"submit\" value=\"Update Quiz\"/>");
				out.println(HTMLHelper.contentEnd());
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
		// TODO Auto-generated method stub
	}

}
