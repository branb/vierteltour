package com.uni_wuppertal.iad.vierteltour.utility;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.model.LatLng;
import com.uni_wuppertal.iad.vierteltour.ui.map.RouteWaypoint;
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourList;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourOld;

import java.util.List;
import java.util.Vector;

public class XmlParser{

  private final static String DEBUG_TAG = "XmlParser";
  private Context context;

  public TourOld tourOld;
  public List<TourOld> listTouren;
  public List<LatLng> track;

  public XmlParser( FragmentActivity context, TourList tourlist ){
    this.context = context;

    listTouren = new Vector<>();

    for( Tour tour : tourlist.allTours() ){
      tourOld = new TourOld( tour.details(), tour.stations(), tour.trkid(), context );
      parseTrack( tour );
      listTouren.add( tourOld );
    }

  }

  public void parseTrack( Tour t){
    for( RouteWaypoint wp : t.route().waypoints() ){
      LatLng latlng = new LatLng( wp.latitude(), wp.longitude() );
      this.tourOld.track.add( latlng );
    }
  }

}
