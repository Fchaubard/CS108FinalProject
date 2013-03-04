package Servlets;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.QuizManager;

import com.mysql.jdbc.Connection;

import Accounts.AccountManager;
import Accounts.MailManager;



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
    	Connection con = (Connection) MyDB.getConnection();
    	sc.setAttribute("connect", con);
    	AccountManager am = new AccountManager(con);
    	sc.setAttribute("accounts", am);
    	MailManager mm = new MailManager(con);
    	sc.setAttribute("mail", mm);
    	QuizManager qm = new QuizManager(con);
    	sc.setAttribute("quizes", qm);
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
