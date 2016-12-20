package com.uni_wuppertal.iad.vierteltour.ui.media_player;

/**
 * Created by Kevin-Laptop on 09.06.2016.
 */
public enum Singletonint {
    INSTANCE;

  //Wenn Videoplayer wie Audio implementiert wird, kann time und playing gelöscht werden

    private int id;     //Zur Zeit nur nummer der Station, soll später zu ID werden
    private int position;
    Singletonint(){
      id=-1;
    }

    public int getId(){
    return id;
  }
    public void setId(int id){
    this.id = id;
  }

    public int getPosition() {return position;}
    public void setPosition(int position) {this.position = position;}
  }
