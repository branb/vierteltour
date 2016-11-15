package com.uni_wuppertal.iad.vierteltour.ui.map;

import com.google.android.gms.maps.model.LatLng;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.StringTokenizer;

@Root( name = "station" )
public class Station extends TourListData{

  @Attribute
  public String id;
  @Attribute
  public String number;
  @Element
  public String description;

  @ElementList( required = false )
  private List<String> images;

  public String image;

  @ElementList
  private List<String> videos;

  public String video;

  @Attribute
  public String audio;

  @Attribute
  public String coordinates;

  public LatLng latlng;

  public Station(){ super(); }

  public Station( String id, String t, String n, String d, String i, String v, String a, String c ){
    this.id = id;
    name = t;
    number = n;
    description = d;
    image = i;
    video = v;
    audio = a;
    coordinates = c;

    StringTokenizer tok = new StringTokenizer( coordinates, "," );

    latlng = new LatLng( Double.parseDouble( tok.nextToken() ), Double.parseDouble( tok.nextToken() ) );
  }


  @Override
  public String toString(){
    return id + "\n" + name + "\n" + number + "\n" + description + "\n" + image + "\n" + video + "\n" + audio + "\n" + coordinates + "\n";
  }
}
