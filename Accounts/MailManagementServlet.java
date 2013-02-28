package Accounts;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    		for (int i : inbox.keySet()) {
    			out.println("<a href = \"MailManagementServlet?&index="+i+"&user="+user+"\">");
    			out.println(inbox.get(i).toString());
    			out.println("</a>");
    		}
    	} else if (request.getParameter("index").equals("outbox")) {
    		HashMap<Integer, Message> outbox = mm.listOutbox(user);
    		for (int i : outbox.keySet()) {
    			out.println("<a href = \"MailManagementServlet?&index="+i+"&user="+user+"\">");
    			out.println(outbox.get(i).toString());
    			out.println("</a>");
    		}
    	} else {//print specific message
    		Message m = mm.recieveMessage(Integer.parseInt(request.getParameter("index")));
    		out.println("Subject: " + m.getSubject() + "<br>");
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
    	String subject = request.getParameter("subject");
    	String body = request.getParameter("body");
    	Message m = new Message(sender, recipient, subject, body, 0);
    	MailManager mm = (MailManager) request.getServletContext().getAttribute("mail");
    	mm.sendMessage(m);
    	request.getRequestDispatcher("/homeDebug.jsp").forward(request, response);
    }
}
