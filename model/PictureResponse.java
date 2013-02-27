package model;

import java.util.ArrayList;
import java.util.Set;

public class PictureResponse implements Question {

	public static final int type=4;
	private String url;
	private Set<String> answers;
	private int qID;
	
	public PictureResponse(String picture, Set<String> ans, int id) {
		url = picture;
		
		for(String s : ans) {
			answers.add(s);
		}
		
		qID = id;
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
	

	@Override
	public void generate() {
		// TODO Auto-generated method stub

	}

	public int solve(ArrayList<String> answer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toHTMLString() {
		StringBuilder html = new StringBuilder();
		
		html.append("<img src=\"" + url + "\" width=\"304\" height=\"228\" />" );
        html.append("<br /><input type=\"text\" name=\"");
        html.append(qID);
        html.append("\" />");
		
		return html.toString();
	}


}
