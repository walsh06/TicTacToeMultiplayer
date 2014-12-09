/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */
package Client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONException;
import org.json.JSONObject;

import URLRequest.*;

public class LoginPanel extends JPanel {

	JTextField name, password;
	//JButton login;
	JLabel nameLabel, passwordLabel;
	
	/**constructor for login Panel*/
	public LoginPanel()
	{
		super();
		
		nameLabel = new JLabel("Name:");
		passwordLabel = new JLabel("Password:");
		name = new JTextField();
		password = new JTextField();
		
		this.setLayout(new GridLayout(3,2));
		this.add(nameLabel);
		this.add(name);
		this.add(passwordLabel);
		this.add(password);
	}
	
	public String getUserName()
	{
		return name.getText();
	}
	
	public String getPassword()
	{
		return password.getText();
	}
	
}
