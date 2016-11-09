package com.uni_wuppertal.iad.vierteltour;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "area" )
public class Area {

  public Area(){};

  @ElementList( name = "city", required = false, inline = true )
  private List<City> cities;

  @Attribute
  private String name;

  @Attribute
  private String slug;

  public List<City> getCities() {
    return cities;
  }

  public String getName() {
    return name;
  }

  public String getSlug() {
    return slug;
  }
}
