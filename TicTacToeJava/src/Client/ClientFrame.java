/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */
package Client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientFrame extends JFrame{
	
	JPanel cardPanel;
	public ClientFrame()
	{
		super();
		//init the frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Chat Client");
		this.setSize(400, 300);
		this.setLayout(new BorderLayout());
		
		//add the card manager panel to the frame
		cardPanel = new CardPanel();
		this.add(cardPanel, BorderLayout.CENTER);
		this.setVisible(true);
	}

}
