package com.uni_wuppertal.iad.vierteltour.utility.waypoints;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * This class holds the actual waypoint coordinates of a route segment
 *
 * It's values are read from a GPX file by the GpxReader class
 */

@Root( name = "trkpt" )
public class RouteWaypoint{
  public RouteWaypoint(){ super(); }


  @Attribute( name = "lat" )
  private Double latitude;

  @Attribute( name = "lon" )
  private Double longitude;

  @Element( name = "ele", required = false )
  private Double element;



  public Double latitude(){
    return latitude;
  }

  public Double longitude(){
    return longitude;
  }

  public Double element(){
    return element;
  }
}
