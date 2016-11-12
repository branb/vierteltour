package com.uni_wuppertal.iad.vierteltour.ui.map;

public class StationInfo{
  public String name;
  public String author;
  public String description;
  public String length;
  public String time;
  public String image;
  public String color;

  public StationInfo(){
  }

  public StationInfo( String n, String a, String d, String l, String t, String i, String c ){
    name = n;
    author = a;
    description = d;
    length = l;
    time = t;
    image = i;
    color = c;
  }

  @Override
  public String toString(){
    return name + "\n" + author + "\n" + description + "\n" + length + "\n" + time + "\n" + image + "\n" + color + "\n";
  }
}
