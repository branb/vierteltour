package com.uni_wuppertal.iad.vierteltour.ui.map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "tour" )
public class TourDetails extends TourListData{

  public TourDetails(){ super(); }

  @Attribute
  private String version;

  @Attribute
  private int trkid;

  @Attribute
  private String author;

  @Element
  private String description;

  @Attribute
  private String length;

  @Attribute
  private String time;

  @Attribute
  private String image;

  @Attribute
  private String color;

  @ElementList( name = "station", inline = true )
  private List<Station> stations;



  public String version(){
    return version;
  }


  public int trkid(){
    return trkid;
  }


  public String author(){
    return author;
  }


  public String description(){
    return description;
  }


  public String length(){
    return length;
  }


  public String time(){
    return time;
  }


  public String image(){
    return image;
  }


  public String color(){
    return color;
  }


  public List<Station> stations(){
    return stations;
  }


  @Override
  public String toString(){
    return name() + "\n" + slug() + "\n" + author + "\n" + description + "\n" + length + "\n" + time + "\n" + image + "\n" + color + "\n";
  }
}
