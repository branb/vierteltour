package com.uni_wuppertal.iad.vierteltour.ui.map;

import java.util.List;

public class TourOld{
  public TourDetails info;
  public List<Station> stations;


  public TourOld( TourDetails i, List<Station> s ){
    info = i;
    stations = s;
  }
}
