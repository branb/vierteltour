package com.uni_wuppertal.iad.vierteltour.utility.updater;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.utility.storage.Singletonint;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Region;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Area;
import com.uni_wuppertal.iad.vierteltour.utility.xml.City;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Tour;
import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourList;
import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourListReader;
import com.uni_wuppertal.iad.vierteltour.utility.storage.OurStorage;

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
import java.util.ArrayList;
import java.util.Map;


public class Updater extends ContextWrapper{
  private static Updater ourInstance;
  static int idsd=0;
  /**
   * Singleton-method
   *
   * @return Updater a static singleton of this class
   */
  public static Updater get( Context context){
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
    super( base );

    // TODO: Move the URL to the resources once you have finetuned the updater behaviour
    updateServerUrl = PreferenceManager.getDefaultSharedPreferences( getBaseContext() ).getString( "updateServerUrl", "http://www.vierteltour.uni-wuppertal.de/files/" );

    downloadManager = new ThinDownloadManager();
  }


  // Constants
  private static final String DEBUG_TAG = "Updater";



  // Properties
  private String updateServerUrl;
  private CustomProgressDialog progressDialog;
  private Singletonint singlepage;
  private ThinDownloadManager downloadManager;

  private int manifestDownloadId;
  private int tourlistDownloadId;
  private UpdateListener updateListener;

  // Indicates if we're checking for updates right now, e.g. in the middle of a download process
  private boolean checkingForUpdates = false;

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
  public boolean updatesOnTourdata(){
    // If the phone has no connection to the internet, tell this to the user.
    if( !isNetworkAvailable() ){
      // TODO: Replace Toasts (all of them, not just this one) with proper UI elements (modals etc.)
     // Toast.makeText( getApplicationContext(), "Can't check for updates: No internet connection found. Please enable a connection to the internet.", Toast.LENGTH_LONG ).show();
      //if()
      try{
      updateListener.noNewTourdataAvailable();}
      catch (Exception e){Log.d( DEBUG_TAG, "No Tourdata found!" );}
      return false;
    }
    Log.d( DEBUG_TAG, "Checking for updates..." );
    checkingForUpdates = true;
    try{new DownloadTourdataVersionTask().execute( updateServerUrl ).get();}
    catch(Exception e){return false;}

    // Assumption: If the version on server differs from our version, the tour data is new
    // There is no reason whatsoever for the data on the server side to be OLDER than this one.
    // Initialize SharedPreferences
  /*  SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences( getBaseContext() );

    //  Make a new preferences editor
    SharedPreferences.Editor e = getPrefs.edit();

    // If we've never stored a tour data version, use the remote one as the local version
    if( !getPrefs.contains( "localTourdataVersion" ) ) {
      e.putString( "localTourdataVersion", getPrefs.getString( "remoteTourdataVersion", "" ) ).apply();
      updateListener.newTourdataAvailable();
      singlepage.INSTANCE.versionUpdate(true);
      return true;
    }*/

    updateListener.noNewTourdataAvailable();
    return false;
  }



  // Uses AsyncTask to create a task away from the main UI thread. This task takes a
  // URL string and uses it to create an HttpUrlConnection. Once the connection
  // has been established, the AsyncTask downloads the contents of the webpage as
  // an InputStream. Finally, the InputStream is converted into a string, which is
  // displayed in the UI by the AsyncTask's onPostExecute method.
  private class DownloadTourdataVersionTask extends AsyncTask<String, Void, String>{


