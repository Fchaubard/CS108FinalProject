package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class MultipleChoiceMultipleAnswer implements Question {

	public static final int type=6;
	private String statement;
	private Set<String> answers;
	private Set<String> wrongAnswers;
	private int qID;
	private ArrayList<String> userAnswers;
	private Set<String> options;
	
	public static String getHTMLInputString(){
		
		StringBuilder html = new StringBuilder();
		html.append("<br />Insert Question Statement: <br /><input type=\"text\" name=\"question\" size=\"75\" requied />");
		html.append("<br />Insert All Correct Answers, one on each line:");
		html.append("<br /><textarea name=\"answers\" cols=\"20\" rows=\"10\" required></textarea>");
		html.append("<br />Insert All Incorrect Options, one on each line:");
		html.append("<br /><textarea name=\"wrongAnswers\" cols=\"20\" rows=\"10\" ></textarea>");
		
		return html.toString();
	}
	

	
	public MultipleChoiceMultipleAnswer(String question, Set<String> ans, Set<String> wrongAns) { // pushes to database
		this.wrongAnswers = wrongAns;
		this.statement = question;
		this.answers = ans;
		
		options = new HashSet<String>();
		for(String s : wrongAnswers) {
			options.add(s);
		}
		
		for(String str : answers) {
			options.add(str);
		}
	}

	public MultipleChoiceMultipleAnswer(int id, Connection con) { // pulls from database
		generate(id, con);
	}
	
	public void generate(int id, Connection con) {
		setqID(id);
		try {
			
			PreparedStatement ps = con.prepareStatement("select * from multiple_choice_multiple_answer_question where question_id = ?");
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
		
			String ans = new String();
			String wrongAns = new String();
			
			while (resultSet.next()) {
				statement = resultSet.getString("statement");
				ans = resultSet.getString("answer");
				wrongAns = resultSet.getString("wrong_answers");
				
			}
			
			options = new HashSet<String>();
			
			String[] strings = ans.split(Pattern.quote(" &&& "));
			answers = new HashSet<String>();
			for (String string : strings) {
				answers.add(string);
				options.add(string);
			}
			strings = wrongAns.split(Pattern.quote(" &&& "));
			wrongAnswers = new HashSet<String>();
			for (String string : strings) {
				wrongAnswers.add(string);
				options.add(string);
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

	public Set<String> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<String> answers) {
		this.answers = answers;
	}

	public int solve(ArrayList<String> answer) {
		int score =0;
		for (String string : answer) {
			if (this.answers.contains(string)) {
				score++;
			}else{
				score--;
			}
		}
		
		int diff;
		if(answers.size() > this.answers.size()) {
			diff = answers.size()-this.answers.size();
		}
		else {
			diff = this.answers.size() - answers.size();
		}
		
		score -= diff;
		if(score < 0) score = 0;
		
		return score;
	}

	public Set<String> getWrongAnswers() {
		return wrongAnswers;
	}

	public void setWrongAnswers(Set<String> wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}

	@Override
	public String toHTMLString() {
		StringBuilder html = new StringBuilder();
		
        html.append(statement);
        html.append("<br />");
        
        int counter = 0;
        
        for(String s : options) {
                html.append("<input type=\"checkbox\" name=\"");
        		html.append(type);
        		html.append("_");
                html.append(qID);
                html.append("_");
                html.append(counter);
                html.append("\" value=\"");
                html.append(s + "\">" + s);
                html.append("<br />");
                counter++;
        }
        
		return html.toString();
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
		
		for(String s : answers) {
			correctAnswers.append(s);
			correctAnswers.append(",  ");
		}
		correctAnswers.replace(correctAnswers.length()-2, correctAnswers.length(), "");
		
		return correctAnswers.toString();
	}

	public int getType(){
		return type;
	}

	@Override
	public void pushToDB(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement("insert into multiple_choice_multiple_answer_question values(null, ?, ?, ?)");
		
		statement = statement.trim();
		ps.setString(1, statement);
		
		StringBuilder answersString = new StringBuilder();
		for(String ans : answers) {
			ans = ans.trim();
			answersString.append(ans);
			answersString.append(" &&& ");
		}
		answersString.replace(answersString.length()-5, answersString.length(), "");
		ps.setString(2, answersString.toString());
		
		StringBuilder wrongAnswersString = new StringBuilder();
		for(String wAns : wrongAnswers) {
			wAns = wAns.trim();
			wrongAnswersString.append(wAns);
			wrongAnswersString.append(" &&& ");
		}
		wrongAnswersString.replace(wrongAnswersString.length()-5, wrongAnswersString.length(), "");
		ps.setString(3, wrongAnswersString.toString());
		
		ps.executeUpdate();
		
		PreparedStatement stat = con.prepareStatement("select * from multiple_choice_multiple_answer_question where statement = ?");
		
		stat.setString(1, statement);
		
		ResultSet rs = stat.executeQuery();
		
		while(rs.next()) {
			this.setqID(rs.getInt("question_id"));
		}	
	}

	@Override
	public void setUserAnswers(ArrayList<String> ans) {
		userAnswers = new ArrayList<String>();
		
		for(String s : ans) {
			userAnswers.add(s);
		}
		
	}

	@Override
	public String getUserAnswers() {
		StringBuilder userAns = new StringBuilder();
		
		for(String s : userAnswers) {
			userAns.append(s);
			userAns.append(", ");
		}
		userAns.replace(userAns.length()-2, userAns.length(), "");
		
		return userAns.toString();
	}



	@Override
	public int getTotalQScore() {
		return answers.size();
	}

	@Override
	public int getNumAnswers() {
		return (answers.size() + wrongAnswers.size());
	}
}
