package com.uni_wuppertal.iad.vierteltour.utility;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.uni_wuppertal.iad.vierteltour.ui.map.RouteWaypoint;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourList;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourOld;
import com.uni_wuppertal.iad.vierteltour.ui.map.Station;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class XmlParser{

  private final static String DEBUG_TAG = "XmlParser";
  private Context context;

  public int trkid;
  public String name;
  public String slug;
  public String author;
  public String description;
  public String length;
  public String time;
  public String image;
  public String color;
  public String id;
  public String title;
  public String number;
  public String video;
  public String audio;
  public String coordinates;

  public XmlPullParser parser;
  public XmlPullParserFactory parserFactory;
  public String text;
  public TourOld tourOld;
  public List<TourOld> listTouren;
  public TourInfo tourInfo;
  public List<Station> stations;
  public List<LatLng> track;

  public TourList tourlist;

  public XmlParser( FragmentActivity context, TourList tourlist ){
    this.context = context;

    FileInputStream inputStream = OurStorage.getInstance( context )
                                            .getFile( "tour.xml" );

    this.tourlist = tourlist;

    listTouren = new Vector<>();
    try{
      parserFactory = XmlPullParserFactory.newInstance();
      parser = parserFactory.newPullParser();
      parser.setInput( inputStream, null );

      int eventType = parser.getEventType();
      while( eventType != XmlPullParser.END_DOCUMENT ){
        if( eventType == XmlPullParser.START_TAG ){
          switch( parser.getName() ){
            case ("tour"):
              tourOld = new TourOld();
              stations = new Vector<>();
              break;
            case ("info"):
              tourInfo = new TourInfo();
              break;
          }

          eventType = parser.next();

          if( eventType == XmlPullParser.TEXT ){
            text = parser.getText();
          }
        }
        if( eventType == XmlPullParser.END_TAG ){
          switch( parser.getName() ){
            // Tour
            case ("trkid"):
              trkid = Integer.parseInt( text );
              break;
            // TourInfo
            case ("name"):
              name = text;
              break;
            case ("slug"):
              slug = text;
              break;
            case ("author"):
              author = text;
              break;
            case ("description"):
              description = text;
              break;
            case ("length"):
              length = text;
              break;
            case ("time"):
              time = text;
              break;
            case ("image"):
              image = text;
              break;
            case ("color"):
              color = text;
              break;
            // construct TourInfo
            case ("info"):
              tourInfo = new TourInfo( name, slug, author, description, length, time, image, color );
              break;

            // Station
            case ("id"):
              id = text;
              break;
            case ("title"):
              title = text;
              break;
            case ("number"):
              number = text;
              break;
            case ("video"):
              video = text;
              break;
            case ("audio"):
              audio = text;
              break;
            case ("coordinates"):
              coordinates = text;
              break;
            // construct Station and add new Station to List
            case ("station"):
              Station station = new Station( id, title, number, description, image, video, audio, coordinates );
              stations.add( station );
              break;
            case ("tour"):
              tourOld = new TourOld( tourInfo, stations, trkid, context );
              listTouren.add( tourOld );
              parseTrack( tourOld );
              break;
          }

          text = "";
        }
        eventType = parser.next();
      }
    } catch( XmlPullParserException | IOException e ){
      Log.d( DEBUG_TAG, e.toString() );
    }

  }

  public void parseTrack( TourOld t_old ){
    for( RouteWaypoint wp : tourlist.tour( t_old.info.slug ).route( context ).segments().get( 0 ).waypoints() ){
      LatLng latlng = new LatLng( wp.latitude(), wp.longitude() );
      t_old.track.add( latlng );
    }
  }

}
