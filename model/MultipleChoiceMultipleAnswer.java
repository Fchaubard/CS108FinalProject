package model;

import java.util.Set;

public class MultipleChoiceMultipleAnswer implements Question {

	public static final int type=6;
	private String statement;
	private Set<String> answers;
	private Set<String> wrongAnswers;
	

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
	public int solve(Set<String> answer) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Set<String> getWrongAnswers() {
		return wrongAnswers;
	}

	public void setWrongAnswers(Set<String> wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}

	@Override
	public String toHTMLString() {
		// TODO Auto-generated method stub
		return null;
	}


}
