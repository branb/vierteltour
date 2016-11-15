package com.uni_wuppertal.iad.vierteltour.ui.map;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

@Root( name = "tour" )
public class Tour extends TourListData{
  public Tour(){ super(); }


  private final static String DEBUG_TAG = "Xml/Tour";

  private final static String xmlFile = "details.xml";
  private final static String routesGpxFile = "routes.gpx";

  private TourDetails details;
  private Route route;


  public TourDetails details(){
    if( details != null )
      return details;

    Serializer serializer = new Persister();

    String xmlDataPath = home + "/" + xmlFile;

    try{
      Log.d( DEBUG_TAG, "Reading " + xmlDataPath );

      details = serializer.read( TourDetails.class, OurStorage.getInstance( new Application().getBaseContext() ).getFile( xmlDataPath ) , false );

      Log.d( DEBUG_TAG, "Tour '" + details.name() + "' found, Version: " + details.version() );
      Log.d( DEBUG_TAG, "It has " + details.stations().size() + " stations" );

      Integer i = 0;

      return details;
    } catch( Exception e ) {
      Log.d( DEBUG_TAG, e.toString() );
      return new TourDetails();
    }

  }


  /**
   * Reads the GPX data of this tours route (if not already read) and returns it.
   *
   * @param context
   * @return
   */
  public Route route( Context context ){
    if( route == null ){
      route = new GpxReader( context ).readRoute( home + "/" + routesGpxFile );
    }

    return route;
  }
}
