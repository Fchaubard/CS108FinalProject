package model;

import java.sql.Connection;
import java.util.ArrayList;


public interface Question {

	public static final int type=0;
	
	public void generate(int id, Connection con);
	
	public int solve(ArrayList<String> answer);
	
	public int getqID();
	
	public String toHTMLString();
	
	
}
