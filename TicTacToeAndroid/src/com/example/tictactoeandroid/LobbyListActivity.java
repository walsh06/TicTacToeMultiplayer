/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */


package com.example.tictactoeandroid;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LobbyListActivity extends Activity {

	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby_list);


		//sets List view to view from xml layout filw
		lv = (ListView)findViewById(R.id.listView1);


		//gets the extras passed in
		Bundle extras = getIntent().getExtras();
		//sets list to lobbies found
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				extras.getStringArray("lobbies")));

		//
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				//Get the id from the list. 
				String lobbyID =(String) ((TextView) v).getText();
				//Join the gamw
				new AsyncJoinGame().execute(joinLobbyURL(lobbyID));
			}

		});
	}
	
	//generates joinGame URL
	public String joinLobbyURL(String id)
	{
		return "http://" + MainActivity.IP + "/webservice/joinGame.py?gameid="+ id + "&playerid=" + MainActivity.user_id;
	}


	//Uses Async Task to run the scripts, as they can't be ran on the UI thread
	class AsyncJoinGame extends AsyncTask<String, String, String> {
		JSONObject json;
		@Override
		protected String doInBackground(String... args) {
			//get url from params passed in
			String url= args[0];
			try {
				//read from python script
				json = JsonHelper.readJsonFromUrl(url); 
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;

		}

		protected void onPostExecute(String file_url) {

			try {
				//if script is successfully ran
				if(json.getBoolean("success")){
					//set game id
					MainActivity.game_id = json.getInt("game_id");
					//change to game class
					Intent i = new Intent(LobbyListActivity.this, GameActivity.class);
					startActivity(i);
				} else {
					Toast.makeText(LobbyListActivity.this, "Try Again!",
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
