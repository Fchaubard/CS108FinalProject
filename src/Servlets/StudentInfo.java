package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StudentInfo
 */
@WebServlet("/StudentInfo")
public class StudentInfo extends HttpServlet {



	
	
		private static final long serialVersionUID = 1L;

		/**
		 * @see HttpServlet#HttpServlet()
		 */
		public StudentInfo() {
			super();
		}

		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

			int i = Integer.parseInt(request.getParameter("id"));
			
			String text = "some text"+i;
		
		    response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
		    response.setCharacterEncoding("UTF-8"); // You want world domination, huh?
		    response.getWriter().write(text);       // Write response body.
		}

		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	public String getResult(String roll){
		String name = "";
		String hostel = "";
		String contact = "";
		if(roll.equalsIgnoreCase("110")){
			name = "Binod Kumar Suman"; 
			hostel = "Ganga"; 
			contact = "999999999";
		} else if(roll.equalsIgnoreCase("120")){
			name = "Pramod Kumar Modi"; 
			hostel = "Godawari"; 
			contact = "111111111111";
		} else{ name = "Roll Number not found"; }
		String result = "<Students>";
		result += "<Student>"; 
		result += "<Name>" + name + "</Name>";
		result += "<Hostel>" +hostel + "</Hostel>";
		result += "<Contact>" +contact + "</Contact>";
		result += "</Student>"; 
		result += "</Students>";
		return result;
	}
}
