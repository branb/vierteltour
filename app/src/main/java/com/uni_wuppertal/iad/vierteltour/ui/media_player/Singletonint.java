package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import com.uni_wuppertal.iad.vierteltour.ui.map.Station;
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;

/**
 * Created by Kevin-Laptop on 09.06.2016.
 */
public enum Singletonint {
    INSTANCE;

  //Wenn Videoplayer wie Audio implementiert wird, kann time und playing gelöscht werden

    private int id;     //Zur Zeit nur nummer der Station, soll später zu ID werden
    private int position;   //Position des GalleryPagers
    private Tour selectedTour;  //Ausgewählte Tour
    private Station selectedStation;   //Ausgewählte Station

    Singletonint(){
      id=-1;
      selectedTour=new Tour();
    }

    public int getId(){
    return id;
  }
    public void setId(int id){
    this.id = id;
  }

    public int position() {return position;}
    public void position(int position) {this.position = position;}

    public Tour selectedTour() {return selectedTour;}
    public void selectedTour(Tour selectedTour) {this.selectedTour = selectedTour;}

    public Station selectedStation() {return selectedStation;}
    public void selectedStation(Station selectedStation) {this.selectedStation = selectedStation;}

  }
