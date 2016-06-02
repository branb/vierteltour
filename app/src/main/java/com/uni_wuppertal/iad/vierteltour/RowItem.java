package com.uni_wuppertal.iad.vierteltour;

//Definiert eine Klasse mit wichtigen Variablen pro Zeile der Liste
public class RowItem{

  private String subtitle1, subtitle2, addinfo, title;
  private int icon;
  private boolean isSelected;

  public RowItem( String title, String subtitle1, String subtitle2, int icon, String addinfo ){
    this.title = title;
    this.icon = icon;
    this.subtitle1 = subtitle1;
    this.subtitle2 = subtitle2;
    this.addinfo = addinfo;
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

  public String getSubtitle1(){
    return subtitle1;
  }

  public void setSubtitle1( String subtitle1 ){
    this.subtitle1 = subtitle1;
  }

  public String getSubtitle2(){
    return subtitle2;
  }

  public void setSubtitle2( String subtitle2 ){
    this.subtitle2 = subtitle2;
  }

  public String getAddinfo(){
    return addinfo;
  }

  public void setAddinfo( String addinfo ){
    this.addinfo = addinfo;
  }

  public boolean isSelected(){
    return isSelected;
  }

  public void setSelected( boolean isSelected ){
    this.isSelected = isSelected;
  }
}
