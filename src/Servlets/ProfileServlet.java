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
import Accounts.MailManager;

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
    	    	MailManager mm = (MailManager) request.getServletContext().getAttribute("mail");
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
    	    	out.println(HTMLHelper.contentStart());
    	    	out.println("<h3>" + profile.getName() + "</h3>");
    	    	out.println(HTMLHelper.contentEnd());
    	    	out.println(HTMLHelper.contentStart());
    	    	out.println("<h4>Acheivements</h4>");
    	    	for(String s : profile.getAcheivementKeySet()) {
    	    		if (profile.getAcheivement(s)) out.println(s + "<br>");
    	    	}
    	    	out.println(HTMLHelper.contentEnd());
    	    	if (selfViewer) {
    	    		out.println(HTMLHelper.contentStart());
    	    		out.println("<a href = \"newMessage.jsp?user="+profile.getName()+"\">Compose Mail</a>");
    	    		int unread = mm.getUnread(viewer);
    	    		String unreadString = "";
    	    		if (unread > 0) unreadString = " ("+unread+")";
    	    		out.println("<a href = \"MailManagementServlet?&index=inbox&user="+profile.getName()+"\">inbox"+unreadString+"</a>");
    	    		out.println("<a href = \"MailManagementServlet?&index=outbox&user="+profile.getName()+"\">outbox</a>");
    	    		out.println("<br>");
    	    		out.println("<a href = \"FriendManagementServlet\">Friends</a>");
    	    		out.println(HTMLHelper.contentEnd());
    	    	} else if (regViewer) {
    	    		out.println(HTMLHelper.contentStart());
    	    		out.println("<a href = \"newMessage.jsp?&user="+viewer.getName()+"&to="+profile.getName()+"\">Send Mail</a>");
    	    		out.println("<br>");
    	    		out.println("<li><form id=\"friend\" action=\"FriendManagementServlet\" method=\"post\">");
    	    		out.println("<input type=\"hidden\" name=\"ID\" value=\""+profile.getId()+"\">");
    	    		if (am.isFriend(viewer.getId(), profile.getId())) {
    	    			out.println("<input type=\"hidden\" name=\"action\" value=\"delete\">");
    	    			out.println("<a href=\"#\" onclick=\"document.getElementById(\'friend\').submit();\"> Remove as friend </a>");
    	    		} else {
    	    			out.println("<input type=\"hidden\" name=\"action\" value=\"add\">");
    	    			out.println("<a href=\"#\" onclick=\"document.getElementById(\'friend\').submit();\"> Add as friend </a>");
    	    		}
    	    		out.println("</form>");
    	    		out.println(HTMLHelper.contentEnd());
    	    	}
    	    }

    	    /**
    	     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    	     */
    	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	    }
}
