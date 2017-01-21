package com.uni_wuppertal.iad.vierteltour.ui.map.up_slider;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourList;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourListReader;

import java.util.ArrayList;

/**
 * Created by Kevin-Laptop on 17.01.2017.
 */

public class Einstellungen extends Activity{

  private ListView listViewTouren, listViewEinstellungen;
  private RelativeLayout layoutEinstellungen, layoutTouren, tourenLoeschen;
  private TourList tourlist = new TourListReader( this ).readTourList();
  private String[] items = {"Tour löschen", "Nach Aktualisierungen suchen"};
  private ArrayAdapter<String> einstellungen;

  private EinstellungenAdapter einstellungenAdapter = new EinstellungenAdapter(tourlist.tours(), this);

  @Override
  public void onBackPressed()
  {if(layoutTouren.getVisibility()==View.VISIBLE)
  {layoutEinstellungen.setVisibility(View.VISIBLE);
    layoutTouren.setVisibility(View.GONE);}
  else super.onBackPressed();}



  @Override
  protected void onCreate(
    @Nullable
      Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.einstellungen );
    einstellungen = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, items);
    listViewEinstellungen = (ListView) findViewById(R.id.listEinstellungen);
    layoutEinstellungen = (RelativeLayout) findViewById(R.id.listLayout1);
    layoutTouren = (RelativeLayout) findViewById(R.id.listLayout2);
    tourenLoeschen = (RelativeLayout) findViewById(R.id.tourenloeschen);
    listViewEinstellungen.setAdapter(einstellungen);
    listViewEinstellungen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0)
        { layoutEinstellungen.setVisibility(View.GONE);
          layoutTouren.setVisibility(View.VISIBLE);}
        if(i==1) System.out.println("UPDATES");
      }
    });
    listViewTouren = (ListView) findViewById(R.id.listTouren);
    listViewTouren.setAdapter(einstellungenAdapter);
    listViewTouren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        System.out.println("click");

        //starte löschbestätigungsfenster
      }
    });
    tourenLoeschen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        System.out.println("ALLES GELÖSCHT");
      }
    });

  }


}
