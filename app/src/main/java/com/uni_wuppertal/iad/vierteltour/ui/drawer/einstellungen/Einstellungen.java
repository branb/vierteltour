package com.uni_wuppertal.iad.vierteltour.ui.drawer.einstellungen;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pixplicity.sharp.Sharp;
import com.pixplicity.sharp.SharpDrawable;
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

public class Einstellungen extends AppCompatActivity {

  private ListView listViewTouren;
  private TextView keineTouren, title;
  private ImageButton xbtn, delete;
  private RelativeLayout layoutTouren, deleteAll;
  private TourList tourlist = new TourListReader( this ).readTourList();
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor e;
  private List<Tour> tours;
  private ActionBar actionBar;

  private EinstellungenTourAdapter einstellungenTourAdapter;

  /**
   * Back Button pressed. If User is in "Touren löschen" he will be moved back to general settings
   */
  @Override
  public void onBackPressed()
  {super.onBackPressed();
    overridePendingTransition(0, 0);}



  @Override
  protected void onCreate(
    @Nullable
      Bundle savedInstanceState ){
    //initialisation
    super.onCreate( savedInstanceState );
    setContentView( R.layout.einstellungen );
    initActionbar();
    keineTouren = (TextView) findViewById(R.id.keineTouren);
    layoutTouren = (RelativeLayout) findViewById(R.id.listLayout2);

    delete = (ImageButton) findViewById(R.id.delete);
    Sharp.loadResource(getResources(), R.raw.alle_loeschen).into(delete);
    delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        createAllToursDialog("Wollen Sie alle Touren löschen?");
      }
    });
    deleteAll = (RelativeLayout) findViewById(R.id.deleteAll);
    //Touren listview to select a tour to delete
    initEinstellungenTourAdapter();
    listViewTouren = (ListView) findViewById(R.id.listTouren);
    listViewTouren.setAdapter(einstellungenTourAdapter);
    listViewTouren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //Dialog to confirm the deletion for one tour
          createTourDialog("Wollen Sie die Tour »" + tours.get(position).name() + "« löschen?", position);}});
  }

  public void initEinstellungenTourAdapter()
  { tours = tourlist.tours();   //Tourlist will be needed to check tourslugs TODO Look for better alternative
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    e = sharedPreferences.edit();
    for(int i=tours.size()-1;i>=0;i--)
    {if(!sharedPreferences.getBoolean(tours.get(i).slug(), false) && !sharedPreferences.getBoolean(tours.get(i).slug()+"-zip", false))    //Check sharedpreferences to compare tour slug. if it contains tourslug, the tour is stored in files directory
    {tours.remove(i);}}
    einstellungenTourAdapter = new EinstellungenTourAdapter(tours, this);

    layoutTouren.setVisibility(View.VISIBLE);
    if(tours.isEmpty())
    {keineTouren.setVisibility(View.VISIBLE);
    deleteAll.setVisibility(View.GONE);}
   }

  public TourList tourlist()
  {return tourlist;}


  public void initActionbar()
  {actionBar = getSupportActionBar();

    actionBar.setDisplayShowHomeEnabled( false );
    actionBar.setDisplayShowTitleEnabled( false );
    actionBar.setDisplayHomeAsUpEnabled( false );
    actionBar.setHomeButtonEnabled( false );
    actionBar.setDisplayShowCustomEnabled( true );     //Deaktiviert alle Buttons und setzt CostumActionBar
    View view = getLayoutInflater().inflate( R.layout.toolbar, null );
    android.support.v7.app.ActionBar.LayoutParams layoutParams = new android.support.v7.app.ActionBar.LayoutParams( android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT, android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT );
    actionBar.setCustomView( view, layoutParams );
    Toolbar parent = (Toolbar) view.getParent();
    parent.setContentInsetsAbsolute( 0, 0 );           //Vermeidet Fehler, dass CostumActionBar zu schmal wird
    actionBar.setElevation( 0 );


    xbtn = (ImageButton) findViewById(R.id.btn_x);      //ActionBar Button: Right
    title = (TextView) findViewById(R.id.toolbar_title);  //ActionBar Title
    title.setText("Touren löschen");
    xbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
      onBackPressed();
      }
    });
    Sharp.loadResource(getResources(), R.raw.beenden_dunkel).into(xbtn);

    title.setVisibility(View.VISIBLE);
    xbtn.setVisibility(View.VISIBLE);
    title.setTypeface(Typeface.MONOSPACE);

  }

  /**
   * //Dialog to confirm the deletion of one tour
   * @param txt that is used to show the given text to the user
   * @param pos to get the selected tour as a number
   */
  public void createTourDialog(String txt, int pos)
  {// Create custom dialog object
    final Dialog dialog = new Dialog(this);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    final int position = pos;

    // Include dialog.xml file
    dialog.setContentView(R.layout.alert_dialog);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();
    // set values for custom dialog components - text, image and button
    TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
    SpannableString dialogtext = new SpannableString(txt+" ");
    dialogtext.setSpan(new StyleSpan(Typeface.BOLD), 0, dialogtext.length(), 0);
    text.setText(dialogtext);

    TextView title = (TextView) dialog.findViewById(R.id.title_dialog);
    title.setText("Löschen der Tour");
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

    ImageButton okayButton = (ImageButton) dialog.findViewById(R.id.button_dialog);
    Sharp.loadResource(getResources(), R.raw.loeschen).into(okayButton);
    // if decline button is clicked, close the custom dialog
    okayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        File test = new File(getExternalFilesDir(null), testpath);
        deleteRecursive(test);
        e.remove(tours.get(position).slug()).remove(tours.get(position).slug()+"-zip").apply();
        if(tours.size()==3)tours.remove(tours.size()-1);
        tours.remove(position);

        setResult(RESULT_OK);
        einstellungenTourAdapter.notifyDataSetChanged();
        MapsActivity.adapter.notifyDataSetChanged();
        if(tours.isEmpty())
        {keineTouren.setVisibility(View.VISIBLE);
        deleteAll.setVisibility(View.GONE);}

        dialog.dismiss();}});

    ImageButton declineButton = (ImageButton) dialog.findViewById(R.id.btn_x_dialog);
    Sharp.loadResource(getResources(), R.raw.beenden_dunkel).into(declineButton);
    // if decline button is clicked, close the custom dialog
    declineButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Close dialog
        dialog.dismiss();}});

  }


  /**
   * //Dialog to confirm the deletion of all tours
   * @param txt that is used to show the given text to the user
   */

  public void createAllToursDialog(String txt)
  {// Create custom dialog object
    final Dialog dialog = new Dialog(this);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    // Include dialog.xml file
    dialog.setContentView(R.layout.alert_dialog);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();
    // set values for custom dialog components - text, image and button
    TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
    SpannableString dialogtext = new SpannableString(txt+" ");
    dialogtext.setSpan(new StyleSpan(Typeface.BOLD), 0, dialogtext.length(), 0);
    text.setText(dialogtext);
    TextView title = (TextView) dialog.findViewById(R.id.title_dialog);
    title.setText("Löschen der Touren");

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

    ImageButton okayButton = (ImageButton) dialog.findViewById(R.id.button_dialog);
    Sharp.loadResource(getResources(), R.raw.loeschen).into(okayButton);
    // if decline button is clicked, close the custom dialog
    okayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        File test = new File(getExternalFilesDir(null), testpath);
        deleteRecursive(test);
        for(int i=tours.size()-1;i>=0;i--)
        {e.remove(tours.get(i).slug()).remove(tours.get(i).slug()+"-zip").apply();
          tours.remove(i);}

        einstellungenTourAdapter.notifyDataSetChanged();
        MapsActivity.adapter.notifyDataSetChanged();
        keineTouren.setVisibility(View.VISIBLE);
        deleteAll.setVisibility(View.GONE);
        dialog.dismiss();}});

    ImageButton declineButton = (ImageButton) dialog.findViewById(R.id.btn_x_dialog);
    Sharp.loadResource(getResources(), R.raw.beenden_dunkel).into(declineButton);
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
    if(!fileOrDirectory.toString().endsWith(".png") && !fileOrDirectory.toString().endsWith(".xml") && !fileOrDirectory.toString().endsWith(".gpx") && !fileOrDirectory.toString().endsWith(".svg") )
    {fileOrDirectory.delete();}
  }

}
