package com.uni_wuppertal.iad.vierteltour;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root( name = "tour" )
class TourInfo {

  public TourInfo(){};

  @Attribute
  private String name;

  @Attribute
  private String slug;

  public String getName() {
    return name;
  }

  public String getSlug() {
    return slug;
  }
}
