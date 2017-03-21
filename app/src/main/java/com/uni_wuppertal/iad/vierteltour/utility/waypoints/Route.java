package com.uni_wuppertal.iad.vierteltour.utility.waypoints;

import com.google.android.gms.maps.model.LatLng;
import com.uni_wuppertal.iad.vierteltour.utility.waypoints.RouteSegment;
import com.uni_wuppertal.iad.vierteltour.utility.waypoints.RouteWaypoint;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.Vector;

/**
 * This class holds sets (called: segments) of waypoint coordinates of a route.
 *
 * At the moment, there is only one segment describing the path from the first station
 * of a route to the last station of it.
 *
 * With segments, you could actually split it up so that each segment describes the subroute between
 * each station and it's successor.
 *
 * It's values are read from a GPX file by the GpxReader class
 */

@Root( name = "trk" )
public class Route{
  public Route(){ super(); }


  @Element( name = "name", required = true )
  private String name;

  @ElementList( name = "trkseg", required = true, inline = true )
  private List<RouteSegment> segments;



  public String name() {
    return name;
  }

  public List<RouteSegment> segments() {
    return segments;
  }


  /**
   * Return the waypoints of all segments at once
   *
   * @return waypoints A List<RouteWaypoints>
   */
  public List<RouteWaypoint> waypoints(){
    List<RouteWaypoint> waypoints = new Vector<>();

    for( RouteSegment segment : segments ){
      for( RouteWaypoint waypoint : segment.waypoints() ){
        waypoints.add( waypoint );
      }
    }

    return waypoints;
  }


  /**
   * Return the LatLngs of all segments at once
   *
   * @return latlngs A List<LatLng>
   */
  public List<LatLng> latLngs(){
    List<LatLng> latlngs = new Vector<>();

    for( RouteWaypoint waypoint : waypoints() ){
      latlngs.add( new LatLng( waypoint.latitude(), waypoint.longitude() ) );
    }

    return latlngs;
  }
}