    @Override
    protected String doInBackground( String... urls ) {
      // urls comes from the execute() call: urls[0] is the url.
      try {
        String result = downloadJSON( urls[0] );
        Log.d( DEBUG_TAG, "Let's see if and what we have retrieved!"  );

        Log.d( DEBUG_TAG, "Result in JSON-Format:" );
        Log.d( DEBUG_TAG, result );

        Map<String, String> gsonResult = new Gson().fromJson(result, new TypeToken<Map<String, String>>() {}.getType());

        Log.d( DEBUG_TAG, "Result parsed by GSON" );
        for( Map.Entry<String, String> entry : gsonResult.entrySet() ){
          Log.d( DEBUG_TAG, entry.getKey() + ": " + entry.getValue() );

          // Save remote tour data version
          if( entry.getKey().equals( "tourDataVersion" ) ){
            PreferenceManager.getDefaultSharedPreferences( getBaseContext() )
              .edit()
              .putString( "remoteTourdataVersion", entry.getValue() )
              .apply();
          }
        }

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences( getBaseContext() );
        if( !getPrefs.getString( "localTourdataVersion", "" ).equals( getPrefs.getString( "remoteTourdataVersion", "" ) )  ){
          singlepage.INSTANCE.versionUpdate(true);
          updateListener.newTourdataAvailable();
        }
        checkingForUpdates = false;
        return result;
      } catch (IOException e) {
        return "Unable to retrieve web page. URL may be invalid.";
      }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute( String result ) {

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



  /**
   *
   * @return true if the download was successful, false else
   */
  public boolean downloadTourlist(){
    // If the phone has no connection to the internet, tell this to the user.
    if( !isNetworkAvailable() ){
     // Toast.makeText( getApplicationContext(), "Can't download tourlist: No internet connection found. Please enable a connection to the internet.", Toast.LENGTH_LONG ).show();
      return false;
    }
    checkingForUpdates = true;

    final UpdateListener listener = updateListener;

    Log.d( DEBUG_TAG, "Starting download of tourlist..." );

    String url = updateServerUrl + "/tourlist";
    String destination = new File( OurStorage.get( this ).storagePath() )  + "/tourlist.xml";

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
   //     Toast statusToast = Toast.makeText( getApplicationContext(), "intentionally left blank - or, something like that", Toast.LENGTH_LONG );

        String successMessage = "Download completed!";
        String errorMessage = "Download FAILED!\n" + "Message:\n";
        @Override
        public void onDownloadComplete( DownloadRequest request ) {
          Log.d( DEBUG_TAG, successMessage  + request.getDestinationURI().toString() );

  //        progressDialog.dismiss();
          checkingForUpdates = false;
          listener.tourlistDownloaded();
        }

        @Override
        public void onDownloadFailed( DownloadRequest request, int returnCode, String returnMessage ) {
          Log.d( DEBUG_TAG, errorMessage + returnMessage + " (" + returnCode + ")" );

   //       progressDialog.dismiss();
         // Toast.makeText(con, "Beim Herunterladen der Tourdaten ist ein Fehler aufgetreten. Bitte stellen Sie sicher, dass Ihr Gerät Zugang zum Internet hat.", Toast.LENGTH_LONG).show();

          checkingForUpdates = false;
        }

        @Override
        public void onProgress( DownloadRequest request, long totalBytes, long downloadedBytes, int progress) {
   //       progressDialog.setMax((int)totalBytes);
   //       progressDialog.setProgress((int)downloadedBytes);
        }
      });
   // createProgressDialog(con, "Lade Tourliste herunter...");

    // Start the download
    this.tourlistDownloadId = downloadManager.add( downloadRequest );

    return true;
  }

