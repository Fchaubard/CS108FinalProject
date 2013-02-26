package model;

import java.util.Set;

public class FillInTheBlank implements Question {

	public static final int type=2;
	private String statement;
	private Set<String> answers;
	private int qID;
	
	public FillInTheBlank(String question, Set<String> ans, int id) {
		statement = question;
		
		for(String s : ans) {
			answers.add(s);
		}
		
		qID = id;
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
		int index = this.statement.indexOf("__________");
		
		StringBuilder html = new StringBuilder();
		html.append(this.statement.substring(0, index));
		html.append("<input type=\"text\" name=\"");
		html.append(qID);
		html.append("\" />");
		html.append(this.statement.substring(index + 10));
		
		return html.toString();
	}

}
