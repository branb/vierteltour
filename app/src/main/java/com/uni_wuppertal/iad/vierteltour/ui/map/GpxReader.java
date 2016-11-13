package com.uni_wuppertal.iad.vierteltour.ui.map;

import android.content.Context;
import android.util.Log;

import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * Only a helper class that wraps the XML-reader for the GPX data.
 *
 * The class that actually holds the read data is GpxReader and it's sub-classes:
 * - Route
 * - RouteSegment
 * - RouteWaypoint
 */

public class GpxReader{
  private final static String DEBUG_TAG = "Xml/GPX";


  private Context context;


  public GpxReader( Context context ){
    this.context = context;
  }


  public Route readRoute( String fileName ){
    Serializer serializer = new Persister();

    try{
      Log.d( DEBUG_TAG, "Reading " + fileName );

      Gpx gpx = serializer.read( Gpx.class, OurStorage.getInstance( context ).getFile( fileName ), false );

      Log.d( DEBUG_TAG, gpx.route().name() + " has " + gpx.route().segments().size() + " segment(s)" );

      Integer i = 0;

      for( RouteSegment segment : gpx.route().segments() ){
        Log.d( DEBUG_TAG, "#" + ++i + " has " + segment.waypoints().size() + " waypoints" );
      }

      return gpx.route();
    } catch( Exception e ) {
      Log.d( DEBUG_TAG, e.toString() );
      return new Route();
    }

  }

}
