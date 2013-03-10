package Servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Quiz;

import Accounts.*;


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
		
		if(request.getSession().getAttribute("account")==null){
		
			String name = request.getParameter("user");
			String pass = request.getParameter("pass");
			ServletContext sc = request.getServletContext();
			AccountManager am = (AccountManager) sc.getAttribute("accounts");
			if (am == null) System.out.println("Huh?");
			Account acct = am.loginAccount(name, pass);
			if (acct != null) {
				if (acct.isBanned()) {
					request.getRequestDispatcher("/GuestHome.jsp?err=banned" ).forward( request, response );
				} else {
					sc.setAttribute("user", name);
					request.getSession().setAttribute("account", acct);
					request.getRequestDispatcher("/UserHome.jsp").forward( request, response );
				}
			} else {
				request.getRequestDispatcher("/GuestHome.jsp?err=badLogin" ).forward( request, response );
			}
		}else{
			request.getRequestDispatcher("/UserHome.jsp").forward( request, response );
		}
		
		
	}

}
