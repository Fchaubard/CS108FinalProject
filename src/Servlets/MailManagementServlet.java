package Servlets;

import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Accounts.*;

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
    	//String user = request.getParameter("user");
    	Account user = (Account) request.getSession().getAttribute("account");
    	response.setContentType("text/html");
    	PrintWriter out = response.getWriter();
    	out.println("<head>");
    	out.println(HTMLHelper.printCSSLink());
    	out.println("</head>");
    	out.println(HTMLHelper.printHeader((Account)request.getSession().getAttribute("account")));
    	out.println("<body>");
    	
    	AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
		Account acct = (Account) request.getSession().getAttribute("account");
		if(request.getSession().getAttribute("account") != null) out.println(HTMLHelper.printNewsFeed(am.getAnnouncements(),am.getNews(acct.getId())));
    	
		out.println(HTMLHelper.contentStart());
    	
    	
    	
    	
    	if (request.getParameter("index").equals("inbox")) {
    		TreeMap<Integer, Message> inbox = mm.listInbox(user.getName());
    		out.println("<table border=\"0\">");
    		out.println("<tr><th><b>Subject</b></th><th><b>Sender</b></th><th><b>Date</b></th><th><b>Attatchment</b></th></tr>");
    		for (int i : inbox.descendingKeySet()) {
    			String boldin = "";
    			String boldout = "";
    			if (inbox.get(i).unread) {
    				boldin = "<b>";
    				boldout = "</b>";
    			}
    			out.println("<tr>");
    			out.println("<td>"+boldin+"<a href = \"MailManagementServlet?&index="+i+"&user="+user+"\">");
    			out.println(inbox.get(i).getSubject()) ;
    			out.println("</a>"+boldout+"</td>");
    			out.println("<td><a href = \"ProfileServlet?user="+inbox.get(i).getSender()+"\">");
    			out.println(inbox.get(i).getSender()) ;
    			out.println("</a></td>");
    			out.println("<td>");
    			out.println(new java.util.Date(inbox.get(i).getTimestamp())) ;
    			out.println("</td>");
    			out.println("</td>");
    			if (inbox.get(i).getChallengeName() != null) {
    				out.println("<td><a href = \"QuizTitleServlet?id="+inbox.get(i).getChallengeID()+"\">"+inbox.get(i).getChallengeName()+"</a>");
    			} else if (inbox.get(i).getSubject().equals("Friends?")) {
    				out.println("<td><a href = \"FriendManagementServlet\">Friend Request</a>");
    			} else {
    				out.println("");
    			}
    			out.println("</td></tr>");
    		}
    		out.println("</table>");
    	} else if (request.getParameter("index").equals("outbox")) {
    		TreeMap<Integer, Message> outbox = mm.listOutbox(user.getName());
    		//out.println("<ul>");
    		out.println("<table border=\"0\">");
    		out.println("<tr><th><b>Subject</b></th><th><b>Sender</b></th><th><b>Date</b></th><th><b>Attatchment</b></th></tr>");
    		for (int i : outbox.descendingKeySet()) {
    			out.println("<tr>");
    			out.println("<td><a href = \"MailManagementServlet?&index="+i+"&user="+user+"\">");
    			out.println(outbox.get(i).getSubject()) ;
    			out.println("</a></td>");
    			out.println("<td><a href = \"ProfileServlet?user="+outbox.get(i).getRecipient()+"\">");
    			out.println(outbox.get(i).getRecipient()) ;
    			out.println("</a></td>");
    			out.println("<td>");
    			out.println(new java.util.Date(outbox.get(i).getTimestamp())) ;
    			out.println("</td>");
    			if (outbox.get(i).getChallengeName() != null) {
    				out.println("<td><a href = \"QuizTitleServlet?id="+outbox.get(i).getChallengeID()+"\">"+outbox.get(i).getChallengeName()+"</a>");
    			} else if (outbox.get(i).getSubject().equals("Friends?")) {
    				out.println("<td><a href = \"FriendManagementServlet\">Friend Request</a>");
    			} else {
    				out.println("");
    			}
    			out.println("</td></tr>");
    		}
    		out.println("</table>");
    	} else {//print specific message
    		int x = 0;
    		try {
    			x = Integer.parseInt(request.getParameter("index"));
    		} catch (NumberFormatException e) {
    		}
    		Message m = mm.recieveMessage(x);
    		out.println("<b>Subject:</b> " + m.getSubject() + "<br>");
    		out.println("<b>From:</b> " + m.getSender() + " @ " + new java.util.Date(m.getTimestamp())+ "<hr>");
    		if (m.getChallengeName() != null) {
    			out.println(am.getChallenge(m.getSender(), m.getChallengeID(), m.getChallengeName()) + "<br><br>");
    		}
    		out.println(m.getBody());
    		
    	}
    	out.println(HTMLHelper.contentEnd() + "</body>");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String action = request.getParameter("type");
    	MailManager mm = (MailManager) request.getServletContext().getAttribute("mail");
    	if (action != null && action.equals("flag")) {
    		String flagger = request.getParameter("uname");
    		String qname = request.getParameter("qname");
    		String qid = request.getParameter("qid");
    		mm.sendFlag(flagger, qid, qname);
    		request.getRequestDispatcher("QuizCatalogServlet").forward(request, response);
    	} else {
    		String sender = request.getParameter("sender");
    		String recipient = request.getParameter("recipient");
    		AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
    		if (!am.accountExists(recipient)) {
    			String errorURL = "/newMessage.jsp?&to=error&subject="+request.getParameter("subject")+"&quiz=" + request.getParameter("quiz")+"&body="+request.getParameter("body");
    			request.getRequestDispatcher(errorURL).forward(request, response);
    		} else {
    			String subject = request.getParameter("subject");
    			String body = request.getParameter("body");
    			int challenge = -1;
    			try {
    				challenge = Integer.parseInt(request.getParameter("quiz"));
    			} catch (NumberFormatException e) {
    				e.printStackTrace();
    			}
    			Message m = new Message(sender, recipient, subject, body, 0, challenge, null, true);
    			
    			mm.sendMessage(m);
    			String name = ((Account) request.getSession().getAttribute("account")).getName();
    			request.getRequestDispatcher("ProfileServlet?user="+name).forward(request, response);
    		}
    	}
    }
}
