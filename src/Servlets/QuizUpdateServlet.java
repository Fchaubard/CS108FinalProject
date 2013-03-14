package Servlets;

import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.FillInTheBlank;
import model.Matching;
import model.MultipleAnswer;
import model.MultipleChoice;
import model.MultipleChoiceMultipleAnswer;
import model.PictureResponse;
import model.Question;
import model.QuestionResponse;
import model.Quiz;
import Accounts.Account;
import Accounts.AccountManager;

/**
 * Servlet implementation class QuizUpdateServlet
 */
@WebServlet("/QuizUpdateServlet")
public class QuizUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizUpdateServlet() {
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
		
		
		if (session.getAttribute("quizID")==null) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
			out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
					      + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			out.println("<html xmlns='http://www.w3.org/1999/xhtml'>");
			out.println("<head>");
			out.println(HTMLHelper.printCSSLink());
			out.println("<title>Issue!</title>");
			out.println("</head>");
			out.println("<h1>Well this is embarrassing. You must have come here the back way. </h1>");
			return;
		}
		else{
			
			quiz = (Quiz) session.getAttribute("quiz_"+session.getAttribute("quizID"));
		}
		
		Question question;
		try {
			// do the quiz settings first
			String quizName = (String)request.getParameter("quizName");
			String quizCategory = (String)request.getParameter("quizCategory");
			String quizDescription = (String)request.getParameter("quizDescription");
			String randomstring = (String)request.getParameter("random")!=null ? (String)request.getParameter("random") : "blah";
			boolean random = randomstring.equals("randomTrue") ? true : false;
			String multipagestring = (String)request.getParameter("multipage")!=null ? (String)request.getParameter("multipage") : "blah";
			boolean onePage = multipagestring.equals("multipageTrue") ? true : false ;
			String immediateCorrectionstring = (String)request.getParameter("immediateCorrection")!=null ? (String)request.getParameter("immediateCorrection") : "blah";
			boolean ic = immediateCorrectionstring.equals("immediateCorrectionTrue") ? true : false ;
			String clearHistoryString = (String)request.getParameter("clearHistory")!=null ? (String)request.getParameter("clearHistory") : "blah";
			boolean clearHistory = clearHistoryString.equals("clearHistoryTrue") ? true : false ;
			
			quiz.setQuizName(quizName);
			quiz.setCategory(quizCategory);
			quiz.setDescription(quizDescription);
			quiz.setRandom(random);
			quiz.setOnePageMultiPage(onePage);
			quiz.setImmediateCorrection(ic);
			ServletContext sc = request.getServletContext();
			AccountManager am = (AccountManager) sc.getAttribute("accounts");
			Connection con = am.getCon();
			
			if (clearHistory) {
				
				PreparedStatement historyQuery = con.prepareStatement("Delete * from history where quiz_id = ?");
				historyQuery.setInt(1, quiz.getQuiz_id());
				historyQuery.executeUpdate();
				
			}
			ArrayList<Integer> updateDeleteAddArrayList = new ArrayList<Integer>();
			for (Question q : quiz.getQuestions()) {
				
				switch(q.getType()) {
				case 1: 
					String deleteString = (String)request.getParameter("delete_"+ q.getType()+"_"+q.getqID())!=null ? (String)request.getParameter("delete_"+q.getType()+"_"+q.getqID()) : "blah";
					boolean delete = deleteString.equals("delete_"+q.getType()+"_"+q.getqID()+"_true") ? true : false ;
					
					if (delete) {
						updateDeleteAddArrayList.add(2);
					}else{
						updateDeleteAddArrayList.add(1);
					
						String statementString = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"statement");
						HashSet<String> hashSet = new HashSet<String>();
						
						String a = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"answers");
						
						String[] s = a.split(Pattern.quote("\r\n"));
						for (String string : s) {
							hashSet.add(string);
						}
						
						((QuestionResponse)q).setStatement(statementString);
						((QuestionResponse)q).setAnswers(hashSet);
						
					}
					break;
				
				case 2:
					deleteString = (String)request.getParameter("delete_"+ q.getType()+"_"+q.getqID())!=null ? (String)request.getParameter("delete_"+q.getType()+"_"+q.getqID()) : "blah";
					delete = deleteString.equals("delete_"+q.getType()+"_"+q.getqID()+"_true") ? true : false ;
					
					if (delete) {
						updateDeleteAddArrayList.add(2);
					}else{
						updateDeleteAddArrayList.add(1);
					
						String statementBefore = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"statementBefore");
						String statementAfter = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"statementAfter");
						String statementString = statementBefore+ "__________" +statementAfter ;
						HashSet<String> hashSet = new HashSet<String>();
						
						String a = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"answers");
						
						String[] s = a.split(Pattern.quote("\r\n"));
						for (String string : s) {
							hashSet.add(string);
						}
						
						((FillInTheBlank)q).setStatement(statementString);
						((FillInTheBlank)q).setAnswers(hashSet);
						
					}
					
					break;
					
				case 3:
					deleteString = (String)request.getParameter("delete_"+ q.getType()+"_"+q.getqID())!=null ? (String)request.getParameter("delete_"+q.getType()+"_"+q.getqID()) : "blah";
					delete = deleteString.equals("delete_"+q.getType()+"_"+q.getqID()+"_true") ? true : false ;
					
					if (delete) {
						updateDeleteAddArrayList.add(2);
					}else{
						updateDeleteAddArrayList.add(1);
					
						String statementString = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"statement");
						HashSet<String> hashSet = new HashSet<String>();
						
						String a = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"answers");
						String wrongOptions = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"wrongOptions");
						
						String[] s = wrongOptions.split(Pattern.quote("\r\n"));
						for (String string : s) {
							hashSet.add(string);
						}
						
						((MultipleChoice)q).setStatement(statementString);
						((MultipleChoice)q).setAnswer(a);
						((MultipleChoice)q).setWrongAnswers(hashSet);
						
					}
					break;
					
				case 4:
					deleteString = (String)request.getParameter("delete_"+ q.getType()+"_"+q.getqID())!=null ? (String)request.getParameter("delete_"+q.getType()+"_"+q.getqID()) : "blah";
					delete = deleteString.equals("delete_"+q.getType()+"_"+q.getqID()+"_true") ? true : false ;
					
					if (delete) {
						updateDeleteAddArrayList.add(2);
					}else{
						updateDeleteAddArrayList.add(1);
					
						String statementString = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"url");
						HashSet<String> hashSet = new HashSet<String>();
						
						String a = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"answers");
						
						String[] s = a.split(Pattern.quote("\r\n"));
						for (String string : s) {
							hashSet.add(string);
						}
						
						((PictureResponse)q).setURL(statementString);
						((PictureResponse)q).setAnswers(hashSet);
					}
					break;
					
				case 5:
					deleteString = (String)request.getParameter("delete_"+ q.getType()+"_"+q.getqID())!=null ? (String)request.getParameter("delete_"+q.getType()+"_"+q.getqID()) : "blah";
					delete = deleteString.equals("delete_"+q.getType()+"_"+q.getqID()+"_true") ? true : false ;
					
					if (delete) {
						updateDeleteAddArrayList.add(2);
					}else{
						updateDeleteAddArrayList.add(1);
					
						String statementString = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"statement");
						HashSet<String> hashSet = new HashSet<String>();
						
						String a = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"answers");
						
						String[] s = a.split(Pattern.quote("\r\n"));
						for (String string : s) {
							hashSet.add(string);
						}

						int numAnswers = Integer.parseInt((String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"numAnswers"));
						((MultipleAnswer)q).setStatement(statementString);
						((MultipleAnswer)q).setAnswers(hashSet);
						((MultipleAnswer)q).setNumAnswers(numAnswers);
						
					}
					
					break;
					
				case 6:
					deleteString = (String)request.getParameter("delete_"+ q.getType()+"_"+q.getqID())!=null ? (String)request.getParameter("delete_"+q.getType()+"_"+q.getqID()) : "blah";
					delete = deleteString.equals("delete_"+q.getType()+"_"+q.getqID()+"_true") ? true : false ;
					
					if (delete) {
						updateDeleteAddArrayList.add(2);
					}else{
						updateDeleteAddArrayList.add(1);
					
						String statementString = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"question");
						String a = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"answers");
						String wrongAns = (String)request.getParameter( q.getType()+"_"+q.getqID()+"_"+"wrongAnswers");
						
						Set<String> wAns = new HashSet<String>();
						String[] wrong = wrongAns.split(Pattern.quote("\r\n"));
						for (String st : wrong) {
							wAns.add(st);
						}
						
						Set<String> ans = new HashSet<String>();
						String[] right = a.split(Pattern.quote("\r\n"));
						for(String str : right) {
							ans.add(str);
						}
						((MultipleChoiceMultipleAnswer)q).setStatement(statementString);
						((MultipleChoiceMultipleAnswer)q).setAnswers(ans);
						((MultipleChoiceMultipleAnswer)q).setWrongAnswers(wAns);
					}
					
					break;
					
				case 7:
					
					
					/*
					Matching matching_question = (Matching)session.getAttribute("matching_question");
					
					quiz.addQuestion(matching_question);
					session.removeAttribute("matching_question");
					*/
					
					break;
				
			}
				
			}
			
			quiz.updateDB(con, updateDeleteAddArrayList);
			
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
			
			am = (AccountManager) request.getServletContext().getAttribute("accounts");
			if(request.getSession().getAttribute("account") != null) out.println(HTMLHelper.printNewsFeed(am.getAnnouncements()));
			
			out.println(HTMLHelper.contentStart());
			out.println("<h1>"+quiz.getQuizName()+"</h1>");
			out.println(HTMLHelper.contentEnd());
			
			
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
			/*out.println(HTMLHelper.contentStart());
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
			*/
			out.println(HTMLHelper.contentEnd());
			out.println(HTMLHelper.contentStart());
			out.println("<form action=\"QuizCatalogServlet\" method=\"post\">");
			out.println("<br /><br />Done Updating Quiz");
			out.println("<br /><input type=\"submit\" value=\"Done Upating Quiz\"/>");
			out.println("</form>");
			out.println(HTMLHelper.contentEnd());
			
			out.println("</body>");
			out.println("</html>");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		
	}

}
}
