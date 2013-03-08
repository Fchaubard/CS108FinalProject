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
public class ServerListener implements ServletContextListener {
	//me testing shit
	
	protected MyDB db;
	
    /**
     * Default constructor. 
     */
    public ServerListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	
    	db = new MyDB();
    	
    	ServletContext sc = arg0.getServletContext();
    	Connection con = (Connection) MyDB.getConnection();
    	if (con == null) System.out.println("con failed");
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
    	db.close();
    }
	
}
