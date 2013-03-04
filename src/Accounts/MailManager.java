package Accounts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import Servlets.MyDB;

import com.mysql.jdbc.Statement;

public class MailManager {
	
	private static Connection con;
	
	public MailManager(Connection con) {
		this.con = con;//Servlets.MyDB.getConnection();
	}
	
	public void sendMessage(Message mail) {
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
		sb.append("\");");
		try {
			stmt = (Statement) con.createStatement();
			stmt.executeUpdate(sb.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Message recieveMessage(int id) {
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
				m = new Message(sender, recipient, subject, body, time.getTime());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	public HashMap<Integer, Message> listInbox(String recipient) {
		ResultSet rs;
		Statement stmt;
		HashMap<Integer,Message> inbox = null;
		try {
			inbox = new HashMap<Integer,Message>();
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery("select message_id, sender, subject, date from message where recipient = \"" + recipient + "\"");
			while (rs.next()) {
				Integer key = rs.getInt("message_ID");
				String sender = rs.getString("sender");
				String subject = rs.getString("subject");
				Date time = rs.getTimestamp("date");
				inbox.put(key, new Message(sender, recipient, subject, null, time.getTime()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return inbox;
		
	}
	
	public HashMap<Integer, Message> listOutbox(String sender) {
		ResultSet rs;
		Statement stmt;
		HashMap<Integer,Message> outbox = null;
		try {
			outbox = new HashMap<Integer,Message>();
			stmt = (Statement) con.createStatement();
			rs = stmt.executeQuery("select message_id, recipient, subject, date from message where sender = \"" + sender + "\"");
			while (rs.next()) {
				Integer key = rs.getInt("message_ID");
				String recipient = rs.getString("recipient");
				String subject = rs.getString("subject");
				Date time = rs.getTimestamp("date");
				outbox.put(key, new Message(sender, recipient, subject, null, time.getTime()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return outbox;	
	}
}
