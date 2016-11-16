package com.uni_wuppertal.iad.vierteltour.ui.map;

import com.google.android.gms.maps.model.LatLng;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

@Root( name = "station" )
public class Station extends TourListData{

  @Attribute
  public String id;

  @Attribute
  public int number;

  @Element
  public String description;

  @ElementList( required = false )
  private List<String> images;

  @ElementList
  private List<String> videos;

  @Attribute
  public String audio;

  @Attribute
  public String coordinates;

  public Station(){ super(); }

  public Station( String id, String t, int n, String d, String a, String c ){
    this.id = id;
    name = t;
    number = n;
    description = d;
    audio = a;
    coordinates = c;
  }


  /**
   * Retrieve the LatLng object for this station
   *
   * @return LatLng
   */
  public LatLng latlng(){
    StringTokenizer tok = new StringTokenizer( coordinates, "," );

    return new LatLng( Double.parseDouble( tok.nextToken() ), Double.parseDouble( tok.nextToken() ) );
  }



  // TODO: REFACTORING: Temporary function, can be removed after refactoring the whole XmlParsing thing
  public String imagesToString(){
    if( images == null )
      images = new Vector<>();

    return listToString( images );
  }



  // TODO: REFACTORING: Temporary function, can be removed after refactoring the whole XmlParsing thing
  public String videosToString(){
    if( videos == null )
      videos = new Vector<>();

    return listToString( videos );
  }



  // TODO: REFACTORING: Temporary function, can be removed after refactoring the whole XmlParsing thing
  /**
   * Converts a List<String> to a comma seperated String,
   *
   * @param list
   * @return
   */
  private String listToString( List<String> list ){
    String s = "";

    for( String l : list ){
      s += l + ",";
    }

    if( list.size() > 1 )
      return s.replaceAll( ",$", "" );
    else
      return s;
  }



  @Override
  public String toString(){
    return id + "\n" + name + "\n" + number + "\n" + description + "\n" + imagesToString() + "\n" + videosToString() + "\n" + audio + "\n" + coordinates + "\n";
  }
}
