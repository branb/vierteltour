package com.uni_wuppertal.iad.vierteltour.utility;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.uni_wuppertal.iad.vierteltour.ui.map.RouteWaypoint;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourDetails;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourList;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourOld;
import com.uni_wuppertal.iad.vierteltour.ui.map.Station;

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

  public String slug;
  public String description;
  public String image;
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
  public TourDetails tourInfo;
  public List<Station> stations;
  public List<LatLng> track;

  private TourList tourlist;

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
              tourInfo = new TourDetails();
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
            // TODO: Remove this after refactoring as well. I just need it to figure out step by step why the app is crashing when replacing XmlParser completely at once
            case ("slug"):
              slug = text;
              break;
            // Station
            // TODO: Remove this after refactoring as well. I just need it to figure out step by step why the app is crashing when replacing XmlParser completely at once
            case ("number"):
              number = text;
              break;
            // TODO: Replace the image parsing in InformationActivity (imagesFromXML.isEmpty() etc)
            // construct Station and add new Station to List
            case ("station"):
              Log.d( DEBUG_TAG, "Adding '" + tourlist.tour(slug).station( Integer.parseInt( number ) ).name() + "' to '" + tourlist.tour(slug).name() + "'" );
              stations.add( tourlist.tour(slug).station(Integer.parseInt(number)) );
              break;
            case ("tour"):
              tourOld = new TourOld( tourlist.tour(slug).details(), stations, tourlist.tour(slug).details().trkid(), context );
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
    for( RouteWaypoint wp : tourlist.tour( t_old.info.slug() ).route( context ).segments().get( 0 ).waypoints() ){
      LatLng latlng = new LatLng( wp.latitude(), wp.longitude() );
      t_old.track.add( latlng );
    }
  }

}
