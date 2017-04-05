package com.uni_wuppertal.iad.vierteltour.ui.drawer.einstellungen;

import android.app.Activity;
import android.app.Dialog;
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
import com.uni_wuppertal.iad.vierteltour.utility.xml.Area;
import com.uni_wuppertal.iad.vierteltour.utility.xml.City;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Region;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Tour;
import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourList;
import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourListReader;
import com.uni_wuppertal.iad.vierteltour.utility.updater.Updater;

import java.io.File;
import java.util.List;

/**
 * Created by Kevin-Laptop on 17.01.2017.
 * Activity for settings of the application.
 */

public class Einstellungen extends Activity{

  private ListView listViewTouren, listViewEinstellungen;
  private RelativeLayout layoutEinstellungen, layoutTouren, tourenLoeschen;
  private TextView keineTouren;
  private TourList tourlist = new TourListReader( this ).readTourList();
  private String[] items = new String[] {"Touren löschen", "Nach Aktualisierungen suchen", "Touren freischalten", "Touren sperren"};
  private EinstellungenAdapter einstellungen;   //Adapter for the listview
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor e;
  private List<Tour> tours;

  private EinstellungenTourAdapter einstellungenTourAdapter;

  /**
   * Back Button pressed. If User is in "Touren löschen" he will be moved back to general settings
   */
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
    //initialisation
    super.onCreate( savedInstanceState );
    setContentView( R.layout.einstellungen );
    einstellungen = new EinstellungenAdapter(this, items);
    listViewEinstellungen = (ListView) findViewById(R.id.listEinstellungen);
    keineTouren = (TextView) findViewById(R.id.keineTouren);
    layoutEinstellungen = (RelativeLayout) findViewById(R.id.listLayout1);
    layoutTouren = (RelativeLayout) findViewById(R.id.listLayout2);
    listViewEinstellungen.setAdapter(einstellungen);
    listViewEinstellungen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //i==0 equals "Touren löschen"
        if(i==0)
        { layoutEinstellungen.setVisibility(View.GONE);
          layoutTouren.setVisibility(View.VISIBLE);
          if(tours.isEmpty())keineTouren.setVisibility(View.VISIBLE);}
        //i==1 equals "Nach Aktualisierungen suchen"
        if(i==1) {e.remove("localTourdataVersion").remove("remoteTourdataVersion").apply();
          Updater.get( getBaseContext() ).updatesOnTourdata(Einstellungen.this);
        }
        //i==2 equals "Touren freischalten". Function only for testing
        if(i==2){for(int k=0;k<tourlist.tours().size();k++)
        {for(int j=1;j<=tourlist.tours().get(k).stations().size();j++)
        {e.putBoolean(tourlist.tours().get(k).station(j).slug() ,true);}}
       e.apply();}
        //i==3 equals "Touren sperren". Function only for testing
        if(i==3)
        {for(int k=0;k<tourlist.tours().size();k++)
        {for(int j=1;j<=tourlist.tours().get(k).stations().size();j++)
        {e.remove(tourlist.tours().get(k).station(j).slug());}}
          e.apply(); }
      }
    });
    //Touren listview to select a tour to delete
    initEinstellungenTourAdapter();
    listViewTouren = (ListView) findViewById(R.id.listTouren);
    listViewTouren.setAdapter(einstellungenTourAdapter);
    listViewTouren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        if(einstellungenTourAdapter.getCount()-1==position && einstellungenTourAdapter.getCount()>1)
        {//Dialog to confirm the deletion for all tours
          createAllToursDialog("Willst du alle Touren wirklich löschen?");}
        else
        {//Dialog to confirm the deletion for one tour
          createTourDialog("Willst du die Tour " + tours.get(position).name() + " wirklich löschen?", position);}
      }
    });
  }

  public void initEinstellungenTourAdapter()
  { tours = tourlist.tours();   //Tourlist will be needed to check tourslugs TODO Look for better alternative
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    e = sharedPreferences.edit();
    for(int i=tours.size()-1;i>=0;i--)
    {if(!sharedPreferences.getBoolean(tours.get(i).slug(), false))    //Check sharedpreferences to compare tour slug. if it contains tourslug, the tour is stored in files directory
    {tours.remove(i);}}
    einstellungenTourAdapter = new EinstellungenTourAdapter(tours, this);
   }

  /**
   * //Dialog to confirm the deletion of one tour
   * @param txt that is used to show the given text to the user
   * @param pos to get the selected tour as a number
     */
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

    //get whole path of the tour that has to be deleted
    String path="";
    TourListReader tourListReader = new TourListReader(this);
    TourList tourlist = tourListReader.readTourList();

    //Through tourlist.xml
    for( Region region : tourlist.regions() ){
      for( Area area : region.areas() ){
        for( City city : area.cities() ){
          for(Tour tour : city.tours()){
            if(tour.slug().equals(tours.get(position).slug())){
              path = tour.home();     //standard path
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
        if(tours.size()==3)tours.remove(tours.size()-1);
        tours.remove(position);

        einstellungenTourAdapter.notifyDataSetChanged();
        MapsActivity.adapter.notifyDataSetChanged();
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

  public TourList tourlist()
  {return tourlist;}


  /**
   * //Dialog to confirm the deletion of all tours
   * @param txt that is used to show the given text to the user
   */
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

  /**
   * deletes all images, videos and audios including station folders
   * function is used recursivly
   * @param fileOrDirectory name of file/directory
     */
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
