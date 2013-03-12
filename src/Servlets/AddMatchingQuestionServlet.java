package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.*;


/**
 * Servlet implementation class AddMatchingQuestionServlet
 */
@WebServlet("/AddMatchingQuestionServlet")
public class AddMatchingQuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Quiz quiz;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddMatchingQuestionServlet() {
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
		HttpSession session = request.getSession(true);
		//public Quiz(ArrayList<Question> q, boolean random, boolean onePage, boolean immediateCorrect, boolean practice, int userID, String quizName, String description, String category){
		Quiz quiz;
		if (session.getAttribute("Quiz")==null) {
			return; // this should never happen
			
		}
		else{
			quiz = (Quiz) session.getAttribute("Quiz");
		}
		Matching matching_question;
		if (session.getAttribute("matching_question")==null) {
			// we have no rows make the question
			matching_question = new Matching(new String(), new ArrayList<String>(), new ArrayList<String>());
			session.setAttribute("matching_question", matching_question);
		}else{
			matching_question = (Matching)session.getAttribute("matching_question");
			// we already have at least one row
			matching_question.addRow( (String)request.getParameter("question_row"),(String)request.getParameter("answer_row"));
			matching_question.setTitle((String)request.getParameter("title"));
			session.setAttribute("matching_question", matching_question);
		}
		
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
			out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
					      + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			out.println("<html xmlns='http://www.w3.org/1999/xhtml'>");
			out.println("<head>");
			out.println("<title>"+quiz.getQuizName()+"</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>"+quiz.getQuizName()+"</h1>");
			out.println("<h3>Input Question "+(quiz.getQuestions().size()+1)+"</h3>");
			String type = request.getParameter("questionType");
			String question;
			String submitType;
			if(request.getParameter("submit_type")!=null){
				submitType=request.getParameter("submit_type");
			}else{
				submitType= "first";
			}
			if (!type.equals("7")) {
				question = "ERROR: Did not submit a matching question!";
			}else{
				question =  Matching.getHTMLInputString();
			
			}
			
			if ( !submitType.equals("Done Creating Matching Question")) {
			
			out.println("<form action=\"AddMatchingQuestionServlet\" method=\"post\">");
			out.println("<br />Matching Title:<input type=\"text\" name=\"title\" value=\""+request.getParameter("title")+"\"  />");
			
			
			}else{
				out.println("<br /><h3>Matching Question Title: "+request.getParameter("title")+" </h3>");	
			}
			for (int i = 0; i <matching_question.getRow1().size(); i++) {
				out.println("<br /> "+(matching_question.getShuffleIntegersForRow2().get(i)+1)+" "+matching_question.getRow1().get(i)+"  "+matching_question.getRow2().get(i)+"");
					
			}
			if (!submitType.equals("Done Creating Matching Question")) {
				
				out.println(question);
				out.println("<input name=\"questionType\" type=\"hidden\" value=\"" +type+"\"/>");
				out.println("<br /><input type=\"submit\" name=\"submit_type\" value=\"Add Another Match to this Question\"/>");
				out.println("<br /><input type=\"submit\" name=\"submit_type\" value=\"Done Creating Matching Question\"/>");
				out.println("</form>");
				
			}else{
				out.println("<form action=\"SubmitQuestionAndUpdateQuizCreationServlet\" method=\"post\">");
				out.println("<input name=\"questionType\" type=\"hidden\" value=\"" +type+"\"/>");
				out.println("<br /><input type=\"submit\" value=\"Save Matching Question\"/>");
				out.println("</form>");
			}
			out.println("</body>");
			out.println("</html>");
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}


