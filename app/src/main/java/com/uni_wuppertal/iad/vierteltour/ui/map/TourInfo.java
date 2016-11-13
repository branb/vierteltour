package com.uni_wuppertal.iad.vierteltour.ui.map;

import android.content.Context;

import org.simpleframework.xml.Root;

@Root( name = "tour" )
class TourInfo extends TourListData{

  public TourInfo(){ super(); }

  private final static String routesGpxFile = "routes.gpx";

  private Route route;


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
