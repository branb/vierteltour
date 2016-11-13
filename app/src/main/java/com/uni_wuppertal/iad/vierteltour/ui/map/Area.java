package com.uni_wuppertal.iad.vierteltour.ui.map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "area" )
public class Area {
  public Area(){ super(); }

  @ElementList( name = "city", inline = true )
  private List<City> cities;

  @Attribute
  private String name;

  @Attribute
  private String slug;

  public List<City> cities() {
    return cities;
  }

  public String name() {
    return name;
  }

  public String slug() {
    return slug;
  }
}
