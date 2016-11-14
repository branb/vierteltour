package com.uni_wuppertal.iad.vierteltour.ui.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.Vector;

public class TourOld{
  public int trkid;
  public TourInfo info;
  public List<Station> stations;
  public List<LatLng> track;
  public PolylineOptions polylines;
  public List<MarkerOptions> ListMarker;


  public TourOld(){
    track = new Vector<>();
  }

  public TourOld( TourInfo i, List<Station> s ){
    info = i;
    stations = s;
  }

  public TourOld( TourInfo i, List<Station> s, int tid, FragmentActivity context ){
    info = i;
    stations = s;
    trkid = tid;
    track = new Vector<>();
    ListMarker = new Vector<>();

    makeMarker( context );
  }

  public void makeMarker( FragmentActivity context ){
    for( Station station : stations ){
      MarkerOptions marker = new MarkerOptions();
      int id = context.getResources()
                      .getIdentifier( "pin_" + trkid, "drawable", context.getPackageName() );
      Bitmap icon = BitmapFactory.decodeResource( context.getResources(), id );
      marker.position( station.latlng );
      marker.icon( BitmapDescriptorFactory.fromBitmap( icon ) );
      ListMarker.add( marker );
    }
  }

  @Override
  public String toString(){
    String s;
    s = Integer.toString( trkid ) + "\n" + info.toString();
    for( Station st : stations ){
      s = s + st.toString();
    }
    s = s + "\n";
    for( LatLng point : track ){
      s = s + point;
    }

    return s;
  }

  public void makePolylines(){
    polylines = new PolylineOptions();
    for( LatLng latlng : track ){
      polylines.add( latlng );
      polylines.color( Color.parseColor( info.color ) );
    }
  }
}
