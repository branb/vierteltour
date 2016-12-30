package com.uni_wuppertal.iad.vierteltour.ui.map;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import com.uni_wuppertal.iad.vierteltour.ui.intro.IntroActivity;

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
import java.util.Map;


public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback, UpdateListener{

  public Location MyLocation;
  public LatLng pos;
  public LocationManager locationManager;
  public LocationListener locationListener;
  public MarkerOptions CurrentMarker;
  public int CurrentZoom = 15;
  int[] drawerIcons = new int[]{ R.drawable.einstellungen,
                                 R.drawable.hilfe,
                                 R.drawable.about,
                                 R.drawable.karte
  };
  String[] drawertitles = new String[]{ "Einstellungen",
                                        "Hilfe",
                                        "About",
                                        "Karte"
  };
  String[] drawersubtitles = new String[]{ " ",
                                           " ",
                                           " ",
                                           "hell / dunkel"
  };

  private ActionBar actionBar;
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private SlidingUpPanelLayout mLayout;
  private RelativeLayout mDrawer;
  private ViewPager mPager;
  private StationAdapter stationAdapter;
  private DrawerAdapter draweradapter;
  private TourAdapter adapter;
  private List<DrawerItem> drawerItems;
  private LatLng wuppertal;
  private ImageButton xbtn, zumstart, homebtn, leftbtn, imgbtn1, arrowbtn, tarbtn;
  private ImageView up, down;
  private ListView lv;
  private TextView title, tourenliste, subtext1, subtext2;
  private RelativeLayout panel;
  public static RelativeLayout audiobar;
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

  }

  public void initAll() {
    ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this); //initMap

    mPager = (ViewPager) findViewById(R.id.pager);
    stationAdapter = new StationAdapter(getSupportFragmentManager(), this);
    mPager.setAdapter(stationAdapter);
    audiobar = (RelativeLayout) findViewById(R.id.audiobar);


    mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
    mLayout.setPanelSlideListener(onSlideListener());
    lv = (ListView) findViewById(R.id.list);
    panel = (RelativeLayout) findViewById(R.id.panelhalf);

    zumstart = (ImageButton) findViewById(R.id.zumstart);       //SUPL Button bottom right

    imgbtn1 = (ImageButton) findViewById( R.id.x );               //SUPL Button top left

    arrowbtn = (ImageButton) findViewById( R.id.arrowbtn );       //Top Twin Button



    tarbtn = (ImageButton) findViewById( R.id.tarbtn );           //Bot Twin Button

    tourenliste = (TextView) findViewById( R.id.tourenliste );

    subtext1 = (TextView) findViewById( R.id.subinfo1 );
    subtext2 = (TextView) findViewById( R.id.subinfo2 );
    up = (ImageView) findViewById( R.id.up );
    down = (ImageView) findViewById( R.id.down );
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
    } else if( !Updater.get( getBaseContext() ).checkingForUpdates() ) {
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
              showInfo( true );
            }
          }
          if( !tourSelected ){
            resetTour();
          }
          drawRoutes();
        }
      }
    };
    mMap.setOnMapClickListener( listener );

    mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener(){
      @Override
      public boolean onMarkerClick( Marker marker ){
        listener.onMapClick( marker.getPosition() );
        return true;    // false: OnMarkerClick aktiv und zoomt zum Marker
      }
    } );

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

  private void vanishTours( Tour tour ){
     singlepage.INSTANCE.selectedTour(tour);
    adapter.select( tour );

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
    adapter.select( tour );
    unfadeTour( tour );

    // Unselect all other tours
    for( Tour t : tourlist.city( visibleCity ).tours() ){
      if( !t.slug().equals( tour.slug() ) ){
        fadeTour( t );
      }
    }
  }


  //Setzt alle Touren auf Sichtbar zurück
  public void resetTour(){
    for( Tour tour : tourlist.city( visibleCity ).tours() ){
      unfadeTour( tour );
    }
    hideInfo( false );
    drawRoutes();
  }


  public void initLocationServices(){
    locationManager = (LocationManager) getSystemService( LOCATION_SERVICE );

    locationListener = new LocationListener(){
      @Override
      public void onLocationChanged( Location location ){
        drawRoutes();

        // define new Location
        MyLocation = location;
        pos = new LatLng( location.getLatitude(), location.getLongitude() );
        // Draw new position of marker
        int id = getResources().getIdentifier( "current3", "drawable", getPackageName() );
        Bitmap icon = BitmapFactory.decodeResource( getResources(), id );
        CurrentMarker = new MarkerOptions().position( pos )
                                           .icon( BitmapDescriptorFactory.fromBitmap( icon ) )
                                           .anchor( 0.5f, 0.5f );
        mMap.addMarker( CurrentMarker );
      }

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

    for( Map.Entry<String, PolylineOptions> polyline : polylines.entrySet() ){
      mMap.addPolyline( polyline.getValue() );
    }

    drawStations();
  }


  /**
   * (Re-)Draw the station markers of the currently visible tours
   */
  private void drawStations(){
    for( Map.Entry<String, MarkerOptions> marker : markers.entrySet() ){
     if(marker.getValue().getPosition()==null)
     {}
      else {mMap.addMarker( marker.getValue() );}
    }
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
      stationAdapter.addFragment( station.number() - 1, singlepage.INSTANCE.selectedTour() );
    }

    vanishTours(singlepage.INSTANCE.selectedTour());
    Typeface tf = Typeface.createFromAsset(getAssets(), "Bariol_Regular.ttf");
    title.setTypeface(tf);

    mLayout.setPanelState( SlidingUpPanelLayout.PanelState.HIDDEN );   //Hide Slider
    xbtn.setVisibility( View.VISIBLE );
    title.setText( singlepage.INSTANCE.selectedTour().name() );
    title.setVisibility( View.VISIBLE );
    mPager.setVisibility( View.VISIBLE );

  }

  //Stationenübersicht schließen und zurück zur Tourenauswahl
  public void swapToSupl(){
    mLayout.setPanelState( SlidingUpPanelLayout.PanelState.COLLAPSED );    //Show Slider
    xbtn.setVisibility( View.GONE );
    title.setVisibility( View.GONE );
    mPager.setVisibility( View.GONE );
    player.stop();
    selectTour(singlepage.INSTANCE.selectedTour());
    drawRoutes();
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    audiobar.setLayoutParams(layoutParams);
    singlepage.INSTANCE.setId(0);
  }

  //Erstelle den Slider
  public void initSupl(){


    lv.setOnItemClickListener( new AdapterView.OnItemClickListener(){
      @Override
      public void onItemClick( AdapterView<?> parent, View view, int position, long id ){
        selectTour( tourlist.city( visibleCity ).tours().get( position ) );
        drawRoutes();
      }
    });

    adapter = new TourAdapter( this, tourlist.city( visibleCity ).tours());
    lv.setAdapter( adapter );
  }


  public void initPager(){
    //Initialisiere Pager

    audiobar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        Intent tmpIntent = new Intent( getApplicationContext(), StationActivity.class );

        // Tour data
        tmpIntent.putExtra( "name", singlepage.INSTANCE.selectedTour().name() );
        tmpIntent.putExtra( "autor", singlepage.INSTANCE.selectedTour().author() );
        tmpIntent.putExtra( "zeit", singlepage.INSTANCE.selectedTour().time() );
        tmpIntent.putExtra( "laenge", singlepage.INSTANCE.selectedTour().length() );
        tmpIntent.putExtra( "farbe", singlepage.INSTANCE.selectedTour().color() );
        tmpIntent.putExtra( "size", "" + singlepage.INSTANCE.selectedTour().stations().size() );
        // Selected Station
        Station station = singlepage.INSTANCE.selectedTour().stations().get( singlepage.INSTANCE.position());
        tmpIntent.putExtra( "station", station.name() );
        tmpIntent.putExtra( "desc", station.description() );
        tmpIntent.putExtra( "pos", "" + (singlepage.INSTANCE.position() + 1) );
        // Station media
        tmpIntent.putExtra( "img", station.imagesToString() );
        tmpIntent.putExtra( "audio", station.audio());
        tmpIntent.putExtra( "video", station.videosToString() );

        overridePendingTransition( R.anim.fade_in, R.anim.map_out );
        startActivity( tmpIntent );
      }});
  }


  //Initialisiere Alle Buttons in der Activity
  public void initBtns(){
    zumstart.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        swapToViewPager( v );
      }
    });
    imgbtn1.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        resetTour();
      }
    });



    arrowbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        // Navigation require current Location
        if( MyLocation != null ){
          // Uri for google navigation
          String start = MyLocation.getLatitude() + "," + MyLocation.getLongitude();
          String target = wuppertal.latitude + "," + wuppertal.longitude;
          String navigationUrl = "http://maps.google.com/maps?"
            + "saddr=" + start
            + "&daddr=" + target;

          Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( navigationUrl ) );
          intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS );
          intent.setClassName( "com.google.android.apps.maps", "com.google.android.maps.MapsActivity" );
          startActivity( intent );
        } else {
          Toast.makeText( getApplicationContext(), "Standort unbekannt", Toast.LENGTH_SHORT )
               .show();
        }

      }
    });
    tarbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        if( MyLocation != null ){ // GPS-Signal ist da
          Toast.makeText( getApplicationContext(), "Signal da!", Toast.LENGTH_SHORT )
               .show();
          mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( pos, CurrentZoom ) );
        } else { // GPS-Signal nicht da
          Toast.makeText( getApplicationContext(), "Kein Signal", Toast.LENGTH_SHORT )
               .show();
        }
      }
    });
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
      DrawerItem items = new DrawerItem( drawertitles[i], drawersubtitles[i], drawerIcons[i] );
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
        if( !singlepage.INSTANCE.selectedTour().slug().isEmpty() ){
          showInfo( true );
        } else {
          hideInfo( false );
        }
        panel.setVisibility( View.VISIBLE );
      }

      @Override
      public void onPanelCollapsed( View view ){
        //ändere Pfeilrichtung nach oben
        if( !singlepage.INSTANCE.selectedTour().slug().isEmpty() ){
          showInfo( true );
        } else {
          showInfo( false );
        }
      }

      @Override
      public void onPanelExpanded( View view ){//Ändere Pfeilimage nach unten
        hideInfo( true );
        panel.setVisibility( View.GONE );
      }

      @Override
      public void onPanelAnchored( View view ){
      }

      @Override
      public void onPanelHidden( View view ){
      }
    };
  }

  //zeigt, wenn gewünscht, alle Informationen auf dem Panel an
  // TODO: Turn this and hideinfo into a toggle method, and rename those silly names here as well...
  private void showInfo( boolean all ){



    up.setVisibility( View.VISIBLE );
    down.setVisibility( View.GONE );
    tourenliste.setVisibility( View.VISIBLE );

    if( all ){


      imgbtn1.setVisibility( View.VISIBLE );
      zumstart.setVisibility( View.VISIBLE );
      subtext1.setVisibility( View.VISIBLE );
      subtext2.setVisibility( View.VISIBLE );
      if( !singlepage.INSTANCE.selectedTour().slug().isEmpty() ){
        tourenliste.setText( singlepage.INSTANCE.selectedTour().name() );
        subtext1.setText( singlepage.INSTANCE.selectedTour().author() );
        subtext2.setText( singlepage.INSTANCE.selectedTour().time() + "/" + singlepage.INSTANCE.selectedTour().length() );
      } else {
        tourenliste.setText( "Tourenliste" );
      }
    }


  }

  //versteckt, wenn gewünscht, alle Informationen auf dem Panel
  private void hideInfo( boolean all ){

    if( !all ){


      imgbtn1.setVisibility( View.INVISIBLE );
      zumstart.setVisibility( View.INVISIBLE );
      subtext1.setVisibility( View.INVISIBLE );
      subtext2.setVisibility( View.INVISIBLE );
      tourenliste.setVisibility( View.VISIBLE );
      tourenliste.setText( "Tourenliste" );
    } else {

      tourenliste.setVisibility( View.GONE );
      imgbtn1.setVisibility( View.GONE );
      zumstart.setVisibility( View.GONE );
      subtext1.setVisibility( View.GONE );
      subtext2.setVisibility( View.GONE );
      up.setVisibility( View.GONE );
      down.setVisibility( View.VISIBLE );
    }
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
    checkForUpdates();
    locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
  }


  /***********************************************************************************************
   *                                       Helper Methods
   ***********************************************************************************************/

  /**
   * Will create polylines for all the tours in the visibleCity
   */
  // TODO: Remove the old polylines before you add new ones!
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
  // TODO: Rmove the old markers before adding new ones
  private void makeMarkers( Tour tour ){
    for( Station station : tour.stations() ){
      MarkerOptions marker = new MarkerOptions();

      int id = getResources().getIdentifier( "pin_" + tour.trkid(), "drawable", getPackageName() );

      Bitmap icon = BitmapFactory.decodeResource( getResources(), id );

      marker.position( station.latlng() );
      marker.icon( BitmapDescriptorFactory.fromBitmap( icon ) );

      markers.put( station.slug(), marker );
    }

  }


  /**
   * Loads the list of available tours
   */
  private void loadTourdata(){
    tourlist = new TourListReader( this ).readTourList();

    Log.d( "Xml/getCity", "Searching for City 'wuppertal': " + tourlist.city( "wuppertal" ).name() );

    Tour tourFortschrott = tourlist.tour( "fortschrott" );
    Log.d( "Xml/getTour", "Searching for Tour 'fortschrott': " + tourFortschrott.name() );
    Log.d( "Xml/getFortschrottHome", "Home directory': " + tourFortschrott.home() );

    RouteWaypoint waypointFortschrott = tourFortschrott.route().waypoints().get( 0 );
    Log.d( "Xml/getFortschrottRoute", "First coordinates of Fortschrott Route: " + waypointFortschrott.latitude() + " / " + waypointFortschrott.longitude() );

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
    loadTourdata();
  }

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
