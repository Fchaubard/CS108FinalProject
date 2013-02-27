package model;

import java.util.ArrayList;

public class Quiz {
	
	private ArrayList<Question> questions;
	private boolean random;
	private boolean onePageMultiPage; //is this true for one page and false for multi-page?
	private boolean immediateCorrection;
	private boolean practiceMode;
	private int currentQuestionInteger;
	private String creatorUserName;
	private String description;
	private ArrayList<QuizAttempts> history;
	private String category;
	
	public Quiz(ArrayList<Question> q, boolean random, boolean onePage, boolean immediateCorrect, boolean practice, String userName, String description, ArrayList<QuizAttempts> history, String category){
		generate(q, random, onePage, immediateCorrect, practice, userName, description, history, category);
	}

	public double score(){
		double score = 0;
		
		for(int i = 0; i < questions.size(); i++) {
			//TODO fix this to get user answer for each question.
			score += questions.get(i).solve(null);
		}
		
		return score;
	}
	
	
	public void generate(ArrayList<Question> q, boolean random, boolean onePage, 
			boolean immediateCorrect, boolean practice, String userName, String description, 
			ArrayList<QuizAttempts> history, String category){
		for(Question quest : q) {
			questions.add(quest);
		}
		
		this.random = random;
		
		onePageMultiPage = onePage;
		
		immediateCorrection = immediateCorrect;
		
		practiceMode = practice;
		
		creatorUserName = userName;
		
		this.description = description;
		
		for(QuizAttempts qa : history) {
			this.history.add(qa);
		}
		
		this.category = category;
	}
	
	public Question getNextQuestion(){
		return questions.get(currentQuestionInteger+1);
		
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDescription() {
		return description;
	}
}
