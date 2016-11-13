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


  private Context context;


  public TourListReader( Context context ){
    this.context = context;
  }


  public TourList readTourList( String fileName ){
    Serializer serializer = new Persister();

    try{
      Log.d( DEBUG_TAG, "Reading " + fileName );

      TourList tourlist = serializer.read( TourList.class, OurStorage.getInstance( context ).getFile( fileName ), false );

      Log.d( DEBUG_TAG, "Version: " + tourlist.version() );
      Log.d( DEBUG_TAG, "Found " + tourlist.regions().size() + " region(s)" );

      Integer i = 0;

      for( Region region : tourlist.regions() ){
        Log.d( DEBUG_TAG, "#" + ++i + " has " + region.areas().size() + " areas" );
      }

      return tourlist;
    } catch( Exception e ) {
      Log.d( DEBUG_TAG, e.toString() );
      return new TourList();
    }

  }

}
