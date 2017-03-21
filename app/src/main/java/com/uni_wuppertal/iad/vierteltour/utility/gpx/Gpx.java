package com.uni_wuppertal.iad.vierteltour.utility.gpx;

import com.uni_wuppertal.iad.vierteltour.utility.waypoints.Route;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "gpx" )
public class Gpx{
  public Gpx(){ super(); }


  @Attribute( name = "creator", required = true )
  private String author;

  @Attribute( name = "version", required = true )
  private String version;


  @Element( name = "trk", required = true )
  private Route route;


  public String version(){
    return version;
  }

  public String author(){
    return author;
  }

  public Route route(){
    return route;
  }
}
