package com.uni_wuppertal.iad.vierteltour.ui.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.StringTokenizer;

public class Station{
  public String id;
  public String name;
  public String slug;
  public String number;
  public String description;
  public String image;
  public String video;
  public String audio;
  public String coordinates;
  public LatLng latlng;

  public Station(){

  }

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
