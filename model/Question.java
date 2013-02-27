package model;

<<<<<<< HEAD
import java.util.Set;
=======
import java.sql.Connection;
import java.util.ArrayList;
>>>>>>> All things

public interface Question {

	public static final int type=0;
	
	public void generate(int id, Connection con);
	
<<<<<<< HEAD
	public int solve(Set<String> answer);
=======
	public int solve(ArrayList<String> answer);
>>>>>>> All things
	
	public String toHTMLString();
	
	
}
