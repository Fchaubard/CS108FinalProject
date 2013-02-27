package model;

import java.util.Set;

public interface Question {

	public static final int type=0;
	
	public void generate();
	
	public int solve(Set<String> answer);
	
	public String toHTMLString();
	
	
}
