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

import model.Quiz;
import model.QuizAttempts;

import Accounts.Account;
import Accounts.AccountManager;

/**
 * Servlet implementation class AcctManagementServlet
 */
@WebServlet("/QuizTitleServlet")
public class QuizTitleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizTitleServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String qid;
    	HttpSession session = request.getSession(true);
		if (request.getParameter("id") != null){
			qid = request.getParameter("id");
		}
		else {
			qid = (String) session.getAttribute("quizID");
		}
		
		session.setAttribute("quizID", qid);
    	AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
    	Account user = (Account) request.getSession().getAttribute("account");
    	//Account user = am.getAccount("Sam I Am");
    	int id = Integer.parseInt(qid);
    	Quiz q;
		try {
			//means the cart hasnt been initialized
			if(session.getAttribute("quiz_"+qid) == null){
				q = new Quiz((id), am.getCon());
				session.setAttribute("quiz_"+qid, q);
				
			}
			else{
				q = (Quiz) session.getAttribute("quiz_"+qid);
			}
			
			
			String qName = q.getQuizName();
	    	Account a = q.getCreator();
	    	String author = a.getName();
	    	response.setContentType("text/html");
	    	PrintWriter out = response.getWriter();
	    	out.println("<head>");
	    	out.println(HTMLHelper.printCSSLink());
	    	out.println("</head>");
	    	out.println("<body>");
	    	out.println(HTMLHelper.printHeader());
	    	out.println(HTMLHelper.contentStart());
	    	out.println("<h1>"+qName+"</h1>");
	    	out.println("In category: " + q.getCategory() + "<br>");
	    	out.println("by <a href = \"ProfileServlet?user="+author+"\">"+author+"</a><br>");
	    	if (request.getSession().getAttribute("account") != null)out.println("<a href = \"newMessage.jsp?quiz="+q.getQuiz_id()+"\">Send to a friend</a><br>");
	    	out.println(q.getDescription());
	    	out.println("<br>");
	    	out.println(HTMLHelper.contentEnd());
	    	out.println(HTMLHelper.contentStart());
	    	if (user == null) {
	    		out.println("NOTE: You are not logged in. Your quiz results will not be saved.");
	    	}
	    	out.println("<h3>Top Scorers</h3>");
	    	out.println("<ol>");
	    	//currently prints ALL the scores. Can switch to a for (0-4) but table needs to be sorted first.
	    	for (QuizAttempts qa : am.getHistory(0, id)) {
	    		out.println("<li>" +qa.getScore()+" "+qa.getTime()+"</li>");
	    	}
	    	out.println("</ol><br>");
	    	out.println(HTMLHelper.contentEnd());
	    	out.println(HTMLHelper.contentStart());
	    	if (user != null) {
	    	out.println("<h3>My Scores</h3>");
	    		out.println("<ol>");
	    		for (QuizAttempts qa : am.getHistory(user.getId(), id)) {
	    			out.println("<li>"+qa.getScore()+" "+qa.getTime()+"</li>");
	    		}
	    	out.println("</ol>");
	    	}
	    	out.println(HTMLHelper.contentEnd());
	    	out.println(HTMLHelper.contentStart());
	    	if(q.isOnePageMultiPage()){

		    	out.println("<li><a href= \"MultiPageQuiz?id="+id+"\">Multi Page</a></li>");
			}else{
		    	out.println("<li><a href= \"SinglePageQuizServlet?id="+id+"\">Single Page</a></li>");
			}
	    	out.println(HTMLHelper.contentEnd());
	    	out.println("</body>");
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
