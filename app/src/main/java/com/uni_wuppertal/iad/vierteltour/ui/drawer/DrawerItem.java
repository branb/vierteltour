package com.uni_wuppertal.iad.vierteltour.ui.drawer;

import android.graphics.drawable.Drawable;

/**
 * One Element of the Drawerlist
 */

public class DrawerItem{
  private String title;

  public DrawerItem(String title){
    this.title = title;
  }

  public String getTitle(){
    return title;
  }

  public void setTitle( String title ){
    this.title = title;
  }

}
