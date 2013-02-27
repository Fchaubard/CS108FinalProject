package accounts;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class SessionListener
 *
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    /**
     * Default constructor. 
     */
    public SessionListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0) {
        Account acct = null;
        arg0.getSession().setAttribute("account", acct);
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent arg0) {
    	Account acct = (Account) arg0.getSession().getAttribute("account");
        if (acct != null) {
        	AccountManager am = (AccountManager) arg0.getSession().getServletContext().getAttribute("accounts");
        	am.logoutAccount(acct);
        }
    }
	
}
