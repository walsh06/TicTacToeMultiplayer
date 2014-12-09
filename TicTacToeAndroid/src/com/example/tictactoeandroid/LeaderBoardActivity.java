/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */

package com.example.tictactoeandroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class LeaderBoardActivity extends Activity {

	//create Gridview
	GridView gridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_board);
		
		//set gridview to xml gridview1
		gridView = (GridView)findViewById(R.id.gridView1);
		
		//get the extras passed in
		Bundle extras = getIntent().getExtras();
		
		//Gets the leaderboard from extras
		String[] x = extras.getStringArray("leaderboard");
		
		//creates adapter and sets it
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.leader_grid_item, x);
 
		gridView.setAdapter(adapter);

	}

}
