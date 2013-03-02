package Accounts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import Servlets.MyDB;

public class AccountManager {
	
	private static Connection con;
	
	public AccountManager() {
		con = MyDB.getConnection();
	}
	
	public boolean accountExists(String name) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE username = \"" + name +"\"");
			if (rs.next()) return true;
			else return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public Account getAccount(String name) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE username = \"" + name +"\"");
			if (rs.next()) {
				Account acct =  new Account(rs);
				stmt.executeUpdate("UPDATE user SET online = 1 WHERE username = \"" + name +"\"");
				return acct;	
			}
			else return null;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public Account loginAccount(String name, String pass) {
		pass = hashString(pass);
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE username = \"" + name +"\"");
			rs.next();
			String password = rs.getString("password");
			if (pass.equals(password))  {
				Account acct =  new Account(rs);
				stmt.executeUpdate("UPDATE user SET online = 1 WHERE username = \"" + name +"\"");
				return acct;
			}
			else return null;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public void logoutAccount(Account acct) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			stmt.executeUpdate("UPDATE user SET amateure = " + acct.getAcheivement("amateure") + " WHERE username = \"" + acct.getName() +"\"");
			stmt.executeUpdate("UPDATE user SET prolific = " + acct.getAcheivement("prolific") + " WHERE username = \"" + acct.getName() +"\"");
			stmt.executeUpdate("UPDATE user SET prodigious = " + acct.getAcheivement("prodigious") + " WHERE username = \"" + acct.getName() +"\"");
			stmt.executeUpdate("UPDATE user SET quiz_machine = " + acct.getAcheivement("quiz_machine") + " WHERE username = \"" + acct.getName() +"\"");
			stmt.executeUpdate("UPDATE user SET greatest = " + acct.getAcheivement("greatest") + " WHERE username = \"" + acct.getName() +"\"");
		
			stmt.executeUpdate("UPDATE user SET online = 0 WHERE username = \"" + acct.getName() +"\"");
		} catch (SQLException e) {
		}
	}
	
	public Account createAccount(String name, String pass) {
		if (accountExists(name)) return null;
		pass = hashString(pass);
			Statement stmt;
			try {
				stmt = (Statement) con.createStatement();
				stmt.executeUpdate("INSERT INTO user (username, password) VALUES (\"" + name + "\", \"" + pass + "\");");
				return getAccount(name);
			} catch (SQLException e) {
				return null;
			}
	}

	public void deleteAccount(String name) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			stmt.executeUpdate("DELETE from user WHERE username = \"" + name + "\";");
		} catch (SQLException e) {
		}
	}
	
	public void makeFriend(int sender, int friend) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pending_friends WHERE pending_user_id = " + sender +";");
			if (rs.next()) { //someone has requested to be your friend, make friends
				stmt.executeUpdate("INSERT INTO friends_mapping VALUES("+sender+", "+friend+");");
				stmt.executeUpdate("INSERT INTO friends_mapping VALUES("+friend+", "+sender+");");
				stmt.executeUpdate("DELETE FROM pending_friends where pending_user_id = ("+sender+");");
			} else { //send request
				rs = stmt.executeQuery("SELECT * FROM pending_friends WHERE accepted_user_id = " + sender +";");
				if (rs.next()) return; //already sent request
				stmt.executeUpdate("INSERT INTO pending_friends VALUES("+sender+", "+friend+");");
			}
		} catch (SQLException e) {
		}
	}
	
	public void deleteFriend(String name) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pending_friends WHERE pending_user_id = " + name +";");
			if (rs.next()) { //someone has requested to be your friend, make friends
				
			} else { //send request
				rs = stmt.executeQuery("SELECT * FROM pending_friends WHERE accepted_user_id = " + name +";");
				if (rs.next()) return; //already sent request
				//stmt.executeUpdate("INSERT INTO pending_friends VALUES("+sender+", "++");");
			}
		} catch (SQLException e) {
		}
	}
	
	public ArrayList<Account> getFriendRequests(String recipient) {
		//table (sender|recipient)
		//return list of sender accounts for parameter recipient
		return null;
	}
	
	public ArrayList<Account> getFriends(String name) {
		return null;
	}
	
	public void addQuizResult(int userID, int quizID, int score, java.sql.Date date, int time) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			stmt.executeUpdate("INSERT INTO history VALUES ("+userID+", "+quizID+", "+score+", NOW(), "+time+");");
		} catch (SQLException e) {
		}
	}
	
	public ArrayList<model.QuizAttempts> getHistory(int id) {
		ResultSet rs;
		Statement stmt;
		ArrayList<model.QuizAttempts> history = null;
		try {
			history = new ArrayList<model.QuizAttempts>();
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery("select * from history where user_id = "+id+";");
			while (rs.next()) {
				model.QuizAttempts qa = new model.QuizAttempts(rs.getInt("user_id"), rs.getInt("quiz_id"), rs.getInt("score"), rs.getDate("date"), rs.getInt("time_took"));
				history.add(qa);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return history;
	}
	
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	public static String hashString(String s) {
		s += "1234"; //world's most secure salt
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(s.getBytes());
			try {
				MessageDigest result = (MessageDigest) md.clone();
				return hexToString(result.digest());
			}catch (Exception e) {
				return null;
			}
		} catch (NoSuchAlgorithmException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void kill() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
