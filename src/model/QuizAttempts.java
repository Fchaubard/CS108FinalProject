package model;

import java.util.Date;

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

}
