package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class QuestionResponse implements Question {

	public static final int type=1;
	private String statement;
	private Set<String> answers;
	private int qID;
	private ArrayList<String> userAnswers;
	
	public static String getHTMLInputString(){
		StringBuilder html = new StringBuilder();
		
		html.append("<br />Insert Question Statement: <br /><input type=\"text\" name=\"statement\" size=\"75\" required/>");
		html.append("<br />Insert All Possible Answers, one on each line:");
		html.append("<br /><textarea name=\"answers\" cols=\"20\" rows=\"10\" required></textarea>");
		
		return html.toString();
	}
	
	public QuestionResponse(String question, HashSet<String> ans) { // pushes to database
		this.statement = question;
		this.answers = ans;
		answers.remove("");
	}
	
	public QuestionResponse(Integer id, Connection con) throws SQLException {
		this.qID = id;
		
		PreparedStatement ps = con.prepareStatement("select * from question_response where question_id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		String ans = new String();
		while(rs.next()) {
			statement = rs.getString("statement");
			ans = rs.getString("answer");
		}
		

		String[] strings = ans.split(Pattern.quote(" &&& "));
		answers = new HashSet<String>();
		for (String string : strings) {
			answers.add(string);
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
	public int solve(ArrayList<String> ans) {
		if (ans.size()!=1) {
			return 0; // input cleansing
		}
		for (String a : ans) {
			if (answers.contains(a)) {
				return 1;
			}
		}
		return 0;
		
	}

	@Override
	public String toHTMLString() {
		StringBuilder html = new StringBuilder();
		
		html.append(statement);
		html.append("<br><input type = \"text\" name = \"");

		html.append(type);
		html.append("_");
		html.append(qID);

		html.append("\" id=\"");
		html.append(type);
		html.append("_");
		html.append(qID);

		html.append("\" />");
		
		return html.toString();
	}
	
	
	
	
	@Override
	public void generate(int id, Connection con) {
		// TODO Auto-generated method stub
		
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
			s=s.trim();
			correctAnswers.append(s);
			correctAnswers.append(",  ");
		}
		correctAnswers.replace(correctAnswers.length()-3, correctAnswers.length(), "");
		
		return correctAnswers.toString();
	}
	public int getType(){
		return type;
	}

	@Override
	public void pushToDB(Connection con) {
		Statement stmt;
		
		try {
			stmt = con.createStatement();
			StringBuilder sqlString = new StringBuilder("INSERT INTO question_response VALUES(null,\"");
			statement = statement.trim();
			sqlString.append(statement);
			sqlString.append("\",\"");
			for (String string : answers) {
				string = string.trim();
				sqlString.append(string);
				sqlString.append(" &&& ");
			}
			sqlString.replace(sqlString.length()-5, sqlString.length(), "");
			sqlString.append("\") ");
			
			System.out.print(sqlString.toString());
			stmt.executeUpdate(sqlString.toString());
			
			stmt = con.createStatement();
			sqlString = new StringBuilder("SELECT * FROM question_response WHERE statement=\"");
			sqlString.append(statement);
			sqlString.append("\" ");
			
			System.out.println(sqlString.toString());
			ResultSet resultSet = stmt.executeQuery(sqlString.toString());
			
			
			while (resultSet.next()) {
				this.setqID(resultSet.getInt("question_id")); // will always be the last one
			}
		}catch(Exception e){
			
		}		
	}

	@Override
	public void setUserAnswers(ArrayList<String> ans) {
		userAnswers = new ArrayList<String>();
		
		for(String s : ans) {
			userAnswers.add(s);
		}
		
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(userAnswers);
		userAnswers.clear();
		userAnswers.addAll(hs);
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
		return 1;
	}

	@Override
	public int getNumAnswers() {
		return 1;
	}
	
	public String getEditAnswersString() {
		StringBuilder str = new StringBuilder();
		
		for(String s : answers) {
			str.append(s);
			str.append("\n");
		}
		
		str.replace(str.length()-1, str.length(), "");
		
		return str.toString();
	}

	@Override
	public String getEditQuizString() {
		StringBuilder html = new StringBuilder();
		
		html.append("<br />Question Response Question");
		html.append("<br />Insert Question Statement: <br /><input type=\"text\" name=\""+type+"_"+qID+"_statement\" size=\"75\" value=\"" + statement + "\" required/>");
		html.append("<br />Insert All Possible Answers, one on each line:");
		html.append("<br /><textarea name=\""+type+"_"+qID+"_answers\" cols=\"20\" rows=\"10\" required>"+getEditAnswersString()+"</textarea>");
		
		return html.toString();
	}

	@Override
	public void updateDB(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE question_response SET statement = ?, answer = ? WHERE question_id = ?");
		
		ps.setString(1, statement);
		
		StringBuilder ans = new StringBuilder();
		for(String a : answers) {
			ans.append(a);
			ans.append(" &&& ");
		}
		ans.replace(ans.length()-5, ans.length(), "");
		
		ps.setString(2, ans.toString());
		
		ps.setInt(3, qID);
		
		System.out.println(ps.toString());
		ps.executeUpdate();
	}

	@Override
	public void deleteFromDB(Connection con) throws SQLException {
		
		PreparedStatement ps = con.prepareStatement("DELETE FROM question_response WHERE question_id = ?");
		ps.setInt(1, qID);
		System.out.println(ps.toString());
		ps.executeUpdate();
		
		PreparedStatement prep = con.prepareStatement("DELETE FROM quiz_question_mapping WHERE question_id = ? AND question_type = ?");
		prep.setInt(1, qID);
		prep.setInt(2, type);
		System.out.println(prep.toString());
		prep.executeUpdate();
	}
}


