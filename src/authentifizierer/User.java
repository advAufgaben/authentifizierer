package authentifizierer;

/**
 * repräsentiert einen Datensatz der User-Datenbank
 * @author SchatteB
 *
 */
public class User {
	
	/**
	 * Userid
	 */
	private String id;
	/**
	 * Passwort
	 */
	private String password;
	
	/**
	 * @param id
	 * @param password
	 */
	public User(String id, String password) {
		super();
		this.setId(id);
		this.setPassword(password);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
