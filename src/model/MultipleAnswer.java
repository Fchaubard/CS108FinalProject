package model;

import java.util.Set;

public class MultipleAnswer implements Question {
	
	public static final int type=5;
	private String statement;
	private Set<String> answers;
	

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
	public void generate() {
		// TODO Auto-generated method stub

	}

	@Override
	public int solve() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toHTMLString() {
		// TODO Auto-generated method stub
		return null;
	}


}
