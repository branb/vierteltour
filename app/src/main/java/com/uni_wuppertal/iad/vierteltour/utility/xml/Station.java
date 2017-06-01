package com.uni_wuppertal.iad.vierteltour.utility.xml;

import com.google.android.gms.maps.model.LatLng;
import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourListData;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

@Root( name = "station" )
public class Station extends TourListData {

  public Station(){ super(); }

  @Attribute
  private String id;

  @Attribute
  private int number;

  @Element
  private String description;

  @ElementList( required = false )
  private ArrayList<Resource> resources;

  @Attribute
  private String audio;

  @Attribute
  private String coordinates;



  public String id(){
    return id;
  }


  public int number(){
    return number;
  }


  public String description(){
    if(description!=null)return description;
    else return "";
  }

  public String audio(){
    return home() + audio;
  }



  /**
   * Retrieve the LatLng object for this station
   *
   * @return LatLng
   */
  public LatLng latlng(){
    if(coordinates==null || coordinates.isEmpty())
    {return null;}
    StringTokenizer tok = new StringTokenizer( coordinates, "," );

    return new LatLng( Double.parseDouble( tok.nextToken() ), Double.parseDouble( tok.nextToken() ) );
  }

  public ArrayList<Resource> resources(){
    if( resources == null )
      resources = new ArrayList<Resource>();

    return resources;
  }



  @Override
  public String toString(){
    return id + "\n" + name() + "\n" + number + "\n" + description + "\n" + audio + "\n" + coordinates + "\n";
  }


}
