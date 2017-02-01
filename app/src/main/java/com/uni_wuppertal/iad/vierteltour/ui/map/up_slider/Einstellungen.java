package com.uni_wuppertal.iad.vierteltour.ui.map.up_slider;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourList;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourListReader;

import java.util.List;
import java.util.Map;

/**
 * Created by Kevin-Laptop on 17.01.2017.
 */

public class Einstellungen extends Activity{

  private ListView listViewTouren, listViewEinstellungen;
  private RelativeLayout layoutEinstellungen, layoutTouren, tourenLoeschen;
  private TextView keineTouren;
  private TourList tourlist = new TourListReader( this ).readTourList();
  private String[] items = new String[] {"Tour löschen", "Nach Aktualisierungen suchen"};
  private EinstellungenAdapter einstellungen;
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor e;
  private List<Tour> tours;

  private EinstellungenTourAdapter einstellungenTourAdapter;

  @Override
  public void onBackPressed()
  {if(layoutTouren.getVisibility()==View.VISIBLE)
  {layoutEinstellungen.setVisibility(View.VISIBLE);
    layoutTouren.setVisibility(View.GONE);
    keineTouren.setVisibility(View.GONE);}
  else super.onBackPressed();}



  @Override
  protected void onCreate(
    @Nullable
      Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.einstellungen );
    einstellungen = new EinstellungenAdapter(this, items);
    listViewEinstellungen = (ListView) findViewById(R.id.listEinstellungen);
    keineTouren = (TextView) findViewById(R.id.keineTouren);
    layoutEinstellungen = (RelativeLayout) findViewById(R.id.listLayout1);
    layoutTouren = (RelativeLayout) findViewById(R.id.listLayout2);
    tourenLoeschen = (RelativeLayout) findViewById(R.id.tourenloeschen);
    listViewEinstellungen.setAdapter(einstellungen);
    listViewEinstellungen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0)
        { layoutEinstellungen.setVisibility(View.GONE);
          layoutTouren.setVisibility(View.VISIBLE);
          if(tours.size()<2)tourenLoeschen.setVisibility(View.GONE);
          if(tours.isEmpty())keineTouren.setVisibility(View.VISIBLE);}
        if(i==1) System.out.println("UPDATES");
      }
    });
    initEinstellungenTourAdapter();
    listViewTouren = (ListView) findViewById(R.id.listTouren);
    listViewTouren.setAdapter(einstellungenTourAdapter);
    listViewTouren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        e.remove(tours.get(position).slug()).apply();
        tours.remove(position);
        //TODO ADD DIALOG AND DELETE TOUR
        einstellungenTourAdapter.notifyDataSetChanged();
        MapsActivity.adapter.notifyDataSetChanged();
        if(tours.size()<2) tourenLoeschen.setVisibility(View.GONE);
        if(tours.isEmpty()) keineTouren.setVisibility(View.VISIBLE);
      }
    });
    tourenLoeschen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        for(int i=tours.size()-1;i>=0;i--)
        {e.remove(tours.get(i).slug()).apply();
          tours.remove(i);}

        //TODO ADD DIALOG AND DELETE TOURS

        einstellungenTourAdapter.notifyDataSetChanged();
        MapsActivity.adapter.notifyDataSetChanged();
        tourenLoeschen.setVisibility(View.GONE);
        keineTouren.setVisibility(View.VISIBLE);
      }
    });

  }

  public void initEinstellungenTourAdapter()
  { tours = tourlist.tours();
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    e = sharedPreferences.edit();
    for(int i=tours.size()-1;i>=0;i--)
    {if(!sharedPreferences.getBoolean(tours.get(i).slug(), false))
    {tours.remove(i);}}
    einstellungenTourAdapter = new EinstellungenTourAdapter(tours, this);
   }

}
