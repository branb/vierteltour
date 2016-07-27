package com.uni_wuppertal.iad.vierteltour.utility;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class OurStorage extends ContextWrapper{
  private static OurStorage ourInstance;
  /**
   * Singleton-method
   *
   * @return OurStorage a static singleton of this class
   */
  public static OurStorage getInstance( Context context){
    if( ourInstance == null) {
      ourInstance = new OurStorage( context );
    }

    return ourInstance;
  }


  private File externalFilesDir;



  /** The main constructor.
   *
   *  Checks if the external storage medium is available and writable
   *  and saves the directory path
   */
  private OurStorage( Context base ){
    super(base);

    if( isExternalStorageMounted() ) {
      externalFilesDir = getExternalFilesDir( null );
      Log.d( "OurStorage", "External storage mounted at: " + externalFilesDir );
    }
  }


  /** Checks if the external storage is mounted
   *
   * @return true if external storage is mounted, false else
   */
  private boolean isExternalStorageMounted(){
    String state = Environment.getExternalStorageState();

    if( Environment.MEDIA_MOUNTED.equals( state ) ){
      return true;
    } else {
      // TODO: Throw exception and inform user about this
      // TODO: For some weird reason, if you use this method in a context with an invalid filename, the return statement will be executed but NOT the Log.e one. Neither should the mounted state be affected by an invalid filename nor should the Log.e statement be skipped - makes no sense at all.
      Log.e( "OurStorage", "External Storage not available!" );
      return false;
    }
  }


  /** Return a usable FileInputStream if the file was found on external storage
   *
   * @param path Path to the file, relative to the applications default (external) storage root
   * @return a FileInputStream if the file was found, null else
   */
  public FileInputStream getFile( String path ){
    if( !isFileAccessible( path ) ){
      return null;
    }

    File file = new File( externalFilesDir, path );
    FileInputStream stream = null;

    try{
      stream = new FileInputStream( file );

      Log.d( "OurStorage", "File found: " + file.toString() );
    } catch( FileNotFoundException ex ){
      // If - for whatever reason - our previous check failed and the file REALLY can't be found
      Log.wtf( "OurStorage|Exception", "File '" + file.toString() + "' REALLY not found! DaFUQ?" );
    }

    return stream;
  }


  /** Returns the absolute filepath to the given filename, the latter including subfolders
   *
   * @param path
   * @return The absolute path if the file was found, null else
   */
  public String getPathToFile( String path ){
    String pathToFile;

    if( !isFileAccessible( path ) ){
      return null;
    } else {
      File file = new File( externalFilesDir, path );
      pathToFile = file.getAbsolutePath();
      Log.d( "OurStorage", "File found: " + file.toString() );
    }

    return pathToFile;
  }


  /**
   * Helper method to check if a file exists and is readable. Only for internal use.
   *
   * @param path The path to our file, relative to our application storage
   * @return true if application storage is available and file exists / is readable, false else
   */
  protected boolean isFileAccessible( String path ){
    File file = new File( externalFilesDir, path );

    if( !isExternalStorageMounted() ){
      Log.e( "OurStorage", "External Storage not available!" );
      return false;
    }

    if( !file.exists() ){
      Log.e( "OurStorage", "File '" + file.toString() + "' not found!" );
      return false;
    }

    if( !file.canRead() ){
      Log.e( "OurStorage", "Can't read '" + file.toString() + "'!" );
      return false;
    }

    return true;
  }



  /**
   * Return the external storage location on the device
   *
   * @return String our external storage path, absolute
   */
  public String getStoragePath(){
    return externalFilesDir.getAbsolutePath();

  }



}
