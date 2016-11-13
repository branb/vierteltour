package com.uni_wuppertal.iad.vierteltour.ui.map;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "area" )
public class Area extends TourListData{
  public Area(){ super(); }

  @ElementList( name = "city", inline = true )
  private List<City> cities;

  public List<City> cities() {
    return cities;
  }

}
