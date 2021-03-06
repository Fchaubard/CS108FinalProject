package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Matching implements Question {

	public static final int type = 7;
	private String title;
	private  ArrayList<String> userAns;
	private ArrayList<String>  row1;
	
	private ArrayList<String>  row2;
	private ArrayList<Integer>  shuffleIntegersForRow2; // these numbers start off ordered but then shuffle.

	private int qID;

	public static String getHTMLInputString(){
		// TODO
		StringBuilder html = new StringBuilder();
		
		html.append("<br /><input type=\"text\" name=\"question_row\" maxlength=\"250\" required />");
		html.append("matches with <input type=\"text\" name=\"answer_row\" maxlength=\"250\" required /><br />");
		return html.toString();
	}
	
	
	public static String getTitleHTMLInputString(){
		// TODO
		StringBuilder html = new StringBuilder();
		
		html.append("<br />Title of your matching question: <input type=\"text\" name=\"title\" maxlength=\"250\" required />");
		return html.toString();
	}
	
	public Matching(String title, ArrayList<String> row1,ArrayList<String> row2) { 
		title = title.replace("\"", "");
		this.title = (title.length() < 250) ? title : (title.substring(0, 245) + "...");
		this.row1 = new ArrayList<String>();
		
		for(String s : row1) {
			this.row1.add(s.replace("\"", ""));
		}
		
		this.row2 = new ArrayList<String>();
		
		for(String str : row2) {
			this.row2.add(str.replace("\"", ""));
		}
		
		shuffleIntegersForRow2 = new ArrayList<Integer>();
		for (int i = 0; i < row2.size(); i++) {
			shuffleIntegersForRow2.add(i);
		}
	}

	public Matching(int id, Connection con) { // pulls from database
		generate(id, con);

		System.out.println("before shuffle");
		System.out.println(shuffleIntegersForRow2);
		Collections.shuffle(shuffleIntegersForRow2);

		System.out.println("after shuffle");
		System.out.println(shuffleIntegersForRow2);

	}
	
	
	@Override
	public void generate(int id, Connection con) {
		this.qID = id;
		try {
			
			PreparedStatement ps = con.prepareStatement("select * from matching_question where question_id = ?");
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
			
			

			while (resultSet.next()) {
				this.title  = resultSet.getString("title");
			}
			ps = con.prepareStatement("select * from matching_question_mapping where matching_entry_id = ?");
			ps.setInt(1, id);
			resultSet = ps.executeQuery();

			row1 = new ArrayList<String>();
			row2 = new ArrayList<String>();
			shuffleIntegersForRow2 = new ArrayList<Integer>();
			// populate the hashmap and ordered arraylist
			
			int counter = 0;
			while (resultSet.next()) {
				row1.add( resultSet.getString("row1"));
				row2.add( resultSet.getString("row2"));
				shuffleIntegersForRow2.add(counter);
				counter++;
			}
			
		}catch(Exception e){
			
		}
		
	}



	@Override
	public String toHTMLString() {
		StringBuilder html = new StringBuilder();
		
        html.append(title);
        html.append("<br />");
        
        
        html.append(" <script src=\"http://code.jquery.com/jquery-latest.min.js\"></script>\n");
        html.append(" <script src=\"  http://code.jquery.com/ui/1.10.1/jquery-ui.js\"></script>\n");
       
        html.append("		<script>\n");
    	html.append( "   $(document).ready(function() {   \n");
        html.append("		$(\"#sortable"+qID+"\").sortable({ });\n");
        html.append("		 $('form').submit(function () {\n");
        html.append("		     $('#thedata"+qID+"').val($(\"#sortable"+qID+"\").sortable(\"serialize\"));\n");
        html.append("		 });\n");
		html.append( "      });\n ");
        html.append("		</script>\n");
        //html.append("<!--		<form method=\"POST\" action=\"/vote\">-->\n");
        html.append("		    <input type=\"hidden\" name=\"key\" value=\"{{ election.key }}\">\n");
        html.append("		    <input type=\"hidden\" name=\"uuid\" value=\"{{ uuid }}\">\n");
        html.append("		    <div style=\"float: left; width: 150px;\">\n");
        html.append("		        <ol>\n");
        for (int j = 0; j < row1.size(); j++) {
        html.append("		            <li>"+row1.get(j)+"  </li>\n");
        }
        html.append("		        </ol>\n");
        html.append("		    </div>\n");
        html.append("		    <div id=\"ballot\" style=\"float: left; width: 100px;\">\n");
        html.append("		        <ol id=\"sortable"+qID+"\" class=\"rankings\">\n");
        for (int j = 0; j < row1.size(); j++) {
        html.append("		            <li style=\"background-color: #9999ff\" id='"+type+"_"+qID+"_"+(shuffleIntegersForRow2.get(j))+"' class=\"ranking\"><b>"+row2.get(shuffleIntegersForRow2.get(j))+"</b></li>\n");	
		}
        html.append("		        </ol>\n");
        html.append("		    </div>\n");
        html.append("		    <div>\n");
        html.append("		        <br />\n");
        html.append("		        <input type='hidden' name='thedata"+qID+"' id='thedata"+qID+"'>\n");
        //html.append("<!--		        <button type='submit'>Submit</button>-->\n");
        html.append("		    </div><br /><br /><br /><br />\n");
        //html.append("<!--		</form> -->\n");
        
        html.append("<br />");
        
		return html.toString();
	}

	public int getqID() {
		return qID;
	}

	public void setqID(int qID) {
		this.qID = qID;
	}

	@Override
	public String getCorrectAnswers() {
		StringBuilder correctAnswers = new StringBuilder("<br />");
		
		for (int i = 0; i < row2.size(); i++) {
			
			correctAnswers.append((i+1));
			correctAnswers.append(": ");
			correctAnswers.append(row1.get(i));
			correctAnswers.append(" ");
			correctAnswers.append(row2.get(i));
			correctAnswers.append("<br />\n");
		}
		
		return correctAnswers.toString();
	}
	public int getType(){
		return type;
	}

	@Override
	public void pushToDB(Connection con) {
		// now create a row in the database
		
		try {
			PreparedStatement ps = con.prepareStatement("insert into matching_question values(null, ?)");
			title = title.trim();
			ps.setString(1, title);
			
			System.out.println(ps.toString());
			ps.executeUpdate();
			
			PreparedStatement getID = con.prepareStatement("select * from matching_question where title = ?");
			getID.setString(1, title);
			
			System.out.print(getID.toString());
			ResultSet resultSet = getID.executeQuery();
			
			while (resultSet.next()) {
				this.qID = resultSet.getInt("question_id"); // will always be the last one
			}
			
			for (int i = 0; i < row2.size(); i++) {
				ps = con.prepareStatement("insert into matching_question_mapping values(?, ?, ?)");
				
				ps.setInt(1, qID);
				row1.set(i, row1.get(i).trim());
				ps.setString(2, row1.get(i));
				row2.set(i, row2.get(i).trim());
				ps.setString(3, row2.get(i));
				
				System.out.println(ps.toString());
				ps.executeUpdate();
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		title = title.replace("\"", "");
		this.title = (title.length() < 250) ? title : (title.substring(0, 245) + "...");
	}


	@Override
	public String getUserAnswers() {
		StringBuilder userAnswers = new StringBuilder();
		for (int i = 0; i < row2.size(); i++) {
			userAnswers.append("<br />");
			userAnswers.append(row1.get(i));
			userAnswers.append(" ");
			try {

				userAnswers.append(row2.get(Integer.parseInt(this.userAns.get(i))));
			} catch (Exception e) {
				userAnswers.append(row2.get(i));
				
			}
			userAnswers.append("<br />");
			
		}
		return userAnswers.toString();
	}

	@Override
	public int getTotalQScore() {
		return row2.size();
	}

	@Override
	public int solve(ArrayList<String> ans) { // I get an array of integer strings
		int score =0;
		if (ans.size()!=row2.size()) {
			return 0; // input cleansing
		}
		for (int i = 0; i < row2.size(); i++) {
		
			if (Integer.parseInt(ans.get(i)) == i) {
				score++;
			}
		}
		return score;
	}

	@Override
	public void setUserAnswers(ArrayList<String> ans) {
		// this is an array of integer strings
		this.userAns = new ArrayList<String>();;
		
		for(String s : ans) {
			userAns.add(s.replace("\"", ""));
		}
		
	}
	public void addRow(String question, String answer) {
		// this is an array of integer strings
		this.row1.add(question);
		this.row2.add(answer);
		this.shuffleIntegersForRow2.add(shuffleIntegersForRow2.size());
		
	}
	public ArrayList<String> getRow1() {
		return row1;
	}

	public void setRow1(ArrayList<String> row1) {
		this.row1 = new ArrayList<String>();
		
		for(String s : row1) {
			this.row1.add(s.replace("\"", ""));
		}
	}

	public ArrayList<String> getRow2() {
		return row2;
	}

	public void setRow2(ArrayList<String> row2) {
		this.row2 = new ArrayList<String>();
		
		for(String s : row2) {
			this.row2.add(s.replace("\"", ""));
		}
	}

	public ArrayList<Integer> getShuffleIntegersForRow2() {
		return shuffleIntegersForRow2;
	}

	public void setShuffleIntegersForRow2(ArrayList<Integer> shuffleIntegersForRow2) {
		this.shuffleIntegersForRow2 = shuffleIntegersForRow2;
	}
	
	public ArrayList<String> getUserAns() {
		return userAns;
	}


	public void setUserAns(ArrayList<String> userAns) {
		this.userAns = new ArrayList<String>();
		
		for(String s : userAns) {
			this.userAns.add(s.replace("\"", ""));
		}
	}

	@Override
	public int getNumAnswers() {
		return row1.size();
	}


	@Override
	public String getEditQuizString() {
		StringBuilder edit = new StringBuilder();
		
		edit.append("<br />Matching Question");
		edit.append("<br />Title: <br /><input type=\"text\" name=\"" + type + "_" + qID + "_title\" size=\"75\" value=\"" + title + "\" maxlength=\"250\" required />");
		for (int i = 0; i < row1.size(); i++) {
			edit.append("<br /><input type=\"text\" name=\"" + type + "_" + qID + "_" + i + "_row1\" value=\"" + row1.get(i) + "\" maxlength=\"250\" required />");
			edit.append("<input type=\"text\" name=\"" + type + "_" + qID + "_" + i + "_row2\" value=\"" + row2.get(i) + "\" maxlength=\"250\" required />");
			
		}
		System.out.println(edit.toString());
		return edit.toString();
	}


	@Override
	public void updateDB(Connection con) throws SQLException {
		// TODO Auto-generated method stub
		
		
		PreparedStatement ps = con.prepareStatement("UPDATE matching_question SET title = ? WHERE question_id = ?");
		
		ps.setString(1, title);
		
		ps.setInt(2, qID);
		
		ps.executeUpdate();
		
		ps = con.prepareStatement("DELETE FROM matching_question_mapping WHERE matching_entry_id = ?");
		
		ps.setInt(1, qID);
		
		ps.executeUpdate();
	
		for (int i = 0; i < row1.size(); i++) {
		
				ps = con.prepareStatement("insert into matching_question_mapping values(?, ?, ?)");
				
				ps.setInt(1, qID);
				row1.set(i, row1.get(i).trim());
				ps.setString(2, row1.get(i));
				row2.set(i, row2.get(i).trim());
				ps.setString(3, row2.get(i));
				
				System.out.println(ps.toString());
				ps.executeUpdate();
			
		
		}
		 
		
	}


	@Override
	public void deleteFromDB(Connection con) throws SQLException {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = con.prepareStatement("DELETE FROM matching_question WHERE question_id = ?");
		
		ps.setInt(1, qID);
		
		ps.executeUpdate();
		
		
		ps = con.prepareStatement("DELETE FROM matching_question_mapping WHERE matching_entry_id = ?");
		
		ps.setInt(1, qID);
		
		ps.executeUpdate();
	
		 
		PreparedStatement prep = con.prepareStatement("DELETE FROM quiz_question_mapping WHERE question_id = ? AND question_type = ?");
		prep.setInt(1, qID);
		prep.setInt(2, type);
		prep.executeUpdate();
		/*
		PreparedStatement ps = con.prepareStatement("DELETE FROM multiple_answer_question WHERE question_id = ?");
		ps.setInt(1, qID);
		ps.executeUpdate();
		
		PreparedStatement prep = con.prepareStatement("DELETE FROM quiz_question_mapping WHERE question_id = ? AND question_type = ?");
		prep.setInt(1, qID);
		prep.setInt(2, type);
		prep.executeUpdate();
		*/
	}
}
