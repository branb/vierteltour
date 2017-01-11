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
    private Station selectedOldStation; //Zuletzt ausgewählte Station (wird nur im StationFragment geändert)
    private boolean onfragmentclicked;  //Bei Stationenübersicht kann im Viewpager in die Lücke geklickt werden, was zu einer fehlerhaften Navigation führt.

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

    public Station selectedStation() {if(selectedStation!=null)return selectedStation;
                                      else return null;}
    public void selectedStation(Station selectedStation) {this.selectedStation = selectedStation;}

    public Station selectedOldStation() {return selectedOldStation;}
    public void selectedOldStation(Station selectedOldStation) {this.selectedOldStation=selectedOldStation;}

    public boolean onfragmentclicked() {return onfragmentclicked;}
    public void onfragmentclicked(boolean onfragmentclicked){this.onfragmentclicked=onfragmentclicked;}

  }
