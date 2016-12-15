package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by Kevin on 23.11.2016.
 */

public class Stationbeendet extends Activity {

  ImageButton x, station_wiederholen, zur_naechsten_station, zur_tourenauswahl;
  Intent getIntent;
  Bundle b;

  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.stationbeendet );

    x = (ImageButton) findViewById( R.id.station_x_button );
    station_wiederholen = (ImageButton) findViewById( R.id.station_wiederholen );
    zur_naechsten_station = (ImageButton) findViewById( R.id.zur_naechsten_station );
    zur_tourenauswahl = (ImageButton) findViewById( R.id.zur_tourenauswahl );

    getIntent = getIntent();
    b = getIntent.getExtras();
    int var = b.getInt("vergleich");
    if(var==1){zur_tourenauswahl.setVisibility(View.VISIBLE);}
    else{zur_naechsten_station.setVisibility(View.VISIBLE);}

    x.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        onBackPressed();
      }
    });

    station_wiederholen.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        onBackPressed();
      }
    });

    zur_naechsten_station.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        onBackPressed();
        //TODO: füge Funktion mit Branis Projekt hinzu
        //Intent next = new Intent(getApplicationContext(), InformationActivity.class);
        //startActivity(next);
      }
    });

    zur_tourenauswahl.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        Intent back = new Intent(getApplicationContext(), MapsActivity.class);
        back.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
      }
    });
  }}
