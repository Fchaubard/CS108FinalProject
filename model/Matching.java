package model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Set;

public class Matching implements Question {

	public static final int type = 7;
	private String statement;
	private Set<Integer> answers;
	private ArrayList<String> rowOne;
	private ArrayList<String> rowTwo;

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

	@Override
	public void generate(int id, Connection con) {
		// TODO Auto-generated method stub
		
	}


}
