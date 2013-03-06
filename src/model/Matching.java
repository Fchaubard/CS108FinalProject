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
	private ArrayList<Integer> correctIndexesOfRow1MappingtoRow2;
	private ArrayList<String> rowOne;
	private ArrayList<String> rowTwo;
	private int qID;

	// TODO this is NOT RIGHT
	public static String getHTMLInputString(){
		// TODO
		StringBuilder html = new StringBuilder();
		html.append("<br />Insert picture url: <br /><input type=\"text\" name=\"url\" />");
		html.append("<br />Insert Possible Answer 1:<br /> <input type=\"text\" name=\"answer1\" />");
		html.append("<br />Insert Possible Answer 2 (optional):<br /> <input type=\"text\" name=\"answer2\" />");
		html.append("<br />Insert Possible Answer 3 (optional):<br /> <input type=\"text\" name=\"answer3\" />");
		
		return html.toString();
	}
	
	public Matching(String statement, ArrayList<Integer> ans, ArrayList<String> rowOne,ArrayList<String> rowTwo) { // pushes to database
		this.statement = statement;
		this.correctIndexesOfRow1MappingtoRow2 = ans;
		this.rowOne = rowOne;
		this.rowTwo = rowTwo;
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
			correctIndexesOfRow1MappingtoRow2 = new ArrayList<Integer>();
			for (String string : strings) {
				correctIndexesOfRow1MappingtoRow2.add(Integer.parseInt(string));
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

	public ArrayList<Integer> getAnswers() {
		return correctIndexesOfRow1MappingtoRow2;
	}

	public void setAnswers(ArrayList<Integer> answers) {
		this.correctIndexesOfRow1MappingtoRow2 = answers;
	}

	public int solve(ArrayList<String>  answer) {
		int score =0;
		if (answer.size()!=rowOne.size()) {
			return 0; // input cleansing
		}
		for (int i = 0; i < answer.size(); i++) {
			if (correctIndexesOfRow1MappingtoRow2.get(i)==Integer.parseInt(answer.get(i))) {
				score++;
			}
		}
		return score;
		
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

	@Override
	public String getCorrectAnswers() {
		StringBuilder correctAnswers = new StringBuilder();
		
		for(int i = 0; i < correctIndexesOfRow1MappingtoRow2.size(); i++) {
			int x = correctIndexesOfRow1MappingtoRow2.get(i);
			correctAnswers.append(rowOne.get(i));
			correctAnswers.append(" ");
			correctAnswers.append(rowTwo.get(x));
			correctAnswers.append("\n");
		}
		
		return correctAnswers.toString();
	}
	public int getType(){
		return type;
	}

	@Override
	public void pushToDB(Connection con) {
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
			for (Integer ints : correctIndexesOfRow1MappingtoRow2) {
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

	@Override
	public void setUserAnswers(ArrayList<String> ans) {
		// TODO IMPLEMENT THIS
		
	}

	@Override
	public String getUserAnswers() {
		// TODO IMPLEMENT THIS
		return null;
	}
}
