package com.uni_wuppertal.iad.vierteltour.utility.waypoints;

import com.uni_wuppertal.iad.vierteltour.utility.waypoints.RouteWaypoint;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

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

@Root( name = "trkseg" )
public class RouteSegment{
  public RouteSegment(){ super(); }


  @ElementList( name = "trkpt", required = true, inline = true )
  private List<RouteWaypoint> waypoints;


  public List<RouteWaypoint> waypoints(){
    return waypoints;
  }
}
