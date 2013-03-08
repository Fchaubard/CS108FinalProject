package helpers;

import java.util.*;


public class HTMLHelper {
	
	private HTMLHelper(){
		
	}
	
	public static String printHeader(){
		return "<div class=header><ul class=header><li class=header style=\"float:left\"><a class=header href=\"HomePage\">Moses-Foe Quiz Project</a></li><li class=header><a class=header href=\"QuizCatalogServlet\">Take a Quiz!</a></li><li class=header><a class=header href=\"http://en.wikipedia.org/wiki/Special:Random\">Fun Fact</a></li></ul></div><div style=\"min-height:40px\"></div>";
	}
	
	public static String printCSSLink(){
		return "<link rel=\"stylesheet\" type=\"text/css\" href=\"mufasa.css\">";
	}
	
}
