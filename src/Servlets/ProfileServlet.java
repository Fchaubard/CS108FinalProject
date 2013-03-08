package Servlets;

    	import helpers.HTMLHelper;

import java.io.IOException;
import java.io.PrintWriter;

    	import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

    	import Accounts.Account;
import Accounts.AccountManager;

    	/**
    	 * Servlet implementation class AcctManagementServlet
    	 */
    	@WebServlet("/ProfileServlet")
    	public class ProfileServlet extends HttpServlet {
    		private static final long serialVersionUID = 1L;
    	    
    	    /**
    	     * @see HttpServlet#HttpServlet()
    	     */
    	    public ProfileServlet() {
    	        super();
    	    }

    	    /**
    	     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
    	     */
    	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	    	AccountManager am = (AccountManager) request.getServletContext().getAttribute("accounts");
    	    	Account profile = am.getAccount(request.getParameter("user"));
    	    	Account viewer = (Account) request.getSession().getAttribute("account");
    	    	boolean regViewer = (viewer != null);
    	    	boolean selfViewer = (viewer != null) && (viewer.getId() == profile.getId());
    	    	response.setContentType("text/html");
    	    	PrintWriter out = response.getWriter();
    	    	out.println("<head>");
    	    	out.println(HTMLHelper.printCSSLink());
    	    	out.println("</head>");
    	    	
    	    	out.println(HTMLHelper.printHeader());
    	    	out.println("<h3>" + profile.getName() + "</h3>");
    	    	out.println("<h4>Acheivements</h4>");
    	    	for(String s : profile.getAcheivementKeySet()) {
    	    		if (profile.getAcheivement(s)) out.println(s + "<br>");
    	    	}
    	    	if (selfViewer) {
    	    		out.println("<a href = \"newMessage.jsp?user="+profile.getName()+"\">Compose Mail</a>");
    	    		out.println("<a href = \"MailManagementServlet?&index=inbox&user="+profile.getName()+"\">inbox</a>");
    	    		out.println("<a href = \"MailManagementServlet?&index=outbox&user="+profile.getName()+"\">outbox</a>");
    	    		out.println("<br>");
    	    		out.println("<a href = \"FriendManagementServlet\">Friends</a>");
    	    	} else if (regViewer) {
    	    		out.println("<a href = \"newMessage.jsp?&user="+viewer.getName()+"&to="+profile.getName()+"\">Send Mail</a>");
    	    		out.println("<br>");
    	    		out.println("<li><form action=\"FriendManagementServlet\" method=\"post\">");
    	    		out.println("<input type=\"hidden\" name=\"ID\" value=\""+profile.getId()+"\">");
    	    		out.println("<input type=\"submit\" name=\"action\" value=\"delete\">");
    	    		out.println("<input type=\"submit\" name=\"action\" value=\"add\">");
    	    		out.println("</form>");
    	    	}
    	    	out.println("<a href = \"HomePage\">Home</a>");
    	    }

    	    /**
    	     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    	     */
    	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	    }
}
