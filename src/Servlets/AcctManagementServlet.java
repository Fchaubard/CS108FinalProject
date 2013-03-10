package Servlets;

import java.io.IOException;

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
@WebServlet("/AcctManagementServlet")
public class AcctManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AcctManagementServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String action = request.getParameter("Action");
    	String name = request.getParameter("User");
    	String pass = request.getParameter("Pass");

    	System.out.println(action);
    	
    	ServletContext sc = request.getServletContext();
		AccountManager am = (AccountManager) sc.getAttribute("accounts");
    	
    	if (action.equals("Create")) {
    		Account acct = am.createAccount(name, pass);
    		if (acct == null) {
    			request.getSession().setAttribute("user", name);
    			request.getRequestDispatcher("/NameTaken.jsp").forward(request, response);
    		} else {
    			request.getSession().setAttribute("user", name);
    			request.getSession().setAttribute("account", acct);
    			request.getRequestDispatcher("/UserHome.jsp").forward(request, response);
    		}
    	} else if (action.equals("Delete")) {
    		am.deleteAccount(name);
    		request.getRequestDispatcher("/GuestHome.jsp").forward(request, response);
    	}  else if (action.equals("Logout")) {
    		am.logoutAccount((Account) request.getSession().getAttribute("account"));
    		request.getSession().setAttribute("account", null);
    		request.getRequestDispatcher("/GuestHome.jsp").forward(request, response);
    	} else if (action.equals("Promote")) {
    		Account acct = am.getAccount(request.getParameter("name"));
    		am.setRank(acct, true);
    	} else if (action.equals("Demote")) {
    		Account acct = am.getAccount(request.getParameter("name"));
    		am.setRank(acct, false);
    	} else if (action.equals("Ban")) {
    		Account acct = am.getAccount(request.getParameter("name"));
    		if (acct == null) System.out.println("no ban" + request.getParameter("name"));
    		am.banUser(acct, true);
    	} else if (action.equals("Pardon")) {
    		Account acct = am.getAccount(request.getParameter("name"));
    		am.banUser(acct, false);
    	}
    	request.getRequestDispatcher("/UserHome.jsp").forward(request, response);
    	//request.getRequestDispatcher("/ProfileServlet?user="+request.getParameter("name")).forward(request, response);
    }
}
