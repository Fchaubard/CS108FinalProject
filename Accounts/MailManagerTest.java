package Accounts;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class MailManagerTest {

	private MailManager mm;
	private Message mail;
	
	//message usage:
	//*Create message any way you like
	//*Use list in/outbox to get (message_id, message abstract) pairings for all messages for a user
	//*use receiveMessage on id found above to get message contents.
	
	@Before
	public void setUp(){
		mm = new MailManager();	
		HashMap<Integer, Message> ib = mm.listInbox("Sally");
		for (Integer i : ib.keySet()) {
			System.out.println(i + " " + ib.get(i).toString());
		}
		mail = mm.recieveMessage(1);
		System.out.println(mail.toString());
		//mail = new Message("Joe", "Sally", "Big Test", "This isa  test of a significantly longer string. I do hope this string does not break. If this string were to break, that would reflect quite poorly on the integrity of this mail widget.", 0);
		//mm.sendMessage(mail);
	}
	
	@Test
	public void testBasic() {
		
	}
	
}
