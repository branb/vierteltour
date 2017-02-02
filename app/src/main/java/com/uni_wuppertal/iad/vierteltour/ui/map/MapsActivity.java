package com.uni_wuppertal.iad.vierteltour.ui.map;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.Visibility;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tagmanager.Container;
import com.google.maps.android.PolyUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import com.thin.downloadmanager.DownloadRequest;
import com.uni_wuppertal.iad.vierteltour.ui.intro.IntroActivity;

import com.uni_wuppertal.iad.vierteltour.ui.map.up_slider.About;
import com.uni_wuppertal.iad.vierteltour.ui.map.up_slider.Einstellungen;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.StationActivity;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.Singletonint;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.ViertelTourMediaPlayer;

import com.uni_wuppertal.iad.vierteltour.ui.map.station_pager.StationAdapter;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.up_slider.TourAdapter;
import com.uni_wuppertal.iad.vierteltour.ui.map.up_slider.DrawerAdapter;
import com.uni_wuppertal.iad.vierteltour.ui.map.up_slider.DrawerItem;
import com.uni_wuppertal.iad.vierteltour.updater.Updater;
import com.uni_wuppertal.iad.vierteltour.updater.UpdateListener;
import com.uni_wuppertal.iad.vierteltour.utility.ReplaceFont;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback, UpdateListener{

  public Location MyLocation;
  public LatLng pos;
  public LocationManager locationManager;
  public LocationListener locationListener;
  final static int REQUEST_LOCATION = 1;
  private GoogleApiClient googleApiClient;
  public int CurrentZoom = 15;
  int[] drawerIcons = new int[]{ R.drawable.einstellungen,
                                 R.drawable.hilfe,
                                 R.drawable.about
  };
  String[] drawertitles = new String[]{ "Einstellungen",
                                        "Info",
                                        "About"
  };

  private final double radius=25;
  private ActionBar actionBar;
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private SlidingUpPanelLayout mLayout;
  private RelativeLayout mDrawer;
  public static ClickableViewpager mPager;
  private StationAdapter stationAdapter;
  private DrawerAdapter draweradapter;
  public static TourAdapter adapter;
  private List<View> tourlistview;
  private List<DrawerItem> drawerItems;
  private LatLng wuppertal;
  private ImageButton xbtn, zumstart, homebtn, leftbtn, x_supl, arrowbtn, tarbtn;
  private Button gpsbtn;
  private ImageView up, down;
  private ListView lv;
  private View listelement;
  private ShadowTransformer mFragmentShadowTransformer;
  private TextView title, tourenliste, subtext1, subtext2;
  private Marker tmpmarker;
  private RelativeLayout panel, gpsinfo;
  public static RelativeLayout audiobar;
  ProgressDialog progressDoalog;
  private ViertelTourMediaPlayer player;
  private Singletonint singlepage;
  // All the tour information that is currently available to us
  private TourList tourlist;


  private GoogleMap mMap;

  // Indicates, if we have checked for new updates on tourdata. Needed at the start of the app
  private boolean checkedForUpdates = false;

  // TODO: Save the currently displayed city into shared preferences and load them on startup
  // Slug of the currently displayed city, e.g. the currently available and displayed tours
  private String visibleCity = "wuppertal";

  // TODO: Save the selected tour into shared preferences and load them on startup
  // The currently selected and highlighted tour;

  // TODO: I don't know if it's the best approach to save it on a map ACTIVITY, but it certainly is NOT a good approach to couple it to the data model aka the Tour* classes
  // Holds the configuration of the current polylines drawn on the map
  private Map<String, PolylineOptions> polylines = new HashMap<String, PolylineOptions>();

  // Holds the configuration of the current markers drawn on the map
  private Map<String, MarkerOptions> markers = new HashMap<String, MarkerOptions>();

  private CircleOptions circle = new CircleOptions();

  @Override
  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    ReplaceFont.replaceDefaultFont(this, "MONOSPACE", "Bariol_Regular.ttf");

    setContentView( R.layout.activity_main );

    player = ViertelTourMediaPlayer.getInstance( this );

    initLocationServices();
    initAll();
    showIntro();


    initPager();
    initBtns();
    moveDrawerToTop();
    initActionBar();
    initDrawer();

    mFragmentShadowTransformer = new ShadowTransformer(mPager, stationAdapter, this);
    mPager.setPageTransformer(false, mFragmentShadowTransformer);

  }



  public void initAll() {
    ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this); //initMap

    mPager = (ClickableViewpager) findViewById(R.id.pager);
    stationAdapter = new StationAdapter(getSupportFragmentManager());
    mPager.setAdapter(stationAdapter);

    mPager.setOnItemClickListener(new ClickableViewpager.OnItemClickListener() {
      @Override
      public void onItemClick(int position) {
  if(singlepage.INSTANCE.onfragmentclicked()!=-1)
  {singlepage.INSTANCE.selectedOldStation(singlepage.INSTANCE.selectedStation());
  singlepage.INSTANCE.selectedStation(singlepage.INSTANCE.selectedTour().station(singlepage.INSTANCE.onfragmentclicked()));

        if(singlepage.INSTANCE.selectedStation()==singlepage.INSTANCE.selectedOldStation())
        {selectStation(singlepage.INSTANCE.selectedStation());
        startStationActivity();}
        else
        {mPager.setCurrentItem(singlepage.INSTANCE.selectedStation().number()-1);}
  singlepage.INSTANCE.onfragmentclicked(-1);
  }
      }
    });

    audiobar = (RelativeLayout) findViewById(R.id.audiobar);


    mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
    mLayout.setPanelSlideListener(onSlideListener());
    lv = (ListView) findViewById(R.id.list);
    panel = (RelativeLayout) findViewById(R.id.panelhalf);

    zumstart = (ImageButton) findViewById(R.id.zumstart);       //SUPL Button bottom right

    x_supl = (ImageButton) findViewById( R.id.x );               //SUPL Button top left

    arrowbtn = (ImageButton) findViewById( R.id.arrowbtn );       //Top Twin Button

    gpsbtn = (Button) findViewById( R.id.gpsbtn );           //Red Button left Bottom corner

    tarbtn = (ImageButton) findViewById( R.id.tarbtn );           //Bot Twin Button

    tourenliste = (TextView) findViewById( R.id.tourenliste );

    subtext1 = (TextView) findViewById( R.id.subinfo1 );
    subtext2 = (TextView) findViewById( R.id.subinfo2 );
    up = (ImageView) findViewById( R.id.up );
    down = (ImageView) findViewById( R.id.down );

    gpsinfo = (RelativeLayout) findViewById( R.id.gpsinfo );

  }

  // Show intro, but only if it's the first start of the app
  private void showIntro(){
    //  Declare a new thread to do a preference check
    Thread t = new Thread( new Runnable(){
      @Override
      public void run(){
        //  If the activity has never started before...
        if( PreferenceManager.getDefaultSharedPreferences( getBaseContext() ).getBoolean( "firstStart", true ) ){
          //  Launch app intro
          Intent i = new Intent( MapsActivity.this, IntroActivity.class );
          startActivity( i );
        }

      }
    });

    // Start the thread
    t.start();
  }



  /**
   * Check for updates
   */
  private void checkForUpdates(){
    if( !checkedForUpdates ){
      Updater.get( getBaseContext() ).updateListener( this );
      Updater.get( getBaseContext() ).updatesOnTourdata();
    }
    else if( !Updater.get( getBaseContext() ).checkingForUpdates() ) {
      loadTourdata();
    }

  }


  /**
   * Get notified when the map is ready to be used.
   */


  @Override
  public void onMapReady( GoogleMap googleMap ){
    mMap = googleMap;

    wuppertal = new LatLng( 51.256972, 7.139341 );
    mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( wuppertal, CurrentZoom ) );

    checkForUpdates();

    /**
     * When the user clicks anywhere on the map, check which tour he clicked onto and mark it as
     * selected
     *
     * TODO: Improve click recognition. Currently, it only checks if the user has clicked a polyline, not station markers
     */
    final GoogleMap.OnMapClickListener listener = new GoogleMap.OnMapClickListener(){
      @Override
      public void onMapClick( LatLng clickCoords ){

        if( mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED ){
          boolean tourSelected = false;

          for( Tour tour : tourlist.city(visibleCity).tours() ){

            if( PolyUtil.isLocationOnPath( clickCoords, tour.route().latLngs(), true, 20 ) && !tourSelected ){
              tourSelected = true;
              selectTour( tour );
              suplInfo( "showall" );
            }
          }
          if( !tourSelected ){
            resetTour();
          }
          drawRoutes();
        }
        //Stationenübersicht
        else if(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)
        { Boolean onMapClicked=false;       //unselect Stations
          Tour tour = singlepage.INSTANCE.selectedTour();
          for( Station station : tour.stations())
            { if( clickCoords.equals(station.latlng())){
              onMapClicked=true;
                mPager.setCurrentItem(station.number()-1);
                stationAdapter.notifyDataSetChanged();
            }

      }
          if(!onMapClicked && tmpmarker!=null) tmpmarker.showInfoWindow();

          //Unselect Station on Map Click
      /*  if(!onMapClicked)
        {if(singlepage.INSTANCE.selectedStation()!=null)
        {markers.get(singlepage.INSTANCE.selectedStation().slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(singlepage.INSTANCE.selectedTour(), "" + (singlepage.INSTANCE.selectedStation().number()))));
        singlepage.INSTANCE.selectedStation(null);
          circle.center(null);
        drawRoutes();}
        }*/
        }
      }
    };

    mMap.setOnMapClickListener( listener );
    mMap.setInfoWindowAdapter(new MapWindowAdapter(this));

    mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener(){
      @Override
      public boolean onMarkerClick( Marker marker ){
        listener.onMapClick( marker.getPosition() );
        return true;    // false: OnMarkerClick aktiv und zoomt zum Marker
      }
    } );
  }

  public void selectStation(Station station)
  {                //löscht alte Station
    if(singlepage.INSTANCE.selectedStation()!=null)
    {for(int i=0; i<singlepage.INSTANCE.selectedTour().stations().size();i++){markers.get(singlepage.INSTANCE.selectedTour().station(i+1).slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(singlepage.INSTANCE.selectedTour(), "" + (i))));}}

    singlepage.INSTANCE.selectedStation(station);       //Setzt neue Station
   // markers.get(station.slug());
    circle.center( station.latlng()).radius(radius).fillColor(Color.parseColor(singlepage.INSTANCE.selectedTour().color().substring(0,1) + "75" + singlepage.INSTANCE.selectedTour().color().substring(1,singlepage.INSTANCE.selectedTour().color().length()))).strokeColor(Color.parseColor(singlepage.INSTANCE.selectedTour().color())).strokeWidth(8).visible(true);
    markers.get(station.slug()).icon(BitmapDescriptorFactory.fromBitmap(scaleMarker(singlepage.INSTANCE.selectedTour(), "" + (station.number()-1))));
    drawRoutes();

    if(PreferenceManager
      .getDefaultSharedPreferences( getBaseContext() ).getBoolean(station.slug(), false) || station.number()==1)
    {gpsbtn.setVisibility(View.GONE);}
    else {gpsbtn.setVisibility(View.VISIBLE);}
  }


  /**
   * Put a tour into the background, giving it's polylines and station markers a high alpha value so
   * it blends into the background
   *
   * @param tour Tour to blend with the background
   */
  private void fadeTour( Tour tour ){
    String color = "#30" + tour.color().substring( 1, 7 ); // #xx (Hex) transparency

    polylines.get(tour.slug()).color( Color.parseColor( color ) );

    for( Station station : tour.stations() ){
      markers.get( station.slug() ).alpha( 0.3f );
    }
  }

  public TourList tourlist()
  {return tourlist;}

  public Bitmap scaleMarker (Tour tour, String tmpNumber)
  {Bitmap tmpMarker = markertext(tour,tmpNumber);
    double height, width;
    height = tmpMarker.getHeight();
    width = tmpMarker.getWidth();
    return Bitmap.createScaledBitmap(tmpMarker,(int) (width*1.5),(int) (height*1.5), true);}

  public void startStationActivity()
  {Intent tmpIntent = new Intent( getApplicationContext(), StationActivity.class );


    // Tour data
    Tour tour = singlepage.INSTANCE.selectedTour();
    tmpIntent.putExtra( "name", tour.name() );
    tmpIntent.putExtra( "autor", tour.author() );
    tmpIntent.putExtra( "zeit", tour.time() );
    tmpIntent.putExtra( "laenge", tour.length() );
    tmpIntent.putExtra( "farbe", tour.color() );
    tmpIntent.putExtra( "size", "" + tour.stations().size() );
    // Selected Station
    Station station = singlepage.INSTANCE.selectedStation();
    tmpIntent.putExtra("slug", station.slug());
    tmpIntent.putExtra( "station", station.name() );
    tmpIntent.putExtra( "desc", station.description() );
    tmpIntent.putExtra( "pos", "" + (station.number()) );
    // Station media
    tmpIntent.putExtra( "img", station.imagesToString() );
    tmpIntent.putExtra( "audio", station.audio());
    tmpIntent.putExtra( "video", station.videosToString() );


    startActivityForResult( tmpIntent, 1 );
    overridePendingTransition( R.anim.fade_in, R.anim.map_out );}

  private void vanishTours( Tour tour ){
    adapter.notifyDataSetChanged();

    //Set Numbers on selected Tour
    for(Station station : tour.stations())
    {
      markers.get(station.slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(tour,(station.number()-1)+"")));}

    // Unselect all other tours
    for( Tour t : tourlist.city( visibleCity ).tours() ){
      if( !t.slug().equals( tour.slug() ) ){
    String color = "#00" + t.color().substring( 1, 7 ); // #xx (Hex) transparency

    polylines.get(t.slug()).color( Color.parseColor( color ) );
    for( Station station : t.stations() ){
      markers.get( station.slug() ).alpha( 0f );

    }}
    else{unfadeTour(t);}
    }
    drawRoutes();
  }

  /**
   * Put a tour into the foreground, removing the alpha value from it's polylines and markers
   *
   * @param tour Tour to put into the foreground
   */
  private void unfadeTour( Tour tour ){
    polylines.get(tour.slug()).color( Color.parseColor( tour.color() ) );

    for( Station station : tour.stations() ){
      markers.get(station.slug()).alpha( 1.0f );
    }
  }


  /**
   * Highlight a tour, setting the polyline color to a non-alpha value and the alpha value of the
   * markers to full alpha
   *
   * @param tour Tour that was selected
   */
  public void selectTour( Tour tour ){
    singlepage.INSTANCE.selectedTour(tour);
    //adapter.notifyDataSetChanged();
    unfadeTour( tour );

    for(Station station : tour.stations())
    {markers.get(station.slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(tour,"")));}

    // Unselect all other tours
    for( Tour t : tourlist.city( visibleCity ).tours() ){
      if( !t.slug().equals( tour.slug() ) ){
        fadeTour( t );
      }
    }
  }


  //Setzt alle Touren auf Sichtbar zurück
  public void resetTour(){
    singlepage.INSTANCE.selectedTour(null);
    for( Tour tour : tourlist.city( visibleCity ).tours() ){
      unfadeTour( tour );
    }
    suplInfo( "invisible" );
    drawRoutes();
  }

  public void createDialog(String txt)
  {// Create custom dialog object
    final Dialog dialog = new Dialog(this);
    // Include dialog.xml file
    dialog.setContentView(R.layout.alert_dialog);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();
    // set values for custom dialog components - text, image and button
    TextView text = (TextView) dialog.findViewById(R.id.main_text);
    text.setText(txt);


    Button okayButton = (Button) dialog.findViewById(R.id.left_btn);
    // if decline button is clicked, close the custom dialog
    okayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Close dialog
        dialog.dismiss();}});

    Button declineButton = (Button) dialog.findViewById(R.id.right_btn);
    // if decline button is clicked, close the custom dialog
    declineButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Close dialog
        dialog.dismiss();}});

  }

  public ListView lv()
  {return lv;}

  // Convert a view to bitmap for Pins with Numbers
  public static Bitmap createDrawableFromView(Context context, View view) {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
    view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
    view.buildDrawingCache();
    Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

    Canvas canvas = new Canvas(bitmap);
    view.draw(canvas);

    return bitmap;
  }


  public void initLocationServices(){
    locationManager = (LocationManager) getSystemService( LOCATION_SERVICE );

    locationListener = new LocationListener(){
      @Override
      public void onLocationChanged( Location location ){


        // define new Location
        MyLocation = location;
        pos = new LatLng( location.getLatitude(), location.getLongitude() );

        if(tourlist!=null)
        {positionInCircle(pos);}

        drawRoutes();

      }


      public void positionInCircle(LatLng pos)
      {float[] distance = new float[2];

        for( Tour tour : tourlist.city(visibleCity).tours() ){
        for(Station station : tour.stations())
        {if(station.latlng()!=null){Location.distanceBetween( pos.latitude, pos.longitude,
          station.latlng().latitude, station.latlng().longitude, distance);
          if(distance[0] < radius )
        {//  Initialize SharedPreferences
          SharedPreferences getPrefs = PreferenceManager
            .getDefaultSharedPreferences( getBaseContext() );

          //  Make a new preferences editor
          SharedPreferences.Editor e = getPrefs.edit();

          //  Edit preference to make it false because we don't want this to run again
          e.putBoolean( station.slug(), true);
          //  Apply changes
          e.apply();
        gpsbtn.setVisibility(View.GONE);
        }}}}}


      @Override
      public void onStatusChanged( String provider, int status, Bundle extras ){
      }

      @Override
      public void onProviderEnabled( String provider ){
      }

      @Override
      public void onProviderDisabled( String provider ){
      }
    };
    // Start GPS
    locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
  }


  /**
   * (Re-)Draw the routes of the currently visible tours and their station markers
   */
  private void drawRoutes(){
  if(mMap!=null)  mMap.clear();

    if(pos!=null)
    {MarkerOptions marker = new MarkerOptions();
      marker.position(pos);
      marker.icon(BitmapDescriptorFactory.fromBitmap( BitmapFactory.decodeResource( getResources(), getResources().getIdentifier( "current3", "drawable", getPackageName() ) )));
      if(mMap!=null)mMap.addMarker(marker);}

    for( Map.Entry<String, PolylineOptions> polyline : polylines.entrySet() ){
      mMap.addPolyline( polyline.getValue() );
    }

    drawStations();
  }


  /**
   * (Re-)Draw the station markers of the currently visible tours
   */
  private void drawStations(){
    tmpmarker = null;
    for( Map.Entry<String, MarkerOptions> marker : markers.entrySet() ){
      if(singlepage.INSTANCE.selectedStation()!=null)
      {for(Station station : singlepage.INSTANCE.selectedTour().stations())
        { if(marker.getValue().getPosition()!=null && marker.getKey()==singlepage.INSTANCE.selectedStation().slug() && marker.getKey()==station.slug())
        {tmpmarker = mMap.addMarker(marker.getValue());
          tmpmarker.showInfoWindow();}
          else if(marker.getValue().getPosition()!=null && marker.getKey()==station.slug())
        {mMap.addMarker(marker.getValue());}}}

    else if(marker.getValue().getPosition()!=null)
    {mMap.addMarker( marker.getValue() );}}

    if(circle.getCenter()!=null)
    {mMap.addCircle(circle);}
  }



  //Durch Auswahl einer Tour wird zur Stationenübersicht gewechselt
  public void swapToViewPager( View v ){

    if( stationAdapter.fragments.size() != 0 ){
      stationAdapter.deleteStrings();
      stationAdapter.fragments.clear();
      stationAdapter.notifyDataSetChanged();
    }

    // Create a page for every station
    for( Station station : singlepage.INSTANCE.selectedTour().stations() ){
      stationAdapter.addFragment( station  );
    }
    stationAdapter.notifyDataSetChanged();
    vanishTours(singlepage.INSTANCE.selectedTour());
    Typeface tf = Typeface.createFromAsset(getAssets(), "Bariol_Regular.ttf");
    title.setTypeface(tf);

    mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( wuppertal, CurrentZoom ) );
    mLayout.setPanelState( SlidingUpPanelLayout.PanelState.HIDDEN );   //Hide Slider
    xbtn.setVisibility( View.VISIBLE );
    title.setText( singlepage.INSTANCE.selectedTour().name() );
    title.setVisibility( View.VISIBLE );
    mPager.setVisibility( View.VISIBLE );
    selectStation(singlepage.INSTANCE.selectedTour().station(1));
  }

  //Stationenübersicht schließen und zurück zur Tourenauswahl
  public void swapToSupl(){
    mLayout.setPanelState( SlidingUpPanelLayout.PanelState.COLLAPSED );    //Show Slider
    xbtn.setVisibility( View.GONE );
    title.setVisibility( View.GONE );
    mPager.setVisibility( View.GONE );
    if(stationAdapter.getItem(0)!=null)
    {selectStation(singlepage.INSTANCE.selectedTour().station(1));
      mPager.setCurrentItem(0);}
    player.reset();

    if(singlepage.INSTANCE.selectedStation()!=null)
    {markers.get(singlepage.INSTANCE.selectedStation().slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(singlepage.INSTANCE.selectedTour(), "" + (singlepage.INSTANCE.selectedStation().number()))));
     singlepage.INSTANCE.selectedStation(null);
      gpsbtn.setVisibility(View.GONE);
     circle = new CircleOptions();
    }

    selectTour(singlepage.INSTANCE.selectedTour());
    drawRoutes();

    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    audiobar.setLayoutParams(layoutParams);
    singlepage.INSTANCE.setId(0);
  }

  //Erstelle den Slider
  public void initSupl(){

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    if(!tourlist.city(visibleCity).tours().get(position).equals(singlepage.INSTANCE.selectedTour()))
       { singlepage.INSTANCE.selectedTour(tourlist.city(visibleCity).tours().get(position));
         selectTour(tourlist.city(visibleCity).tours().get(position));}
        System.out.println("Scroll");

      adapter.notifyDataSetChanged();
        lv.smoothScrollToPosition(position);
      drawRoutes();
    }
    });

    adapter = new TourAdapter( tourlist.city( visibleCity ).tours(), this);
    lv.setAdapter( adapter );

  }

  public void initPager(){
    //Initialisiere Pager

    audiobar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        startStationActivity();
      }});

  }


  //Initialisiere Alle Buttons in der Activity
  public void initBtns(){
    zumstart.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        //listelement.setClickable(false);
        swapToViewPager( v );
      }
    });
    x_supl.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        resetTour();
      }
    });


    arrowbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))  {enableLoc();
      //    googleApiClient=null;
        }
        // Navigation require current Location
        if( MyLocation != null && singlepage.INSTANCE.selectedStation()!=null){
          // Uri for google navigation
          String start = MyLocation.getLatitude() + "," + MyLocation.getLongitude();
          String target = singlepage.INSTANCE.selectedStation().latlng().latitude + "," + singlepage.INSTANCE.selectedStation().latlng().longitude;
          String navigationUrl = "http://maps.google.com/maps?" + "saddr=" + start + "&daddr=" + target;

          Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( navigationUrl ) );
          intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS );
          intent.setClassName( "com.google.android.apps.maps", "com.google.android.maps.MapsActivity" );
          startActivity( intent );
        } else if (MyLocation == null){
          Toast.makeText( getApplicationContext(), "GPS Signal wird gesucht...", Toast.LENGTH_SHORT )
               .show();
        }
        else {
          Toast.makeText( getApplicationContext(), "Keine Station ausgewählt.", Toast.LENGTH_SHORT )
            .show();
          }

      }
    });
    tarbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){

      final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))  {enableLoc();
     //   googleApiClient=null;
        }
        if(MyLocation==null && manager.isProviderEnabled(LocationManager.GPS_PROVIDER))Toast.makeText( getApplicationContext(), "GPS Signal wird gesucht...", Toast.LENGTH_SHORT ).show();

        if( MyLocation != null ){ // GPS-Signal ist da
          mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( pos, mMap.getCameraPosition().zoom ) );
        }
    }
    });

    gpsbtn.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
          gpsinfo.setVisibility(View.VISIBLE);

        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
          gpsinfo.setVisibility(View.GONE);

        }
        return false;
      }
    });

  }

  private void enableLoc() {
    if (googleApiClient == null) {
      googleApiClient = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
          @Override
          public void onConnected(Bundle bundle) {

          }
          @Override
          public void onConnectionSuspended(int i) {
            googleApiClient.connect();
          }
        })
        .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
          @Override
          public void onConnectionFailed(ConnectionResult connectionResult) {

            Log.d("Location error","Location error " + connectionResult.getErrorCode());
          }
        }).build();
      googleApiClient.connect();

      LocationRequest locationRequest = LocationRequest.create();
      locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      locationRequest.setInterval(30 * 1000);
      locationRequest.setFastestInterval(5 * 1000);
      LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest);
      builder.setAlwaysShow(true);

      PendingResult<LocationSettingsResult> result =
        LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
      result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(LocationSettingsResult result) {
          final Status status = result.getStatus();
          switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
              try {
                // Show the dialog by calling startResolutionForResult(),
                // and check the result in onActivityResult().
                status.startResolutionForResult(MapsActivity.this, REQUEST_LOCATION);
              } catch (IntentSender.SendIntentException e) {
                // Ignore the error.
              }
              break;
          }
        }
      });
    }

  }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    switch (requestCode) {
      case REQUEST_LOCATION:
        switch (resultCode) {
          case Activity.RESULT_CANCELED: {
            // The user was asked to change settings, but chose not to
            break;
          }
          default: {
            break;
          }
        }
        break;
    }

  }



  @Override
  protected void onPostCreate( Bundle savedInstanceState ){
    super.onPostCreate( savedInstanceState );
    mDrawerToggle.syncState();
  }

  //DRAWER START
  //Trick um den Drawer über die ActionBar zu legen
  private void moveDrawerToTop(){
    LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    DrawerLayout drawer = (DrawerLayout) inflater.inflate( R.layout.decor, null ); // "null" is important.

    // HACK: "steal" the first child of decor view
    ViewGroup decor = (ViewGroup) getWindow().getDecorView();
    View child = decor.getChildAt( 0 );
    decor.removeView( child );
    LinearLayout container = (LinearLayout) drawer.findViewById( R.id.drawer_content ); // This is the container we defined just now.
    container.addView( child, 0 );
    drawer.findViewById( R.id.drawer )
          .setPadding( 0, getStatusBarHeight(), 0, 0 );

    // Make the drawer replace the first child
    decor.addView( drawer );
  }

  public int getStatusBarHeight(){
    int result = 0;
    int resourceId = getResources().getIdentifier( "status_bar_height", "dimen", "android" );
    if( resourceId > 0 ){
      result = getResources().getDimensionPixelSize( resourceId );
    }
    return result;
  }

  private int getContentIdResource(){
    return getResources().getIdentifier( "content", "id", "android" );
  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item ){
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    mDrawerToggle.syncState();
    if( mDrawerToggle.onOptionsItemSelected( item ) ){
      return true;
    }
       /*   int id = item.getItemId();*/

    return super.onOptionsItemSelected( item );
  }

  private void initActionBar(){
    actionBar = getSupportActionBar();

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

    initActionBarBtn();
    actionBar.setElevation( 0 );
  }

  public void initActionBarBtn(){
    homebtn = (ImageButton) findViewById(R.id.homebtn);     //ActionBar Button: Right
    homebtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        if( mDrawerLayout.isDrawerOpen( mDrawer ) ){
          mDrawerLayout.closeDrawer( mDrawer );
        } else {
          mDrawerLayout.openDrawer( mDrawer );
        }
      }
    });
    xbtn = (ImageButton) findViewById(R.id.btn_x);      //ActionBar Button: Right
    title = (TextView) findViewById(R.id.toolbar_title);  //ActionBar Title
    xbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        swapToSupl();
      }
    });
  }

  private void initDrawer(){
    mDrawerList = (ListView) findViewById( R.id.drawerlist );
    mDrawer = (RelativeLayout) findViewById( R.id.drawer );
    mDrawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
    drawerItems = new ArrayList<DrawerItem>();
    mDrawerLayout.setDrawerListener( createDrawerToggle() );
    for( int i = 0; i < drawertitles.length; i++ ){
      DrawerItem items = new DrawerItem( drawertitles[i], drawerIcons[i] );
      drawerItems.add( items );
    }
    draweradapter = new DrawerAdapter( this, drawerItems );
    mDrawerList.setAdapter( draweradapter );

    mDrawerList.setOnItemClickListener( new ListView.OnItemClickListener(){
      @Override
      public void onItemClick( AdapterView<?> parent, View view, int position, long id ){
        //Noch nicht bearbeitet
        mDrawerLayout.closeDrawer( mDrawer );
        // FragmentManager fragmentManager = getSupportFragmentManager();
        // FragmentTransaction ftx = fragmentManager.beginTransaction();
        if( position == 0 ){
          Intent i = new Intent(MapsActivity.this, Einstellungen.class);

          //i.putExtra("tours", tourlist.tours());     //gib touren weiter
          startActivity(i);

        } else if( position == 1 ){

          showIntro();

          //  Initialize SharedPreferences
          SharedPreferences getPrefs = PreferenceManager
            .getDefaultSharedPreferences( getBaseContext() );

          //  Make a new preferences editor
          SharedPreferences.Editor e = getPrefs.edit();

          //  Edit preference to make it false because we don't want this to run again
          e.putBoolean( "firstStart", true );
          //  Apply changes
          e.apply();

        }
        else if(position == 2)
        {Intent i = new Intent(MapsActivity.this, About.class);
          startActivity(i);}

        //  ftx.commit();
      }

    });

    leftbtn = (ImageButton) findViewById(R.id.leftarrow);
    leftbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        mDrawerLayout.closeDrawer( mDrawer );
      }
    });
  }

  private DrawerLayout.DrawerListener createDrawerToggle(){
    mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close ){

      @Override
      public void onDrawerClosed( View view ){
        super.onDrawerClosed( view );
      }

      @Override
      public void onDrawerOpened( View drawerView ){
        super.onDrawerOpened( drawerView );
      }

      @Override
      public void onDrawerStateChanged( int state ){
      }
    };
    return mDrawerToggle;
  }


  //DRAWER END


  //SUPL START
  private SlidingUpPanelLayout.PanelSlideListener onSlideListener(){
    return new SlidingUpPanelLayout.PanelSlideListener(){
      @Override
      public void onPanelSlide( View view, float v ){
        if( singlepage.INSTANCE.selectedTour()!= null ){
          suplInfo( "showall" );
        } else {
          suplInfo( "invisible" );
        }
        panel.setVisibility( View.VISIBLE );
        adapter.notifyDataSetChanged();
      }

      @Override
      public void onPanelCollapsed( View view ){
        //ändere Pfeilrichtung nach oben
        if( singlepage.INSTANCE.selectedTour()!=null){
          suplInfo( "showall" );
        } else {
          suplInfo( "show" );
        }
     //   adapter.notifyDataSetChanged();
      }

      @Override
      public void onPanelExpanded( View view ){//Ändere Pfeilimage nach unten
        suplInfo( "gone" );
        panel.setVisibility( View.GONE );
      //  adapter.notifyDataSetChanged();
      }

      @Override
      public void onPanelAnchored( View view ){
      }

      @Override
      public void onPanelHidden( View view ){
      }
    };
  }

  //zeigt/versteckt, wenn gewünscht, alle Informationen auf dem Panel an
  private void suplInfo(String info){

    if(info.contains("show"))
    {up.setVisibility( View.VISIBLE );
    down.setVisibility( View.GONE );
    tourenliste.setVisibility( View.VISIBLE );

    if(info=="showall"){

      x_supl.setVisibility( View.VISIBLE );
      zumstart.setVisibility( View.VISIBLE );
      subtext1.setVisibility( View.VISIBLE );
      subtext2.setVisibility( View.VISIBLE );
      tourenliste.setText( singlepage.INSTANCE.selectedTour().name() );
      subtext1.setText( singlepage.INSTANCE.selectedTour().author() );
      subtext2.setText( singlepage.INSTANCE.selectedTour().time() + "/" + singlepage.INSTANCE.selectedTour().length() );
      } else {
        tourenliste.setText( "Tourenliste" );
      }
    }
   if(info=="invisible")
     {x_supl.setVisibility( View.INVISIBLE );
        zumstart.setVisibility( View.INVISIBLE );
        subtext1.setVisibility( View.INVISIBLE );
        subtext2.setVisibility( View.INVISIBLE );
        tourenliste.setVisibility( View.VISIBLE );
        tourenliste.setText( "Tourenliste" );}

    if(info=="gone")
    {tourenliste.setVisibility( View.GONE );
      x_supl.setVisibility( View.GONE );
      zumstart.setVisibility( View.GONE );
      subtext1.setVisibility( View.GONE );
      subtext2.setVisibility( View.GONE );
      up.setVisibility( View.GONE );
      down.setVisibility( View.VISIBLE );}

  }

  @Override
  public void onBackPressed(){
    if( mLayout != null && mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED )      //Wenn SUPL geöffnet, und zurück gedrückt wird, schließe nur SUPL
    {
      mLayout.setPanelState( SlidingUpPanelLayout.PanelState.COLLAPSED );
    } else if( mPager.getVisibility() == View.VISIBLE ){
      swapToSupl();
    } else if( getFragmentManager().getBackStackEntryCount() == 0 ){
      super.onBackPressed();
      // GPS cancel
      locationManager.removeUpdates( locationListener );
    }
  }

  @Override
  public void onPause(){
    super.onPause();
    locationManager.removeUpdates( locationListener );
  }

  @Override
  public void onStop(){
    super.onStop();
    locationManager.removeUpdates( locationListener );
  }

  @Override
  public void onRestart(){
    super.onRestart();
    locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
  }


  /***********************************************************************************************
   *                                       Helper Methods
   ***********************************************************************************************/

  /**
   * Will create polylines for all the tours in the visibleCity
   */
  private void makePolylines(){
    for( Tour tour : tourlist.city(visibleCity).tours() ){
      PolylineOptions polyline = new PolylineOptions();

      for( RouteWaypoint waypoint : tour.route().waypoints() ){
        polyline.add( new LatLng( waypoint.latitude(), waypoint.longitude() ) );
      }

      polyline.color( Color.parseColor( tour.color() ) );
      polylines.put( tour.slug(), polyline );

      makeMarkers( tour );
    }

  }


  /**
   * Will create the markers on the map
   * @param tour Tour object to create the markers from
   */
  private void makeMarkers( Tour tour ){
    for( Station station : tour.stations() ){
      MarkerOptions marker = new MarkerOptions();

      marker.position( station.latlng() );
      marker.icon( BitmapDescriptorFactory.fromBitmap( markertext(tour,"") ) );

      markers.put( station.slug(), marker );
    }

  }

public Bitmap markertext(Tour tour, String text)
{
  View markerlayout = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_layout, null);
  int id = getResources().getIdentifier( "pin_" + tour.trkid(), "drawable", getPackageName() );
  TextView markertxt = (TextView)markerlayout.findViewById(R.id.markernumber);
  ImageView markerimage = (ImageView) markerlayout.findViewById(R.id.marker);
  markerimage.setImageResource(id);

  markertxt.setText(text);
  return createDrawableFromView(this, markerlayout);
}

  /**
   * Loads the list of available tours
   */
  private void loadTourdata(){
    tourlist = new TourListReader( this ).readTourData();
    makePolylines();
    drawRoutes();

    initSupl();
  }

  @Override
  public void newTourdataAvailable(){
    Updater.get( getBaseContext() ).downloadTourlist();
  }

  @Override
  public void noNewTourdataAvailable(){
    checkedForUpdates = true;
    loadTourdata();}

  @Override
  public void tourlistDownloaded(){
    Updater.get( getBaseContext() ).downloadTourdata();
  }

  @Override
  public void tourdataDownloaded(){
    checkedForUpdates = true;
    loadTourdata();
  }

}
