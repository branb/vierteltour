package com.uni_wuppertal.iad.vierteltour.utility;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;

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
      System.out.println( "(OurStorage) External storage mounted at: " + externalFilesDir );
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
      System.out.println( "External Storage not available! " );
      return false;
    }
  }


  /** Return a usable FileInputStream if the file was found on external storage
   *
   * @param path Path to the file, relative to the applications default (external) storage root
   * @return a FileInputStream if the file was found, null else
   */
  public FileInputStream getFile( String path ){
    File file = new File( externalFilesDir, path );
    FileInputStream stream = null;

    if( isExternalStorageMounted() && file.exists() ){
      System.out.println( "(OurStorage) File found: " + file.toString() );

      try{
        stream = new FileInputStream( file );
      } catch( FileNotFoundException ex ){
        System.out.println( "(OurStorage) File not found: " + file.toString() );
      }
    }

    return stream;
  }

}
