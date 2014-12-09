/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */
package Client;

import java.awt.BorderLayout;
import java.awt.GridLayout;



import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.json.JSONArray;


public class LeaderboardPanel extends JPanel {

	JPanel board;
	JLabel[] boardInfo;
	/**Constructor for leaderboard panel*/
	public LeaderboardPanel()
	{
		this.setLayout(new BorderLayout());
		boardInfo = new JLabel[55];
		board = new JPanel();
		board.setLayout(new GridLayout(11,5));
		
		this.add(board, BorderLayout.CENTER);
	}
	
	/**Breaks up the array and places it in individual buttons*/
	public void setPlayerList(JSONArray players)
	{
		boardInfo = new JLabel[55];
		
		//turn the jsonarray into an array of strings
		String listString = players.toString();
		listString = listString.substring(1, listString.length() -1);
		listString = listString.replaceAll("\"", "");
		String[] list = listString.split(",");
		
		int count =0;
		//for each string add it to a label and then to the panel
		for(String s: list)
		{
			System.out.println(count + " " + s);
			boardInfo[count] = new JLabel(s);
			board.add(boardInfo[count]);
			count++;
		}
	}
}
