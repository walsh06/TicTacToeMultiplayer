/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */
package TicTacToe;

public class Game {

	char[][] board;
	int currentPlayer, id, opponent;
	char symbol;
	/**Default constructor for Game */
	public Game()
	{
		currentPlayer = 1;
		board = new char[3][3];
		setBoardFromString("BBBBBBBBB");
	}
	
	/**Constructor for Game, takes in the currentplayer, boardstate and gameID*/
	public Game(int currentPlayer, String board, int id)
	{
		this.currentPlayer = currentPlayer;
		this.board = new char[3][3];
		setBoardFromString(board);
		this.id = id;
	}
	
	/**Constructor for Game, takes in the currentplayer, boardstate and gameID and the players symbol*/
	public Game(int currentPlayer, String board, int id, int opponent, char symbol)
	{
		this.symbol = symbol;
		this.currentPlayer = currentPlayer;
		this.board = new char[3][3];
		setBoardFromString(board);
		this.id = id;
		this.opponent = opponent;
	}
	
	/**Turns the 2D array of the board into a string */
	public String toString()
	{
		String currentGame = "";
		
		//loop through the array and add each character to the string
		for(int i =0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				currentGame += board[i][j];
			}
		}
		
		return currentGame;
	}
	
	/**Turns a provided string into a 2D array representation of the board */
	public void setBoardFromString(String boardString)
	{
		int c = 0;
		//loop through the string and set each character to a position in the array
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				updateBoard(i, j, boardString.charAt(c));
				c++;
			}
		}
	}
	
	/**updates a position in the board with the correct symbol */
	public void updateBoard(int x, int y, char player)
	{
		board[x][y] = player;
	}
	
	//Gets and sets for the Game class
	public void setSymbol(char symbol)
	{
		this.symbol = symbol;
	}
	
	public char getSymbol()
	{
		return symbol;
	}
	
	public int getOpponent()
	{
		return opponent;
	}
	
	public void setOpponent(int o)
	{
		this.opponent = o;
	}
	
	public char[][] getBoard()
	{
		return board;
	}
	
	public int getPlayer()
	{
		return currentPlayer;
	}
	
	public void setPlayer(int x)
	{
		currentPlayer = x;
	}
	
	public int getID()
	{
		return id;
	}
}
