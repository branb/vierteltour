package com.uni_wuppertal.iad.vierteltour.ui.station;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.utility.storage.Singletonint;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Layout for an ended Station
 */
public class Stationbeendet extends Activity {

  ImageButton x, /*station_wiederholen,*/ zur_naechsten_station, zur_tourenauswahl;
  TextView beendet_text;
  Singletonint singlepage;
  Intent getIntent;
  Bundle b;
  int RESULT_NEXT=10;

  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.stationbeendet );

    //Initialize buttons on layout
    x = (ImageButton) findViewById( R.id.station_x_button );
    Sharp.loadResource(getResources(), R.raw.beenden_hell).into(x);
    /*station_wiederholen = (ImageButton) findViewById( R.id.station_wiederholen );
    Sharp.loadResource(getResources(), R.raw.station_wiederholen).into(station_wiederholen);*/
    zur_naechsten_station = (ImageButton) findViewById( R.id.zur_naechsten_station );
    Sharp.loadResource(getResources(), R.raw.zur_naechsten_station).into(zur_naechsten_station);
    zur_tourenauswahl = (ImageButton) findViewById( R.id.zur_tourenauswahl );
    Sharp.loadResource(getResources(), R.raw.zur_tourenauswahl).into(zur_tourenauswahl);
    beendet_text = (TextView) findViewById(R.id.stationbeendettext);

    getIntent = getIntent();
    b = getIntent.getExtras();
    int var = b.getInt("vergleich");
    final String path = b.getString("pfad");
    if(var==1){zur_tourenauswahl.setVisibility(View.VISIBLE);
    beendet_text.setText("Sie haben diese Tour\nbeendet!");}
    else{zur_naechsten_station.setVisibility(View.VISIBLE);}

    x.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        setResult(RESULT_OK);
        onBackPressed();
        overridePendingTransition(0, 0);
      }
    });

   /* station_wiederholen.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        setResult(RESULT_OK);
        onBackPressed();
      }
    });*/

    //Set Intent to next StationActivity with all needed informations
    zur_naechsten_station.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        setResult(RESULT_NEXT);
        System.out.println("onBackPressed");
        onBackPressed();

      }
    });

    //Sets all singleton variables to null and opens MapsActivity again
    zur_tourenauswahl.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        Intent back = new Intent(getApplicationContext(), MapsActivity.class);
        singlepage.INSTANCE.selectedTour(null);
        singlepage.INSTANCE.selectedStation(null);
        singlepage.INSTANCE.selectedOldStation(null);
        back.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
        overridePendingTransition(0, 0);
      }
    });
  }}
