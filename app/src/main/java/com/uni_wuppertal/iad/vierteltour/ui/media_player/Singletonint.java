package com.uni_wuppertal.iad.vierteltour.ui.media_player;

/**
 * Created by Kevin-Laptop on 09.06.2016.
 */
public enum Singletonint {
    INSTANCE;

  //TODO: überprüfe ob variablen noch notwendig

    private int page;
    private int id;
    private double time;
    private boolean playing;
    private Singletonint(){
      page = 0;
      time = 0;
      playing = false;
      id=-1;
    }

    public boolean getPlaying() {return playing;}
    public void setPlaying(boolean p)  {this.playing = p;}

    public double getTime() {return time;}
    public void setTime(double time) {this.time = time;}

    public int getPage(){
      return page;
    }
    public void setPage(int page){
      this.page = page;
    }

    public int getId(){
    return id;
  }
    public void setId(int id){
    this.id = id;
  }

    public void reset()
    {page = 0;
     time = 0;
     playing = false;}


}
