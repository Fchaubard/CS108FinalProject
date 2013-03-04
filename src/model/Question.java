package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


public interface Question {
	
	public void generate(int id, Connection con);
	
	public int solve(ArrayList<String> answer);
	
	public int getqID();
	
	public int getType();
	
	public String toHTMLString();
	
	public String getCorrectAnswers();
	
	public void pushToDB(Connection con) throws SQLException;
}
