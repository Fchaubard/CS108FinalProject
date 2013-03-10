package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	public static String getHTMLInputString(){
		
		StringBuilder html = new StringBuilder();
		html.append("<br />Insert Question Statement: <br /><input type=\"text\" name=\"question\" size=\"75\" />");
		html.append("<br />Insert All Correct Answers, one on each line:");
		html.append("<br /><textarea name=\"answers\" cols=\"20\" rows=\"10\" required></textarea>");
		html.append("<br />Insert All Incorrect Options, one on each line:");
		html.append("<br /><textarea name=\"wrongAnswers\" cols=\"20\" rows=\"10\" required></textarea>");
		
		return html.toString();
	}
	

	
	public MultipleChoiceMultipleAnswer(String question, Set<String> ans, Set<String> wrongAns) { // pushes to database
		this.wrongAnswers = wrongAns;
		this.statement = question;
		this.answers = ans;
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
			
			String[] strings = ans.split(Pattern.quote(" &&& "));
			answers = new HashSet<String>();
			for (String string : strings) {
				answers.add(string);
			}
			strings = wrongAns.split(Pattern.quote(" &&& "));
			wrongAnswers = new HashSet<String>();
			for (String string : strings) {
				wrongAnswers.add(string);
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
		//if (answer.size()!=numAnswers) {
		//	return 0; // input cleansing
		//}
		// need to implement error checking TODO
		int score =0;
		for (String string : answer) {
			if (this.answers.contains(string)) { //TODO this might not be good
				return 1;
			}else{
				return 0;
			}
		}
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
        
        for(String s : wrongAnswers) {
                html.append("<input type=\"checkbox\" name=\"");
        		html.append(type);
        		html.append("_");
                html.append(qID);
                html.append("\" value=\"");
                html.append(s + "\">" + s);
                html.append("<br />");       
        }
        
        for(String string : answers) {
        	html.append("<input type=\"checkbox\" name=\"");

    		html.append(type);
    		html.append("_");

    		html.append(qID);
    		html.append("\" value=\"");
            html.append(string + "\">" + string);
            html.append("<br />");
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
		
		ps.setString(1, statement);
		
		StringBuilder answersString = new StringBuilder();
		for(String ans : answers) {
			answersString.append(ans);
			answersString.append(" &&& ");
		}
		answersString.replace(answersString.length()-5, answersString.length(), "");
		ps.setString(2, answersString.toString());
		
		StringBuilder wrongAnswersString = new StringBuilder();
		for(String wAns : wrongAnswers) {
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
		return answers.size();
	}
}