  /**
   *
   * @return true if the download was successful, false else
   */
  public boolean downloadTourdata(){
    // If the phone has no connection to the internet, tell this to the user.

    if( !isNetworkAvailable() ){
   //   Toast.makeText( getApplicationContext(), "Can't download file: No internet connection found. Please enable a connection to the internet.", Toast.LENGTH_LONG ).show();
      return false;
    }
    checkingForUpdates = true;

    final UpdateListener listener = updateListener;
    Log.d( DEBUG_TAG, "Starting file download..." );

    ArrayList<String> citiesPath = new ArrayList<>();
    TourListReader tourListReader = new TourListReader(this);
    TourList tourlist = tourListReader.readTourList();

    for( Region region : tourlist.regions() ){
      for( Area area : region.areas() ){
        for( City city : area.cities() ){
         citiesPath.add(city.home());}}}

    //TODO: Works for one City only at the moment, expand it for more cities
    Uri downloadUri = null;
    for(int i=0;i<citiesPath.size();i++)
    {downloadUri = Uri.parse( updateServerUrl + citiesPath.get(i) + "/tours.zip" );}

    //Uri downloadUri = Uri.parse( updateServerUrl + "/tours/europe/germany/wuppertal"  + "/tours.zip" );
    String destination = new File( OurStorage.get( Updater.this ).storagePath() ) + "/tours.zip";
    Uri destinationUri = Uri.parse( destination );

    // Setup the download, with nice callback function on different events throughout the download
    DownloadRequest downloadRequest = new DownloadRequest( downloadUri)
      .setRetryPolicy( new DefaultRetryPolicy() )
      .setDestinationURI( destinationUri ).setPriority( DownloadRequest.Priority.LOW )
      .setStatusListener( new DownloadStatusListenerV1() {
        // Define a Toast object so we can update it and display it for as long as the onProgress-object fires
        // It's only a temporary solution during development anyways, so, don't tweak it further, e.g. it would still disappear
        // if onProgress doesn't fire for more than 3.5s due to network delay etc.
   //     Toast statusToast = Toast.makeText( getApplicationContext(), "intentionally left blank - or, something like that", Toast.LENGTH_LONG );

        String successMessage = "Download completed!";
        String errorMessage = "Download FAILED!\n" + "Message:\n";
    //    String progressMessage = "Download in progress! (";
   //     String toastText = "Tourdaten werden heruntergeladen - bitte einen Moment Geduld";
    //    Boolean updateProgress = true;

        @Override
        public void onDownloadComplete( DownloadRequest request ) {
          Log.d( DEBUG_TAG, successMessage  + request.getDestinationURI().toString() );
          // unzip
          unzipFile( request.getDestinationURI().toString() );
          //statusToast.setText( "Die Tourdaten wurden vollständig heruntergeladen." );
          //statusToast.show();

          // Save local tour data version
          SharedPreferences getPrefs = PreferenceManager
            .getDefaultSharedPreferences( getBaseContext() );

          getPrefs.edit()
                  .putString( "localTourdataVersion", getPrefs.getString( "remoteTourdataVersion", "" ) )
                  .apply();
         // progressDialog.dismiss();

          checkingForUpdates = false;
          listener.tourdataDownloaded();
        }

        @Override
        public void onDownloadFailed( DownloadRequest request, int returnCode, String returnMessage ) {
          Log.d( DEBUG_TAG, errorMessage + returnMessage + " (" + returnCode + ")" );

        //  progressDialog.dismiss();
         // Toast.makeText(con, "Beim Herunterladen der Tourdaten ist ein Fehler aufgetreten. Bitte stellen Sie sicher, dass Ihr Gerät Zugang zum Internet hat.", Toast.LENGTH_LONG).show();

          checkingForUpdates = false;
        }

        @Override
        public void onProgress( DownloadRequest request, long totalBytes, long downloadedBytes, int progress) {
         // progressDialog.setMax((int)totalBytes);
        //  progressDialog.setProgress((int)downloadedBytes);
        }
      });
   // createProgressDialog(con, "Lade Tourinformationen runter...");
    // Start the download

    // Display the message to the user for as long as the download lasts
   // Toast.makeText( getApplicationContext(), "Beginne die Tourdaten herunterzuladen...", Toast.LENGTH_LONG ).show();

    this.manifestDownloadId = downloadManager.add( downloadRequest );

    return true;
  }


