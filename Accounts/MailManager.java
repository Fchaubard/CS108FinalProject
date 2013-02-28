package Accounts;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

public class MailManager {
	
	private static Connection con;
	
	public MailManager() {
		con = MyDB.getConnection();
	}
	
	public void sendMessage(Message mail) {
		Statement stmt;
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO message VALUES (\"");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Message recieveMessage() {
		return null;
		
	}
	
	public ArrayList<Message> listInbox(String recipient) {
		return null;
		
	}
	
	public ArrayList<Message> listOutbox(String sender) {
		return null;
		
	}
}
