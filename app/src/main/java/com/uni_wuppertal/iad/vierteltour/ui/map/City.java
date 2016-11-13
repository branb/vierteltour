package com.uni_wuppertal.iad.vierteltour.ui.map;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "city" )
public class City extends TourListData{
  public City(){ super(); }


  @ElementList( name = "tour", required = false, inline = true )
  private List<TourInfo> tours;


  public List<TourInfo> tours() {
    return tours;
  }
}
