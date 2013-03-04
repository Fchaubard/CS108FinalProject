package Accounts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

public class Account {
	private int id;
	private String name;
	
	private boolean practiceMode;
	private boolean online;
	
	private HashMap<String, Boolean> acheivements;
	
	//private boolean acheivement_amatuer;
	//private boolean acheivement_prolific;
	//private boolean acheivement_prodigy;
	//private boolean acheivement_greatest;
	//private boolean acheivement_machine;
	
	public Account(ResultSet rs) {
		try {
			setId(rs.getInt("user_id"));
			name = rs.getString("username");
			System.out.println(name);
			
			practiceMode = rs.getBoolean("practice");
			online = rs.getBoolean("online");
			
			acheivements = new HashMap<String, Boolean>();
			
			acheivements.put("amateure", rs.getBoolean("amateure"));
			acheivements.put("prolific", rs.getBoolean("prolific"));
			acheivements.put("prodigious", rs.getBoolean("prodigious"));
			acheivements.put("greatest", rs.getBoolean("greatest"));
			acheivements.put("quiz_machine", rs.getBoolean("quiz_machine"));
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getMode() {
		return practiceMode;
	}
	
	public boolean getAcheivement(String key) {
		return acheivements.get(key);
		
	}
	
		public Set<String> getAcheivementKeySet() {
				return acheivements.keySet();
		 		
		 	}

	
	public void giveAcheivement(String key) {
		acheivements.put(key, true);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
