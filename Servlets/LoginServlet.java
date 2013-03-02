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
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Never called, definition required
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("user");
		String pass = request.getParameter("pass");

		ServletContext sc = request.getServletContext();
		AccountManager am = (AccountManager) sc.getAttribute("accounts");
		Account acct = am.loginAccount(name, pass);
		if (acct != null) {
			sc.setAttribute("user", name);
			request.getSession().setAttribute("account", acct);
			request.getRequestDispatcher("/accountDebug.jsp" ).forward( request, response );
		} else {
			request.getRequestDispatcher("/badPass.html" ).forward( request, response );
		}
	}

}
