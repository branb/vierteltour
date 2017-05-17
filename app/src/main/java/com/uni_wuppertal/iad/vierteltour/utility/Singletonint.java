package com.uni_wuppertal.iad.vierteltour.utility;

import com.uni_wuppertal.iad.vierteltour.utility.xml.Station;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Tour;

import java.util.ArrayList;

/**
 * Created by Kevin-Laptop on 09.06.2016.
 * Verwaltet Variablen, welche Applikationsweit genutzt werden
 */
public enum Singletonint {
    INSTANCE;

    private boolean isAudio, versionUpdate=false;
    private int videotime;    //Videozeitpunkt einspeichern.
    private int id;     //Zur Zeit nur Nummer der Station, soll später zu ID werden
    private int position;   //Position des GalleryPagers
    private Tour selectedTour;  //Ausgewählte Tour
    private Station selectedStation;   //Ausgewählte Station
    private Station selectedOldStation; //Zuletzt ausgewählte Station (wird nur im StationFragment geändert)
    private int onfragmentclicked;  //Bei Stationenübersicht kann im Viewpager in die Lücke geklickt werden, was zu einer fehlerhaften Navigation führt.
    private ArrayList<Integer> countWaypoints = new ArrayList<Integer>();

    Singletonint(){
      id=-1;
      selectedTour=null;
      selectedStation=null;
      selectedOldStation=null;
      onfragmentclicked=-1;
      videotime=0;
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

    public int onfragmentclicked() {return onfragmentclicked;}
    public void onfragmentclicked(int onfragmentclicked){this.onfragmentclicked=onfragmentclicked;}

   public boolean versionUpdate(){return versionUpdate;}
   public void versionUpdate(boolean versionUpdate){this.versionUpdate = versionUpdate;}

    public boolean isAudio() {return isAudio;}
    public void isAudio(boolean isAudio) {this.isAudio=isAudio;}

    public int videotime(){return videotime;}
    public void videotime(int videotime){this.videotime=videotime;}

    public ArrayList<Integer> countWaypoints() {return countWaypoints;}

  }
