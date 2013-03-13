package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


public class MultipleAnswer implements Question {
	
	public static final int type=5;
	private String statement;
	private Set<String> answers;
	private int qID;
	private int numAnswers;
	private ArrayList<String> userAnswers;
	
	public static String getHTMLInputString(){
		
		StringBuilder html = new StringBuilder();
		html.append("<br />Insert Question Statement: <br /><input type=\"text\" name=\"question\" size=\"75\" required />");
		html.append("<br />Number of Expected Answers: <br /><input type=\"text\" name=\"numAnswers\" required />");
		html.append("<br />Insert All Possible Answers, one on each line:");
		html.append("<br /><textarea name=\"answers\" cols=\"20\" rows=\"10\" required></textarea>");
		
		return html.toString();
	}
	
	public MultipleAnswer(String question, Set<String> ans, int numAnswers) { // pushes to database
		this.numAnswers = numAnswers;
		this.statement = question;
		this.answers = ans;
	}

	public MultipleAnswer(int id, Connection con) { // pulls from database
		generate(id, con);
	}
	
	
	
	public void generate(int id, Connection con) {
		setqID(id);
		try {
			
			PreparedStatement ps = con.prepareStatement("select * from multiple_answer_question where question_id = ?");
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
		
			String ans = new String();
			while (resultSet.next()) {
				statement = resultSet.getString("statement");
				ans = resultSet.getString("answer");
				numAnswers = resultSet.getInt("numAnswers");
				
			}
			
			String[] strings = ans.split(Pattern.quote(" &&& "));
			answers = new HashSet<String>();
			for (String string : strings) {
				answers.add(string);
			}
			
			
		}catch(Exception e){
			
		}
		
	}
	
	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public Set<String> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<String> answers) {
		this.answers = answers;
	}
	
	@Override
	public int solve(ArrayList<String> answer) {
		int score = 0;
		for (String string : answer) {
			if (this.answers.contains(string)) { //TODO this might not be good
				score++;
			}
		}
		return score;
	
	}

	@Override
	public String toHTMLString() {
		StringBuilder html = new StringBuilder();
		
		html.append(statement);
		html.append("<br />");
		
		for(int i = 0; i < numAnswers; i++) {
			html.append("<input type = \"text\" name = \""+qID +"_");
			html.append(i);
			html.append("\" /><br />");
		}
		
		return html.toString();
	}

	public int getqID() {
		return qID;
	}

	public void setqID(int qID) {
		this.qID = qID;
	}

	@Override
	public String getCorrectAnswers() {
		StringBuilder correctAnswers = new StringBuilder();
		
		for(String s : answers) {
			correctAnswers.append(s);
			correctAnswers.append(",  ");
		}
		correctAnswers.replace(correctAnswers.length()-2, correctAnswers.length(), "");
		
		return correctAnswers.toString();
	}

	public int getType(){
		return type;
	}

	@Override
	public void pushToDB(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement("insert into multiple_answer_question values(null, ?, ?, ?)");
		
		ps.setString(1, statement);
		
		StringBuilder a = new StringBuilder();
		for(String s : answers) {
			a.append(s);
			a.append(" &&& ");
		}
		a.replace(a.length()-5, a.length(), "");
		
		ps.setString(2, a.toString());
		
		ps.setInt(3, numAnswers);
		
		System.out.println(ps.toString());
		ps.executeUpdate();
		
		PreparedStatement ps2 = con.prepareStatement("select * from multiple_answer_question where statement = ?");
		ps2.setString(1, statement);
		
		ResultSet rs = ps2.executeQuery();
		
		while(rs.next()) {
			this.setqID(rs.getInt("question_id"));
		}	
	}

	@Override
	public void setUserAnswers(ArrayList<String> ans) {
		userAnswers = new ArrayList<String>();
		
		for(String s : ans) {
			userAnswers.add(s);
		}
	}

	@Override
	public String getUserAnswers() {
		StringBuilder userAns = new StringBuilder();
		
		for(String s : userAnswers) {
			userAns.append(s);
			userAns.append(", ");
		}
		userAns.replace(userAns.length()-2, userAns.length(), "");
		
		return userAns.toString();
	}

	@Override
	public int getTotalQScore() {
		return numAnswers;
	}

	@Override
	public int getNumAnswers() {
		return numAnswers;
	}
}
