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

public class QuestionResponse implements Question {

	public static final int type=1;
	private String statement;
	private Set<String> answers;
	private int qID;
	
	public int getqID() {
		return qID;
	}

	public void setqID(int qID) {
		this.qID = qID;
	}

	public QuestionResponse(String question, HashSet<String> ans, Connection con) { // pushes to database
		Statement stmt;
		this.statement = question;
		this.answers = ans;
		
		try {
			stmt = con.createStatement();
			StringBuilder sqlString = new StringBuilder("INSERT INTO question_response VALUES(null,");
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
		
		StringTokenizer tokenizer = new StringTokenizer(ans, " &&& ");
		answers = new HashSet<String>();
		while(tokenizer.hasMoreTokens()) {
			answers.add(tokenizer.nextToken());
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
		html.append("<br><input type = \"text\" name = \"");
		html.append(qID);
		html.append("\" />");
		
		return html.toString();
	}

	@Override
	public void generate(int id, Connection con) {
		// TODO Auto-generated method stub
		
	}

}


