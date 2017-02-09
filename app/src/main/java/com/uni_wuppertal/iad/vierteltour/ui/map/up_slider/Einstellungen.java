package com.uni_wuppertal.iad.vierteltour.ui.map.up_slider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.Area;
import com.uni_wuppertal.iad.vierteltour.ui.map.City;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.ui.map.Region;
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourList;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourListReader;
import com.uni_wuppertal.iad.vierteltour.updater.Updater;
import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kevin-Laptop on 17.01.2017.
 */

public class Einstellungen extends Activity{

  private ListView listViewTouren, listViewEinstellungen;
  private RelativeLayout layoutEinstellungen, layoutTouren, tourenLoeschen;
  private TextView keineTouren;
  private TourList tourlist = new TourListReader( this ).readTourList();
  private String[] items = new String[] {"Tour löschen", "Nach Aktualisierungen suchen", "Touren freischalten", "Touren sperren"};
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
        if(i==1) {e.remove("localTourdataVersion").remove("remoteTourdataVersion").apply();
          Updater.get( getBaseContext() ).updatesOnTourdata(Einstellungen.this);
        }
        if(i==2){for(int k=0;k<tourlist.tours().size();k++)
        {for(int j=1;j<=tourlist.tours().get(k).stations().size();j++)
        {e.putBoolean(tourlist.tours().get(k).station(j).slug() ,true);}}
       e.apply();}
        if(i==3)
        {for(int k=0;k<tourlist.tours().size();k++)
        {for(int j=1;j<=tourlist.tours().get(k).stations().size();j++)
        {e.remove(tourlist.tours().get(k).station(j).slug());}}
          e.apply(); }
      }
    });
    initEinstellungenTourAdapter();
    listViewTouren = (ListView) findViewById(R.id.listTouren);
    listViewTouren.setAdapter(einstellungenTourAdapter);
    listViewTouren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        createTourDialog("Willst du die Tour " + tours.get(position).name() + " wirklich löschen?", position);
      }
    });
    tourenLoeschen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        createAllToursDialog("Willst du alle Touren wirklich löschen?");


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

  public void createTourDialog(String txt, int pos)
  {// Create custom dialog object
    final Dialog dialog = new Dialog(this);
    final int position = pos;

    // Include dialog.xml file
    dialog.setContentView(R.layout.alert_dialog);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();
    // set values for custom dialog components - text, image and button
    TextView text = (TextView) dialog.findViewById(R.id.main_text);
    text.setText(txt);

    String path="";
    TourListReader tourListReader = new TourListReader(this);
    TourList tourlist = tourListReader.readTourList();

    for( Region region : tourlist.regions() ){
      for( Area area : region.areas() ){
        for( City city : area.cities() ){
          for(Tour tour : city.tours()){
            if(tour.slug().equals(tours.get(position).slug())){
              path = tour.home();
            }}}}}

    final String testpath = path ;

    Button okayButton = (Button) dialog.findViewById(R.id.left_btn);
    // if decline button is clicked, close the custom dialog
    okayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        File test = new File(getExternalFilesDir(null), testpath);
        deleteRecursive(test);
        e.remove(tours.get(position).slug()).apply();
        tours.remove(position);

        einstellungenTourAdapter.notifyDataSetChanged();
        MapsActivity.adapter.notifyDataSetChanged();
        if(tours.size()<2) tourenLoeschen.setVisibility(View.GONE);
        if(tours.isEmpty()) keineTouren.setVisibility(View.VISIBLE);

        dialog.dismiss();}});

    Button declineButton = (Button) dialog.findViewById(R.id.right_btn);
    // if decline button is clicked, close the custom dialog
    declineButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Close dialog
        dialog.dismiss();}});

  }

  public void createAllToursDialog(String txt)
  {// Create custom dialog object
    final Dialog dialog = new Dialog(this);

    // Include dialog.xml file
    dialog.setContentView(R.layout.alert_dialog);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();
    // set values for custom dialog components - text, image and button
    TextView text = (TextView) dialog.findViewById(R.id.main_text);
    text.setText(txt);

    String path="";
    TourListReader tourListReader = new TourListReader(this);
    TourList tourlist = tourListReader.readTourList();

    for( Region region : tourlist.regions() ){
      for( Area area : region.areas() ){
        for( City city : area.cities() ){
          for(Tour tour : city.tours()){
            if(tour.slug().equals(tours.get(0).slug())){
              path = city.home();
            }}}}}

    final String testpath = path ;

    Button okayButton = (Button) dialog.findViewById(R.id.left_btn);
    // if decline button is clicked, close the custom dialog
    okayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        File test = new File(getExternalFilesDir(null), testpath);
        deleteRecursive(test);
        for(int i=tours.size()-1;i>=0;i--)
        {e.remove(tours.get(i).slug()).apply();
          tours.remove(i);}

        einstellungenTourAdapter.notifyDataSetChanged();
        MapsActivity.adapter.notifyDataSetChanged();
        tourenLoeschen.setVisibility(View.GONE);
        keineTouren.setVisibility(View.VISIBLE);

        dialog.dismiss();}});

    Button declineButton = (Button) dialog.findViewById(R.id.right_btn);
    // if decline button is clicked, close the custom dialog
    declineButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Close dialog
        dialog.dismiss();}});

  }

  public void deleteRecursive(File fileOrDirectory) {

    if (fileOrDirectory.isDirectory()) {
      for (File child : fileOrDirectory.listFiles()) {
       deleteRecursive(child);
      }
    }
    if(!fileOrDirectory.toString().endsWith(".png") && !fileOrDirectory.toString().endsWith(".xml") && !fileOrDirectory.toString().endsWith(".gpx") )
    {fileOrDirectory.delete();}
  }

}
