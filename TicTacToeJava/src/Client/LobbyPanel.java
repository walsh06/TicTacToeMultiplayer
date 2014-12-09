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
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.json.JSONException;
import org.json.JSONObject;

import TicTacToe.Game;
import TicTacToe.User;
import URLRequest.URLRequest;

public class LobbyPanel extends JPanel {
	
	JPanel userPanel, infoPanel, buttonPanel;
	JTextArea userInfo, gameInfo;
	JComboBox gameBox;
	String currentID;
	
	ArrayList<String> gameStringList;
	ArrayList<Game> gameList;
			
	DefaultComboBoxModel model;
	
	/**Constructor for Lobby Panel */
	public LobbyPanel()
	{
		super();
		this.setLayout(new GridLayout(4,1));
		
		gameStringList = new ArrayList<String>();
		gameStringList.add("Game1");
		gameStringList.add("Game2");
		userPanel = new JPanel();
		userInfo = new JTextArea("Name: ");
		userPanel.add(userInfo);
		
		gameInfo = new JTextArea();
		infoPanel = new JPanel();
		infoPanel.add(gameInfo);
		
		model =  model = new DefaultComboBoxModel();
		gameBox = new JComboBox(model);
		gameBox.addActionListener(new comboBoxSelect());
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3,2));
		
		this.add(userPanel);
		this.add(gameBox);
		this.add(infoPanel);
		this.add(buttonPanel);
		
	}
	
	public void addButton(JButton button)
	{
		buttonPanel.add(button);
	}
	
	/**Listener for the combo box*/
	private class comboBoxSelect implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			//get the selected id and display it
			JComboBox cb = (JComboBox)e.getSource();
	        currentID = (String)cb.getSelectedItem();
	        gameInfo.setText("Game ID: " + currentID);
		}
		
	}
	
	/**Updates the combo box with open games*/
	public void updateGameList()
	{
		JSONObject json;
		try {
			//Get the list of games from the server
			json = URLRequest.readJsonFromUrl(URLRequest.getGameList());
			//convert jsonarray into a string array
			String listString = json.getJSONArray("game_id").toString();
			listString = listString.substring(1, listString.length() -1);
			String[] list = listString.split(",");
			model.removeAllElements();
			//add each game to the combobox
			for(int i =0 ; i < list.length; i++)
			{
				model.addElement(list[i]);
			}		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateUserLabel(User u)
	{
		userInfo.setText("NAME: " + u.getName());
	}
	
	public String getCurrentGame()
	{
		return currentID;
	}

}
