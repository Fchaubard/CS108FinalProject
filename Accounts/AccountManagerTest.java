package Accounts;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AccountManagerTest {
	private AccountManager am;
	private Account acct1;
	
	@Before
	public void setUp(){
		am = new AccountManager();
		am.createAccount("Menin", "Pinguin");
		acct1 = am.loginAccount("Menin", "Pinguin");
		
	}
	
	@Test
	public void testBasic() {
		am.createAccount("Don","Emerejildo");
		assertTrue(am.accountExists("Don"));
		Account acct2 = am.loginAccount("Don", "Emerejildo");
		assertEquals("Don", acct2.getName());
		am.logoutAccount(acct2);
		acct2 = null;
		am.deleteAccount("Don");
		assertFalse(am.accountExists("Don"));
	}
	
	@Test
	public void testMultiple(){
		Account acct2 = am.loginAccount("Menin", "Pinguin");
		assertTrue(acct2 == null);
		acct2 = am.createAccount("Menin", "NiMades");
		assertTrue(acct2 == null);
	}
	
	@Test
	public void testDataPersistence(){
		acct1.giveAcheivement("greatest");
		assertTrue(acct1.getAcheivement("greatest")); //NOTE: Acheivement is misspelled... correct way: achievement
		am.logoutAccount(acct1);
		acct1 = null;
		acct1 = am.loginAccount("Menin", "Pinguin");
		assertTrue(acct1.getAcheivement("greatest"));
	}

}
