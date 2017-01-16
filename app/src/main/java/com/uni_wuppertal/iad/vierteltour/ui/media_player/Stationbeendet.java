package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.ui.map.Station;
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * Created by Kevin on 23.11.2016.
 */

public class Stationbeendet extends Activity {

  ImageButton x, station_wiederholen, zur_naechsten_station, zur_tourenauswahl;
  TextView beendet_text;
  Singletonint singlepage;
  Intent getIntent;
  Bundle b;

  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.stationbeendet );

    x = (ImageButton) findViewById( R.id.station_x_button );
    station_wiederholen = (ImageButton) findViewById( R.id.station_wiederholen );
    zur_naechsten_station = (ImageButton) findViewById( R.id.zur_naechsten_station );
    zur_tourenauswahl = (ImageButton) findViewById( R.id.zur_tourenauswahl );
    beendet_text = (TextView) findViewById(R.id.stationbeendettext);

    getIntent = getIntent();
    b = getIntent.getExtras();
    int var = b.getInt("vergleich");
    if(var==1){zur_tourenauswahl.setVisibility(View.VISIBLE);
    beendet_text.setText("Sie haben diese Tour\nbeendet!");}
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
        Intent next = new Intent(getApplicationContext(), StationActivity.class);
        next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Tour tour = singlepage.INSTANCE.selectedTour();
        next.putExtra( "name", tour.name() );
        next.putExtra( "autor", tour.author() );
        next.putExtra( "zeit", tour.time() );
        next.putExtra( "laenge", tour.length() );
        next.putExtra( "farbe", tour.color() );
        next.putExtra( "size", "" + tour.stations().size() );
        // Selected Station
        singlepage.INSTANCE.selectedStation(singlepage.INSTANCE.selectedTour().station(singlepage.INSTANCE.selectedStation().number()+1));
        Station station = singlepage.INSTANCE.selectedStation();
        next.putExtra("slug", station.slug());
        next.putExtra( "station", station.name() );
        next.putExtra( "desc", station.description() );
        next.putExtra( "pos", "" + (station.number()) );
        // Station media
        next.putExtra( "img", station.imagesToString() );
        next.putExtra( "audio", station.audio());
        next.putExtra( "video", station.videosToString() );
        MapsActivity.mPager.setCurrentItem(station.number()-1);
        startActivity(next);
      }
    });

    zur_tourenauswahl.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        Intent back = new Intent(getApplicationContext(), MapsActivity.class);
        singlepage.INSTANCE.selectedTour(null);
        singlepage.INSTANCE.selectedStation(null);
        singlepage.INSTANCE.selectedOldStation(null);
        back.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
      }
    });
  }}
