package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class Quiz {
	
	private String quizName;
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
	
	public Quiz(int id) throws SQLException {
		Connection con = MyDB.getConnection();
		
		PreparedStatement quizQuery = con.prepareStatement("select * from quiz where quiz_id = ?");
		
		quizQuery.setInt(1, id);
		ResultSet rs = quizQuery.executeQuery();
		
		int creatorID = -1;
		
		while(rs.next()) {
			quizName = rs.getString("name");
			random = rs.getBoolean("random");
			onePageMultiPage = rs.getBoolean("one_page");
			immediateCorrection = rs.getBoolean("immediate_correction");
			practiceMode = rs.getBoolean("practice_mode");
			creatorID = rs.getInt("creator_id");
			category = rs.getString("category");
			description = rs.getString("description");
		}
		
		//TODO get Creator Username
		PreparedStatement userQuery = con.prepareStatement("select * from user where user_id = ?");
		userQuery.setInt(1, creatorID);
		ResultSet r = userQuery.executeQuery();
		
		while(r.next()) {
			creatorUserName = r.getString("username");
		}
		
		//TODO get history of quiz
		PreparedStatement historyQuery = con.prepareStatement("select * from history where quiz_id = ?");
		historyQuery.setInt(1, id);
		ResultSet set = historyQuery.executeQuery();
		
		while(set.next()) {
			QuizAttempts qa = new QuizAttempts(set.getInt("user_id"), set.getInt("score"), set.getDate("date"), set.getInt("time_took"));
			history.add(qa);
		}
		
		//TODO get questions from table
		PreparedStatement questionQuery = con.prepareStatement("select * from quiz_question_mapping where quiz_id = ?");
		questionQuery.setInt(1, id);
		ResultSet resultSet = questionQuery.executeQuery();
		questions = new ArrayList<Question>();
		while(resultSet.next()) {
			int questionType = resultSet.getInt("question_type");
			int questionID = resultSet.getInt("question_id");
			switch(questionType) {
				case 1: 
					QuestionResponse qr = new QuestionResponse(questionID, con);
					questions.add(qr);
					break;
				
				case 2:
					FillInTheBlank fb = new FillInTheBlank(questionID, con);
					questions.add(fb);
					break;
					
				case 3:
					MultipleChoice mc = new MultipleChoice(questionID, con);
					questions.add(mc);
					break;
					
				case 4:
					PictureResponse pr = new PictureResponse(questionID, con);
					questions.add(pr);
					break;
					
				case 5:
					MultipleAnswer ma = new MultipleAnswer(questionID, con);
					questions.add(ma);
					break;
					
				case 6:
					//MultipleChoiceMultipleAnswer mcma = new MultipleChoiceMultipleAnswer(questionID, con );
					//questions.add(mcma);
					break;
					
				case 7:
					//Matching m = new Matching(questionID, con);
					//questions.add(m);
					break;
			}
		}
		
		currentQuestionInteger = -1;
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
		
		currentQuestionInteger = -1;
	}
	
	public Question getNextQuestion(){
		currentQuestionInteger++;
		return questions.get(currentQuestionInteger);
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDescription() {
		return description;
	}
}
