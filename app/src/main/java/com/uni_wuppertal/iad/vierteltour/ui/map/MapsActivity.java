package com.uni_wuppertal.iad.vierteltour.ui.map;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.pixplicity.sharp.Sharp;
import com.pixplicity.sharp.SharpDrawable;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import com.uni_wuppertal.iad.vierteltour.ui.drawer.intro.IntroActivity;
import com.uni_wuppertal.iad.vierteltour.ui.gallery.GalleryMode;
import com.uni_wuppertal.iad.vierteltour.ui.map.Marker.MapWindowAdapter;
import com.uni_wuppertal.iad.vierteltour.ui.map.station_pager.ClickableViewpager;
import com.uni_wuppertal.iad.vierteltour.ui.map.station_pager.ShadowTransformer;
import com.uni_wuppertal.iad.vierteltour.ui.drawer.about.About;
import com.uni_wuppertal.iad.vierteltour.ui.drawer.einstellungen.Einstellungen;
import com.uni_wuppertal.iad.vierteltour.ui.station.Stationbeendet;
import com.uni_wuppertal.iad.vierteltour.utility.Singletonint;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.ViertelTourMediaPlayer;

import com.uni_wuppertal.iad.vierteltour.ui.map.station_pager.FragmentAdapter;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.up_slider.TourAdapter;
import com.uni_wuppertal.iad.vierteltour.ui.drawer.DrawerAdapter;
import com.uni_wuppertal.iad.vierteltour.ui.drawer.DrawerItem;
import com.uni_wuppertal.iad.vierteltour.utility.updater.Updater;
import com.uni_wuppertal.iad.vierteltour.utility.updater.UpdateListener;
import com.uni_wuppertal.iad.vierteltour.utility.storage.OurStorage;
import com.uni_wuppertal.iad.vierteltour.utility.ReplaceFont;
import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourList;
import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourListReader;
import com.uni_wuppertal.iad.vierteltour.utility.waypoints.RouteWaypoint;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Station;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Tour;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.location.GpsStatus.GPS_EVENT_STOPPED;

/**
 * MapsActivity is the main activity of the application
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

  public Location MyLocation;
  public LatLng pos;
  public LocationManager locationManager;
  public LocationListener locationListener;
  final static int REQUEST_LOCATION = 1;
  private GoogleApiClient googleApiClient;
  private LocationSettingsRequest.Builder builder;
  public int CurrentZoom = 15;
  String[] drawertitles = new String[]{"Touren löschen",
    "Einführung",
    "About",
    "Updates suchen",
    "Touren freischalten",
    "Touren sperren"
  };
  protected static final int TINY_BAR = 0x101, BIG_BAR = 0x102, SEEK_BAR = 0x103;
  private final double radius = 25;
  private ActionBar actionBar;
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private SlidingUpPanelLayout supl;
  private RelativeLayout mDrawer, pager_layout, gpsbtn_layout;
  private LinearLayout slidingLayout;
  public static ClickableViewpager mPager;
  private FragmentAdapter fragmentAdapter;
  private DrawerAdapter draweradapter;
  public static TourAdapter adapter;
  private List<View> tourlistview;
  private List<DrawerItem> drawerItems;
  private LatLng wuppertal;
  private ImageButton xbtn, zumstart, homebtn, leftbtn, x_supl, arrowbtn, tarbtn;
  private ImageButton gpsbtn;
  private int slidingLayoutHeight;
  private ImageView up, down;
  private ExpandableListView lv;
  private DisplayMetrics displayMetrics = new DisplayMetrics();
  private int defaultPanelHeight, pxPager, lastExpandedPosition = -1;;
  private View listelement;
  private ShadowTransformer mFragmentShadowTransformer;
  private TextView title, tourenliste, subtext1, subtext2;
  private Marker tmpmarker, curLocation;
  private RelativeLayout panel, gpsinfo, stationLayout;
  private ViertelTourMediaPlayer player;
  private Singletonint singlepage;
  static final int BACK_FROM_SETTINGS = 1, BACK_FROM_GALLERY = 2, BACK_FROM_STATION_FINISHED = 3, STATION_BEENDET_GALLERY = 4;  // The request code
  // All the tour stationactivity that is currently available to us
  private TourList tourlist;

  //Start StationActivity
  String color, author, name, length, desc, time, slug, path;
  int number, size;
  static boolean stationActivityRunning = false;
  SeekBar seekbar, seekbar_supl;        //Fortschrittsbalken
  ImageButton play_button, play_button_supl;      //diverse Bilderbuttons
  int isimages = -1;
  boolean startaudio = true;  //Variable für Status des Play-Buttons
  Handler seekHandler = new Handler();
  ;
  TextView duration, duration_supl;  //Textfeld
  TextView routenname, prof, info2, description, stationtitle;
  double timeElapsed = 0;
  int dotsCount;
  ImageView dots[], tourimage, pager_play_btn, pfeilhell;
  Intent myIntent2;
  Bundle b;
  String colorString;
  ArrayList<String> stationImagePaths;
  RelativeLayout layout, seekbar_layout_supl;
  ViewPager imagePager;    //Slidebare Gallery
  com.uni_wuppertal.iad.vierteltour.ui.station.StationAdapter mAdapter;
  ScrollView scroll;
  LinearLayout pager_indicator;
  RelativeLayout gesperrt, videopanel, panel_top;
  boolean sperrvariable = true, stationEnabled = false;
  String audio, video;
  //End StationActivity


  private GoogleMap mMap;

  // Indicates, if we have checked for new updates on tourdata. Needed at the start of the app
  private boolean tourdataAvailable = false, zoomToLocation = false;

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

  private Map<String, Marker> tourMarker = new HashMap<String, Marker>();

  private CircleOptions circle = new CircleOptions();
  private Circle mapCircle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ReplaceFont.replaceDefaultFont(this, "MONOSPACE", "Bariol_Regular.ttf");

    setContentView(R.layout.activity_main);

    player = ViertelTourMediaPlayer.getInstance(this);

    initLocationServices();
    initAll();

    initPager();

    moveDrawerToTop();
    initActionBar();
    initDrawer();


    mFragmentShadowTransformer = new ShadowTransformer(mPager, fragmentAdapter, this);
    mPager.setPageTransformer(false, mFragmentShadowTransformer);


  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (googleApiClient != null) googleApiClient.disconnect();
    if (fragmentAdapter.fragments.size() != 0) {
      fragmentAdapter.deleteStrings();
      fragmentAdapter.fragments.clear();
    }
  }


  /**
   * Initializes everything on this activity
   */
  public void initAll() {
    ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this); //initMap

    pager_layout = (RelativeLayout) findViewById(R.id.pager_layout);

    mPager = (ClickableViewpager) findViewById(R.id.pager);
    fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
    mPager.setAdapter(fragmentAdapter);

    mPager.setOnItemClickListener(new ClickableViewpager.OnItemClickListener() {
      @Override
      public void onItemClick(int position) {
        if (singlepage.INSTANCE.onfragmentclicked() != -1) {
          mPager.setCurrentItem(singlepage.INSTANCE.onfragmentclicked() - 1);
          if (singlepage.INSTANCE.selectedStation().number() == singlepage.INSTANCE.onfragmentclicked()) {
            startStationActivity();
          }
        }
      }
    });


    supl = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
    slidingLayout = (LinearLayout) findViewById(R.id.dragView);

    lv = (ExpandableListView) findViewById(R.id.list);
    panel_top = (RelativeLayout) findViewById(R.id.panelhalf1);
    panel = (RelativeLayout) findViewById(R.id.panelhalf);

    zumstart = (ImageButton) findViewById(R.id.zumstart);       //SUPL Button bottom right
    Sharp.loadResource(getResources(), R.raw.zum_start).into(zumstart);

    x_supl = (ImageButton) findViewById(R.id.x);               //SUPL Button top left
    Sharp.loadResource(getResources(), R.raw.beenden_dunkel).into(x_supl);

    arrowbtn = (ImageButton) findViewById(R.id.arrowbtn);       //Top Twin Button
    Sharp.loadResource(getResources(), R.raw.google_navi_dunkel).into(arrowbtn);

    gpsbtn_layout = (RelativeLayout) findViewById(R.id.gpsbtn);
    gpsbtn = (ImageButton) findViewById(R.id.ausrufezeichen);           //Red Button left Bottom corner
    Sharp.loadResource(getResources(), R.raw.ausrufezeichen).into(gpsbtn);

    tarbtn = (ImageButton) findViewById(R.id.tarbtn);           //Bot Twin Button
    Sharp.loadResource(getResources(), R.raw.standort).into(tarbtn);

    tourenliste = (TextView) findViewById(R.id.tourenliste);

    subtext1 = (TextView) findViewById(R.id.subinfo1);
    subtext2 = (TextView) findViewById(R.id.subinfo2);
    up = (ImageView) findViewById(R.id.up);
    Sharp.loadResource(getResources(), R.raw.pfeil_hoch).into(up);

    down = (ImageView) findViewById(R.id.down);
    Sharp.loadResource(getResources(), R.raw.pfeil_runter).into(down);

    stationLayout = (RelativeLayout) findViewById(R.id.station);
    gpsinfo = (RelativeLayout) findViewById(R.id.gpsinfo);
    seekbar_layout_supl = (RelativeLayout) findViewById(R.id.media_panel_supl);
    duration_supl = (TextView) findViewById(R.id.duration_supl);
    seekbar_supl = (SeekBar) findViewById(R.id.seek_bar_supl);
    play_button_supl = (ImageButton) findViewById(R.id.play_button_supl);
    Sharp.loadResource(getResources(), R.raw.play_dunkel).into(play_button_supl);

    ImageView gpsarrow = (ImageView) findViewById(R.id.arrowwhite);
    Sharp.loadResource(getResources(), R.raw.google_navi_hell).into(gpsarrow);
  }

  /**
   * shows Intro of the application
   */
  private void showIntro() {
    //  Declare a new thread to do a preference check
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        //  If the activity has never started before...
        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("firstStart", true)) {
          //  Launch app intro
          Intent i = new Intent(MapsActivity.this, IntroActivity.class);
          startActivity(i);
          overridePendingTransition(0, 0);
        }

      }
    });

    // Start the thread
    t.start();
  }

  /**
   * Get notified when the map is ready to be used.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    // Calculate ActionBar height
  /*  int actionBarHeight=0;
    TypedValue tv = new TypedValue();
    if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
    {
      actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
    }*/
    wuppertal = new LatLng(51.256972, 7.139341);
    //Gesamte Bildschirmgröße - Toolbargröße
    slidingLayoutHeight = slidingLayout.getLayoutParams().height+20;
    mMap.setPadding(0, 0, 0, slidingLayoutHeight);

    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wuppertal, CurrentZoom));
    loadTourdata();

    /**
     * When the user clicks anywhere on the map, check which tour or station he clicked onto and mark it as
     * selected
     *
     *
     */
    final GoogleMap.OnMapClickListener listener = new GoogleMap.OnMapClickListener() {
      @Override
      public void onMapClick(LatLng clickCoords) {
        if (tourdataAvailable) {
          if (supl.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {

            boolean test = false;
            for (Tour tour : tourlist.city(visibleCity).tours()) {

              if (PolyUtil.isLocationOnPath(clickCoords, tour.route().latLngs(), false, 10)) {
                test = true;
                if (singlepage.INSTANCE.selectedTour() != tour) {
                  selectTour(tour);
                  suplInfo("showall");
                  drawRoutes();
                  break;
                }
              }
            }
            if (singlepage.INSTANCE.selectedTour() != null && !test) {
              resetTour();
            }

          }
          //Stationenübersicht
          else if (supl.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
            Boolean onMapClicked = false;       //unselect Stations
            Tour tour = singlepage.INSTANCE.selectedTour();
            for (Station station : tour.stations()) {
              if (clickCoords.equals(station.latlng())) {
                onMapClicked = true;
                mPager.setCurrentItem(station.number() - 1, false);
                fragmentAdapter.notifyDataSetChanged();
              }

            }
            if (!onMapClicked && tmpmarker != null) tmpmarker.showInfoWindow();


          }
        }


      }
    };

    mMap.setOnMapClickListener(listener);
    mMap.setInfoWindowAdapter(new MapWindowAdapter(this));

    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
      @Override
      public boolean onMarkerClick(Marker marker) {
        listener.onMapClick(marker.getPosition());
        return true;    // false: OnMarkerClick aktiv und zoomt zum Marker
      }
    });

    showIntro();
  }

  /**
   * Will be used if a station is selected
   *
   * @param station Station will be marked as selected
   */
