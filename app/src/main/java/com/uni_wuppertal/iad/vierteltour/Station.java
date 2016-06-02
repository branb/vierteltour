package com.uni_wuppertal.iad.vierteltour;

import com.google.android.gms.maps.model.LatLng;

import java.util.StringTokenizer;

public class Station{
  public String id;
  public String title;
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
    title = t;
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
    return id + "\n" + title + "\n" + number + "\n" + description + "\n" + image + "\n" + video + "\n" + audio + "\n" + coordinates + "\n";
  }
}
