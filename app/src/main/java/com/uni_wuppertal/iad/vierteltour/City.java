package com.uni_wuppertal.iad.vierteltour;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "city" )
public class City {

  public City(){};

  @ElementList( name = "tour", required = false, inline = true )
  private List<TourInfo> tours;

  @Attribute
  private String name;

  @Attribute
  private String slug;

  public List<TourInfo> getTours() {
    return tours;
  }

  public String getName() {
    return name;
  }

  public String getSlug() {
    return slug;
  }
}
