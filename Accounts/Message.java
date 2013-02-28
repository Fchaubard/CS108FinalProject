package Accounts;

public class Message {

	private String sender;
	private String recipient;
	private String subject;
	private String body;
	private long timestamp;
	
	public Message(String sender, String recipient, String subject, String body) {
		this.sender = sender;
		this.recipient = recipient;
		this.subject = subject;
		this.body = body;
		this.timestamp = System.currentTimeMillis();
	}
		
	public String getSender() {
		return sender;
	}
	
	public String getRecipient() {
		return recipient;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getBody() {
		return body;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
}
