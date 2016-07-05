package resources;

/**
 * Users Class: 
 * 
 * This class represents the 3 types of users of the Project Management application (ADMINS, MANAGERS, MEMBERS).
 * Currently only MANAGERS are supported.
 * Attributes include name(username), first and last names, password, id and an enum type for userType.
 * 
 * @author daveT
 *
 */


public class Users {

	String name;
	String firstName;
	String lastName;
	String password;
	int id;
	userType type;	
	
	enum userType {	
		ADMIN, MANAGER, MEMBER
	}
	
	/**
	 * Default Constructor
	 * Sets all parameters to null or junk values.
	 */
	public Users(){
		this.name = null;
		this.password = null;
		this.id = -1;
		this.type = null;
		this.firstName = null;
		this.lastName = null;
	}
	
	/**
	 * Parameterized Constructor.
	 * Accepts a String for userType, and uses a switch statement to set the appropriate type.
	 * This is because userType is stored as a String in the database.
	 * 
	 * @param userName value for name
	 * @param firstName value for firstName
	 * @param lastName value for lastName
	 * @param password value for password
	 * @param iD value for id
	 * @param usertype string value used to set userType using switch statement
	 */
	public Users(String userName, String firstName, String lastName, String password, int iD, String usertype){
		this.name = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.id = iD;
		switch (usertype)
		{
		case "MANAGER":
		{
			this.type = userType.MANAGER;
			break;
		}
		case "ADMIN":
		{
			this.type = userType.ADMIN;
			break;
		}
		case "MEMBER":
		{
			this.type = userType.MEMBER;
			break;
		}
		default:
			this.type = userType.MEMBER;
			break;
		}		
	}
	
	/**
	 * Getter for name
	 * @return String name
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Setter for name
	 * @param userName value for name
	 */
	public void setName(String userName){
		this.name = userName;
	}
	
	/**
	 * Getter for password
	 * @return String password
	 */
	public String getPassword(){
		return this.password;
	}
	
	/**
	 * Setter for password
	 * @param newPassword value for password
	 */
	public void setPassword(String newPassword){
		this.password = newPassword;
	}
	
	/**
	 * Getter for id
	 * @return int id
	 */
	public int getID(){
		return this.id;
	}
	
	/**
	 * Setter for id
	 * @param newID value for id
	 */
	public void setID(int newID) {
		this.id = newID;
	}
	
	/**
	 * Getter for firstName
	 * @return String firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setter for firstName
	 * @param firstName value for firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Getter for lastName
	 * @return String lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setter for lastName
	 * @param lastName value for lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Getter for type
	 * Note: Returns an enum type
	 * @return userType type
	 */
	public userType getType() {
		return type;
	}

	/**
	 * Setter for type
	 * Note: Must pass an enum of type userType
	 * @param type value for type
	 */
	public void setType(userType type) {
		this.type = type;
	}

	/**
	 * For future functionality, will draw a specific type of menu based on userType
	 * For now this method is not functional
	 */
	public void drawMenu(){
		
		switch(this.type){
		
		case ADMIN: ;
		
		case MANAGER: ;
		
		case MEMBER: ;
		
		}
	}
}