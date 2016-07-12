package com.uni_wuppertal.iad.vierteltour.ui.media_player;

/**
 * Created by Kevin-Laptop on 09.06.2016.
 */
public enum Singletonint {
    INSTANCE;

    private int page;
    private double time,timeAudio;
    private boolean playing,playingAudio;
    private Singletonint(){
      page = 0;
      time = 0;
      playing = false;
      timeAudio = 0;
      playingAudio = false;
    }

    public boolean getPlayingAudio() {return playingAudio;}
    public void setPlayingAudio(boolean p) {this.playingAudio = p;}

    public double getTimeAudio() {return timeAudio;}
    public void setTimeAudio(double time) {this.timeAudio = time;}

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

    public void reset()
    {page = 0;
     time = 0;
     playing = false;
     timeAudio = 0;
     playingAudio = false;}


}
