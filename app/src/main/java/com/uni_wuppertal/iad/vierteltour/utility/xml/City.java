package com.uni_wuppertal.iad.vierteltour.utility.xml;

import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourListData;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Tour;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "city" )
public class City extends TourListData {
  public City(){ super(); }


  @ElementList( name = "tour", required = false, inline = true )
  private List<Tour> tours;


  public List<Tour> tours() {
    return tours;
  }
}
