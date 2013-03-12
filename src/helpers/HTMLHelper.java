package helpers;

import java.util.*;

import Accounts.Account;


public class HTMLHelper {
	private static final String HOME_LINK = "<li class=header style=\"float:left\"><a class=header href=\"HomePage\">Moses-Foe Quiz Project</a></li>";
	private static final String TAKE_QUIZ_LINK = "<li class=header><a class=header href=\"QuizCatalogServlet\">Take a Quiz!</a></li>";
	private static final String CREATE_LINK = "<li class=header><a class=header href=\"BeginQuizCreationServlet\">Create Quiz</a></li>";
	private static final String STATS_LINK = "<li class=header><a class=header href=\"SiteStatsServlet\">Site Stats</a></li>";
	private static final String FUN_FACT = "<li class=header><a class=header href=\"http://en.wikipedia.org/wiki/Special:Random\">Fun Fact</a></li>";
	private static final String LOGOUT = "<li class=header><form action=\"AcctManagementServlet\" method=\"post\"><input type=\"hidden\" name =\"Action\" value=\"Logout\"><input style=\"float:right\" type=\"submit\" value=\"Logout\"></form></li>";
	private static final String QUIZ_ICON = "<img class=quiz src=\"http://upload.wikimedia.org/wikipedia/commons/1/13/Blue_square_Q.PNG\">";
	private static final String DEFAULT_PROFILE_PIC = "http://www.iconsdb.com/icons/download/caribbean-blue/user-256.gif";
	private static final String BLUE_STAR_ICON = "http://www.gettyicons.com/free-icons/136/stars/png/256/star_blue_256.png";

	private HTMLHelper(){
	
	}
	
	public static String printHeader(Account loggedDude){
		if (loggedDude != null){
			return printFullHeader(loggedDude.isAdmin());
		}
		else{
			return printBasicHeader();
		}
	}
	
	public static String printCSSLink(){
		return "<link rel=\"stylesheet\" type=\"text/css\" href=\"mufasa.css\">";
	}
	
	public static String contentStart(){
		return "<div class=content>";
	}
	
	public static String contentEnd(){
		return "</div>";
	}
	
	public static String printFullHeader(boolean isAdmin){
		StringBuilder fullHeader = new StringBuilder();
		fullHeader.append("<div class=header><ul class=header>");
		fullHeader.append(HOME_LINK);
		fullHeader.append(LOGOUT);
		fullHeader.append(FUN_FACT);
		fullHeader.append(TAKE_QUIZ_LINK);
		fullHeader.append(CREATE_LINK);
		if (isAdmin) fullHeader.append(STATS_LINK);
		fullHeader.append("</ul></div><div style=\"min-height:80px\"></div>");
		
		return fullHeader.toString();
	}
	
	public static String printBasicHeader(){
		StringBuilder fullHeader = new StringBuilder();
		fullHeader.append("<div class=header><ul class=header>");
		fullHeader.append(HOME_LINK);
		fullHeader.append(FUN_FACT);
		fullHeader.append("</ul></div><div style=\"min-height:80px\"></div>");
		return fullHeader.toString();
	}
	
	public static String printQuizListing(int id, String name, String cat, Account author){
		StringBuilder listing = new StringBuilder();
		listing.append("<div class=quiz>");
		listing.append(QUIZ_ICON);
		listing.append("<ul class=boxlisting>");
		listing.append("<li class=quiz><b>"+name+"</b></li>");
		listing.append("<li class=quiz>Category: " + cat );
		listing.append("<b><a class=quiz href= \"QuizTitleServlet?id="+id+"\">Take Quiz</a></b></li>");
		listing.append("<li class=quiz>Creator:<a href =\"ProfileServlet?user="+author.getName()+"\">"+author.getName()+"</a></li>");
		listing.append("</div>");
		
		return listing.toString();
	}
	
	public static String printTitle(String img, String name){
		if (img.isEmpty()) img = DEFAULT_PROFILE_PIC;
		StringBuilder titleBox = new StringBuilder();
		titleBox.append("<div class=quiz>");
		titleBox.append("<img class=quiz src=\""+img+"\">");
		titleBox.append("<h1 class=title>"+name+"</h1>");
		titleBox.append("</div>");
		return titleBox.toString();
	}
	
	public static String printActionList(String img, String title, ArrayList<String> actions){
		if (img.isEmpty()) img = BLUE_STAR_ICON;
		StringBuilder actionBox = new StringBuilder();
		actionBox.append("<div class=quiz>");
		actionBox.append("<h2 class=title>");
		actionBox.append("<img class=quiz style=\"height:30px\" style src=\""+img+"\">");
		actionBox.append(title+"</h2>");
		actionBox.append("<ul style=\"list-style-type:none\">");
		for (String action : actions){
			actionBox.append("<li class=quiz>");
			actionBox.append("<img class=quiz style=\"height:20px\" style src=\""+img+"\">");
			actionBox.append(action);
			actionBox.append("</li>");
		}
		actionBox.append("</ul></div>");
		
		return actionBox.toString();
	}

	
	
	public static String printUserListing(String name){
		StringBuilder listing = new StringBuilder();
		listing.append("<div class=quiz>");
		listing.append("<h3>"+name);
		listing.append("<a class=quiz href= \"ProfileServlet?user="+name+"\">Profile</a>");
		listing.append("</h3></div>");
		
		return listing.toString();
	}
}
