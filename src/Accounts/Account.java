package Accounts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

public class Account {
	private int id;
	private String name;
	
	private boolean practiceMode;
	private boolean admin;
	private boolean banned;
	private boolean online;
	private boolean privacy;
	
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
			
			practiceMode = rs.getBoolean("practice");
			online = rs.getBoolean("online");
			admin = rs.getBoolean("admin");
			banned = rs.getBoolean("banned");
			privacy = rs.getBoolean("private");
			
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
	
	
	public void setMode(boolean mode) {
		this.practiceMode = mode;
	}
	
	public boolean getMode() {
		return practiceMode;
	}
	
	public void setAdmin(boolean rank) {
		this.admin = rank;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	public void setBan(boolean ban) {
		this.banned = ban;
	}
	
	public boolean isBanned() {
		return banned;
	}
	
	public void setPrivacy(boolean setting) {
		this.privacy = setting;
	}
	
	public boolean isPrivate() {
		return privacy;
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
