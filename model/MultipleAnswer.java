package model;

import java.util.Set;

public class MultipleAnswer implements Question {
	
	public static final int type=5;
	private String statement;
	private Set<String> answers;
	private int qID;
	private int numAnswers;
	
	public MultipleAnswer(String question, Set<String> ans, int id, int numAnswers) {
		generate(question, ans, id, numAnswers);
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
	
	public void generate(String question, Set<String> ans, int id, int numAnswers) {
		statement = question;
		
		for(String s : ans) {
			answers.add(s);
		}
		
		this.numAnswers = numAnswers;
		
		qID = id;

	}

	@Override
	public int solve(Set<String> answer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toHTMLString() {
		StringBuilder html = new StringBuilder();
		
		html.append(statement);
		html.append("<br />");
		
		for(int i = 0; i < numAnswers; i++) {
			html.append("<input type = \"text\" name = \"");
			html.append(i);
			html.append("\" /><br />");
		}
		
		return html.toString();
	}

	@Override
	public void generate() {
		// TODO Auto-generated method stub
		
	}


}
