package com.uni_wuppertal.iad.vierteltour.ui.media_player;

/**
 * Created by Kevin-Laptop on 09.06.2016.
 */
public enum Singletonint {
    INSTANCE;

    private int page;
    private Singletonint(){
      page = 0;
    }

    public int getPage(){
      return page;
    }
    public void setPage(int page){
      this.page = page;
    }


}
