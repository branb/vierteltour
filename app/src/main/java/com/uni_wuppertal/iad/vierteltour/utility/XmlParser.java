package com.uni_wuppertal.iad.vierteltour.utility;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourList;
import com.uni_wuppertal.iad.vierteltour.ui.map.Station;
import com.uni_wuppertal.iad.vierteltour.ui.map.StationInfo;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

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
  public Tour tour;
  public List<Tour> ListTouren;
  public StationInfo tourInfo;
  public List<Station> stations;
  public List<LatLng> track;


  public XmlParser( FragmentActivity context ){
    this.context = context;

    FileInputStream inputStream = OurStorage.getInstance( context )
                                            .getFile( "tour.xml" );

    ListTouren = new Vector<>();
    try{
      parserFactory = XmlPullParserFactory.newInstance();
      parser = parserFactory.newPullParser();
      parser.setInput( inputStream, null );

      int eventType = parser.getEventType();
      while( eventType != XmlPullParser.END_DOCUMENT ){
        if( eventType == XmlPullParser.START_TAG ){
          switch( parser.getName() ){
            case ("tour"):
              tour = new Tour();
              stations = new Vector<>();
              break;
            case ("info"):
              tourInfo = new StationInfo();
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
              tourInfo = new StationInfo( name, author, description, length, time, image, color );
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
              tour = new Tour( tourInfo, stations, trkid, context );
              ListTouren.add( tour );
              parseTrack( context, tour );
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

  public void parseTrack( FragmentActivity context, Tour t ){
    FileInputStream inputStream = OurStorage.getInstance( context )
                                            .getFile( "track_" + t.trkid + ".gpx" );

    try{
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      XmlPullParser parser = factory.newPullParser();
      parser.setInput( inputStream, null );

      int eventType = parser.getEventType();
      while( eventType != XmlPullParser.END_DOCUMENT ){
        if( eventType == XmlPullParser.START_TAG ){
          if( parser.getName()
                    .equals( "trkpt" ) ){
            Double lat = Double.parseDouble( parser.getAttributeValue( null, "lat" ) );
            Double lon = Double.parseDouble( parser.getAttributeValue( null, "lon" ) );
            LatLng latlng = new LatLng( lat, lon );
            t.track.add( latlng );
          }
        }
        if( eventType == XmlPullParser.TEXT ){
        }
        if( eventType == XmlPullParser.END_TAG ){
        }
        eventType = parser.next();
      }
    } catch( XmlPullParserException | IOException e ){
      Log.d( DEBUG_TAG, e.toString() );
    }

  }


  public String readTourlist( String fileName ){
    Serializer serializer = new Persister();

    String result = "";

    try{
      Log.d( DEBUG_TAG, "Starting the deserialization (I hope so, at least..." );

      TourList tourlist = serializer.read( TourList.class, OurStorage.getInstance( context ).getFile( fileName ) );

      Log.d( DEBUG_TAG, tourlist.toString() );
      Log.d( DEBUG_TAG, "Version of downloaded tourlist: " + tourlist.getVersion() );

      result = tourlist.toString();
    } catch( Exception e ) {
      Log.d( DEBUG_TAG, e.toString() );
    }

    return result;
  }



}
