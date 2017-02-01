package com.uni_wuppertal.iad.vierteltour.ui.map;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root( name = "region" )
public class Region extends TourListData{
  public Region(){ super(); }

  @ElementList( name = "area", inline = true )
  private List<Area> areas;

  public List<Area> areas() {
    return areas;
  }
}
