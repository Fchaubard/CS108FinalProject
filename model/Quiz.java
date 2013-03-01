package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Accounts.Account;

public class Quiz {
	
	private String quizName;
	private ArrayList<Question> questions;
	private boolean random;
	private boolean onePageMultiPage; //is this true for one page and false for multi-page?
	private boolean immediateCorrection;
	private boolean practiceMode;
	private int currentQuestionInteger;
	private Account creator;
	private String description;
	private ArrayList<QuizAttempts> history;
	private String category;
	private int quiz_id;
	private Connection con;
	

	//creating a quiz
	public Quiz(ArrayList<Question> q, boolean random, boolean onePage, boolean immediateCorrect, boolean practice, int userID, String quizName, String description, String category){
		con = MyDB.getConnection(); // should probably get passed in when the site is ready
		
		this.quizName=quizName;
		this.questions=q;
		this.random=random;
		this.onePageMultiPage=onePage; //is this true for one page and false for multi-page?
		this.immediateCorrection=immediateCorrect;
		this.practiceMode=practice;
		this.description=description;
		this.category=category;
		this.creator =getCreatorFromID(userID);
	}
	// inserting a quiz into the database
	public void finishAndStoreQuizInDatabase(){
		
		StringBuilder sqlString = new StringBuilder("INSERT INTO quiz VALUES(null,\"");
		
		//name
		sqlString.append(quizName);
		sqlString.append("\",\" ");
		//random
		sqlString.append(random);
		sqlString.append("\",\" ");
		//one_page
		sqlString.append(onePageMultiPage);
		sqlString.append("\",\" ");
		//immediate_correction
		sqlString.append(immediateCorrection);
		sqlString.append("\",\" ");
		//practice_mode	
		sqlString.append(practiceMode);
		sqlString.append("\",\" ");
		//creator_id	
		sqlString.append(creator.getId());
		sqlString.append("\",\" ");
		//create_date	
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		String d = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
		sqlString.append(d);
		sqlString.append("\",\" ");
		//category	
		sqlString.append(category);
		sqlString.append("\",\" ");
		//description
		sqlString.append(description);
		sqlString.append("\" ");
		
		System.out.println(sqlString.toString());
		
		try {
			Statement stmt;
			stmt = con.createStatement();
			ResultSet resultSet;
			resultSet = stmt.executeQuery(sqlString.toString());
			stmt = con.createStatement();
			sqlString = new StringBuilder("SELECT * FROM quiz WHERE name=\"");
			sqlString.append(quizName);
			sqlString.append("\" ");
			System.out.println(sqlString.toString());
			resultSet = stmt.executeQuery(sqlString.toString());
			
			while (resultSet.next()) {
				this.setQuiz_id(resultSet.getInt("question_id")); // will always be the last one
			}
			
			for (int j = 0; j < questions.size(); j++) {
				String databaseName = new String();
				switch(questions.get(j).type) {
				case 1: 
					databaseName = "question_response";
					break;
				
				case 2:
					databaseName = "fill_in_the_blank_question";
					
					break;
					
				case 3:
					databaseName = "multiple_choice_question";
					
					break;
					
				case 4:
					databaseName = "picture_response_question";
					break;
					
				case 5:
					databaseName= "multiple_answer_question";
					break;
					
				case 6:
					databaseName="multiple_choice_multiple_answer_question";
					break;
					
				case 7:
					databaseName="matching_question";
					
					break;
			}
				//input question quiz mappings
				sqlString = new StringBuilder("INSERT INTO " + databaseName +
												" VALUES(null,"+quiz_id+","
												+questions.get(j).getqID()+","
												+questions.get(j).type+""
												);
			}
			
			
			System.out.println(sqlString.toString());
			resultSet = stmt.executeQuery(sqlString.toString());
		
		
		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO
	}
	
	
	public Account getCreatorFromID(int id){
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("select * from users where user_id = ?");
		
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		return new Account(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Quiz(int id) throws SQLException {
		this.quiz_id = id;
		Connection con = MyDB.getConnection();
		
		PreparedStatement quizQuery = con.prepareStatement("select * from quiz where quiz_id = ?");
		
		quizQuery.setInt(1, id);
		ResultSet rs = quizQuery.executeQuery();
		
		int creatorID = -1;
		
		while(rs.next()) {
			setQuizName(rs.getString("name"));
			setRandom(rs.getBoolean("random"));
			setOnePageMultiPage(rs.getBoolean("one_page"));
			setImmediateCorrection(rs.getBoolean("immediate_correction"));
			setPracticeMode(rs.getBoolean("practice_mode"));
			creatorID = rs.getInt("creator_id");
			category = rs.getString("category");
			description = rs.getString("description");
		}
		
		
		this.creator =getCreatorFromID(creatorID);
		
		// get history of quiz
		PreparedStatement historyQuery = con.prepareStatement("select * from history where quiz_id = ?");
		historyQuery.setInt(1, id);
		ResultSet set = historyQuery.executeQuery();
		
		while(set.next()) {
			QuizAttempts qa = new QuizAttempts(set.getInt("user_id"), set.getInt("score"), set.getDate("date"), set.getInt("time_took"));
			history.add(qa);
		}
		
		// get questions from table
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
					MultipleChoiceMultipleAnswer mcma = new MultipleChoiceMultipleAnswer(questionID, con );
					questions.add(mcma);
					break;
					
				case 7:
					Matching m = new Matching(questionID, con);
					questions.add(m);
					break;
			}
		}
		
		currentQuestionInteger = -1;
		
		con.close();
	}

	// for scoring the entire quiz in one shot
	public double score(ArrayList<ArrayList<String>> answer){
		double score = 0;
		
		for(int i = 0; i < questions.size(); i++) {
			//TODO fix this to get user answer for each question.
			score += scoreQuestion(i,answer.get(i));
		}
		
		return score;
	}
	
	// good idea to do this in case we want to score a quiz question by question
	public double scoreQuestion(int questionNumberInThisQuiz, ArrayList<String> answer){
		double score = 0;
		
		score += questions.get(questionNumberInThisQuiz).solve(answer);
		return score;
	}
	
	
	
	
	public void generate(ArrayList<Question> q, boolean random, boolean onePage, 
			boolean immediateCorrect, boolean practice, int userID, String description, 
			ArrayList<QuizAttempts> history, String category){
		for(Question quest : q) {
			questions.add(quest);
		}
		
		this.setRandom(random);
		
		setOnePageMultiPage(onePage);
		
		setImmediateCorrection(immediateCorrect);
		
		setPracticeMode(practice);
		
		this.creator =getCreatorFromID(userID);
		
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

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean random) {
		this.random = random;
	}

	public boolean isOnePageMultiPage() {
		return onePageMultiPage;
	}

	public void setOnePageMultiPage(boolean onePageMultiPage) {
		this.onePageMultiPage = onePageMultiPage;
	}

	public boolean isImmediateCorrection() {
		return immediateCorrection;
	}

	public void setImmediateCorrection(boolean immediateCorrection) {
		this.immediateCorrection = immediateCorrection;
	}

	public boolean isPracticeMode() {
		return practiceMode;
	}

	public void setPracticeMode(boolean practiceMode) {
		this.practiceMode = practiceMode;
	}

	public Account getCreator() {
		return creator;
	}

	public void setCreator(Account creator) {
		this.creator = creator;
	}
	

	public int getQuiz_id() {
		return quiz_id;
	}

	public void setQuiz_id(int quiz_id) {
		this.quiz_id = quiz_id;
	}
}
