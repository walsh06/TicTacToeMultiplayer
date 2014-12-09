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
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONException;
import org.json.JSONObject;

import TicTacToe.Game;
import TicTacToe.User;
import URLRequest.URLRequest;


/**CardPanel manages the panels and the transitions between panels*/
public class CardPanel extends JPanel {

	LoginPanel loginPanel;
	GamePanel gamePanel;
	LobbyPanel lobbyPanel;
	LeaderboardPanel leaderboardPanel;
	JTextField name, password;
	JButton loginButton, register, joinGame, createGame, leaveGame, rematch, back, leaderboard, backLeader, logout, refresh;
	User currentUser;

	/**Constructor for the CardPanel class*/
	public CardPanel()
	{
		super();
		loginPanel = new LoginPanel();
		gamePanel = new GamePanel();
		lobbyPanel = new LobbyPanel();
		leaderboardPanel = new LeaderboardPanel();
		
		//Add the panels to the cardlayout with their key
		this.setLayout(new CardLayout());
		this.add(loginPanel, "LOGIN");
		this.add(lobbyPanel, "LOBBY");
		this.add(gamePanel, "GAME");
		this.add(leaderboardPanel, "LEADERBOARD");
		
		//
		//Login Panel
		//
		loginButton = new JButton("Login");
		
		loginButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					// get the text from the text fiels and make sure they are not blank
					String name = loginPanel.getUserName(), password = loginPanel.getPassword();
					if(!name.equals("") || !password.equals(""))
					{
						//Get the json response for login
						JSONObject json = URLRequest.readJsonFromUrl(URLRequest.login(name, password));
						//if the login was succesful set the currentuser and change panel
						//else display a pop up
						if(json.getBoolean("success") == true)
						{
							currentUser = new User(json.getInt("id"), name, password);
							gamePanel.setUser(currentUser);
							CardPanel.this.changeCard("LOBBY");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Failed log in");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Please fill in all fields");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
						
			}
			
		});
		
			
		register = new JButton("Register");
		register.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Get the text from the text fields and check if it is blank
				String name = loginPanel.getUserName(), password = loginPanel.getPassword();
				if(!name.equals("") || !password.equals(""))
				{
					JSONObject json;
					try {
						//get the json response from register
						json = URLRequest.readJsonFromUrl(URLRequest.register(name, password));
						//if successful automatically login the user
						if(json.getBoolean("success") == true)
						{
							json = URLRequest.readJsonFromUrl(URLRequest.login(name, password));
							if(json.getBoolean("success") == true)
							{
								currentUser = new User(json.getInt("id"), name, password);
								CardPanel.this.changeCard("LOBBY");
							}
							else
							{
								JOptionPane.showMessageDialog(null, "Failed log in");
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Failed to register");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		//add the buttons to the panel
		loginPanel.add(loginButton);
		loginPanel.add(register);
		
		//
		//Lobby Panel
		//
		
		joinGame = new JButton("Join");
		createGame = new JButton("Create");
		leaderboard = new JButton("Leaderboard");
		logout = new JButton("Logout");
		refresh = new JButton("Refresh Game List");
		
		logout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//change to the login screen and set the user to null
				CardPanel.this.changeCard("LOGIN");
				currentUser = null;
			}
			
		});
		
		refresh.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//update the list of games in the lobby
				lobbyPanel.updateGameList();
			}
		});
		
		joinGame.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JSONObject json;
				int opponent;
				char symbol;
				try {
					//call join game on the currently selected game
					json = URLRequest.readJsonFromUrl(URLRequest.joinGame(currentUser, lobbyPanel.getCurrentGame()));
					
					if(json.getBoolean("success") == true)
					{
						//if successfully joined request the game information
						json = URLRequest.readJsonFromUrl(URLRequest.getGame(Integer.parseInt(lobbyPanel.getCurrentGame())));
						
						//if the game is returned succesfully
						if(json.getInt("id") != -1)
						{
							//get the opponent and set the players symbol
							if(json.getInt("player_1") == currentUser.getID())
							{
								opponent = json.getInt("player_2");
								symbol = 'O';
							}
							else
							{
								opponent = json.getInt("player_1");
								symbol = 'X';
							}
							//create the instance of the game and send it to the game panel
							Game newGame = new Game(json.getInt("current_player"), json.getString("board_state"), json.getInt("id"), opponent, symbol);
							gamePanel.newGame(newGame);
							gamePanel.updateBoard();
							//change to the game panel
							CardPanel.this.changeCard("GAME");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Failed to join game");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		createGame.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JSONObject json;
				int opponent, id;
				
				try {
					//get response from the create game request
					json = URLRequest.readJsonFromUrl(URLRequest.createGame(currentUser));
					
					if(json.getBoolean("success") == true)
					{
						//if created successfully get the game details from the server
						id = json.getInt("game_id");
						json = URLRequest.readJsonFromUrl(URLRequest.getGame(id));
						
						if(json.getInt("id") != -1)
						{
							//create the new instance of the game
							Game newGame = new Game(json.getInt("current_player"), json.getString("board_state"), json.getInt("id"), 0, 'X');
							gamePanel.newGame(newGame);
							gamePanel.updateBoard();
							
						}
						gamePanel.updateBoard();
						CardPanel.this.changeCard("GAME");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Failed to create game");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		});
		leaderboard.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				CardPanel.this.changeCard("LEADERBOARD");
				try {
					//get the response from the leaderboard request
					JSONObject json = URLRequest.readJsonFromUrl(URLRequest.leaderboard());
					
					//pass the response to the leaderboard panel
					leaderboardPanel.setPlayerList(json.getJSONArray("leaderboard"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		//add buttons to the panel
		lobbyPanel.addButton(joinGame);
		lobbyPanel.addButton(createGame);
		lobbyPanel.addButton(refresh);
		lobbyPanel.addButton(logout);
		lobbyPanel.addButton(leaderboard);
		
		//
		//Game Panel
		//
		
		leaveGame = new JButton("Leave");
		leaveGame.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//change the panel
				CardPanel.this.changeCard("LOBBY");		
				//stop the timer
				gamePanel.endCheck();
				JSONObject json;
				
				try {
					//call leave game on the server
					json = URLRequest.readJsonFromUrl(URLRequest.leaveGame(gamePanel.getGameID() ,currentUser.getID()));
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		rematch = new JButton("Rematch");
		rematch.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg) {
				//check the game is finished
				if(gamePanel.isGameOver())
				{
					JSONObject json;
					try {
						//make call to the server for a rematch
						json = URLRequest.readJsonFromUrl(URLRequest.rematch(gamePanel.getGameID() ,currentUser.getID()));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					JOptionPane.showMessageDialog(null, "Waiting for Player 2......");
				}
			}
			
		});
		gamePanel.addButton(leaveGame);
		gamePanel.addButton(rematch);
		

		//
		//leaderboard panel
		//
		
		backLeader = new JButton("Back");
		backLeader.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//change to lobby panel
				CardPanel.this.changeCard("LOBBY");
			}
		});
		leaderboardPanel.add(backLeader, BorderLayout.SOUTH);
	}
	
	/**Change the card to cardName */
	public void changeCard(String cardName)
	{
		if(cardName.equals("LOBBY"))
		{
			//if the card is lobby update the list and user
			lobbyPanel.updateGameList();
			lobbyPanel.updateUserLabel(currentUser);
		}
		else if(cardName.equals("GAME"))
		{
			//if the card is game start the timer
			gamePanel.startCheck();
		}
		//change to the card with the key passed in
		CardLayout cl = (CardLayout)(this.getLayout());
		cl.show(this, cardName);	
	}
	
}