//Wird aufgerufen, sobald eine Station ausgewaehlt wird
  public void selectStation(Station station) {//selectedOldStation dient als Zwischenspeicher der abgewaehlten Station
//selectedStation dient als Zwischenspeicher der ausgewaehlten Station
//vorherig ausgewaehlte Station wird abgewaehlte Station
    singlepage.INSTANCE.selectedOldStation(singlepage.INSTANCE.selectedStation());   //vorherige Station wird alte Station
    singlepage.INSTANCE.selectedStation(station);       //Setzt neue Station
    //loescht alte Station, setzt Groesse auf Ursprung zurueck
    //Ausnahme: Einleitungen haben keine Marker
    if (singlepage.INSTANCE.selectedOldStation() != null && !singlepage.INSTANCE.selectedOldStation().slug().contains("einleitung")) {
      removeStation(singlepage.INSTANCE.selectedOldStation().slug());
      Marker m;
      if (singlepage.INSTANCE.selectedTour().station(1).slug().contains("einleitung"))
        m = mMap.addMarker(markers.get(singlepage.INSTANCE.selectedOldStation().slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(singlepage.INSTANCE.selectedTour(), singlepage.INSTANCE.selectedOldStation().number() - 1 + "", false))));
        //Ausgewaehlte Station wird per Bitmap groesser skaliert
      else
        m = mMap.addMarker(markers.get(singlepage.INSTANCE.selectedOldStation().slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(singlepage.INSTANCE.selectedTour(), singlepage.INSTANCE.selectedOldStation().number() + "", false))));
      //Erstellte Bitmap wird der Karte hinzugefuegt
      tourMarker.put(singlepage.INSTANCE.selectedOldStation().slug(), m);

      //delete Circle
      if (mapCircle != null) mapCircle.remove();
    }

    //Lösche neue Station und setze vergrößerten Pin
    if (!singlepage.INSTANCE.selectedStation().slug().contains("einleitung")) {// Setze Kreis auf neue Station
      circle.center(station.latlng()).radius(radius).fillColor(Color.parseColor(singlepage.INSTANCE.selectedTour().color().substring(0, 1) + "75" + singlepage.INSTANCE.selectedTour().color().substring(1, singlepage.INSTANCE.selectedTour().color().length()))).strokeColor(Color.parseColor(singlepage.INSTANCE.selectedTour().color())).strokeWidth(8).visible(true);
      mapCircle = mMap.addCircle(circle);
      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(station.latlng(), mMap.getCameraPosition().zoom));

      removeStation(singlepage.INSTANCE.selectedStation().slug());
      Marker m;
      int countnumber = 0;
      if (singlepage.INSTANCE.selectedTour().station(1).slug().contains("einleitung")) {
        try {
          while (singlepage.INSTANCE.countWaypoints().get(countnumber) < (station.number() - 1))
            countnumber++;
        } catch (Exception e) {
        }
        m = mMap.addMarker(markers.get(station.slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(singlepage.INSTANCE.selectedTour(), "" + (station.number() - 1), true))));
      } else {
        try {
          while (singlepage.INSTANCE.countWaypoints().get(countnumber) < (station.number()))
            countnumber++;
        } catch (Exception e) {}
        m = mMap.addMarker(markers.get(station.slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(singlepage.INSTANCE.selectedTour(), "" + (station.number()), true))));
      }
      m.showInfoWindow();
      tourMarker.put(singlepage.INSTANCE.selectedStation().slug(), m);
    }

    if (PreferenceManager
      .getDefaultSharedPreferences(getBaseContext()).getBoolean(station.slug(), false) || station.slug().startsWith("einleitung")) {
      gpsbtn_layout.setVisibility(View.GONE);
    } else {
      gpsbtn_layout.setVisibility(View.VISIBLE);
    }
  }


  /**
   * Put a tour into the background, giving it's polylines and station markers a high alpha value so
   * it blends into the background
   *
   * @param tour Tour to blend with the background
   */
  private void fadeTour(Tour tour) {
    String color = "#30" + tour.color().substring(1, 7); // #xx (Hex) transparency

    polylines.get(tour.slug()).color(Color.parseColor(color));

    for (Station station : tour.stations()) {
      markers.get(station.slug()).alpha(0.3f);
    }
  }

  public TourList tourlist() {
    return tourlist;
  }

  /**
   * Redraws Marker with a bigger size
   *
   * @param tour      Tour is needed to get the right Marker Color
   * @param tmpNumber Number is needed to draw the right number into the Marker
   * @return Marker as Bitmap will be returned
   */
