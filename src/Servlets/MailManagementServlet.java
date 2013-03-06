package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Accounts.AccountManager;
import Accounts.MailManager;
import Accounts.Message;

/**
 * Servlet implementation class AcctManagementServlet
 */
@WebServlet("/MailManagementServlet")
public class MailManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MailManagementServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	MailManager mm = (MailManager) request.getServletContext().getAttribute("mail");
    	String user = request.getParameter("user");
    	response.setContentType("text/html");
    	PrintWriter out = response.getWriter();
    	if (request.getParameter("index").equals("inbox")) {
    		HashMap<Integer, Message> inbox = mm.listInbox(user);
    		out.println("</ul>");
    		for (int i : inbox.keySet()) {
    			out.println("<li>");
    			out.println("<a href = \"MailManagementServlet?&index="+i+"&user="+user+"\">");
    			out.println(inbox.get(i).getSender() + " " + inbox.get(i).getSubject() + " " + new java.util.Date(inbox.get(i).getTimestamp())) ;
    			out.println("</a>");
    			out.println("</li>");
    		}
    		out.println("</ul>");
    	} else if (request.getParameter("index").equals("outbox")) {
    		HashMap<Integer, Message> outbox = mm.listOutbox(user);
    		out.println("<ul>");
    		for (int i : outbox.keySet()) {
    			out.println("<li>");
    			out.println("<a href = \"MailManagementServlet?&index="+i+"&user="+user+"\">");
    			out.println(outbox.get(i).getSender() + " " + outbox.get(i).getSubject() + " " + new java.util.Date(outbox.get(i).getTimestamp())) ;
    			out.println("</a>");
    			out.println("</li>");
    		}
    		out.println("</ul>");
    	} else {//print specific message
    		int x = 37;
    		try {
    			x = Integer.parseInt(request.getParameter("index"));
    		} catch (NumberFormatException e) {
    			System.out.println("mail fail");
    		}
    		System.out.println(x);
    		Message m = mm.recieveMessage(x);
    		out.println("Subject: " + m.getSubject() + "<br>");
    		if (m.getChallengeName() != null) {
    			out.println("You have been challenged to the <a href = QuizTitleServlet?id="+m.getChallengeID()+">"+m.getChallengeName()+"</a><br>");
    		}
    		out.println("From: " + m.getSender() + " @ " + new java.util.Date(m.getTimestamp())+ "<br>");
    		out.println(m.getBody());
    		
    	}
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String sender = request.getParameter("sender");
    	String recipient = request.getParameter("recipient");
    	AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
    	if (!am.accountExists(recipient)) {
    		request.getRequestDispatcher("/newMessage.jsp?to=Invalid Name").forward(request, response);
    	} else {
    	String subject = request.getParameter("subject");
    	String body = request.getParameter("body");
    	int challenge = -1;
    	try {
    		challenge = Integer.parseInt(request.getParameter("challenge"));
    	} catch (NumberFormatException e) {
	
    	}
    	Message m = new Message(sender, recipient, subject, body, 0, challenge, null);
    	MailManager mm = (MailManager) request.getServletContext().getAttribute("mail");
    	mm.sendMessage(m);
    	request.getRequestDispatcher("/UserHome.jsp").forward(request, response);
    	}
    }
}
