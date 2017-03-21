package com.uni_wuppertal.iad.vierteltour.ui.drawer;

/**
 * One Element of the Drawerlist
 */

public class DrawerItem{
  private String title;
  private int icon;

  public DrawerItem( String title, int icon ){
    this.title = title;
    this.icon = icon;
  }

  public String getTitle(){
    return title;
  }

  public void setTitle( String title ){
    this.title = title;
  }

  public int getIcon(){
    return icon;
  }

  public void setIcon( int icon ){
    this.icon = icon;
  }

}
