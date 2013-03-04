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

public class MultipleChoice implements Question {

	public static final int type=3;
	private String statement;
	private Set<String> wrongAnswers;
	private String answer;
	private int qID;
	
	public static String getHTMLInputString(){
		
		StringBuilder html = new StringBuilder();
		html.append("<br />Insert Question Statement: <br /><input type=\"text\" name=\"statement\" />");
		html.append("<br />Insert Answer:<br /> <input type=\"text\" name=\"answer1\" />");
		html.append("<br />Insert Wrong Answer 1:<br /> <input type=\"text\" name=\"wrongAnswer1\" />");
		html.append("<br />Insert Wrong Answer 2:<br /> <input type=\"text\" name=\"wrongAnswer2\" />");
		html.append("<br />Insert Wrong Answer 3:<br /> <input type=\"text\" name=\"wrongAnswer3\" />");
		
		return html.toString();
	}

	public int getqID() {
		return qID;
	}

	public void setqID(int qID) {
		this.qID = qID;
	}

	public MultipleChoice(String question, HashSet<String> wrongAns, String ans) { // pushes to database
		this.statement = question;
		this.wrongAnswers = wrongAns;
		this.answer = ans;
	}
	
	public MultipleChoice(Integer id, Connection con) throws SQLException {
		this.qID = id;
		
		PreparedStatement ps = con.prepareStatement("select * from multiple_choice_question where question_id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		String wrong = new String();
		while(rs.next()) {
			statement = rs.getString("statement");
			answer = rs.getString("answer");
			wrong = rs.getString("wrong_answers");
		}
		
		
		String[] strings = wrong.split(Pattern.quote(" &&& "));
		wrongAnswers = new HashSet<String>();
		for (String string : strings) {
			wrongAnswers.add(string);
		}
			
		
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public Set<String> getWrongAnswers() {
		return wrongAnswers;
	}

	public void setWrongAnswers(Set<String> wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}
	

	@Override
	public int solve(ArrayList<String> ans) {
		if (ans.size()!=1) {
			return 0; // input cleansing
		}
		for (String a : ans) {
			
			if (answer.equals(a)) {
				return 1;
			}
		
		}
		return 0;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Override
	public String toHTMLString() {
		StringBuilder html = new StringBuilder();
		
        html.append(statement);
        html.append("<br />");
        
        for(String s : wrongAnswers) {
                html.append("<input type=\"radio\" name=\"");
        		html.append(type);
        		html.append("_");
                html.append(qID);
                html.append("\" value=\"");
                html.append(s + "\"> " + s);
                html.append("<br />");       
        }
        
        html.append("<input type=\"radio\" name=\"");

		html.append(type);
		html.append("_");

		html.append(qID);
		html.append("\" value=\"");
        html.append(answer + "\"> " + answer);
        html.append("<br />");
        
		return html.toString();
	}
	
	@Override
	public String getCorrectAnswers() {
		return answer;
	}

	@Override
	public void generate(int id, Connection con) {
		// TODO Auto-generated method stub
		
	}
	public int getType(){
		return type;
	}

	@Override
	public void pushToDB(Connection con) {
		Statement stmt;
		
		try {
			stmt = con.createStatement();
			StringBuilder sqlString = new StringBuilder("INSERT INTO multiple_choice_question VALUES(null,");
			sqlString.append(statement);
			sqlString.append("\",\"");
			sqlString.append(answer);
			sqlString.append("\",\"");
			for (String string : wrongAnswers) {
				sqlString.append(string);
				sqlString.append(" &&& ");
			}
			sqlString.replace(sqlString.length()-5, sqlString.length(), "");
			sqlString.append("\" ");
			
			System.out.print(sqlString.toString());
			ResultSet resultSet = stmt.executeQuery(sqlString.toString());
			
			stmt = con.createStatement();
			sqlString = new StringBuilder("SELECT * FROM multiple_choice_question WHERE statement=\"");
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

}
