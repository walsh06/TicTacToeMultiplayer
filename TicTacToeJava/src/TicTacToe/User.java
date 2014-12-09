/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */
package TicTacToe;

public class User {

	private String name, password;
	private int id;
	
	/**Constructor for User taking in userID, their name and password */
	public User(int id, String name, String password)
	{
		this.id = id;
		this.name = name;
		this.password = password;
	}
	
	/**Constructor for User taking in an instance of user*/
	public User(User user)
	{
		name = user.getName();
		password = user.getPassword();
		id = user.getID();
	}
	
	//Gets and sets
	public int getID()
	{
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
