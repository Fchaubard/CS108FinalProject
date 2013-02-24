package model;

import java.util.ArrayList;
import java.util.Set;

public class Matching implements Question {

	public static final int type=7;
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
	

	@Override
	public void generate() {
		// TODO Auto-generated method stub

	}

	@Override
	public int solve() {
		// TODO Auto-generated method stub
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


}
