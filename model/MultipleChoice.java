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
	
	public int getqID() {
		return qID;
	}

	public void setqID(int qID) {
		this.qID = qID;
	}

	public MultipleChoice(String question, HashSet<String> wrongAns, String ans, Connection con) { // pushes to database
		Statement stmt;
		this.statement = question;
		this.wrongAnswers = wrongAns;
		this.answer = ans;
		
		try {
			stmt = con.createStatement();
			StringBuilder sqlString = new StringBuilder("INSERT INTO multiple_choice_question VALUES(null,");
			sqlString.append(question);
			sqlString.append("\",\"");
			sqlString.append(ans);
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
		if (this.answer.equals(ans)) {
			return 1;
		}else{
			return 0;
		}
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
                html.append("<input type=\"radio\" name=\""+ qID + "\" value=\"");
                html.append(s + "\"> " + s);
                html.append("<br />");       
        }
        
        html.append("<input type=\"radio\" name=\""+ qID + "\" value=\"");
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

}
