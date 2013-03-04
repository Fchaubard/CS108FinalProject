package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class QuestionResponse implements Question {

	public static final int type=1;
	private String statement;
	private Set<String> answers;
	private int qID;
	
	
	public QuestionResponse(String question, HashSet<String> ans, Connection con) { // pushes to database
		
		this.statement = question;
		this.answers = ans;
		Statement stmt;
		try {
			stmt = con.createStatement();
			StringBuilder sqlString = new StringBuilder("INSERT INTO question_response VALUES(null,");
			sqlString.append(question);
			sqlString.append("\",\" ");
			for (String string : ans) {
				sqlString.append(string);
				sqlString.append(" &&& ");
			}
			sqlString.replace(sqlString.length()-5, sqlString.length(), "");
			sqlString.append("\" ");
			
			System.out.print(sqlString.toString());
			ResultSet resultSet = stmt.executeQuery(sqlString.toString());
			
			stmt = con.createStatement();
			sqlString = new StringBuilder("SELECT * FROM question_response WHERE statement=\"");
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
			correctAnswers.append(s);
			correctAnswers.append(" ");
		}
		
		return correctAnswers.toString();
	}
	public int getType(){
		return type;
	}
}


