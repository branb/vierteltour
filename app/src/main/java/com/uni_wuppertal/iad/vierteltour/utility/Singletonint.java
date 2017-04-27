package com.uni_wuppertal.iad.vierteltour.utility;

import com.uni_wuppertal.iad.vierteltour.utility.xml.Station;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Tour;

/**
 * Created by Kevin-Laptop on 09.06.2016.
 * Verwaltet Variablen, welche Applikationsweit genutzt werden
 */
public enum Singletonint {
    INSTANCE;

    private boolean isAudio;
    private int id;     //Zur Zeit nur Nummer der Station, soll später zu ID werden
    private int position;   //Position des GalleryPagers
    private Tour selectedTour;  //Ausgewählte Tour
    private Station selectedStation;   //Ausgewählte Station
    private Station selectedOldStation; //Zuletzt ausgewählte Station (wird nur im StationFragment geändert)
    private int onfragmentclicked;  //Bei Stationenübersicht kann im Viewpager in die Lücke geklickt werden, was zu einer fehlerhaften Navigation führt.

    Singletonint(){
      id=-1;
      selectedTour=null;
      selectedStation=null;
      selectedOldStation=null;
      onfragmentclicked=-1;
    }

    public int getId(){
    return id;
  }
    public void setId(int id){
    this.id = id;
  }

    public int position() {return position;}
    public void position(int position) {this.position = position;
      System.out.println(position);}

    public Tour selectedTour() {return selectedTour;}
    public void selectedTour(Tour selectedTour) {this.selectedTour = selectedTour;}

    public Station selectedStation() {if(selectedStation!=null)return selectedStation;
                                      else return null;}
    public void selectedStation(Station selectedStation) {this.selectedStation = selectedStation;}

    public Station selectedOldStation() {return selectedOldStation;}
    public void selectedOldStation(Station selectedOldStation) {this.selectedOldStation=selectedOldStation;}

    public int onfragmentclicked() {return onfragmentclicked;}
    public void onfragmentclicked(int onfragmentclicked){this.onfragmentclicked=onfragmentclicked;}

    public boolean isAudio() {return isAudio;}
    public void isAudio(boolean isAudio) {this.isAudio=isAudio;}
  }
