package Accounts;

public class Message {

	private String sender;
	private String recipient;
	private String subject;
	private String body;
	private long timestamp;
	private int challengeID;
	private String challengeName;
	public boolean unread;
	
	public Message(String sender, String recipient, String subject, String body, long timestamp, int challengeID, String challengeName, boolean unread) {
		this.sender = sender;
		this.recipient = recipient;
		this.subject = subject;
		this.body = body;
		this.timestamp = (timestamp > 0) ? timestamp : System.currentTimeMillis();
		this.challengeID  =challengeID;
		this.challengeName = challengeName;
		this.unread = unread;
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
	
	public int getChallengeID() {
		return challengeID;
	}
	
	public String getChallengeName() {
		return challengeName;
	}
	
	@Override
	public String toString() {
		return "Message [sender=" + sender + ", recipient=" + recipient
				+ ", Subject=" + subject + ", timestamp=" + new java.util.Date(timestamp) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result
				+ ((recipient == null) ? 0 : recipient.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (recipient == null) {
			if (other.recipient != null)
				return false;
		} else if (!recipient.equals(other.recipient))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}
	
}
