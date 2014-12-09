/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */


package com.example.tictactoeandroid;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	//Text Fields for username and password
	EditText usernameText, passwordText;
	//Buttons for login and register
	Button loginButton, registerButton;

	static int user_id ;
	static int game_id;
	static final String IP = "10.0.0.4";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//calls activity's onCreate
		super.onCreate(savedInstanceState);
		//sets view to layout xml file
		setContentView(R.layout.activity_main);

		//sets text fields
		usernameText = (EditText)findViewById(R.id.usernameText);
		passwordText   = (EditText)findViewById(R.id.passwordText);

		//sets buttons
		loginButton = (Button)findViewById(R.id.loginButton);
		registerButton = (Button)findViewById(R.id.registerButton);


		//sets on click listeners for both buttons
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {		
		//String for username and password;
		String username =  usernameText.getText().toString();
		String password = passwordText.getText().toString();

		switch (v.getId()) {
		//if login button pressed
		case R.id.loginButton:
			//sets paramater array to pass into AsyncLogin 
			String[] params = {"login", loginURL(username, password)};
			//starts AsyncLogin
			new AsyncLogin().execute(params);
			break;
			//if register button pressed
		case R.id.registerButton:
			//sets paramater array to pass into AsyncLogin 
			String[] regParams = {"register", registerURL(username, password)};
			//starts AsyncLogin
			new AsyncLogin().execute(regParams);
			break;

		default:
			break;
		}
	}
	
	

	//Method used to generate the url for login
	public String loginURL(String username, String password)
	{
		String urlString = "";
		if(username.length() > 0 && password.length() > 0 ){
			urlString = "http://" + IP + "/webservice/login.py?username="+ username +"&password=" + password;
		} 
		urlString = urlString.replaceAll(" ", "&#32");

		return urlString;
	}

	//Method used to generate the url for login
	public String registerURL(String username, String password)
	{
		String urlString = "";
		if(username.length() > 0 && password.length() > 0 ){
			urlString = "http://" + IP + "/webservice/register.py?username="+ username +"&password=" + password;
		} 
		urlString = urlString.replaceAll(" ", "&#32");
		return urlString;
	}


	//Uses Async Task to run the scripts, as they can't be ran on the UI thread
	class AsyncLogin extends AsyncTask<String[], String, String> {


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

				//if login
				if(type.equals("login"))
				{

					//if script success is true
					if(json.getBoolean("success")){
						//sets user_id
						user_id = json.getInt("id");
						//starts MenuList Intent
						Intent i = new Intent(MainActivity.this, MenuList.class);
						startActivity(i);
					} else {
						//Toast to display failure message
						Toast.makeText(MainActivity.this, json.getString("message"),
								Toast.LENGTH_SHORT).show();
					}
					//if register
				}else if(type.equals("register"))
				{
					//message showing response from script
					System.out.println("NULL?" + json.getBoolean("success"));
					Toast.makeText(MainActivity.this, json.getString("message"),
							Toast.LENGTH_SHORT).show();

				} 
			} catch (JSONException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}


