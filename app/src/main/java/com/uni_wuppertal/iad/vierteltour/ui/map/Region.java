package com.uni_wuppertal.iad.vierteltour.ui.map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "region" )
class Region {

  public Region(){};

  @ElementList( name = "area", required = false, inline = true )
  private List<Area> areas;

  @Attribute
  private String name;

  @Attribute
  private String slug;

  public List<Area> getAreas() {
    return areas;
  }

  public String getName() {
    return name;
  }

  public String getSlug() {
    return slug;
  }
}
