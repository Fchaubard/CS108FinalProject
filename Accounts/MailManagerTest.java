package Accounts;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MailManagerTest {

	private MailManager mm;
	private Message mail;
	
	@Before
	public void setUp(){
		mm = new MailManager();	
		mail = new Message("Joe", "Sally", "Big Test", "This isa  test of a significantly longer string. I do hope this string does not break. If this string were to break, that would reflect quite poorly on the integrity of this mail widget.");
		mm.sendMessage(mail);
	}
	
	@Test
	public void testBasic() {
		
	}
	
}
