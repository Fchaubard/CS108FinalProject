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
    	@WebServlet("/FriendManagementServlet")
    	public class FriendManagementServlet extends HttpServlet {
    		private static final long serialVersionUID = 1L;
    	    
    	    /**
    	     * @see HttpServlet#HttpServlet()
    	     */
    	    public FriendManagementServlet() {
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
    	    }
}