package com.uni_wuppertal.iad.vierteltour.updater;


import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedReader;
import java.io.File;
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

    downloadManager = new ThinDownloadManager();
  }



  // Constants
  private static final String DEBUG_TAG = "Updater";



  // Properties
  private ThinDownloadManager downloadManager;
  private int manifestDownloadId;



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
   * Check if we have any new updates on our tour data
   *
   * @return true if there is either new data or data has changed, false else
   */
  public boolean anyUpdatesOnTourdata(){
    // If the phone has no connection to the internet, tell this to the user.
    if( !isNetworkAvailable() ){
      // TODO: Replace Toasts (all of them, not just this one) with proper UI elements (modals etc.)
      Toast.makeText( getApplicationContext(), "Can't check for updates: No internet connection found. Please enable a connection to the internet.", Toast.LENGTH_LONG ).show();
      return false;
    }

    // TODO: Well, actually, DO check for updates ^^
    Log.d( DEBUG_TAG, "Checking for updates..." );

    // TODO: Move the URL to the resources once you have finetuned the updater behaviour
    String stringUrl = "http://10.0.2.2:8888/";

    new DownloadWebpageTask().execute(stringUrl);

    return true;
  }



  /**
   *
   * @return true if the download was successful, false else
   */
  public boolean downloadTourdata(){
    // If the phone has no connection to the internet, tell this to the user.
    if( !isNetworkAvailable() ){
      Toast.makeText( getApplicationContext(), "Can't download file: No internet connection found. Please enable a connection to the internet.", Toast.LENGTH_LONG ).show();
      return false;
    }

    Log.d( DEBUG_TAG, "Starting file download..." );

    // TODO: Move the URL to the resources
    //String url = "http://10.0.2.2:8888/fortschrott.zip";
    String url = "http://10.0.2.2:8888/files.zip";
    //String destination = OurStorage.getInstance( Updater.this ).getStoragePath() + "/fortschrott.zip";
    String destination = new File( OurStorage.getInstance( Updater.this ).getStoragePath() ).getParentFile().getAbsolutePath()  + "/fortschrott.zip";

    //new DownloadFileTask().execute(stringUrl);
    this.downloadFile( url, destination );


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



  private void downloadFile( String url, String destination ){
    Uri downloadUri = Uri.parse( url );
    Uri destinationUri = Uri.parse( destination );

    // Setup the download, with nice callback function on different events throughout the download
    DownloadRequest downloadRequest = new DownloadRequest( downloadUri)
      .setRetryPolicy( new DefaultRetryPolicy() )
      .setDestinationURI( destinationUri ).setPriority( DownloadRequest.Priority.LOW )
      .setStatusListener( new DownloadStatusListenerV1() {
        // Define a Toast object so we can update it and display it for as long as the onProgress-object fires
        // It's only a temporary solution during development anyways, so, don't tweak it further, e.g. it would still disappear
        // if onProgress doesn't fire for more than 3.5s due to network delay etc.
        Toast statusToast = Toast.makeText( getApplicationContext(), "intentionally left blank - or, something like that", Toast.LENGTH_LONG );

        String successMessage = "Download completed!";
        String errorMessage = "Download FAILED!\n" + "Message:\n";
        String progressMessage = "Download in progress! (";
        String toastText = "Tourdaten werden heruntergeladen - bitte einen Moment Geduld";
        Boolean updateProgress = true;

        @Override
        public void onDownloadComplete( DownloadRequest request ) {
          Log.d( DEBUG_TAG, successMessage  + request.getDestinationURI().toString() );

          // unzip
          unzipFile( request.getDestinationURI().toString() );

          statusToast.setText( "Die Tourdaten wurden vollständig heruntergeladen." );
          statusToast.show();
        }

        @Override
        public void onDownloadFailed( DownloadRequest request, int returnCode, String returnMessage ) {
          Log.d( DEBUG_TAG, errorMessage + returnMessage + " (" + returnCode + ")" );

          statusToast.setText( "Beim Herunterladen der Tourdaten ist ein Fehler aufgetreten. Bitte stellen Sie sicher, dass Ihr Gerät Zugang zum Internet hat." );
          statusToast.show();
        }

        @Override
        public void onProgress( DownloadRequest request, long totalBytes, long downlaodedBytes, int progress) {
          if( (progress % 5) == 0 ){
            if( updateProgress ){
              Log.d( DEBUG_TAG, progressMessage + downlaodedBytes + " / " + totalBytes + ") - Progress? => " + progress + " (" + updateProgress.toString() + ")" );

              toastText = toastText + ".";
              statusToast.setText( toastText );
              updateProgress = false;
            }
          } else {
            updateProgress = true;
          }

          statusToast.show();
        }
      });

    // Start the download

    // Display the message to the user for as long as the download lasts
    Toast.makeText( getApplicationContext(), "Beginne die Tourdaten herunterzuladen...", Toast.LENGTH_LONG ).show();

    this.manifestDownloadId = downloadManager.add( downloadRequest );
  }



  /**
   * Unzip a .zip compressed file into the same destination where the .zip file is in.
   *
   * The folder name where the content from the .zip file are extracted into has the same name
   * as the .zip file itself, without it's extension.
   *
   * Example:
   * /location/of/zip/file/unzip_me.zip -> /location/of/zip/file/unzip_me
   *
   * @param zipFile - The destination of our ZIP-File
   */
  private void unzipFile( String zipFile ){
    try {
      // Determine output folder
      File rootfolder = new File( zipFile );
      File outputFolder = new File( rootfolder.getParentFile().getAbsolutePath() );

      Log.d( DEBUG_TAG, "Output folder:");
      Log.d( DEBUG_TAG, outputFolder.getAbsolutePath() );

      ZipFile zip = new ZipFile( zipFile );

      zip.extractAll( outputFolder.getAbsolutePath() );

    } catch( ZipException e ){
      e.printStackTrace();
    }
  }



}
