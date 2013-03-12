package Accounts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Accounts.Account;


public class AccountManager {
	
	private Connection con;
	
	public Connection getCon() {
		return con;
	}

	public AccountManager(Connection con) {
		this.con = con;
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
			//	stmt.executeUpdate("UPDATE user SET online = 1 WHERE username = \"" + name +"\"");
				return acct;	
			}
			else return null;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public Account getAccount(int id) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE user_id = "+ id +";");
			if (rs.next()) {
				Account acct =  new Account(rs);
			//	stmt.executeUpdate("UPDATE user SET online = 1 WHERE username = \"" + name +"\"");
				return acct;	
			}
			else return null;
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	public void setRank(Account acct, boolean rank) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			stmt.executeUpdate("update user set admin = "+rank+" WHERE username = \"" + acct.getName() + "\";");
			acct.setAdmin(rank);
		} catch (SQLException e) {
		}
	}
	
	public void banUser(Account acct, boolean banStatus) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			stmt.executeUpdate("update user set banned = "+banStatus+" WHERE username = \"" + acct.getName() + "\";");
			acct.setBan(banStatus);
		} catch (SQLException e) {
		}
	}
	
	public void makeFriend(int sender, int friend) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pending_friends WHERE pending_user_id = " + sender +" AND accepted_user_id = "+ friend +";");
			if (rs.next()) { //someone has requested to be your friend, make friends
				stmt.executeUpdate("INSERT INTO friends_mapping VALUES("+sender+", "+friend+");");
				stmt.executeUpdate("INSERT INTO friends_mapping VALUES("+friend+", "+sender+");");
				stmt.executeUpdate("DELETE FROM pending_friends where pending_user_id = ("+sender+") AND accepted_user_id = "+ friend +";");
			} else { //send request
				System.out.println("boop");
				rs = stmt.executeQuery("SELECT * FROM pending_friends WHERE accepted_user_id = " + sender +" AND pending_user_id = "+ friend +"");
				if (rs.next()) return; //already sent request
				stmt.executeUpdate("INSERT INTO pending_friends VALUES("+sender+", "+friend+");");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isFriend(int user, int friend) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pending_friends WHERE pending_user_id = " + friend +" AND accepted_user_id = "+ user +";");
			if (rs.next()) return true;
			rs = stmt.executeQuery("SELECT * FROM friends_mapping WHERE first_user_id = " + user +" AND second_user_id = "+ friend +"");
			if (rs.next()) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void deleteFriend(int lousyFriend, int foreverAlone) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM pending_friends WHERE pending_user_id = " + lousyFriend +" AND accepted_user_id = " + foreverAlone +";");
			if (rs.next()) { //someone has requested to be your friend, say no
				stmt.executeUpdate("DELETE FROM pending_friends where pending_user_id = ("+lousyFriend+") AND accepted_user_id = " + foreverAlone +";");
			} else { //burn bridge
				stmt.executeUpdate("delete from friends_mapping where first_user_id = "+lousyFriend+" AND second_user_id = " + foreverAlone +";");
				stmt.executeUpdate("delete from friends_mapping where second_user_id = "+lousyFriend+" AND first_user_id = " + foreverAlone +";");
			}
		} catch (SQLException e) {
		}
	}
	
	public ArrayList<Account> getFriendRequests(int id) {
		ResultSet rs;
		Statement stmt;
		ArrayList<Account> friendsList = null;
		try {
			friendsList = new ArrayList<Account>();
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery("select * from pending_friends where pending_user_id = "+id+";");
			while (rs.next()) {
				Statement stmt2 = (Statement) con.createStatement();
				ResultSet rs2 = stmt2.executeQuery("select * from user where user_id = "+rs.getInt("accepted_user_id")+";");
				rs2.next();
				Account acct = new Account(rs2);
				friendsList.add(acct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friendsList;
	}
	
	public ArrayList<Account> getFriends(int id) {
		ResultSet rs;
		Statement stmt;
		ArrayList<Account> friendsList = null;
		try {
			friendsList = new ArrayList<Account>();
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery("select * from friends_mapping where first_user_id = "+id+";");
			while (rs.next()) {
				Statement stmt2 = (Statement) con.createStatement();
				ResultSet rs2 = stmt2.executeQuery("select * from user where user_id = "+rs.getInt("second_user_id")+";");
				rs2.next();
				Account acct = new Account(rs2);
				friendsList.add(acct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return friendsList;
	}
	
	public void addQuizResult(int userID, int quizID, int score, java.sql.Date date, int time) {
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			stmt.executeUpdate("INSERT INTO history VALUES ("+userID+", "+quizID+", "+score+", NOW(), "+time+");");
		} catch (SQLException e) {
		}
	}
	
	//pass either arguement as zero or negative to ignore
	public ArrayList<model.QuizAttempts> getHistory(int userid, int quizid) {
		ResultSet rs;
		Statement stmt;
		ArrayList<model.QuizAttempts> history = new ArrayList<model.QuizAttempts>();
		try {
			//history = new ArrayList<model.QuizAttempts>();
			stmt = (Statement) con.createStatement();
			StringBuilder sb = new StringBuilder();
			sb.append("select * from history where ");
			if (userid > 0)  sb.append("user_id = "+userid);
			if (userid > 0 && quizid > 0) sb.append(" AND ");
			if (quizid > 0) sb.append("quiz_id = "+quizid);
			sb.append(" order by score DESC, time_took ASC;");
					
			rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				model.QuizAttempts qa = new model.QuizAttempts(rs.getInt("user_id"), rs.getInt("quiz_id"), rs.getInt("score"), rs.getDate("date"), rs.getInt("time_took"));
				history.add(qa);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return history;
	}
	
	public int quizesAuthored(Account acct) {
		ResultSet rs;
		Statement stmt;
		int history = 0;
		try {
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery("select COUNT(*) from quiz where creator_id ="+acct.getId()+"");
			rs.next();
			history = rs.getInt("count(*)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return history;
	}
	
	public int quizesTaken(Account acct) {
		ResultSet rs;
		Statement stmt;
		int history = 0;
		try {
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery("select COUNT(*) from history where user_id ="+acct.getId()+"");
			rs.next();
			history = rs.getInt("count(*)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return history;
	}
	
	public void updateAchievements (Account acct, int quizesDone) {
		/*System.out.println(quizesDone);
		if (quizesDone > 0) acct.giveAcheivement("amateure");
		if (quizesDone > 5) acct.giveAcheivement("prolific");
		if (quizesDone > 10) acct.giveAcheivement("prodigious");
		if (quizesDone > 50) acct.giveAcheivement("greatest");
		if (quizesDone > 100) acct.giveAcheivement("quiz_machine");*/
		Statement stmt;
		try {
			stmt = (Statement) con.createStatement();
			if (quizesDone >= 1) stmt.executeUpdate("update user set amateure = true where user_id = " + acct.getId());
			if (quizesDone >= 5) stmt.executeUpdate("update user set prolific = true where user_id = " + acct.getId());
			if (quizesDone >= 10) stmt.executeUpdate("update user set prodigious = true where user_id = " + acct.getId());
			if (quizesDone >= 50) stmt.executeUpdate("update user set greatest = true where user_id = " + acct.getId());
			if (quizesDone >= 100) stmt.executeUpdate("update user set quiz_machine = true where user_id = " + acct.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
