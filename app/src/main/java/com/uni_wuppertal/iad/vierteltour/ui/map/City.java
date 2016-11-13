package com.uni_wuppertal.iad.vierteltour.ui.map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "city" )
public class City {
  public City(){ super(); }


  @ElementList( name = "tour", required = false, inline = true )
  private List<TourInfo> tours;

  @Attribute
  private String name;

  @Attribute
  private String slug;


  public List<TourInfo> tours() {
    return tours;
  }

  public String name() {
    return name;
  }

  public String slug() {
    return slug;
  }
}
