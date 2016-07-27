package com.uni_wuppertal.iad.vierteltour.updater;


import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


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


    return true;
  }



}
