package Accounts;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;



/**
 * Application Lifecycle Listener implementation class serverListener
 *
 */
@WebListener
public class serverListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public serverListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	ServletContext sc = arg0.getServletContext();
    	AccountManager am = new AccountManager();
    	sc.setAttribute("accounts", am);
        // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	AccountManager am = (AccountManager) arg0.getServletContext().getAttribute("accounts");
    	am.kill();
    }
	
}
