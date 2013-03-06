package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import Accounts.Account;
import Accounts.AccountManager;

public class QuizAttempts {

	public Integer userID;
	public Integer quizID;
	public Integer score;
	public Date date;
	public Integer time;
	
	public QuizAttempts(){
		
	}
	
	public QuizAttempts(Integer userID,Integer quizID,Integer score,Date date, Integer time ){
		
		this.userID = userID;
		this.quizID = quizID;
		this.score = score;
		this.date = date;
		this.time = time;
	}
	
	public Integer getQuizID() {
		return quizID;
	}

	public void setQuizID(Integer quizID) {
		this.quizID = quizID;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	
	public void pushAttemptToDB(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement("insert into history values(?, ?, ?, CURDATE(), ?)");
		
		ps.setInt(1, userID);
		ps.setInt(2, quizID);
		ps.setInt(3, score);
		ps.setInt(4, time);
		
		ps.execute();
	}
	
	public String printAttemt(AccountManager am) {
		StringBuilder attempt = new StringBuilder();
		
		attempt.append(am.getAccount(userID).getName());
		attempt.append("(score: ");
		attempt.append(score);
		attempt.append("; time: ");
		attempt.append(time/1000);
		attempt.append(" s)");
		
		return attempt.toString();
	}
}