  /**
   *
   * @return true if the download was successful, false else
   */
  public boolean downloadTourMedia(Tour tour, Context context){
    final Context con = context;
    checkingForUpdates = true;

    final Tour selectedTour = tour;
    final String tourslug = tour.slug();
    String path="";
    TourListReader tourListReader = new TourListReader(this);
    TourList tourlist = tourListReader.readTourList();

    //Sucht den Pfad der ausgewaehlten Tour
    for( Region region : tourlist.regions() ){
      for( Area area : region.areas() ){
        for( City city : area.cities() ){
          for(Tour tours : city.tours()){
            if(tours.slug().equals(tour.slug())){
              path = city.home();
            }}}}}
    //updateServerUrl="http://www.vierteltour.uni-wuppertal.de/files/"
    //Downloadlink wird erstellt
    Uri downloadUri = Uri.parse( updateServerUrl + path + "/" + tour.slug() + ".zip" );
    //Zielverzeichnis wird festgelegt (gleicher "path")
    String destination = new File( OurStorage.get( Updater.this ).storagePath() ) + "/" + path + "/" + tour.slug() + ".zip";
    Uri destinationUri = Uri.parse( destination );

    SharedPreferences getPrefs = PreferenceManager
      .getDefaultSharedPreferences( getBaseContext() );
    if(getPrefs.getBoolean(tour.slug()+"-zip", false))
    {unzipTour(destinationUri.toString() ,tourslug, con);
      ((MapsActivity)con).adapter.notifyDataSetChanged();}

    else{
    // If the phone has no connection to the internet, tell this to the user.
    if( !isNetworkAvailable() ){
      Toast.makeText( getApplicationContext(), "Beim Herunterladen der Tourdaten ist ein Fehler aufgetreten. Bitte stellen Sie sicher, dass Ihr Gerät Zugang zum Internet hat.", Toast.LENGTH_LONG ).show();
      return false;
    }

    Log.d( DEBUG_TAG, "Starting file download..." );


    // Setup the download, with nice callback function on different events throughout the download
    //DownloadRequest mit zusammengesetzter URL
    //initialisiert und gestartet
    DownloadRequest downloadRequest = new DownloadRequest( downloadUri)
      .setRetryPolicy( new DefaultRetryPolicy() )
      .setDestinationURI( destinationUri ).setPriority( DownloadRequest.Priority.LOW )
    .setStatusListener( new DownloadStatusListenerV1() {
        boolean checkSize=true;
        String successMessage = "Download completed!";
        String errorMessage = "Download FAILED!\n" + "Message:\n";

        @Override
        public void onDownloadComplete( DownloadRequest request ) {
          Log.d( DEBUG_TAG, successMessage  + request.getDestinationURI().toString() );
          progressDialog.setText(selectedTour.name()+"... 100%");
          progressDialog.setTextTitle("Entpacken der Tour");
          SharedPreferences getPrefs = PreferenceManager
            .getDefaultSharedPreferences( getBaseContext() );

          ((MapsActivity)con).adapter.notifyDataSetChanged();

          getPrefs.edit()
            .putBoolean( tourslug+ "-zip", true)
            .apply();
          //Entpacke die heruntergeladene Zip Datei
          //Innerhalb der Methode wird das Paket am Ende geloescht
          unzipTour(request.getDestinationURI().toString(), tourslug, con);
          //Beende Fortschrittsdialog
          progressDialog.dismiss();

          checkingForUpdates = false;

        }

        @Override
        public void onDownloadFailed( DownloadRequest request, int returnCode, String returnMessage ) {
          Log.d( DEBUG_TAG, errorMessage + returnMessage + " (" + returnCode + ")" );
          //Beende Fortschrittsdialog und gebe Fehlermeldung aus
          progressDialog.dismiss();
          if(returnCode==1004)Toast.makeText(con, "Beim Herunterladen der Tourdaten ist ein Fehler aufgetreten. Bitte stellen Sie sicher, dass Ihr Gerät Zugang zum Internet hat.", Toast.LENGTH_LONG).show();
          else if(returnCode==1008)Toast.makeText(con, "Nicht genügend Speicherplatz auf dem Gerät vorhanden.", Toast.LENGTH_LONG).show();
          checkingForUpdates = false;
        }

        @Override
        public void onProgress( DownloadRequest request, long totalBytes, long downloadedBytes, int progress) {
          //Aktualisiere Fortschrittsdialog
              if(checkSize && totalBytes>bytesAvailable(Environment.getExternalStorageDirectory())){
                checkSize=false;
                request.cancel();
              }
              else if(checkSize){checkSize=false;}
              progressDialog.getProgressBar().setMax((int)totalBytes);
              progressDialog.getProgressBar().setProgress((int)(downloadedBytes));
              progressDialog.setText(selectedTour.name()+"... "+(downloadedBytes*100/totalBytes)+"%");
       }
      });
      createProgressDialog(context, tour.name()+"... 0%");
     // ShowCustomProgressBarAsyncTask progressdialog = new ShowCustomProgressBarAsyncTask();
     // progressdialog.execute();

    // Start the download

    // Display the message to the user for as long as the download lasts
    // Toast.makeText( getApplicationContext(), "Beginne die Tourdaten herunterzuladen...", Toast.LENGTH_LONG ).show();

    this.manifestDownloadId = downloadManager.add( downloadRequest );


  }
    return true;}

