package com.uni_wuppertal.iad.vierteltour.ui.map;

import android.app.Application;
import android.util.Log;

import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.util.List;

@Root( name = "tour" )
public class Tour extends TourListData{
  public Tour(){ super(); }


  private final static String DEBUG_TAG = "Xml/Tour";

  private final static String xmlFile = "details.xml";
  private final static String routesGpxFile = "routes.gpx";

  private TourDetails details;
  private Route route;


  /**
   * A wrapper function to load TourDetails if they are not already loaded. The actual contents are
   * accessed by special public functions
   *
   * @return
   */
  private TourDetails details(){
    if( details != null )
      return details;

    Serializer serializer = new Persister();

    String xmlDataPath = home() + "/" + xmlFile;

    try{
      Log.d( DEBUG_TAG, "Reading " + xmlDataPath );

      details = serializer.read( TourDetails.class, OurStorage.get( new Application().getBaseContext() ).file( xmlDataPath ) , false );

      Log.d( DEBUG_TAG, "Tour '" + details.name() + "' found, Version: " + details.version() );
      Log.d( DEBUG_TAG, "It has " + details.stations().size() + " stations" );

      return details;
    } catch( Exception e ) {
      Log.d( DEBUG_TAG, e.toString() );
      return new TourDetails();
    }

  }


  /**
   * Returns the version of this tours data
   *
   * @return String
   */
  public String version(){
    return details().version();
  }


  /**
   * Returns the trkid of this tour
   *
   * @return int
   */
  public int trkid(){
    return details().trkid();
  }


  /**
   * Returns the author of this tour
   *
   * @return String
   */
  public String author(){
    return details().author();
  }


  /**
   * Returns the description of this tour
   *
   * @return String
   */
  public String description(){
    return details().description();
  }


  /**
   * Returns the length of the whole tour, in kilometers
   *
   * @return String
   */
  public String length(){
    return details().length();
  }


  /**
   * Returns the approximate time it takes to listen to the complete audio guide
   *
   * @return String
   */
  public String time(){
    return details().time();
  }


  /**
   * Returns the tours associated color, as a hexadecimal value in #000000 notation
   *
   * @return String
   */
  public String color(){
    return details().color();
  }


  /**
   * Returns the authors image name of this tour.
   *
   * @return String
   */
  public String image(){
    return details().image();
  }


  /**
   * Reads the GPX data of this tours route (if not already read) and returns it.
   *
   * @return
   */
  public Route route(){
    if( route == null ){
      route = new GpxReader( new Application().getBaseContext() ).readRoute( home() + "/" + routesGpxFile );
    }

    return route;
  }


  /**
   * Returns a List<Station> of all stations of this tour
   *
   * @return List<Station>
   */
  public List<Station> stations(){
    return details().stations();
  }


  /**
   * Returns the Station with the same number or an empty Station if the number was not found
   *
   * NOTE:
   * This returns the FIRST station found. There is currently NO error handling if two or more
   * stations have the same number
   *
   * TODO: Implement error handling in case more then one station have the same number, which is only the case if it's already wrong in the source XML
   *
   * @param number as defined in the XML
   * @return the Station
   */
  public Station station( int number ){
    for( Station station : details().stations() ){
      if( station.number() == number ){
        return station;
      }
    }

    // TODO: Use exceptions rather than a simple log message
    Log.d( DEBUG_TAG, "WARNING: The tour " + name() + " does not contain a station with the number " + number );
    return new Station();
  }
}
