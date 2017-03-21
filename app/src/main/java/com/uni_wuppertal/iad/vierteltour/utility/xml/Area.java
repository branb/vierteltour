package com.uni_wuppertal.iad.vierteltour.utility.xml;

import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourListData;
import com.uni_wuppertal.iad.vierteltour.utility.xml.City;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "area" )
public class Area extends TourListData {
  public Area(){ super(); }

  @ElementList( name = "city", inline = true )
  private List<City> cities;

  public List<City> cities() {
    return cities;
  }

}
