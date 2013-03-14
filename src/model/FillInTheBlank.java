package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class FillInTheBlank implements Question {

	public static final int type=2;
	private String statement;
	private Set<String> answers;
	private int qID;
	private ArrayList<String> userAnswers;
	
	public static String getHTMLInputString(){
		
		StringBuilder html = new StringBuilder();
		html.append("<br /> Question:");
		html.append("<br /><input type=\"text\" name=\"statementBefore\" size=\"50\" />");
		html.append(" __________ ");
		html.append("<input type=\"text\" name=\"statementAfter\" size=\"50\" >");
		html.append("<br />Insert All Possible Answers, one on each line:");
		html.append("<br /><textarea name=\"answers\" cols=\"20\" rows=\"10\" required></textarea>");
		
		return html.toString();
	}
	
	public void pushToDB(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement("insert into fill_in_the_blank_question values(null, ?, ?)");
		statement = statement.trim();
		ps.setString(1, statement);
		
		StringBuilder ans = new StringBuilder();
		for (String string : answers) {
			string = string.trim();
			ans.append(string);
			ans.append(" &&& ");
		}
		
		ans.replace(ans.length()-5, ans.length(), "");
		
		
		ps.setString(2, ans.toString());
		
		System.out.println(ans.toString());
		System.out.println(ps.toString());
		ps.executeUpdate();
		
		PreparedStatement getID = con.prepareStatement("select * from fill_in_the_blank_question where statement = ?");
		getID.setString(1, statement);
		
		System.out.print(getID.toString());
		ResultSet resultSet = getID.executeQuery();
		
		while (resultSet.next()) {
			this.qID = resultSet.getInt("question_id"); // will always be the last one
		}
	}
	
	public FillInTheBlank(String question, Set<String> ans) { 
		
		this.statement = question.trim(); // this should have the ________ in it already
		
		this.answers = ans; // need to add the &&&
	}

	
	public FillInTheBlank(int id, Connection con) { // pulls from database
		generate(id, con);
	}
	
	@Override
	public void generate(int id, Connection con) {
		this.qID = id;
		try {
			
			PreparedStatement ps = con.prepareStatement("select * from fill_in_the_blank_question where question_id = ?");
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
			
			
			String ans = new String();
			while (resultSet.next()) {
				statement = resultSet.getString("statement");
				ans = resultSet.getString("answer");
				
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
		int index = this.statement.indexOf("__________");
		StringBuilder html = new StringBuilder();
		html.append(this.statement.substring(0, index));
		html.append("<input type=\"text\" name=\"");
		html.append(type);
		html.append("_");
		html.append(qID);
		html.append("\" id=\"");
		html.append(type);
		html.append("_");
		html.append(qID);
		html.append("\" />");
		html.append(this.statement.substring(index + 10));
		
		return html.toString();
	}

	@Override
	public int getqID() {
		return qID;
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
		
		str.replace(str.length()-2, str.length(), "");
		
		return str.toString();
	}

	@Override
	public String getEditQuizString() {
		int indexOfBlank = statement.indexOf("__________");
		
		StringBuilder edit = new StringBuilder();
		edit.append("<br /> Question:");
		edit.append("<br /><input type=\"text\" name=\"" + type + "_" + qID + "_statementBefore\" size=\"50\" value=\"" + statement.substring(0, indexOfBlank) + "\" />");
		edit.append(" __________ ");
		edit.append("<input type=\"text\" name=\"" + type + "_" + qID + "_statementAfter\" size=\"50\" value=\"" + statement.substring(indexOfBlank+10) + "\" >");
		edit.append("<br />Insert All Possible Answers, one on each line:");
		edit.append("<br /><textarea name=\"" + type + "_" + qID + "_answers\" cols=\"20\" rows=\"10\" required>" + getEditAnswersString() + "</textarea>");
		
		return edit.toString();
	}

	@Override
	public void updateDB(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement("update fill_in_the_blank_question set statement = ?, answer = ? where question_id = ?");
		
		ps.setString(1, statement);
		
		StringBuilder ans = new StringBuilder();
		for (String string : answers) {
			string = string.trim();
			ans.append(string);
			ans.append(" &&& ");
		}
		ans.replace(ans.length()-5, ans.length(), "");
		
		ps.setString(1, ans.toString());
		ps.setInt(3, qID);
		
		ps.executeUpdate();
	}

	@Override
	public void deleteFromDB(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement("DELETE FROM fill_in_the_blank_question WHERE question_id = ?");
		ps.setInt(1, qID);
		ps.executeUpdate();
		
		PreparedStatement prep = con.prepareStatement("DELETE FROM quiz_question_mapping WHERE question_id = ? AND question_type = ?");
		prep.setInt(1, qID);
		prep.setInt(2, type);
		prep.executeUpdate();
	}
}
