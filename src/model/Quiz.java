package model;

import java.util.ArrayList;

public class Quiz {
	
	ArrayList<Question> questions;
	Boolean random;
	Boolean onePageMultiPage;
	Boolean immediateCorrection;
	Boolean practiceMode;
	Integer currentQuestionInteger;
	String creatorUserName;
	String description;
	ArrayList<QuizAttempts> history;
	
	public Quiz(){
		// TODO populate all the attributes above
		
	}



	public double score(){
		
		
		return 0.0;
	}
	
	
	public void generate(){
	
		
		
	}
	
	public Question getNextQuestion(){
		
		
		return new QuestionResponse();
		
	}
	
	

}
