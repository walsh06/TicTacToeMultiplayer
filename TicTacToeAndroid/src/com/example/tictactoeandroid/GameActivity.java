/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */


package com.example.tictactoeandroid;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class GameActivity extends Activity {


	GridView gridView;
	Timer timer;
	//init board state
	String player = "", state = "BBBBBBBBB";
	int currentPlayer, opponentID;

	//firstTime used to make sure dialog box only shows once at gameover
	boolean firstTime = true;


	//init board state array
	String[] board = new String[] { 
			" ", " ", " ", 
			" ", " ", " ", 
			" ", " ", " "};



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);

		//Set gridview to grid from layout files
		gridView = (GridView) findViewById(R.id.gridView1);

		//Create String Array Adapter and set it
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.grid_item, board);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new itemListener());
		
		//Start Timer. 
		timer = new Timer();
		timer.schedule(new RemindTask(), 0,1000);

	}

	
	@Override
	//overrides back button to go back to main menu
	public void onBackPressed() {
		//init parameters for async tast
		String[] params = {"leave", leaveURL()};
		new AsyncUpdateGame().execute(params);
		
		//switch to menu
		Intent i = new Intent(GameActivity.this, MenuList.class);
		startActivity(i);
	}

	//generates leave url
	public String leaveURL()
	{
		//cancels timer
		timer.cancel();
		return "http://" + MainActivity.IP + "/webservice/leave.py?game_id=" + MainActivity.game_id + 
				"&player_id=" + MainActivity.user_id;
	}


	//generates rematch url
	public String rematchURL()
	{
		return "http://" + MainActivity.IP + "/webservice/rematch.py?gameid=" + MainActivity.game_id + 
				"&playerid=" + MainActivity.user_id;
	}

	//updates the grid
	public void updateBoard(String state){

		this.state = state;
		for(int i = 0; i < 9 ; i++){
			//change B to space and add it to the grid
			if(state.charAt(i) == 'B'){
				board[i] = " ";
			} else
				board[i] = "" + state.charAt(i);
		}

		//reset adapter to new values
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.grid_item, board);
		gridView.setAdapter(adapter);

	}

	//method to show dialog box
	private void showPopUp() {
		//sets up dialog box
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		helpBuilder.setTitle("Game Over");
		helpBuilder.setMessage("Would you like to rematch or leave?");
		
		//Sets rematch button
		helpBuilder.setPositiveButton("Rematch",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				//if clicked resets game
				String[] params = {"rematch", rematchURL()};
				new AsyncUpdateGame().execute(params);

			}
		});

		
		//Sets Leave button
		helpBuilder.setNegativeButton("Leave", 
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				//if clicked leaves game, changes intent
				String[] params = {"leave", leaveURL()};
				new AsyncUpdateGame().execute(params);
				Intent i = new Intent(GameActivity.this, LobbyListActivity.class);
				startActivity(i);

			}
		});

		// create dialog, and show it.
		AlertDialog helpDialog = helpBuilder.create();
		helpDialog.show();
	}



	class itemListener implements OnItemClickListener
	{

		public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {

			//Check if space is empty, you can play, and you have an opponent
			if(state.charAt(position)=='B' && currentPlayer == MainActivity.user_id 
					&& opponentID != 0)
			{
				//Change current player
				currentPlayer = opponentID;
				
				//update state string
				state = state.substring(0, position) + player + state.substring(position +1, 9);

				//update grid
				updateBoard(state);


				//update game url
				String url = "http://" + MainActivity.IP+"/webservice/updateGame.py?gameid=" + MainActivity.game_id +
						"&playerid=" + opponentID+ "&board=" + state;

				//call update game script
				String[] params = {"update", url};
				new AsyncUpdateGame().execute(params);
			}
		}
	}

	
	//Ref: http://enos.itcollege.ee/~jpoial/docs/tutorial/essential/threads/timer.html
	class RemindTask extends TimerTask {
		public void run() {
			
			//call getGameState script
			String[] params = {"getState", "http://" + MainActivity.IP + "/webservice/getGameState.py?id=" + 
					MainActivity.game_id + "&board_state=" + state};
			new AsyncUpdateGame().execute(params);
		}
	}


	//Uses Async Task to run the scripts, as they can't be ran on the UI thread
		class AsyncUpdateGame extends AsyncTask<String[], String, String> {


			JSONObject json; //json object read from url
			String type; //Type used to run different scripts depending on the parameter passed in

			@Override
			protected String doInBackground(String[]... args) {

				//get paramaters passed in from .execute()
				type = args[0][0];
				String url= args[0][1];

				try {
					json = JsonHelper.readJsonFromUrl(url); 
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				return null;

			}

		protected void onPostExecute(String file_url) {
			try {
				//if get State
				if(type.equals("getState"))
				{
					//if there is a change
					if(json.getInt("change") == 1){ //changed
						state = json.getString("board_state");
						firstTime = true;

						updateBoard(state);
						
						//if the game is over
					} else if(json.getInt("change") == 2){ //gameover

						//if the dialog hasn't been shown, show it
						if(firstTime){
							showPopUp();
							firstTime = false;
						}
						//update the state
						state = json.getString("board_state");
						//update grid
						updateBoard(state);
					}


					//Sets the player to either X or O
					if(json.getInt("player_1") == MainActivity.user_id)
					{
						player = "X";
						opponentID = json.getInt("player_2");
					}else{
						opponentID = json.getInt("player_1");
						player = "O";
					}

					//sets the current Player
					currentPlayer = json.getInt("current_player");


				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}



}
