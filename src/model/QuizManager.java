package model;

import java.sql.Connection;

public class QuizManager {
	
	private Connection con;
	
	public QuizManager(Connection con) {
		this.con = con;//Servlets.MyDB.getConnection();
	}
	
	
	//Question DB getters
	public MultipleChoice getMC(int id) {
		return null;
		
	}
	
	public MultipleAnswer getMA(int id) {
		return null;
		
	}
	
	public MultipleChoiceMultipleAnswer getMCMA(int id) {
		return null;
		
	}
	
	public PictureResponse getPR(int id) {
		return null;
		
	}
	
	public QuestionResponse getQR(int id) {
		return null;
		
	}
	
	public FillInTheBlank getFB(int id) {
		return null;
	}
	
	public Matching getMatching(int id) {
		return null;
		
	}
	
	//Question DB setters
	public void addMC(MultipleChoice mc) {
		
	}
	
	public void addMA(MultipleAnswer ma) {
		
	}

	public void addMCMA(MultipleChoiceMultipleAnswer mc) {
	
	}
	
	public void addPR(PictureResponse pr) {
		
	}
	
	public void addQR(QuestionResponse qr) {
		
	}
	
	//quiz stuff
	public Quiz getQuiz(int id) {
		return null;
	}
	
	public void addQuiz(Quiz q) {
		
	}

}
