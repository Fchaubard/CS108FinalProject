package model;

import java.util.Set;

public class PictureResponse implements Question {

	public static final int type=4;
	private String url;
	private Set<String> answers;
	

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
