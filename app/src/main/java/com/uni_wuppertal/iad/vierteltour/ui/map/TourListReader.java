package com.uni_wuppertal.iad.vierteltour.ui.map;

import android.content.Context;
import android.util.Log;

import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Only a helper class that wraps the XML-reader for the XML tourlist data.
 *
 * The class that actually holds the read data is TourList and it's sub-classes:
 * - Region
 * - Area
 * - City
 */

public class TourListReader{
  private final static String DEBUG_TAG = "Xml/TourListReader";

  private final static String xmlFile = "tourlist.xml";

  /**
   * Home directory of the tours. Always *relative* to wherever you want it to be stored on the
   * device (usually, external storage).
   *
   */
  private final static String toursHome = "tours";

  private Context context;


  public TourListReader( Context context ){
    this.context = context;
  }


  public TourList readTourList(){
    Serializer serializer = new Persister();

    String xmlDataPath = OurStorage.get( context ).pathToFile( xmlFile );


    try{
      Log.d( DEBUG_TAG, "Reading " + xmlDataPath );

      TourList tourlist = serializer.read( TourList.class, OurStorage.get( context ).file( xmlFile ), false );

      Log.d( DEBUG_TAG, "Version: " + tourlist.version() );
      Log.d( DEBUG_TAG, "Found " + tourlist.regions().size() + " region(s)" );

      Integer i = 0;

      for( Region region : tourlist.regions() ){
        Log.d( DEBUG_TAG, "#" + ++i + " has " + region.areas().size() + " areas" );
      }
      // Initialize tourlist
      tourlist.init( toursHome );
      return tourlist;
    } catch( Exception e ) {
      Log.d( DEBUG_TAG, e.toString() );
      return new TourList();
    }}

  public TourList readTourData(){
    Serializer serializer = new Persister();

    String xmlDataPath = OurStorage.get( context ).pathToFile( xmlFile );


    try{
      Log.d( DEBUG_TAG, "Reading " + xmlDataPath );

      TourList tourlist = serializer.read( TourList.class, OurStorage.get( context ).file( xmlFile ), false );

      Log.d( DEBUG_TAG, "Version: " + tourlist.version() );
      Log.d( DEBUG_TAG, "Found " + tourlist.regions().size() + " region(s)" );

      Integer i = 0;

      for( Region region : tourlist.regions() ){
        Log.d( DEBUG_TAG, "#" + ++i + " has " + region.areas().size() + " areas" );
      }
      // Initialize tourlist
      tourlist.initAll( toursHome );
      return tourlist;
    } catch( Exception e ) {
      Log.d( DEBUG_TAG, e.toString() );
      return new TourList();
    }

  }

}
