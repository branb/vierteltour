package com.uni_wuppertal.iad.vierteltour.ui.map.up_slider;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourList;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourListReader;

/**
 * Created by Kevin-Laptop on 17.01.2017.
 */

public class Einstellungen extends Activity{

  private ListView listView;
  private TourList tourlist = new TourListReader( this ).readTourList();

  private EinstellungenAdapter einstellungenAdapter = new EinstellungenAdapter(tourlist.tours(), this);

  @Override
  protected void onCreate(
    @Nullable
      Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.einstellungen );
    listView = (ListView) findViewById(R.id.listEinstellungen);
    listView.setAdapter(einstellungenAdapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        System.out.println("click");

        //starte löschbestätigungsfenster
      }
    });


  }


}
