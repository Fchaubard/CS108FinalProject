package Accounts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import Servlets.MyDB;

import com.mysql.jdbc.Statement;

public class MailManager {
	
	private static Connection con;
	
	public MailManager(Connection con) {
		this.con = con;//Servlets.MyDB.getConnection();
	}
	
	synchronized public boolean sendMessage(Message mail) {
		Statement stmt;
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO message VALUES (default, \"");
		sb.append(mail.getSender());
		sb.append("\", \"");
		sb.append(mail.getRecipient());
		sb.append("\", \"");
		sb.append(mail.getSubject());
		sb.append("\", \"");
		sb.append(mail.getBody());
		sb.append("\", \"");
		sb.append(new Timestamp(mail.getTimestamp()));
		sb.append("\", ");
		if (mail.getChallengeID() > 0) {	
			sb.append(mail.getChallengeID());
		} else {
			sb.append("default");
		}
		sb.append(", default);");
		try {
			System.out.println(sb.toString());
			stmt = (Statement) con.createStatement();
			stmt.executeUpdate(sb.toString());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	synchronized public boolean sendChallenge(Account sender, int qid, String qname) {
		try {
			String body = "<a href = \"ProfileServlet?user="+sender.getName()+"\">"+sender.getName()+"</a> has challenged you to" +
			": <a href = \"QuizTitleServlet?id="+qid+"\">"+qname+"</a>!\n";
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from history where user_id = "+sender.getId()+" and quiz_id = "+qid+" order by score desc, time_took asc");
			if (rs.next()) {
				body += "Can you beat their top score of "+rs.getInt("score")+", completed in "+rs.getInt("time_took")/1000+" seconds?";
			}
			//Message m = new Message()
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	synchronized public boolean sendFriend(Account sender, Account friend) {
			String body = "<a href = \\\"ProfileServlet?user="+sender.getName()+"\\\">"+sender.getName()+"</a> wants to be your friend." +
			" click <a href = \\\"FriendManagementServlet\\\">here</a> to view their request.";
		return sendMessage(new Message(sender.getName(), friend.getName(), "Friends?", body, 0, 0, null, true));
	}
	
	synchronized public Message recieveMessage(int id) {
		ResultSet rs;
		Statement stmt;
		Message m = null;
		try {
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery("select * from message where message_id = " + id + ";");
			if(rs.next()) {
				String sender = rs.getString("sender");
				String recipient = rs.getString("recipient");
				String subject = rs.getString("subject");
				String body = rs.getString("message");
				Date time = rs.getTimestamp("date");
				int challengeID = rs.getInt("quiz_id");
				boolean unread = rs.getBoolean("unread");
				String challengeName = null;
				if (challengeID > 0) {
					rs = stmt.executeQuery("select name from quiz where quiz_id = " + challengeID + ";");
					if (rs.next()) challengeName = rs.getString("name");
				}
				m = new Message(sender, recipient, subject, body, time.getTime(), challengeID, challengeName, unread);
				stmt.executeUpdate("update message set unread = false where message_id = "+id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	synchronized public int getUnread(Account acct) {
		int newMessages = 0;
		Statement stmt;
		ResultSet rs;
		try {
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery("select count(*) from message where recipient = \"" + acct.getName() + "\" and unread = true;");
			if(rs.next()) newMessages = rs.getInt("count(*)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newMessages;
		
	}
	
	synchronized public TreeMap<Integer, Message> listInbox(String recipient) {
		ResultSet rs;
		Statement stmt;
		TreeMap<Integer,Message> inbox = null;
		try {
			inbox = new TreeMap<Integer,Message>();
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery("select message_id, sender, subject, date, unread from message where recipient = \"" + recipient + "\" order by date");
			while (rs.next()) {
				Integer key = rs.getInt("message_ID");
				String sender = rs.getString("sender");
				String subject = rs.getString("subject");
				Date time = rs.getTimestamp("date");
				boolean unread = rs.getBoolean("unread");
				inbox.put(key, new Message(sender, recipient, subject, null, time.getTime(), 0, null, unread));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inbox;
		
	}
	
	synchronized public TreeMap<Integer, Message> listOutbox(String sender) {
		ResultSet rs;
		Statement stmt;
		TreeMap<Integer,Message> outbox = null;
		try {
			outbox = new TreeMap<Integer,Message>();
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery("select message_id, recipient, subject, date from message where sender = \"" + sender + "\" order by date");
			while (rs.next()) {
				Integer key = rs.getInt("message_ID");
				String recipient = rs.getString("recipient");
				String subject = rs.getString("subject");
				Date time = rs.getTimestamp("date");
				outbox.put(key, new Message(sender, recipient, subject, null, time.getTime(), 0, null, false));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return outbox;	
	}
}
