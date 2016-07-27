package com.uni_wuppertal.iad.vierteltour.updater;


import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


public class Updater extends ContextWrapper{
  private static Updater ourInstance;

  /**
   * Singleton-method
   *
   * @return Updater a static singleton of this class
   */
  public static Updater getInstance( Context context){
    if( ourInstance == null) {
      ourInstance = new Updater( context );
    }

    return ourInstance;
  }


  /**
   * The main constructor.
   *
   *
   */
  private Updater( Context base ){
    super(base);
  }


  // Constants
  private static final String DEBUG_TAG = "Updater";
  /**
   * Check if there is a network connection available
   *
   * @return true if there is a network connection available, false else
   */
  public boolean isNetworkAvailable(){
    ConnectivityManager connMgr = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

    if( networkInfo != null && networkInfo.isConnected() ) {
      Log.d( DEBUG_TAG, "Network connection of type " + networkInfo.getTypeName() + " (" + networkInfo.getSubtypeName() + ") found" );
      return true;
    } else {
      Log.e( DEBUG_TAG, "No network connection available.");
      return false;
    }
  }



  /**
   *
   * @return true if there is either new data or data has changed, false else
   */
  public boolean anyUpdatesOnTourdata(){
    // If the phone has no connection to the internet, tell this to the user.
    if( !isNetworkAvailable() ){
      Toast.makeText( getApplicationContext(), "Can't check for updates: No internet connection found. Please enable a connection to the internet.", Toast.LENGTH_LONG ).show();
      return false;
    }

    Log.d( DEBUG_TAG, "Checking for updates..." );

    // TODO: Move the URL to the resources
    String stringUrl = "http://10.0.2.2:8888/";

    new DownloadWebpageTask().execute(stringUrl);

    return true;
  }



  // Uses AsyncTask to create a task away from the main UI thread. This task takes a
  // URL string and uses it to create an HttpUrlConnection. Once the connection
  // has been established, the AsyncTask downloads the contents of the webpage as
  // an InputStream. Finally, the InputStream is converted into a string, which is
  // displayed in the UI by the AsyncTask's onPostExecute method.
  private class DownloadWebpageTask extends AsyncTask<String, Void, String>{
    @Override
    protected String doInBackground( String... urls ) {
      // urls comes from the execute() call: urls[0] is the url.
      try {
        return downloadJSON( urls[0] );
      } catch (IOException e) {
        return "Unable to retrieve web page. URL may be invalid.";
      }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute( String result ) {
      Log.d( DEBUG_TAG, "Let's see if and what we have retrieved!"  );

      Log.d( DEBUG_TAG, "Result in JSON-Format:" );
      Log.d( DEBUG_TAG, result );

      Map<String, String> gsonResult = new Gson().fromJson(result, new TypeToken<Map<String, String>>() {}.getType());

      Log.d( DEBUG_TAG, "Result parsed by GSON (isTourDataAvailable)" );
      for( Map.Entry<String, String> entry : gsonResult.entrySet() ){
        Log.d( DEBUG_TAG, entry.getKey() + ": " + entry.getValue() );
      }

    }


    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadJSON( String myurl ) throws IOException {
      InputStream is = null;

      try {
        URL url = new URL( myurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout( 10000 );
        conn.setConnectTimeout( 15000 );
        conn.setRequestMethod( "GET" );
        conn.setDoInput( true );

        // Starts the query
        conn.connect();

        int response = conn.getResponseCode();
        Log.d( DEBUG_TAG, "Webserver responded with code: " + response );

        if( response == 200 ) {
          is = conn.getInputStream();

          // Convert the InputStream into a JSON-string
          return readJSON( is );
        } else {
          Log.e( DEBUG_TAG, "Unable to download JSON data (" +  response + ")" );
          return Integer.toString( response );
        }

        // Makes sure that the InputStream is closed after the app is finished using it.
      } finally {
        if (is != null) {
          is.close();
        }
      }
    }


    // Reads an InputStream as JSON and converts it to a named array aka Map.
    public String readJSON( InputStream stream ) throws IOException, UnsupportedEncodingException {
      BufferedReader response = new BufferedReader( new InputStreamReader(stream, "UTF-8") );
      StringBuilder json = new StringBuilder();
      String line;

      while ((line = response.readLine()) != null) {
        json.append(line);
      }

      // Clean up
      response.close();

      return json.toString();
    }

  }


}