/*  public Bitmap scaleMarker(Tour tour, String tmpNumber) {
    path = OurStorage.get(this).storagePath() + "/" + OurStorage.get(this).lookForTourFile(tourlist(), tour.image())+"pin.svg";
    markerimage.setImageDrawable(Sharp.loadFile(new File(path)).getDrawable());
    Bitmap tmpMarker = markertext(tour, tmpNumber);
    double height, width;
    height = tmpMarker.getHeight();
    width = tmpMarker.getWidth();
    return Bitmap.createScaledBitmap(tmpMarker, (int) (width * 1.5), (int) (height * 1.5), true);
  }*/

  Handler myHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MapsActivity.TINY_BAR:
          supl.setPanelHeight(panel_top.getHeight());
          ;
          break;

        case MapsActivity.BIG_BAR:
          supl.setPanelHeight(defaultPanelHeight);
          supl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
          break;

        case MapsActivity.SEEK_BAR:
          supl.setPanelHeight(panel_top.getHeight() + 63 + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, displayMetrics));
          break;
      }
      super.handleMessage(msg);
    }
  };


  //Station Activity

  /**
   * Starts Station Activity will all needed Extras
   */
  public void startStationActivity() {
    pager_layout.setVisibility(View.GONE);
    mMap.setPadding(0, 0, 0, 0);
    startStationLayout();
    supl.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
  }


  public void startStationLayout() {//if(seekbar_layout_supl.getVisibility()==View.VISIBLE)suplInfo("h_seekbar");
    lv.setVisibility(View.GONE);
    slidingLayout.setVisibility(View.VISIBLE);
    stationLayout.setVisibility(View.VISIBLE);

    supl.setScrollableView(scroll);
    x_supl.setVisibility(View.GONE);
    panel_top.setClickable(true);
    panel_top.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (player.isPlaying()) {
          suplInfo("s_seekbar");
          panel_top.setClickable(false);
        } else {
          supl.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
          pager_layout.setVisibility(View.VISIBLE);
        }

      }
    });
    stationEnabled = true;

    initLayout();
    checkGPS();

    setVisibility();
    initAudio();
    initImages();

  }

  public void initLayout() {
    stationActivityRunning = true;
    length = singlepage.INSTANCE.selectedTour().length();
    desc = singlepage.INSTANCE.selectedTour().description();
    time = singlepage.INSTANCE.selectedTour().time();
    author = singlepage.INSTANCE.selectedTour().author();
    slug = singlepage.INSTANCE.selectedStation().slug();
    color = singlepage.INSTANCE.selectedTour().color();
    size = (singlepage.INSTANCE.selectedTour().stations().size() - 1);
    number = (singlepage.INSTANCE.selectedStation().number() - 1);
    name = singlepage.INSTANCE.selectedTour().name();
    path = OurStorage.get(this).storagePath() + "/" + OurStorage.get(this).lookForTourFile(tourlist(), singlepage.INSTANCE.selectedTour().image());
    layout = (RelativeLayout) findViewById(R.id.rellayout);
    layout.setBackgroundColor(Color.parseColor(color));
    scroll = (ScrollView) findViewById(R.id.scroll);
    scroll.setBackgroundColor(Color.parseColor(color));
    scroll.fullScroll(View.FOCUS_UP);
    stationtitle = (TextView) findViewById(R.id.stationtitle);
    stationtitle.setText(singlepage.INSTANCE.selectedStation().name() + "  (" + number + "/" + size + ")");
    if (slug.contains("einleitung")) stationtitle.setText("Einleitung");
    routenname = (TextView) findViewById(R.id.routenname);
    routenname.setText(name);
    tourimage = (ImageView) findViewById(R.id.routenbild);
    tourimage.setImageURI(Uri.fromFile(new File(path + singlepage.INSTANCE.selectedTour().image() + ".png")));
    prof = (TextView) findViewById(R.id.routeninfo1);
    prof.setText(author);
    info2 = (TextView) findViewById(R.id.routeninfo2);
    info2.setText(time + "/" + length);
    description = (TextView) findViewById(R.id.stationenbeschreibung);
    description.setText(singlepage.INSTANCE.selectedStation().description());
    singlepage.INSTANCE.position(0);

    seekbar = (SeekBar) findViewById(R.id.seek_bar);
    pager_play_btn = (ImageView) findViewById(R.id.pager_play_button);
    play_button = (ImageButton) findViewById(R.id.play_button);
    duration = (TextView) findViewById(R.id.duration);
    gesperrt = (RelativeLayout) findViewById(R.id.gesperrt);
    pfeilhell = (ImageView) findViewById(R.id.pfeilhell);
    Sharp.loadResource(getResources(), R.raw.google_navi_hell).into(pfeilhell);
    imagePager = (ViewPager) findViewById(R.id.ImagePager);
    imagePager.setOffscreenPageLimit(2);
    String imagesFromXML = singlepage.INSTANCE.selectedStation().imagesToString();
    stationImagePaths = new ArrayList<String>();
    if (!imagesFromXML.isEmpty()) {
      stationImagePaths = new ArrayList<String>(Arrays.asList(imagesFromXML.split("\\s*,\\s*")));
    }

    video = singlepage.INSTANCE.selectedStation().videosToString();
    if (!video.isEmpty()) {
      stationImagePaths.add(0, singlepage.INSTANCE.selectedStation().videosToString());
    }
    audio = singlepage.INSTANCE.selectedStation().audio();
    mAdapter = new com.uni_wuppertal.iad.vierteltour.ui.station.StationAdapter(this, stationImagePaths);
    pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
    videopanel = (RelativeLayout) findViewById(R.id.video_panel);
    imagePager.setAdapter(mAdapter);
    seekbar.setOnSeekBarChangeListener(customSeekBarListener);
    seekbar_supl.setOnSeekBarChangeListener(customSeekBarListener);
    setImageResource(true);
  }

  public void endStationLayout() {
    lv.setVisibility(View.VISIBLE);
    supl.setScrollableView(lv);
    stationLayout.setVisibility(View.GONE);
    slidingLayout.setVisibility(View.GONE);
    if (imagePager != null) {
      imagePager.removeAllViews();
      imagePager.getAdapter().notifyDataSetChanged();
    }

    singlepage.INSTANCE.position(0);
    stationActivityRunning = false;
    panel_top.setClickable(false);

  }


  Runnable run = new Runnable() {
    @Override
    public void run() {
      seekUpdationAudio();
    }
  };

  /**
   * Updating Audio seekbar and textview
   */
  public void seekUpdationAudio() {
    if (player != null && startaudio) {

      seekbar.setProgress(player.getCurrentPosition());
      seekbar_supl.setProgress(player.getCurrentPosition());
      timeElapsed = player.getCurrentPosition();

      duration.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));
      duration_supl.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));
      seekHandler.postDelayed(run, 100);
    }
  }

  /**
   * Checks with SharedPreferences, if the station can be shown
   */
  public void checkGPS() {
    SharedPreferences prefs =
      PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(slug, false) || slug.startsWith("einleitung")) {
      sperrvariable = false;
    } else {
      sperrvariable = true;
    }

    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
      @Override
      public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(slug, false) || slug.startsWith("einleitung")) {
          sperrvariable = false;
          setVisibility();
        } else {
          sperrvariable = true;
        }
      }
    };
    prefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
  }

  /**
   * Filters available layout
   */
  public void setVisibility() {
    if (!audio.contains(".mp3") || sperrvariable || OurStorage.get(this).pathToFile(audio) == null) {
      seekbar.setVisibility(View.GONE);
      play_button.setVisibility(View.GONE);
      duration.setVisibility(View.GONE);
    } else {
      seekbar.setVisibility(View.VISIBLE);
      play_button.setVisibility(View.VISIBLE);
      duration.setVisibility(View.VISIBLE);
    }

    if ((stationImagePaths.size() == 0 && video.isEmpty()) || sperrvariable) {
      imagePager.setVisibility(View.GONE);
      pager_indicator.setVisibility(View.GONE);
      videopanel.setVisibility(View.GONE);
    } else {
      imagePager.setVisibility(View.VISIBLE);
      pager_indicator.setVisibility(View.VISIBLE);
      videopanel.setVisibility(View.VISIBLE);
    }

    if (sperrvariable) {
      gesperrt.setVisibility(View.VISIBLE);
      pfeilhell.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          sperrvariable = false;
          gesperrt.setVisibility(View.GONE);
          if (audio.contains(".mp3") && OurStorage.get(getApplicationContext()).pathToFile(audio) != null) {
            seekbar.setVisibility(View.VISIBLE);
            play_button.setVisibility(View.VISIBLE);
            duration.setVisibility(View.VISIBLE);
          }
          if (!(stationImagePaths.size() == 0 && video.isEmpty())) {
            imagePager.setVisibility(View.VISIBLE);
            pager_indicator.setVisibility(View.VISIBLE);
            videopanel.setVisibility(View.VISIBLE);
          }
        }
      });
    } else {
      gesperrt.setVisibility(View.GONE);
    }
  }

  /**
   * Manages the dots below the viewpager
   */
  private void setUiPageViewController() {
    if (mAdapter.getCount() > 1) {
      pager_indicator.removeAllViews();
      dotsCount = mAdapter.getCount();
      dots = new ImageView[dotsCount];

      for (int i = 0; i < dotsCount; i++) {
        dots[i] = new ImageView(this);
        dots[i].setColorFilter(Color.parseColor(colorString));
        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(4, 0, 4, 0);

        pager_indicator.addView(dots[i], params);
      }

      dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem));

    }
  }

  /**
   * Initialize audio
   */
  public void initAudio() {
    if (!audio.contains(".mp3") || OurStorage.get(getApplicationContext()).pathToFile(audio) == null) {
      singlepage.INSTANCE.isAudio(false);
      play_button.setVisibility(View.GONE);
      seekbar.setVisibility(View.GONE);
      duration.setVisibility(View.GONE);
      return;
    }
    player = ViertelTourMediaPlayer.getInstance(this);
    singlepage.INSTANCE.isAudio(true);


    //number soll später mit id ersetzt werden, leider wurde id bis jetzt noch nicht gesetzt
    //Wenn die gleiche Station geöffnet wird, soll audio nicht neu geladen werden
    if (singlepage.INSTANCE.getId() != number) {
      player.loadAudio(audio);
      singlepage.INSTANCE.setId(number);
    } else if (player.isPlaying()) {
      startaudio = true;
      setImageResource(false);
      seekUpdationAudio();
    }


    //CustomKlasse Seekbar

    seekbar.setMax(player.getDuration());
    seekbar_supl.setMax(player.getDuration());
    seekbar.setProgress(player.getCurrentPosition());
    seekbar_supl.setProgress(player.getCurrentPosition());
    seekbar_supl.getProgressDrawable().setColorFilter(
      Color.parseColor("#353535"), android.graphics.PorterDuff.Mode.SRC_IN);
    seekbar_supl.getThumb().setColorFilter(Color.parseColor("#353535"), android.graphics.PorterDuff.Mode.SRC_IN);
    timeElapsed = player.getCurrentPosition();

    duration.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));
    duration_supl.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));

    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer player) {
        player.seekTo(0);
        startaudio = false;
        setImageResource(true);
        if (stationActivityRunning) {
          Intent background = new Intent(getApplicationContext(), Stationbeendet.class);
          if (size == number) {
            background.putExtra("vergleich", 1);
          } else {
            background.putExtra("vergleich", 0);
          }
          background.putExtra("pfad", path);
          startActivityForResult(background, BACK_FROM_STATION_FINISHED);
          overridePendingTransition(0, 0);
          duration.setText("0:00");
          seekbar.setProgress(0);
          seekbar_supl.setProgress(0);
         /* Message message = new Message();
          message.what = MapsActivity.BIG_BAR;
          myHandler.sendMessage(message);*/
        } else {
          seekbar_supl.setProgress(0);
          Sharp.loadResource(getResources(), R.raw.play_dunkel).into(play_button_supl);
          duration_supl.setText("0:00");
        }
      }

    });


    play_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View play) {

        switch (play.getId()) {
          case R.id.play_button:
            if (!player.isPlaying()) {
              startaudio = true;
              player.start();
              setImageResource(false);
              seekUpdationAudio();
              Message message = new Message();
              message.what = MapsActivity.SEEK_BAR;
              myHandler.sendMessage(message);
            } else {
              startaudio = false;
              player.pause();
              setImageResource(true);
             /* Message message = new Message();
              message.what = MapsActivity.BIG_BAR;
              myHandler.sendMessage(message);*/
            }
            break;
        }
      }
    });


    play_button_supl.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View play) {

        switch (play.getId()) {
          case R.id.play_button_supl:
            if (!player.isPlaying()) {
              startaudio = true;
              player.start();
              Sharp.loadResource(getResources(), R.raw.stop_dunkel).into(play_button_supl);
              seekUpdationAudio();
            } else {
              startaudio = false;
              player.pause();
              Sharp.loadResource(getResources(), R.raw.play_dunkel).into(play_button_supl);
            }
            break;
        }
      }
    });

  }


  public void startGallery() {
    Intent gallery = new Intent(getApplicationContext(), GalleryMode.class);
    gallery.putExtra("resources", stationImagePaths);
    gallery.putExtra("station", singlepage.INSTANCE.selectedStation().name());
    gallery.putExtra("video", video);
    gallery.putExtra("pfad", path);
    gallery.putExtra("size", size);
    gallery.putExtra("number", number - 1);

    startActivityForResult(gallery, BACK_FROM_GALLERY);
    overridePendingTransition(0, 0);
  }

  /**
   * Initializes images
   */
  public void initImages() {
    if (stationImagePaths.size() == 0) {
      return;
    }
    isimages = 0;
    imagePager.setOnPageChangeListener(pagechangelisten);
    setUiPageViewController();
  }

  //Custom Class Seekbar start
  public SeekBar.OnSeekBarChangeListener customSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      if (fromUser) {
        player.seekTo(progress);
      }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
  };
  //Custom Class Seekbar stop

  //ViewPager.OnPageChangeListener
  ViewPager.OnPageChangeListener pagechangelisten = new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      isimages = position;
      imagePager.setCurrentItem(position);
      singlepage.INSTANCE.position(position);

      for (int i = 0; i < dotsCount; i++) {
        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem));
      }

      dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
  };
  //Viewpager.onPageChangeListener end

  /**
   * calculates white or black color on seekbar in dependence of tour color
   *
   * @param play
   */
  public void setImageResource(boolean play) {
    int red = Integer.valueOf(color.substring(1, 3), 16);
    int green = Integer.valueOf(color.substring(3, 5), 16);
    int blue = Integer.valueOf(color.substring(5, 7), 16);
    if ((red * 0.299 + green * 0.587 + blue * 0.114) > 186) {
      duration.setTextColor(Color.parseColor("#353535"));
      colorString = "#353535";
      seekbar.getProgressDrawable().setColorFilter(
        Color.parseColor("#353535"), android.graphics.PorterDuff.Mode.SRC_IN);
      seekbar.getThumb().setColorFilter(Color.parseColor("#353535"), android.graphics.PorterDuff.Mode.SRC_IN);
      if (play) Sharp.loadResource(getResources(), R.raw.play_dunkel).into(play_button);
      else Sharp.loadResource(getResources(), R.raw.stop_dunkel).into(play_button);
    } else {
      duration.setTextColor(Color.parseColor("#E6EBE0"));
      colorString = "#E6EBE0";
      seekbar.getProgressDrawable().setColorFilter(
        Color.parseColor("#E6EBE0"), android.graphics.PorterDuff.Mode.SRC_IN);

      seekbar.getThumb().setColorFilter(Color.parseColor("#E6EBE0"), android.graphics.PorterDuff.Mode.SRC_IN);
      if (play) Sharp.loadResource(getResources(), R.raw.play_hell).into(play_button);
      else {
        Sharp.loadResource(getResources(), R.raw.stop_hell).into(play_button);
      }
    }
  }

  //End StationActivty

  /**
   * All tours except the select will be vanished
   *
   * @param tour Tour will stay on Map
   */
  private void vanishTours(Tour tour) {
    adapter.notifyDataSetChanged();

    //Set Numbers on selected Tour
    for (Station station : tour.stations()) {
      if (tour.station(1).slug().contains("einleitung"))
        markers.get(station.slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(tour, (station.number() - 1) + "", false)));
      else
        markers.get(station.slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(tour, (station.number()) + "", false)));
    }

    // Unselect all other tours
    for (Tour t : tourlist.city(visibleCity).tours()) {
      if (!t.slug().equals(tour.slug())) {
        String color = "#00" + t.color().substring(1, 7); // #xx (Hex) transparency

        polylines.get(t.slug()).color(Color.parseColor(color));
        for (Station station : t.stations()) {
          markers.get(station.slug()).alpha(0f);

        }
      } else {
        unfadeTour(t);
      }
    }
    selectStation(tour.station(1));
    drawRoutes();
  }

  /**
   * Put a tour into the foreground, removing the alpha value from it's polylines and markers
   *
   * @param tour Tour to put into the foreground
   */
  private void unfadeTour(Tour tour) {
    polylines.get(tour.slug()).color(Color.parseColor(tour.color()));

    for (Station station : tour.stations()) {
      markers.get(station.slug()).alpha(1.0f);
    }
  }


  /**
   * Highlight a tour, setting the polyline color to a non-alpha value and the alpha value of the
   * markers to full alpha
   *
   * @param tour Tour that was selected
   */
  public void selectTour(Tour tour) {
    singlepage.INSTANCE.selectedTour(tour);
    //adapter.notifyDataSetChanged();
    unfadeTour(tour);

    for (Station station : tour.stations()) {
      markers.get(station.slug()).icon(BitmapDescriptorFactory.fromBitmap(markertext(tour, "", false)));
    }

    // Unselect all other tours
    for (Tour t : tourlist.city(visibleCity).tours()) {
      if (!t.slug().equals(tour.slug())) {
        fadeTour(t);
      }
    }
  }


  /**
   * Reset all tours, make all markers and polylines visible again
   */
  public void resetTour() {
    singlepage.INSTANCE.selectedTour(null);
    for (Tour tour : tourlist.city(visibleCity).tours()) {
      unfadeTour(tour);
    }
    suplInfo("invisible");
    drawRoutes();
  }

  /**
   * Convert a view to bitmap with markers and numbers
   */

  public static Bitmap createDrawableFromView(Context context, View view) {
    ((MapsActivity) context).getWindowManager().getDefaultDisplay().getMetrics(((MapsActivity) context).displayMetrics);
    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    view.measure(((MapsActivity) context).displayMetrics.widthPixels, ((MapsActivity) context).displayMetrics.heightPixels);
    view.layout(0, 0, ((MapsActivity) context).displayMetrics.widthPixels, ((MapsActivity) context).displayMetrics.heightPixels);
    view.buildDrawingCache();
    Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

    Canvas canvas = new Canvas(bitmap);
    view.draw(canvas);

    return bitmap;
  }

  /**
   * Preparing Location Services
   */
  public void initLocationServices() {
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    GpsStatus.Listener gpsStatus = new GpsStatus.Listener() {
      @Override
      public void onGpsStatusChanged(int i) {
        if (i == GPS_EVENT_STOPPED) {
          if (curLocation != null) curLocation.remove();
        }
      }
    };
    locationManager.addGpsStatusListener(gpsStatus);

    locationListener = new LocationListener() {
      @Override
      /**
       * Whenever new location informations are retrieved,
       * this method will be executed to update own position
       * and compare station coordinates with new coordinates
       * @param location location is used to save new location information
       */
      public void onLocationChanged(Location location) {
        MyLocation = location;
        //erstellt LatLng Variable fuer den Vergleich
        pos = new LatLng(MyLocation.getLatitude(), MyLocation.getLongitude());

        if (zoomToLocation)
        //Falls Variable durch Zentrierenfunktion auf true gesetzt, zoome zum aktuellen Standort
        {
          try {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, mMap.getCameraPosition().zoom));
            zoomToLocation = false;
          } catch (Exception e) {
            System.out.println("No Position Found!");
          }
        }

        if (tourlist != null && singlepage.INSTANCE.selectedTour()!=null) {
          positionInCircle(pos);
        }   //Ueberprueft eigenen Standort mit allen Stationskoordinaten

        //Entferne letzte Position und zeichne neue Location auf Map
        if (curLocation != null) curLocation.remove();
        drawOwnLocation();
      }

      /**
       * Checks if own coordinates are within the station range (circle)
       * @param pos own position to compare
       */
      //Funktion wird im onLocationChanged aufgerufen
      public void positionInCircle(LatLng pos) {
        float[] distance = new float[2];
        //uebergebene Position wird mit allen Stationen jeder Tour verglichen
          for (Station station : singlepage.INSTANCE.selectedTour().stations()) {
            if (station.latlng() != null)
            //errechnet Distanz von Koordinaten der Station und der eigenen Position
            {Location.distanceBetween(pos.latitude, pos.longitude,
                station.latlng().latitude, station.latlng().longitude, distance);
              //Die Distanz wird, wie der Radius, in Metern angegeben
              if (distance[0] < radius) {//  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                  .getDefaultSharedPreferences(getBaseContext());

                //  Make a new preferences editor
                SharedPreferences.Editor e = getPrefs.edit();
                //Setzt einen Eintrag der Station,
                //falls eigene Position in der Naehe der Station ist
                // Damit ist die Station freigeschaltet
                e.putBoolean(station.slug(), true);
                //  Apply changes
                //Aktualisiere Stationen im ViewPager,
                //damit eine sofortige Freischaltung stattfindet
                e.apply();
                fragmentAdapter.notifyDataSetChanged();

                gpsbtn_layout.setVisibility(View.GONE);
              }
            }
          }
        }
    };
    initGoogleApiClient();
  }

  /**
   * (Re-)Draw the routes of the currently visible tours and their station markers
   */
  private void drawRoutes() {
    //lösche alles
    if (mMap != null) mMap.clear();
    drawOwnLocation();
    drawPolylines();
    drawStations();
  }

  /**
   * (Re-)Draw current Location of the User
   */
  private void drawOwnLocation() {//Eigene Position zeichnen
    if (pos != null) {
      MarkerOptions marker = new MarkerOptions();
      marker.position(pos);

      marker.icon(BitmapDescriptorFactory.fromBitmap(createBitmapFromSharp(this, Sharp.loadResource(getResources(), R.raw.standort_blau).getDrawable(), 2.6)));

      if (mMap != null) curLocation = mMap.addMarker(marker);
    }
  }


  /**
   * (Re-)Draw the Path of the currently visible tours
   */
  private void drawPolylines() {//nur Pfad der ausgewählten Tour anzeigen
    if (singlepage.INSTANCE.selectedStation() != null) {
      for (Map.Entry<String, PolylineOptions> polyline : polylines.entrySet())
        if (singlepage.INSTANCE.selectedTour().slug().equals(polyline.getKey()))
          mMap.addPolyline(polyline.getValue());
    }
    //sonst Pfad aller Touren anzeigen
    else {
      for (Map.Entry<String, PolylineOptions> polyline : polylines.entrySet()) {

        mMap.addPolyline(polyline.getValue());
      }
    }
  }

  /**
   * (Re-)Draw the station markers of the currently visible tours
   */
  private void drawStations() {
    tmpmarker = null;
    //singlepage.INSTANCE.countWaypoints().clear();
    //Setze Marker
    for (Map.Entry<String, MarkerOptions> marker : markers.entrySet()) {
      //Wenn Tour ausgewählt, zeichne nur Stationen der Tour
      if (singlepage.INSTANCE.selectedStation() != null) {
        for (Station station : singlepage.INSTANCE.selectedTour().stations()) {
          if (marker.getValue().getPosition() != null && marker.getKey().equals(singlepage.INSTANCE.selectedStation().slug()) && marker.getKey().equals(station.slug())) {
            tmpmarker = mMap.addMarker(marker.getValue());
            tourMarker.put(station.slug(), tmpmarker);
          } else if (marker.getValue().getPosition() != null && marker.getKey().equals(station.slug())) {
            Marker m = mMap.addMarker(marker.getValue());
            tourMarker.put(station.slug(), m);
          }
        }
      }

      //Sonst zeichne alle Marker
      else if (marker.getValue().getPosition() != null) {
        mMap.addMarker(marker.getValue());
      }
    }

    //Wenn ein Kreis gesetzt wurde, zeichne ihn
    if (circle.getCenter() != null) {
      mapCircle = mMap.addCircle(circle);
    }
  }

  public Bitmap createBitmapFromSharp(Context context, SharpDrawable d, double scale)
  {Bitmap bitmap = null;

    if (d.getIntrinsicWidth() <= 0 || d.getIntrinsicHeight() <= 0) {
      bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
    } else {
      bitmap = Bitmap.createBitmap((int) (d.getIntrinsicWidth() / scale), (int) (d.getIntrinsicHeight() / scale), Bitmap.Config.ARGB_8888);
    }

    Canvas canvas = new Canvas(bitmap);
    d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    d.draw(canvas);
    return bitmap;}

  /**
   * Remove one marker of a station
   * @param slug
     */
  private void removeStation(String slug)
  { for( Map.Entry<String, Marker> marker : tourMarker.entrySet() ){
    if(marker.getKey().equals(slug))
    {marker.getValue().remove();}
  }}


  /**
   * After Selecting a tour, the interface is switching to the viewpager with station overview
     */
  public void swapToViewPager(View v){
    mMap.setPadding(0, 0, 0, pxPager);

    if( fragmentAdapter.fragments.size() != 0 ){
      fragmentAdapter.deleteStrings();
      fragmentAdapter.fragments.clear();
      fragmentAdapter.notifyDataSetChanged();
    }
    // Create a page for every station
    for( Station station : singlepage.INSTANCE.selectedTour().stations() ){
      fragmentAdapter.addFragment( station  );
    }
    fragmentAdapter.notifyDataSetChanged();
    vanishTours(singlepage.INSTANCE.selectedTour());

    Typeface tf = Typeface.createFromAsset(getAssets(), "Bariol_Regular.ttf");
    title.setTypeface(tf);

    //Zoom to first station coordinates
    CameraPosition cameraPosition = new CameraPosition.Builder()
      .target(new LatLng(singlepage.INSTANCE.selectedTour().station(2).latlng().latitude , singlepage.INSTANCE.selectedTour().station(2).latlng().longitude))
      .zoom(mMap.getCameraPosition().zoom)
      .build();
    //mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( singlepage.INSTANCE.selectedTour().station(2).latlng(), CurrentZoom ) );

    mMap.moveCamera( CameraUpdateFactory.newCameraPosition(cameraPosition));
    supl.setPanelState( SlidingUpPanelLayout.PanelState.HIDDEN );   //Hide Slider

    xbtn.setVisibility( View.VISIBLE );
    title.setText( singlepage.INSTANCE.selectedTour().name() );
    title.setVisibility( View.VISIBLE );
    pager_layout.setVisibility( View.VISIBLE );
  }

  /**
   * After Removing a selected tour, the interface is switching to the viewpager with tour overview
   */
  public void swapToSupl(){
    mMap.setPadding(0,0,0,slidingLayoutHeight);
    suplInfo( "showall" );
    slidingLayout.setVisibility(View.VISIBLE);
    supl.setScrollableView(lv);
    singlepage.INSTANCE.countWaypoints().clear();

    xbtn.setVisibility( View.GONE );
    title.setVisibility( View.GONE );
    pager_layout.setVisibility( View.GONE );
    if(fragmentAdapter.getItem(0)!=null)
    {selectStation(singlepage.INSTANCE.selectedTour().station(1));
      mPager.setCurrentItem(0,false);}

    startaudio=false;
    if(player.isPlaying())player.stop();
    player.reset();

    tourMarker.clear();
    singlepage.INSTANCE.selectedStation(null);
    singlepage.INSTANCE.selectedOldStation(null);
    circle = new CircleOptions();

    selectTour(singlepage.INSTANCE.selectedTour());
    drawRoutes();

    singlepage.INSTANCE.setId(0);

  }

  /**
   * Initialize SUPL
   */
  public void initSupl(){
    supl.setPanelSlideListener(onSlideListener());
    lv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
      @Override
      public void onGroupExpand(int groupPosition) {
        if (lastExpandedPosition != -1
          && groupPosition != lastExpandedPosition) {
          lv.collapseGroup(lastExpandedPosition);
        }
        lastExpandedPosition = groupPosition;

        selectTour(tourlist.city(visibleCity).tours().get(groupPosition));
        adapter.notifyDataSetChanged();

        drawRoutes();
      }
    });

    adapter = new TourAdapter( tourlist.city( visibleCity ).tours(), this);
    lv.setAdapter( adapter );
    initBtns();
    defaultPanelHeight = supl.getPanelHeight();
    slidingLayout.setLayoutParams(new SlidingUpPanelLayout.LayoutParams(SlidingUpPanelLayout.LayoutParams.MATCH_PARENT, (int) (displayMetrics.heightPixels*0.7)));
  }

  /**
   * Initialize viewpager
   */
  public void initPager(){
    //Get Screen Sizes
    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    float pagerPadding = (float)(displayMetrics.widthPixels*0.312);
    ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mPager.getLayoutParams();
    pxPager = (int) (displayMetrics.heightPixels*0.3);
    lp.height = pxPager;
    mPager.setLayoutParams(lp);
    mPager.setPadding((int)pagerPadding, 0,(int) pagerPadding, 0);
    System.out.println(displayMetrics.widthPixels + " SIDE: " + displayMetrics.widthPixels*0.312 + "padd");


  }


  /**
   * Initialize all buttons
   */
  public void initBtns(){
    zumstart.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        //listelement.setClickable(false);
        swapToViewPager(v);
      }
    });
    x_supl.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        resetTour();
      }
    });

    //Redirection to Google Maps application with the coordinates of the station and own position
    arrowbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        enableGPSMessage();
        // Navigation require current Location
        if( MyLocation != null && singlepage.INSTANCE.selectedStation()!=null){
          // Uri for google navigation
          zoomToLocation=true;
          String start = MyLocation.getLatitude() + "," + MyLocation.getLongitude();
          String target;
          if(singlepage.INSTANCE.selectedStation().number()!=1)
          {target = singlepage.INSTANCE.selectedStation().latlng().latitude + "," + singlepage.INSTANCE.selectedStation().latlng().longitude;}
          else{target = singlepage.INSTANCE.selectedTour().station(2).latlng().latitude + "," + singlepage.INSTANCE.selectedTour().station(2).latlng().longitude;}
          String navigationUrl = "http://maps.google.com/maps?saddr=" + start + "&daddr=" + target;

          Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( navigationUrl ) );
          intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS );
          intent.setClassName( "com.google.android.apps.maps", "com.google.android.maps.MapsActivity" );
          startActivity( intent );
          overridePendingTransition(0, 0);
        }
        else if(MyLocation != null && singlepage.INSTANCE.selectedTour()!=null)
        { zoomToLocation=true;
          String start = MyLocation.getLatitude() + "," + MyLocation.getLongitude();
          String target = singlepage.INSTANCE.selectedTour().station(2).latlng().latitude + "," + singlepage.INSTANCE.selectedTour().station(2).latlng().longitude;
          String navigationUrl = "http://maps.google.com/maps?saddr=" + start + "&daddr=" + target;

          Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( navigationUrl ) );
          intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS );
          intent.setClassName( "com.google.android.apps.maps", "com.google.android.maps.MapsActivity" );
          startActivity( intent );
          overridePendingTransition(0, 0);}
        else if (MyLocation == null){
          Toast.makeText( getApplicationContext(), "GPS Signal wird gesucht...", Toast.LENGTH_SHORT )
               .show();
        }
        else {
          Toast.makeText( getApplicationContext(), "Keine Tour ausgewählt.", Toast.LENGTH_SHORT )
            .show();
          }

      }
    });


    //zooms to own location
    tarbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        enableGPSMessage();

        if(MyLocation==null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {Toast.makeText( getApplicationContext(), "GPS Signal wird gesucht...", Toast.LENGTH_SHORT ).show();
        zoomToLocation=true;}
        else
        {try{mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( pos, mMap.getCameraPosition().zoom ) );}
        catch(Exception e){
          System.out.println("No Position Found!");}}


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

  /**
   * Show a GPS Dialog to active GPS by clicking ok
   */
  private void enableGPSMessage()
  {if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))  {
    if(googleApiClient!=null) googleApiClient.disconnect();
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
    googleApiClient.connect();
    zoomToLocation=true;
  }}


  /**
   * initializing googleapiclient with gps params
   */
  private void initGoogleApiClient()
  {//Erzeugt GoogleApiClient Objekt zum Verbinden der Google Play services
    googleApiClient = new GoogleApiClient.Builder(this)
     //Fügt LocationServices API hinzu
    .addApi(LocationServices.API)
    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
      @Override
      public void onConnected(Bundle bundle) {
        //Erzeugt LocationRequest Objekt, um GPS Genauigkeit und Intervalle zu definieren
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        builder = new LocationSettingsRequest.Builder()
          //Fügt erstelltes LocationRequest Objekt hinzu
          .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        //Verknüpft Client, GPS Konfigurationen und LocationListener
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,locationListener);}
      @Override
      public void onConnectionSuspended(int i) {
        googleApiClient.connect();
      }})
    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
      @Override
      public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Location error","Location error " + connectionResult.getErrorCode());  }
    }).build();
    googleApiClient.connect();    //Verbindet Client mit Services
    }

  @Override
  protected void onPostCreate( Bundle savedInstanceState ){
    super.onPostCreate( savedInstanceState );
  }

  /**
   * Special method to set drawer on top of the screen (above action bar)
   */
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

  /**
   * Initialize action bar
   */
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

  /**
   * Initialize action bar buttons
   */
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
    //homebtn.setLayoutParams(new RelativeLayout.LayoutParams(150,150));
    Sharp.loadResource(getResources(), R.raw.menu).into(homebtn);

    xbtn = (ImageButton) findViewById(R.id.btn_x);      //ActionBar Button: Right
    title = (TextView) findViewById(R.id.toolbar_title);  //ActionBar Title
    xbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        Message message = new Message();
        message.what = MapsActivity.BIG_BAR;
        myHandler.sendMessage(message);
        if(seekbar_layout_supl.getVisibility()==View.VISIBLE){suplInfo("h_seekbar");}
        endStationLayout();
        swapToSupl();
      }
    });
    Sharp.loadResource(getResources(), R.raw.beenden_dunkel).into(xbtn);
  }

  /**
   * Initialize drawer
   */
  private void initDrawer(){
    mDrawerList = (ListView) findViewById( R.id.drawerlist );
    mDrawer = (RelativeLayout) findViewById( R.id.drawer );
    mDrawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
    drawerItems = new ArrayList<DrawerItem>();
    mDrawerLayout.setDrawerListener( createDrawerToggle() );
    for( int i = 0; i < drawertitles.length; i++ ){
      DrawerItem items = new DrawerItem( drawertitles[i] );
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
          if( position == 0 && tourdataAvailable){
            Intent i = new Intent(MapsActivity.this, Einstellungen.class);

            //i.putExtra("tours", tourlist.tours());     //gib touren weiter
            startActivityForResult(i, BACK_FROM_SETTINGS);
            overridePendingTransition(0, 0);

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
            startActivity(i);
            overridePendingTransition(0, 0);}
        else if(position == 3)
          {Updater.get(getBaseContext()).updatesOnTourdata();
            if(singlepage.INSTANCE.versionUpdate())
          {createDialog("Nach Updates Suchen", "Die Touren wurden aktualisiert.");
            singlepage.INSTANCE.versionUpdate(false);}
          else{createDialog("Nach Updates Suchen", "Die Touren sind bereits aktuell.");}}
        //TMP INSERT START
          //i==4 equals "Touren freischalten". Function only for testing
        else if(position == 4){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor e = sharedPreferences.edit();
            for(int k=0;k<tourlist.tours().size();k++)
        {for(int j=1;j<=tourlist.tours().get(k).stations().size();j++)
        {e.putBoolean(tourlist.tours().get(k).station(j).slug() ,true);}}
          e.apply();}
        //i==5 equals "Touren sperren". Function only for testing
        else if(position == 5)
        {SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
          SharedPreferences.Editor e = sharedPreferences.edit();
          for(int k=0;k<tourlist.tours().size();k++)
        {for(int j=1;j<=tourlist.tours().get(k).stations().size();j++)
        {e.remove(tourlist.tours().get(k).station(j).slug());}}
          e.apply(); }
        //TMP INSERT END
        //  ftx.commit();
      }

    });

    leftbtn = (ImageButton) findViewById(R.id.leftarrow);
    Sharp.loadResource(getResources(), R.raw.pfeil_links).into(leftbtn);
    leftbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        mDrawerLayout.closeDrawer( mDrawer );
      }
    });
  }

  /**
   * Defines drawer actions
   */
  private DrawerLayout.DrawerListener createDrawerToggle(){
    mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout, R.drawable.i, R.string.drawer_open, R.string.drawer_close ){

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


  /**
   * SUPL Listener to define supl actions
   * @return
     */
  private SlidingUpPanelLayout.PanelSlideListener onSlideListener(){
    return new SlidingUpPanelLayout.PanelSlideListener(){
      @Override
      public void onPanelSlide( View view, float v ){
        if(singlepage.INSTANCE.selectedStation()==null)
        {if( singlepage.INSTANCE.selectedTour()!= null ){
          suplInfo( "showall" );
        } else {
          suplInfo( "invisible" );
        }
        panel.setVisibility( View.VISIBLE );
        adapter.notifyDataSetChanged();}
        else if(singlepage.INSTANCE.selectedStation()!=null)
        {//if(stationActivityRunning)pager_layout.setVisibility(View.VISIBLE);
          panel.setVisibility(View.GONE);}
      }

      @Override
      public void onPanelCollapsed( View view ){
        panel_top.setClickable(false);
        stationActivityRunning=false;
        //ändere Pfeilrichtung nach oben
        if(singlepage.INSTANCE.selectedStation()==null)
        { if( singlepage.INSTANCE.selectedTour()!=null){
          suplInfo( "showall" );
        } else {
          suplInfo( "show" );
        }
     //   adapter.notifyDataSetChanged();
      }
      if(singlepage.INSTANCE.selectedStation()!=null)
      {if(player.isPlaying())
      {
        suplInfo("s_seekbar");

        up.setVisibility(View.VISIBLE);
      down.setVisibility(View.GONE);
        //umanopanelheight
        }
        else{slidingLayout.setVisibility(View.GONE);supl.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);}//Setze supl höhe, nur wenn audio läuft

        pager_layout.setVisibility(View.VISIBLE);
        //zeige Viewpager
      }
      }

      @Override
      public void onPanelExpanded( View view ){//Ändere Pfeilimage nach unten
        suplInfo( "gone" );
        panel.setVisibility( View.GONE );
        if(singlepage.INSTANCE.selectedStation()!=null)
        {stationActivityRunning=true;pager_layout.setVisibility(View.GONE);suplInfo("h_seekbar");}

      }

      @Override
      public void onPanelAnchored( View view ){
      }

      @Override
      public void onPanelHidden( View view ){
        stationActivityRunning=false;

        if(!player.isPlaying()){slidingLayout.setVisibility(View.GONE);
        if(stationActivityRunning)endStationLayout();}

      }
    };
  }

  /**
   * Shows or hides information on interface of supl
   * @param info
     */
  private void suplInfo(String info){

    if(info.contains("show"))
    {up.setVisibility( View.VISIBLE );
    down.setVisibility( View.GONE );
    tourenliste.setVisibility( View.VISIBLE );

    if(info.equals("showall")){
      panel.setVisibility(View.VISIBLE);
      x_supl.setVisibility( View.VISIBLE );
      SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      SharedPreferences.Editor e = sharedPreferences.edit();

      if(sharedPreferences.getBoolean(singlepage.INSTANCE.selectedTour().slug(), false))zumstart.setVisibility( View.VISIBLE );
      else{zumstart.setVisibility(View.INVISIBLE);}
      subtext1.setVisibility( View.VISIBLE );
      subtext2.setVisibility( View.VISIBLE );
      tourenliste.setText( singlepage.INSTANCE.selectedTour().name() );
      subtext1.setText( singlepage.INSTANCE.selectedTour().author() );
      subtext2.setText( singlepage.INSTANCE.selectedTour().time() + "/" + singlepage.INSTANCE.selectedTour().length() );
      } else {
        tourenliste.setText( "Tourenliste" );
      }
    }
   if(info.equals("invisible"))
     {x_supl.setVisibility( View.INVISIBLE );
        zumstart.setVisibility( View.INVISIBLE );
        subtext1.setVisibility( View.INVISIBLE );
        subtext2.setVisibility( View.INVISIBLE );
        tourenliste.setVisibility( View.VISIBLE );
        tourenliste.setText( "Tourenliste" );}

    if(info.equals("gone"))
    {tourenliste.setVisibility( View.GONE );
      x_supl.setVisibility( View.GONE );
      zumstart.setVisibility( View.GONE );
      subtext1.setVisibility( View.GONE );
      subtext2.setVisibility( View.GONE );
      up.setVisibility( View.GONE );
      down.setVisibility( View.VISIBLE );}

    if(info.equals("s_seekbar"))
    {
      seekbar_layout_supl.setVisibility(View.VISIBLE);
      if(player.isPlaying())Sharp.loadResource(getResources(), R.raw.stop_dunkel).into(play_button_supl);
      else Sharp.loadResource(getResources(), R.raw.play_dunkel).into(play_button_supl);


      supl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
      RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(pager_layout.getLayoutParams());
      lp.setMargins(0,0,0, supl.getPanelHeight());
      lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
      pager_layout.setLayoutParams(lp);
      mMap.setPadding(0,0,0,supl.getPanelHeight()+pxPager);
      pager_layout.setVisibility(View.VISIBLE);
    }

    if(info.equals("h_seekbar"))
    {seekbar_layout_supl.setVisibility(View.GONE);
      RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(pager_layout.getLayoutParams());
      lp.setMargins(0,0,0,0);
      lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
      pager_layout.setLayoutParams(lp);
      mMap.setPadding(0,0,0,pxPager);
    }

  }


  /**
   * standard method to define interaction when back button is pressed
   */
  @Override
  public void onBackPressed(){
    if( supl != null && supl.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED && singlepage.INSTANCE.selectedStation()==null )      //Wenn SUPL geöffnet, und zurück gedrückt wird, schließe nur SUPL
    {supl.setPanelState( SlidingUpPanelLayout.PanelState.COLLAPSED );}
    else if( supl != null && supl.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED && singlepage.INSTANCE.selectedStation()!=null )    //Wenn Station geöffnet ist, schließe nur Station mit SUPL
    { if(player.isPlaying()){suplInfo("s_seekbar");}
    else{supl.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);pager_layout.setVisibility(View.VISIBLE);}}

    else if( pager_layout.getVisibility() == View.VISIBLE ){
      Message message = new Message();
      message.what = MapsActivity.BIG_BAR;
      myHandler.sendMessage(message);
      if(seekbar_layout_supl.getVisibility()==View.VISIBLE){suplInfo("h_seekbar");}
      endStationLayout();
      swapToSupl();}        //Gehe von Stationenauswahl zurück zur Tourenauswahl

    else if(supl != null && supl.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED && singlepage.INSTANCE.selectedStation()!=null){}    //Mache nichts wenn er gerade von einem in den anderen Zustand geht
    else if( getFragmentManager().getBackStackEntryCount() == 0 ){super.onBackPressed();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Check which request we're responding to
    if (requestCode == BACK_FROM_SETTINGS) {
      // Make sure the request was successful
    if(resultCode == RESULT_CANCELED)
    {SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
      SharedPreferences.Editor e = sharedPreferences.edit();
      if(singlepage.INSTANCE.selectedTour()!=null && sharedPreferences.getBoolean(singlepage.INSTANCE.selectedTour().slug(), false))zumstart.setVisibility( View.VISIBLE );
      else{zumstart.setVisibility(View.INVISIBLE);}}
  }
  else if(requestCode == BACK_FROM_GALLERY)
  {imagePager.setCurrentItem(singlepage.INSTANCE.position());
    for (int i = 0; i < dotsCount; i++) {
    if(i==singlepage.INSTANCE.position())
    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem));
  else
      dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem));}
  if(resultCode==STATION_BEENDET_GALLERY)
  {endStationLayout();mPager.setCurrentItem(mPager.getCurrentItem()+1);startStationLayout();}
  }

    else if(requestCode == BACK_FROM_STATION_FINISHED)
    {int RESULT_NEXT = 10;
      if(resultCode == RESULT_OK){imagePager.setCurrentItem(0);}
    else if(resultCode == RESULT_NEXT){endStationLayout();mPager.setCurrentItem(mPager.getCurrentItem()+1);startStationLayout();}}
  }

  @Override
  public void onPause(){
    super.onPause();
  }

  @Override
  public void onStop(){
    super.onStop();
  }

  @Override
  public void onRestart(){
    super.onRestart();
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
      markers.put( station.slug(), createMarker(station, tour) );
    }

  }

  public MarkerOptions createMarker(Station station, Tour tour)
  {
    MarkerOptions marker = new MarkerOptions();

    try{marker.position( station.latlng() );}
    catch (Exception e){}
    marker.icon( BitmapDescriptorFactory.fromBitmap( markertext(tour,"", false) ) );

  return marker;}

  /**
   * Marker png and Text with number will be merged together to a bitmap
   * @param tour to get pin color
   * @param text to get number
   * @return
     */
    //Es wird die Tour fuer die entsprechende Farbe
    //und der Zahl der Station uebergeben
    public Bitmap markertext(Tour tour, String text, boolean bigger)
{//Das vorgegebene Marker Layout wird verwendet
//Bestehend aus einem Image (Pin) und einer TextView (Zahl)

  View markerlayout = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_layout, null);
  //Der zugehoerige Pin wird festgelegt
  int id,numbermarker=0;
  TextView markertxt = (TextView)markerlayout.findViewById(R.id.markernumber);
  ImageView markerimage = (ImageView) markerlayout.findViewById(R.id.marker);
 //Excluded: Audio to hear between two Stations
  /* if(!text.isEmpty() && singlepage.INSTANCE.selectedTour().stations().get(Integer.parseInt(text)).description().contains("Hören Sie den folgenden Text während Sie von hier aus zu der nächsten Station gehen."))
  {if(!singlepage.INSTANCE.countWaypoints().contains(Integer.parseInt(text))){singlepage.INSTANCE.countWaypoints().add(Integer.parseInt(text));}
    id = getResources().getIdentifier("pin_"+tour.trkid()+"_weg", "drawable", getPackageName());
    int countnumber=0;
    try{while(singlepage.INSTANCE.countWaypoints().get(countnumber)<Integer.parseInt(text))countnumber++;}catch (Exception e){}
    markertxt.setText("W"+(countnumber+1)+"");}
  else {*/
  if(!text.isEmpty()){numbermarker=Integer.parseInt(text);
    //  int countnumber=0;
  //    try{while(singlepage.INSTANCE.countWaypoints().get(countnumber)<Integer.parseInt(text))countnumber++;}catch (Exception e){}
      markertxt.setText((numbermarker/*-countnumber*/)+"");}
    else {markertxt.setText("");}

   // id = getResources().getIdentifier( "pin_" + tour.trkid(), "drawable", getPackageName() );
  //BitmapDescriptorFactory.fromBitmap(createBitmapFromSharp(this, Sharp.loadResource(getResources(), R.raw.standort_blau).getDrawable(), 2.6))
  //Sharp.loadFile("test").getDrawable();


  //Text und Bild wird festgelegt
  path = OurStorage.get(this).storagePath() + "/" + OurStorage.get(this).lookForTourFile(tourlist(), tour.image())+"pin.svg";
  if(bigger) {
     markerimage.setLayoutParams(new RelativeLayout.LayoutParams((int)(markerimage.getLayoutParams().width*1.3), (int)(markerimage.getLayoutParams().height*1.3)));
    markertxt.setTextSize(18);
  }
  markerimage.setImageDrawable(Sharp.loadFile(new File(path)).getDrawable());

  //Bitmap wird zurueckgegeben
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
    tourdataAvailable=true;
  }

  public void createDialog(String title, String text)
  {
    // declare the dialog as a member field of your activity
    final Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.alert_dialog);
    TextView txt = (TextView) dialog.findViewById(R.id.text_dialog);
    txt.setText(text);
    TextView titleDialog = (TextView) dialog.findViewById(R.id.title_dialog);
    titleDialog.setText(title);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();

    ImageButton okayButton = (ImageButton) dialog.findViewById(R.id.button_dialog);
    Sharp.loadResource(getResources(), R.raw.schliessen).into(okayButton);
    // if decline button is clicked, close the custom dialog
    okayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {dialog.dismiss();}});

  }
}
