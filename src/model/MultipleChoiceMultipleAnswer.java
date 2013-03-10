package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class MultipleChoiceMultipleAnswer implements Question {

	public static final int type=6;
	private String statement;
	private Set<String> answers;
	private Set<String> wrongAnswers;
	private int qID;
	private ArrayList<String> userAnswers;
	
	public static String getHTMLInputString(){
		
		StringBuilder html = new StringBuilder();
		html.append("<br />Insert picture url: <br /><input type=\"text\" name=\"url\" />");
		html.append("<br />Insert Possible Answer 1:<br /> <input type=\"text\" name=\"answer1\" />");
		html.append("<br />Insert Possible Answer 2 (optional):<br /> <input type=\"text\" name=\"answer2\" />");
		html.append("<br />Insert Possible Answer 3 (optional):<br /> <input type=\"text\" name=\"answer3\" />");
		
		return html.toString();
	}
	

	
	public MultipleChoiceMultipleAnswer(String question, Set<String> ans, Set<String> wrongAns) { // pushes to database
		this.wrongAnswers = wrongAns;
		this.statement = question;
		this.answers = ans;
	}

	public MultipleChoiceMultipleAnswer(int id, Connection con) { // pulls from database
		generate(id, con);
	}
	
	public void generate(int id, Connection con) {
		setqID(id);
		try {
			
			PreparedStatement ps = con.prepareStatement("select * from multiple_choice_multiple_answer_question where question_id = ?");
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
		
			String ans = new String();
			String wrongAns = new String();
			
			while (resultSet.next()) {
				statement = resultSet.getString("statement");
				ans = resultSet.getString("answer");
				wrongAns = resultSet.getString("wrong_answer");
				
			}
			
			String[] strings = ans.split(Pattern.quote(" &&& "));
			answers = new HashSet<String>();
			for (String string : strings) {
				answers.add(string);
			}
			strings = wrongAns.split(Pattern.quote(" &&& "));
			wrongAnswers = new HashSet<String>();
			for (String string : strings) {
				wrongAnswers.add(string);
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

	public int solve(ArrayList<String> answer) {
		//if (answer.size()!=numAnswers) {
		//	return 0; // input cleansing
		//}
		// need to implement error checking TODO
		int score =0;
		for (String string : answer) {
			if (this.answers.contains(string)) { //TODO this might not be good
				return 1;
			}else{
				return 0;
			}
		}
		return score;
	}

	public Set<String> getWrongAnswers() {
		return wrongAnswers;
	}

	public void setWrongAnswers(Set<String> wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}

	@Override
	public String toHTMLString() {
		// TODO Auto-generated method stub
		return null;
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
	public void pushToDB(Connection con) {
		Statement stmt;
		
		try {
			stmt = con.createStatement();
			StringBuilder sqlString = new StringBuilder("INSERT INTO multiple_choice_multiple_answer_question VALUES(null,");
			sqlString.append(statement);
			sqlString.append("\",\" ");
			for (String string : answers) {
				sqlString.append(string);
				sqlString.append(" &&& ");
			}
			sqlString.replace(sqlString.length()-5, sqlString.length(), "");
			sqlString.append("\" ");
			for (String string : wrongAnswers) {
				sqlString.append(string);
				sqlString.append(" &&& ");
			}
			sqlString.replace(sqlString.length()-5, sqlString.length(), "");
			sqlString.append("\" ");
			System.out.print(sqlString.toString());
			ResultSet resultSet = stmt.executeQuery(sqlString.toString());
			
			stmt = con.createStatement();
			sqlString = new StringBuilder("SELECT * FROM multiple_choice_multiple_answer_question WHERE statement=\"");
			sqlString.append(statement);
			sqlString.append("\" ");
			
			System.out.print(sqlString.toString());
			resultSet = stmt.executeQuery(sqlString.toString());
			
			
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
		return answers.size();
	}



	@Override
	public int getNumAnswers() {
		return answers.size();
	}
}
