package com.uni_wuppertal.iad.vierteltour.ui.map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Class that represents the list of all available tours
 *
 */
@Root( name = "tourlist" )
public class TourList {

  public TourList(){};

  @ElementList( name = "region", required = false, inline = true )
  private List<Region> regions;

  @Attribute()
  private String version;

  public String getVersion() {
    return version;
  }

  public List<Region> getRegions() {
    return regions;
  }
}
