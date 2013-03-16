package Servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Accounts.Account;
import Accounts.AccountManager;

/**
 * Servlet implementation class SinglePageQuizServlet
 */
@WebServlet("/HomePage")
public class HomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HomePage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//check for cookies
		Cookie[] cookies = request.getCookies();
		Cookie cookie;
		String cookieName = "no cookie";
		if (cookies!=null) {
			
			
			for(int i=0; i<cookies.length; i++) {
			      cookie = cookies[i];
			      if (cookie.getName().equals("name")){
			        cookieName = cookie.getValue();
			        
			        break;
			      }
			}
		}
		 
		if (!cookieName.equals("no cookie")) {
			ServletContext sc = request.getServletContext();
			AccountManager am = (AccountManager) sc.getAttribute("accounts");
			Account acct = am.getAccount(cookieName);
			sc.setAttribute("user", cookieName);
			request.getSession().setAttribute("account", acct);
			request.getRequestDispatcher("/ProfileServlet?user="+acct.getName()).forward(request, response);
			
		}else{
			Account acct = (Account) request.getSession().getAttribute("account");
			if (acct == null) {
				request.getRequestDispatcher("/GuestHome.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/ProfileServlet?user="+acct.getName()).forward(request, response);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}


