package com.uni_wuppertal.iad.vierteltour.ui.map;

public class TourDetails{
  public String name;
  public String slug;
  public String author;
  public String description;
  public String length;
  public String time;
  public String image;
  public String color;

  public TourDetails(){
  }

  public TourDetails( String n, String s, String a, String d, String l, String t, String i, String c ){
    name = n;
    slug = s;
    author = a;
    description = d;
    length = l;
    time = t;
    image = i;
    color = c;
  }

  @Override
  public String toString(){
    return name + "\n" + slug + "\n" + author + "\n" + description + "\n" + length + "\n" + time + "\n" + image + "\n" + color + "\n";
  }
}
