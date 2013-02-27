package Accounts;

//!!!!!!!!!!!
//DEPRECIATED
//!!!!!!!!!!!
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class CreationServlet
 */
@WebServlet("/CreationServlet")
public class CreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     * 
     */
    public CreationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("NewUser");
		String pass = request.getParameter("NewPass");
		
		ServletContext sc = request.getServletContext();
		AccountManager am = (AccountManager) sc.getAttribute("accounts");
		
		if (am.createAccount(name, pass) == null) {
			request.getRequestDispatcher("/NameTaken.jsp").forward(request, response);
		} else {
			sc.setAttribute("user", name);
			request.getRequestDispatcher("/accountDebug.jsp").forward(request, response);
		}
	}
}
