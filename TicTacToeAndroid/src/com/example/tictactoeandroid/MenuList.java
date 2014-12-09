/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */


package com.example.tictactoeandroid;


import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MenuList extends Activity {

	//array for Menu
	static final String[] MENU = new String[] { "Create Lobby", "Join Lobby", "Get Leaderboard" };
	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_menu_list);
		//set list view
		lv = (ListView)findViewById(R.id.listView1);
		//sets adapter for list
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				MENU));
		
		//sets on click listener for the list
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String urlString = "";
				switch(position){
				case 0: // create lobby
					//generate and run create game script
					urlString = "http://" + MainActivity.IP + "/webservice/createGame.py?id=" + MainActivity.user_id;
					String[] params = {"create", urlString};
					new AttemptLobby().execute(params);
					break;

				case 1: //find lobby
					//runs get game list script
					urlString = "http://" + MainActivity.IP + "/webservice/getGameList.py";
					String[] joinParams = {"find", urlString};
					new AttemptLobby().execute(joinParams);
					break;


				case 2: //find lobby
					//runs leaderboard list
					urlString = "http://" + MainActivity.IP + "/webservice/getLeaderboard.py";
					String[] leaderParams = {"leaderboard", urlString};
					new AttemptLobby().execute(leaderParams);
					break;

				}

			}

		});


	}


	@Override
	//overided the back button so after leave you can't return to a game.
	public void onBackPressed() {
		Intent i = new Intent(MenuList.this, MainActivity.class);
		startActivity(i);
	}

	//Uses Async Task to run the scripts, as they can't be ran on the UI thread
	class AttemptLobby extends AsyncTask<String[], String, String> {
		JSONObject json;
		String type;
		@Override
		protected String doInBackground(String[]... args) {
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
				
				if(type.equals("create"))
				{
					if(json.getBoolean("success")){
						//switches intent
						Intent i = new Intent(MenuList.this, GameActivity.class);
						MainActivity.game_id = json.getInt("game_id");
						startActivity(i);
					}
					else 
						//shows error message toast
						Toast.makeText(MenuList.this, "You can't create a lobby now", Toast.LENGTH_LONG).show();
				}else if(type.equals("find"))
				{
					if(json.getBoolean("success")){
						//gets the array, and puts it toString
						String listString = json.getJSONArray("game_id").toString();
						//removes [] from string
						listString = listString.substring(1, listString.length() -1);
						//creates array from string
						String[] list = listString.split(",");
						
						//creates intent and passes in all the lobbies retrieved from the getGameList
						Intent i = new Intent(MenuList.this, LobbyListActivity.class);
						i.putExtra("lobbies", list);
						//switches intent
						startActivity(i);
					}
				} else if(type.equals("leaderboard")){

					//gets the array, and puts it toString
					String listString = json.getJSONArray("leaderboard").toString();
					//removes [] from string
					listString = listString.substring(1, listString.length() -1);
					//removes "" from string
					listString = listString.replaceAll("\"", "");
					//creates array from string
					String[] list = listString.split(",");

					//creates intent and passes in all the lobbies retrieved from the getGameList
					Intent i = new Intent(MenuList.this, LeaderBoardActivity.class);
					i.putExtra("leaderboard", list);
					//switches intent
					startActivity(i);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}





