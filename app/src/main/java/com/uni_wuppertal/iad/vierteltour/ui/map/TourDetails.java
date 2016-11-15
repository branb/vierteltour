package com.uni_wuppertal.iad.vierteltour.ui.map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "tour" )
public class TourDetails extends TourListData{

  @Attribute
  public String author;

  @Element
  public String description;

  @Attribute
  public String length;

  @Attribute
  public String time;

  @Attribute
  public String image;

  @Attribute
  public String color;

  public TourDetails(){ super(); }

  public TourDetails( String n, String s, String a, String d, String l, String t, String i, String c ){
    name = n;
    slug = s;
    author = a;
    description = d;
    length = l;
    time = t;
    image = i;
    color = c;
  }

  @Attribute
  private String version;

  @Attribute
  private int trkid;

  @ElementList( name = "station", inline = true )
  private List<Station> stations;


  public List<Station> stations(){
    return stations;
  }

  public String version(){
    return version;
  }

  public int trkid() { return trkid; }

  @Override
  public String toString(){
    return name + "\n" + slug + "\n" + author + "\n" + description + "\n" + length + "\n" + time + "\n" + image + "\n" + color + "\n";
  }
}
