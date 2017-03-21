package com.uni_wuppertal.iad.vierteltour.utility.xml;

import com.google.android.gms.maps.model.LatLng;
import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourListData;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

@Root( name = "station" )
public class Station extends TourListData {

  public Station(){ super(); }

  @Attribute
  private String id;

  @Attribute
  private int number;

  @Element
  private String description;

  @ElementList( required = false )
  private List<String> images;

  @ElementList
  private List<String> videos;

  @Attribute
  private String audio;

  @Attribute
  private String coordinates;



  public String id(){
    return id;
  }


  public int number(){
    return number;
  }


  public String description(){
    return description;
  }


  public List<String> images(){
    return images;
  }


  public List<String> videos(){
    return videos;
  }


  public String audio(){
    return home() + audio;
  }



  /**
   * Retrieve the LatLng object for this station
   *
   * @return LatLng
   */
  public LatLng latlng(){
    if(coordinates==null || coordinates.isEmpty())
    {return null;}
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
      s += home() + l + ",";
    }

      return s.replaceAll( ",$", "" );

  }



  @Override
  public String toString(){
    return id + "\n" + name() + "\n" + number + "\n" + description + "\n" + imagesToString() + "\n" + videosToString() + "\n" + audio + "\n" + coordinates + "\n";
  }
}
