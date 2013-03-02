package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class FillInTheBlank implements Question {

	public static final int type=2;
	private String statement;
	private Set<String> answers;
	private int qID;
	
	public FillInTheBlank(String question, Set<String> ans, Connection con) { // pushes to database
		Statement stmt;
		this.statement = question; // this should have the ________ in it already
		this.answers = ans; // need to add the &&&
		
		try {
			stmt = con.createStatement();
			StringBuilder sqlString = new StringBuilder("INSERT INTO fill_in_the_blank_question VALUES(null,\"");
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
			sqlString = new StringBuilder("SELECT * FROM fill_in_the_blank_question WHERE statement=\"");
			sqlString.append(statement);
			sqlString.append("\" ");
			
			System.out.print(sqlString.toString());
			resultSet = stmt.executeQuery(sqlString.toString());
			
			
			while (resultSet.next()) {
				this.qID = resultSet.getInt("question_id"); // will always be the last one
			}
		}catch(Exception e){
			
		}
		
		
		
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
		
		if (answers.contains(ans)) {
			return 1;
		}else{
			return 0;
		}		
	}

	
	@Override
	public String toHTMLString() {
		int index = this.statement.indexOf("__________");
		StringBuilder html = new StringBuilder();
		html.append(this.statement.substring(0, index));
		html.append("<input type=\"text\" name=\"");
		html.append(qID);
		html.append("\" />");
		html.append(this.statement.substring(index + 10));
		
		return html.toString();
	}

	@Override
	public int getqID() {
		return qID;
	}

	
}