 /* public class ShowCustomProgressBarAsyncTask extends AsyncTask<Void, Integer, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
      progressDialog.setTextTitle("Entpacken der Tour");
    }

    @Override
    protected Void doInBackground(Void... params) {
      System.out.println(progressDialog.getMax() + "   " + progressDialog.getProgress());
      while(progressDialog.getMax()!=progressDialog.getProgress())
      {System.out.println(progressDialog.getMax() + "   " + progressDialog.getProgress());
        publishProgress(progressDialog.getProgress());
        //progressDialog.setText(singlepage.INSTANCE.selectedTour().name()+"... "+(progressDialog.getProgress()*100/progressDialog.getMax())+"%");
      }
      return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      progressDialog.setProgress(values[0]);
      progressDialog.setSecondaryProgress(values[0] + 5);
    }
  }*/

  private void unzipTour(String path, String tourslug, Context context)
  {if(unzipFile( path )){
    SharedPreferences getPrefs = PreferenceManager
      .getDefaultSharedPreferences( getBaseContext() );
    //Setze Tour und Einleitung in SharedPreferences,
    //damit diese freigeschaltet werden
    getPrefs.edit()
      .remove(tourslug+"-zip")
      .putBoolean( tourslug, true)
      .putBoolean( "einleitung-"+tourslug, true)
      .apply();}
  else{Toast.makeText(context, "Nicht genügend Speicher, um die Tour zu entpacken.", Toast.LENGTH_LONG).show();}}

  /**
   * Unzip a .zip compressed file into the same destination where the .zip file is in.
   *
   * The folder name where the content from the .zip file are extracted into has the same name
   * as the .zip file itself, without it's extension.
   *
   * Example:
   * /location/of/zip/file/unzip_me.zip -> /location/of/zip/file/unzip_me
   *
   * @param zipPath - The destination of our ZIP-File
   */
  private boolean unzipFile( String zipPath ){
    try {
      idsd++;
      // Determine output folder

      File zipFile = new File( zipPath );
      File outputFolder = new File( zipFile.getParentFile().getAbsolutePath() );

      Log.d( DEBUG_TAG, "Output folder:");
      Log.d( DEBUG_TAG, outputFolder.getAbsolutePath() );

      ZipFile zip = new ZipFile( zipPath );

      zip.extractAll( outputFolder.getAbsolutePath() );

      // Remove .zip after unzipping
      zipFile.delete();

      return true;
    } catch( ZipException e ){
      e.printStackTrace();
      return false;
    }
  }

  public static long bytesAvailable(File f) {
    StatFs stat = new StatFs(f.getPath());
    long bytesAvailable = 0;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
      bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
    else
      bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
    return bytesAvailable;
  }

  /**
   * Set a listener from e.g. an activity so we can inform the activity about finished updates
   *
   * @param listener
   */
  public void updateListener( UpdateListener listener ){
    updateListener = listener;
  }


  public void createProgressDialog(final Context context, String text)
  {
    // declare the dialog as a member field of your activity
    progressDialog = new CustomProgressDialog(context, downloadManager);
    progressDialog.show();
    progressDialog.setText(text);
    progressDialog.setTextTitle("Laden der Tour");
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

    progressDialog.getBtnx().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        progressDialog.onBackPressed();
      }
    });

    }


}
