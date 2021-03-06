package authentifizierer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Beinhaltet die Logik
 * @author SchatteB
 *
 */
public class Authentifizierer {
	private static Connection connection;
	private static final String TABLENAME = "T_USERS";
	
	static{
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			connection = DriverManager.getConnection("jdbc:oracle:thin:@advora11g:1521:oracle11g", "schatteb", "SchatteB");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Authentifizierer(){
		super();
	}
	
	public void authentifizieren(){
		boolean anmeldungGescheitert = true;
		boolean isDBConnected = isConnected();
		int anzahlFehlversuche = 0;
		
		while(isDBConnected && anmeldungGescheitert && anzahlFehlversuche < 3){
			String id = this.enterUserid();
			String password = this.enterPassword();
			User user = this.searchUser(id);
			if(user == null){
				anmeldungGescheitert = true;
				anzahlFehlversuche++;
				System.out.println("Anmeldung gescheitert! User existiert nicht. " + anzahlFehlversuche + ". Versuch");
			} else if(user.getPassword().equals(password)){
				anmeldungGescheitert = false;
				System.out.println("Anmeldung erfolgreich!");
			} else{
				anmeldungGescheitert = true;
				anzahlFehlversuche++;
				System.out.println("Anmeldung gescheitert! Falsches Passwort. " + anzahlFehlversuche + ". Versuch");
			}
			if(anzahlFehlversuche >= 3){
				System.out.println("3. Fehlversuch. System beendet sich.");
			}
		}
	}
	
	/**
	 * fordert Benutzer zur Eingabe der Userid auf
	 * @return Eingabe des Users
	 */
	public String enterUserid(){
		System.out.println("Enter Userid: ");
		return ConsolReader.readString();
	}
	
	/**
	 * fordert Benutzer zur Eingabe des Passworts auf
	 * @return Eingabe des Users
	 */
	public String enterPassword(){
		System.out.println("Enter Password: ");
		return ConsolReader.readString();
	}
	
	/**
	 * sucht User in der Datenbank
	 * @param id
	 * @return gefundener User bzw. {@code null} wenn nicht gefunden
	 */
	public User searchUser(String id){
		User user = null;
		try {
			String sqlSearchUser = "SELECT id, password "
					+ "FROM " + TABLENAME + " "
					+ "WHERE id=" + id + "";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sqlSearchUser);
			user = createUserObject(result);
			result.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	private static User createUserObject(ResultSet result) throws SQLException{
		if(result.next()){
			String id = result.getString(1);
			String password = result.getString(2);
			User user = new User(id, password);
			return user;
		}
		return null;
	}
	
	private static boolean isConnected(){
		try {
			return connection.isValid(100);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
