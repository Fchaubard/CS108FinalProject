package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;


public interface Question {
	
	public void generate(int id, Connection con);
	
	public int solve(ArrayList<String> answer);
	
	public int getqID();
	
	public int getType();

	public String toHTMLString();
	
	public String getCorrectAnswers();
	
	public void pushToDB(Connection con) throws SQLException;
	
	public void setUserAnswers(ArrayList<String> ans);
	
	public String getUserAnswers();
	
	public int getTotalQScore();
	
	public int getNumAnswers();
	
	public String getEditQuizString();
	
	public void updateDB(Connection con) throws SQLException;
	
	public void deleteFromDB(Connection con) throws SQLException;
}