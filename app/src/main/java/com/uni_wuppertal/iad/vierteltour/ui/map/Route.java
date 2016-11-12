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
import java.util.StringTokenizer;
import java.util.Vector;

public class Route extends FragmentActivity{
  String name;
  String color;
  String routeString;
  String stationenString;
  List<LatLng> route;
  List<LatLng> stationen;
  List<MarkerOptions> markerList;
  PolylineOptions lines;
  FragmentActivity context;


  public Route( String r, String c, String s, String n, FragmentActivity fr ){
    route = new Vector<>();
    lines = new PolylineOptions();
    stationen = new Vector<>();
    markerList = new Vector<>();

    routeString = r;
    color = new String( c );
    stationenString = s;
    name = n;
    context = fr;

    lines.color( Color.parseColor( c ) );

    // Parse
    StringTokenizer tok = new StringTokenizer( routeString, ", " );
    tok.nextToken();    // Erstes Element (Return) überspringen
    while( tok.hasMoreTokens() ){
      String LatLngElement = tok.nextToken();
      Double lat = Double.parseDouble( LatLngElement );
      LatLngElement = tok.nextToken();
      Double lng = Double.parseDouble( LatLngElement );
      lines.add( new LatLng( lng, lat ) );
      route.add( new LatLng( lng, lat ) );
      tok.nextToken();    // "0" überspringen
    }

    // Parse Stationen
    tok = new StringTokenizer( stationenString, ", " );
    tok.nextToken();    // Erstes Element (Return) überspringen
    while( tok.hasMoreTokens() ){
      String LatLngElement = tok.nextToken();
      Double lat = Double.parseDouble( LatLngElement );
      LatLngElement = tok.nextToken();
      Double lng = Double.parseDouble( LatLngElement );
      String iconName = "marker_" + name;
      int id = context.getResources()
                      .getIdentifier( iconName, "drawable", context.getPackageName() );
      Bitmap icon = BitmapFactory.decodeResource( context.getResources(), id );
      MarkerOptions marker = new MarkerOptions();
      marker.position( new LatLng( lng, lat ) );
      marker.icon( BitmapDescriptorFactory.fromBitmap( icon ) );
      markerList.add( marker );
      tok.nextToken();     // "0" überspringen
    }

  }
}
