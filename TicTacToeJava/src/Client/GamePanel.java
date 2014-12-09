/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */
package Client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import TicTacToe.*;
import URLRequest.URLRequest;

public class GamePanel extends JPanel {
	
	JButton[][] squares;
	JPanel boardPanel, buttonPanel, infoPanel;
	Game game;
	JLabel currentPlayer;
	User currentUser;
	Timer timer;
	boolean firstTime, gameOver;
	public GamePanel()
	{
		super();
		//game = new Game();
		this.setLayout(new BorderLayout());
		gameOver = false;
		firstTime = true;
		infoPanel = new JPanel();
		currentPlayer = new JLabel("");
		infoPanel.add(currentPlayer);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(3,3));
		squares = new JButton[3][3];
		
		//create the buttons for the game
		for(int i= 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				squares[i][j] = new JButton(" ");
				squares[i][j].addActionListener(new buttonPush());
				boardPanel.add(squares[i][j]);
				
			}
		}
		
		this.add(infoPanel,BorderLayout.NORTH);
		this.add(boardPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**inner class the runs the update timer for the game*/
	private class CheckGameTask extends TimerTask
	{

		@Override
		public void run() {
			try {
				//get the server response for checkGameState
				JSONObject json = URLRequest.readJsonFromUrl(URLRequest.checkGameState(game.toString(), game.getID()));
				
				//if the change is 1 then update the game
				//if the change is 2 the game has been won
				//0 means no change
				if(json.getInt("change") == 1)
				{
					//update the game and the gui
					game.setBoardFromString(json.getString("board_state"));
					game.setPlayer(json.getInt("current_player"));
					updateBoard();
					setLabel();
					firstTime = true;
					gameOver = false;
				}
				else if(json.getInt("change") == 2)
				{
					//update the game and gui
					game.setBoardFromString(json.getString("board_state"));
					game.setPlayer(json.getInt("current_player"));
					updateBoard();
					//GAME OVER
					//notify user of result
					if(json.getInt("winner") == currentUser.getID() && firstTime)
					{
						JOptionPane.showMessageDialog(null, "YOU WON!!");
					}
					else if(json.getInt("winner") == -1 && firstTime)
					{
						JOptionPane.showMessageDialog(null, "Its a draw");
					}
					else if(firstTime)
					{
						JOptionPane.showMessageDialog(null, "You SUCK!!");
					}
					firstTime = false;
					gameOver = true;
				}
				
				setLabel();
				
				if(json.getInt("player_2") == currentUser.getID())
				{
					game.setOpponent(json.getInt("player_1"));
					game.setSymbol('O');
				}
				else
				{
					game.setOpponent(json.getInt("player_2"));
					game.setSymbol('X');

				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**action listener for the buttons on the board*/
	private class buttonPush implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			//if the game is not over and its their turn
			if(game.getPlayer() == currentUser.getID() && game.getOpponent() != 0 && !gameOver)
			{
				//store the old board state
				String oldBoard = game.toString();
				JButton b = (JButton) e.getSource();
				
				//find which button was pressed and update that square
				for(int i= 0; i < 3; i++)
				{
					for(int j = 0; j < 3; j++)
					{
						if(squares[i][j] == b)
						{
							game.updateBoard(i, j, game.getSymbol());
						}	
					}
				}
				
				//update game with database
				try {
					JSONObject json = URLRequest.readJsonFromUrl(URLRequest.updateGame(game));
					
					if(json.getBoolean("success") == true)
					{
						game.setPlayer(game.getOpponent());
					}
					else
					{
						game.setBoardFromString(oldBoard);
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				updateBoard();
			}
			
		}
			
		
	}
	
	/**Update the gui board with the board state */
	public void updateBoard()
	{
		for(int i= 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				if(game.getBoard()[i][j] == 'X')
				{
					squares[i][j].setText("X");
					squares[i][j].setEnabled(false);	
				}
				else if(game.getBoard()[i][j] == 'O')
				{
					squares[i][j].setText("O");
					squares[i][j].setEnabled(false);	
				}
				else
				{
					squares[i][j].setText(" ");
					squares[i][j].setEnabled(true);
				}
							
			}
		}
	}
	/**start a new game*/
	public void newGame(Game game)
	{
		this.game = game;
		updateBoard();
	}
	
	/**start the timer*/
	public void startCheck()
	{
		timer = new Timer();
        timer.schedule(new CheckGameTask(),0, 1000);
	}
	
	/**top the timer*/
	public void endCheck()
	{
		timer.cancel();
	}
	
	/**set the label of the current player*/
	private void setLabel()
	{
		if(game.getPlayer() == currentUser.getID())
		{
			currentPlayer.setText("Your Move");
		}
		else
		{
			currentPlayer.setText("Opponent's Move");
		}
	}
	
	//gets ands sets
	public void setUser(User user)
	{
		currentUser = user;
	}
	
	public int getGameID()
	{
		return game.getID();
	}
	
	public boolean isGameOver()
	{
		return gameOver;
	}
	public void addButton(JButton b)
	{
		buttonPanel.add(b);
	}
}
