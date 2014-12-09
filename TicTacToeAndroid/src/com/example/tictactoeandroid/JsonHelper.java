/* Cathal O'Halloran - 11123834 - Computer Games Development
 * Liam Walsh - 11122048 - Computer Games Development
 * Final Assignment - Distributed Tic Tac Toe Java/Android
 * Sumbission date - 1-12-13
 */

package com.example.tictactoeandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

/*JsonHelper deals with the server requests from the client
 * Used to interpreted json referenced from:
 * http://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
 */

public class JsonHelper {

			
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

}
