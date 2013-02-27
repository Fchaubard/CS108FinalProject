package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;


public class MultipleAnswer implements Question {
	
	public static final int type=5;
	private String statement;
	private Set<String> answers;
	private int qID;
	private int numAnswers;
	
	public MultipleAnswer(String question, Set<String> ans, int numAnswers, Connection con) { // pushes to database
		Statement stmt;
		this.numAnswers = numAnswers;
		this.statement = question;
		this.answers = ans;
		
		try {
			stmt = con.createStatement();
			StringBuilder sqlString = new StringBuilder("INSERT INTO multiple_answer_question VALUES(null,\"");
			sqlString.append(question);
			sqlString.append("\",\" ");
			for (String string : ans) {
				sqlString.append(string);
				sqlString.append(" &&& ");
			}
			sqlString.append("\" ");
			
			System.out.print(sqlString.toString());
			ResultSet resultSet = stmt.executeQuery(sqlString.toString());
			
			stmt = con.createStatement();
			sqlString = new StringBuilder("SELECT * FROM multiple_answer_question WHERE statement=\"\"");
			sqlString.append(statement);
			sqlString.append("\" ");
			
			System.out.print(sqlString.toString());
			resultSet = stmt.executeQuery(sqlString.toString());
			
			
			while (resultSet.next()) {
				this.setqID(resultSet.getInt(0)); // will always be the last one
			}
			
			
			
			
		}catch(Exception e){
			
		}
		
		
		
	}

	public MultipleAnswer(int id, Connection con) { // pulls from database
		generate(id, con);
	}
	
	public void generate(int id, Connection con) {
		setqID(id);
		Statement stmt;
		try {
			stmt = con.createStatement();
			StringBuilder sqlString = new StringBuilder("SELECT * FROM multiple_answer_question WHERE id=\"\"");
			sqlString.append(id);
			sqlString.append("\" ");
			
			System.out.print(sqlString.toString());
			ResultSet resultSet = stmt.executeQuery(sqlString.toString());
			
			String ans = new String();
			while (resultSet.next()) {
				statement = resultSet.getString(1);
				ans = resultSet.getString(2);
				
			}
			
			StringTokenizer tokenizer = new StringTokenizer(ans, "&&&");
			while(tokenizer.hasMoreTokens()) {
				answers.add(tokenizer.nextToken());
			}
			
			this.numAnswers = answers.size();
			
			
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toHTMLString() {
		StringBuilder html = new StringBuilder();
		
		html.append(statement);
		html.append("<br />");
		
		for(int i = 0; i < numAnswers; i++) {
			html.append("<input type = \"text\" name = \"");
			html.append(i);
			html.append("\" /><br />");
		}
		
		return html.toString();
	}

	@Override
	public void generate() {
		// TODO Auto-generated method stub
		
	}

	public int getqID() {
		return qID;
	}

	public void setqID(int qID) {
		this.qID = qID;
	}


}
