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

public class Matching implements Question {

	public static final int type = 7;
	private String statement;
	private ArrayList<Integer> answers;
	private ArrayList<String> rowOne;
	private ArrayList<String> rowTwo;
	private int qID;


	public Matching(String statement, ArrayList<Integer> ans, ArrayList<String> rowOne,ArrayList<String> rowTwo, Connection con) { // pushes to database
		this.statement = statement;
		this.answers = ans;
		this.rowOne = rowOne;
		this.rowTwo = rowTwo;
		
		// now create a row in the database
		Statement stmt;
		try {
			stmt = con.createStatement();
			StringBuilder sqlString = new StringBuilder("INSERT INTO matching_question VALUES(null,\"");
			
			sqlString.append(statement);
			
			sqlString.append("\",\" ");
			for (String strings : rowOne) {
				sqlString.append(strings);
				sqlString.append(" &&& ");
			}
			sqlString.replace(sqlString.length()-5, sqlString.length(), "");
			sqlString.append("\",\" ");
			for (String strings : rowTwo) {
				sqlString.append(strings);
				sqlString.append(" &&& ");
			}
			sqlString.replace(sqlString.length()-5, sqlString.length(), "");
			sqlString.append("\",\" ");
			for (Integer ints : ans) {
				sqlString.append(ints);
				sqlString.append(" &&& ");
			}
			sqlString.replace(sqlString.length()-5, sqlString.length(), "");
			sqlString.append("\" ");
			
			System.out.print(sqlString.toString());
			ResultSet resultSet = stmt.executeQuery(sqlString.toString());
			
			stmt = con.createStatement();
			sqlString = new StringBuilder("SELECT * FROM matching_question WHERE statement=\"");
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

	public Matching(int id, Connection con) { // pulls from database
		generate(id, con);
	}
	
	@Override
	public void generate(int id, Connection con) {
		this.qID = id;
		try {
			
			PreparedStatement ps = con.prepareStatement("select * from matching_question where question_id = ?");
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
			
			
			String ans = new String();
			String row_1 = new String();
			String row_2 = new String();
			while (resultSet.next()) {
				statement = resultSet.getString("statement");
				ans = resultSet.getString("answer");
				row_1 = resultSet.getString("row_one");
				row_2 = resultSet.getString("row_two");
			}
			String[] strings = row_1.split(Pattern.quote(" &&& "));
			rowOne = new ArrayList<String>();
			for (String string : strings) {
				rowOne.add((string));
			}
			
			strings = row_2.split(Pattern.quote(" &&& "));
			rowTwo = new ArrayList<String>();
			for (String string : strings) {
				rowTwo.add((string));
			}

			strings = ans.split(Pattern.quote(" &&& "));
			answers = new ArrayList<Integer>();
			for (String string : strings) {
				answers.add(Integer.parseInt(string));
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

	public Set<Integer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<Integer> answers) {
		this.answers = answers;
	}

	public int solve(ArrayList<String>  answer) {
		//TODO implement this shit
		return 0;
	}

	public ArrayList<String> getRowOne() {
		return rowOne;
	}

	public void setRowOne(ArrayList<String> rowOne) {
		this.rowOne = rowOne;
	}

	public ArrayList<String> getRowTwo() {
		return rowTwo;
	}

	public void setRowTwo(ArrayList<String> rowTwo) {
		this.rowTwo = rowTwo;
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



}
