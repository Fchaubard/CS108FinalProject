package accounts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

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
