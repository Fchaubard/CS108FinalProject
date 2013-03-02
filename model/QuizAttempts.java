package model;

import java.sql.Date;

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
	
}
