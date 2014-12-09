/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */
package URLRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import TicTacToe.Game;
import TicTacToe.User;


/*URLRequest deals with the server requests from the client
 * It creates the urls and gets the responses
 * These are interpreted using json
 * referenced from:
 * http://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
 */
public class URLRequest {

		//ip of the  server
		private static String ip = "10.0.0.4";
		
		/**Return the string from the reader */
		private static String readAll(Reader rd) throws IOException {
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    //add each character to the stringbuilder
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
		  }

		/**Get and return the json response from the url */
		  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		    InputStream is = new URL(url).openStream();
		    try {
		    	//read in the stream from the url
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      //get the string from the stream
		      String jsonText = readAll(rd);
		      //convert the string into a json object
		      JSONObject json = new JSONObject(jsonText);
		      return json;
		    } finally {
		      is.close();
		    }
		  }

		  /**create the url for login*/
		  public static String login(String username, String password)
		  {
			  String urlString = "http://" + ip + "/webservice/login.py?username=fail&password=fail";
			  if(username.length() > 0 && (!username.contains(" ")) && 
					  password.length() > 0 && (!password.contains(" "))){
				  urlString = "http://" + ip + "/webservice/login.py?username="+ username +"&password=" + password;
			  } 
			  return urlString;
		  }
		  
		  /**create the url for register*/
		  public static String register(String username, String password)
		  {
			  String urlString = "http://" + ip + "/webservice/register.py?username=fail&password=fail";
			  if(username.length() > 0 && (!username.contains(" ")) && 
					  password.length() > 0 && (!password.contains(" "))){
				  urlString = "http://" + ip + "/webservice/register.py?username="+ username +"&password=" + password;
			  } 
			  return urlString;
		  }
		  
		  /**create the url for getGame*/
		  public static String getGame(int id)
		  {
			  String urlString = "http://" + ip + "/webservice/getGame.py?username=fail&password=fail";
			  if(id != 0){
				  urlString = "http://" + ip + "/webservice/getGame.py?id="+ id;
			  } 
			  return urlString;
		  }
		  
		  /**create the url for getGameList*/
		  public static String getGameList()
		  {
			  String urlString = "http://" + ip + "/webservice/getGameList.py";
			  return urlString;
		  }
		  
		  /**create the url for updateGame*/
		  public static String updateGame(Game game)
		  {
			  String urlString = "http://" + ip + "/webservice/updategame.py?";
			  System.out.println("CALL: " + game.getOpponent());
			  if(game != null){
				  urlString = "http://" + ip + "/webservice/updategame.py?playerid="+ game.getOpponent() + 
						  		"&board=" + game.toString() + "&gameid=" + game.getID();
			  } 
			  return urlString;
		  }
		  
		  /**create the url for createGame*/
		  public static String createGame(User user)
		  {
			  String urlString = "http://" + ip + "/webservice/createGame.py?id=fail";
			  if(user.getID() != 0){
				  urlString = "http://" + ip + "/webservice/createGame.py?id="+ user.getID();
			  } 
			  return urlString;
		  }
		  
		  /**create the url for joinGame*/
		  public static String joinGame(User user, String id)
		  {
			  String urlString = "http://" + ip + "/webservice/joinGame.py?playerid=fail&gameid=fail";
			  if(user.getID() != 0 && !id.equals("")){
				  urlString = "http://" + ip + "/webservice/joinGame.py?playerid="+ user.getID()+"&gameid=" + id;
			  } 
			  return urlString;
		  }
		  
		  /**create the url for checkGameGame*/
		  public static String checkGameState(String board, int id)
		  {
			  String urlString = "http://" + ip + "/webservice/getGameState.py?board_state=fail&id=fail";
			  if(id != 0 && !board.equals("")){
				  urlString = "http://" + ip + "/webservice/getGameState.py?board_state="+ board +"&id=" + id;
			  } 
			  return urlString;
		  }
		  
		  /**create the url for leaveGame*/
		  public static String leaveGame(int gameID, int playerID)
		  {
			  String urlString = "http://" + ip + "/webservice/leave.py?game_id=fail&player_id=fail";
			  if(gameID != 0 && playerID != 0){
				  urlString = "http://" + ip + "/webservice/leave.py?game_id="+ gameID +"&player_id=" + playerID;
			  } 
			  return urlString;
		  }
		  
		  /**create the url for rematch*/
		  public static String rematch(int gameID, int playerID)
		  {
			  String urlString = "http://" + ip + "/webservice/rematch.py?gameid=fail&playerid=fail";
			  if(gameID != 0 && playerID != 0){
				  urlString = "http://" + ip + "/webservice/rematch.py?gameid="+ gameID +"&playerid=" + playerID;
			  } 
			  return urlString;
		  }
		  
		  /**create the url for leaderboard*/
		  public static String leaderboard()
		  {
			String urlString = "http://" + ip + "/webservice/getLeaderboard.py";
			  return urlString;
		  }
}
