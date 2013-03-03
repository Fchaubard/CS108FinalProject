package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class PictureResponse implements Question {

	public static final int type=4;
	private String url;
	private Set<String> answers;
	private int qID;
	

	public int getqID() {
		return qID;
	}

	public void setqID(int qID) {
		this.qID = qID;
	}

	public PictureResponse(String url, Set<String> ans, int numAnswers, Connection con) { // pushes to database
		Statement stmt;
		
		this.url = url;
		this.answers = ans;
		
		try {
			stmt = con.createStatement();
			StringBuilder sqlString = new StringBuilder("INSERT INTO picture_response_question VALUES(null,\"");
			sqlString.append(url);
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
			sqlString = new StringBuilder("SELECT * FROM picture_response_question WHERE statement=\"");
			sqlString.append(url);
			sqlString.append("\" ");
			
			System.out.print(sqlString.toString());
			resultSet = stmt.executeQuery(sqlString.toString());
			
			
			while (resultSet.next()) {
				this.setqID(resultSet.getInt(0)); // will always be the last one
			}
			
			
			
			
		}catch(Exception e){
			
		}
		
		
		
	}

	public PictureResponse(int id, Connection con) { // pulls from database
		generate(id, con);
	}
	
	public void generate(int id, Connection con) {
		setqID(id);
		try {
			
			PreparedStatement ps = con.prepareStatement("select * from picture_response_question where question_id = ?");
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
			
			
			String ans = new String();
			while (resultSet.next()) {
				url = resultSet.getString("url");
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

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
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
		StringBuilder html = new StringBuilder();
		
		html.append("<img src=\"" + url + "\" width=\"200\" height=\"200\" />" );
        html.append("<br /><input type=\"text\" name=\"");
        html.append(qID);
        html.append("\" />");
		
		return html.toString();
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
}
