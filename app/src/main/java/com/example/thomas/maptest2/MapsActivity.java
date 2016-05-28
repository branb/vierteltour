package com.example.thomas.maptest2;


import android.Manifest;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.google.maps.android.PolyUtil;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;


public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Route2> routen = new Vector<>();
    public Location MyLocation;
    public LatLng pos;
    public LocationManager locationManager;
    public LocationListener locationListener;
    public MarkerOptions CurrentMarker;
    public int CurrentZoom=15;

    private ActionBar actionBar;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private SlidingUpPanelLayout mLayout;
    private RelativeLayout mDrawer;
    private ViewPager mPager;
    private PagerAdapter pageradapter;

    private DrawerAdapter draweradapter;
    private TourenAdapter adapter;
    private List<RowItem> rowItems;
    private List<DrawerItem> drawerItems;

    private LatLng wuppertal;

    private XmlParser parse;
    private XmlParser2 tour;
    private int marked;         //marked für Tour ausgewählt: -1 für nicht ausgewählt, 0-xxx für ausgewählte Tour
    private RelativeLayout panel;

    int[] menuIcons = new int[]{
            R.drawable.ic_drawer,
            R.drawable.ic_drawer,
            R.drawable.ic_drawer,
            R.drawable.ic_drawer,
            R.drawable.ic_drawer,
            R.drawable.ic_drawer,
            R.drawable.ic_drawer,
            R.drawable.ic_drawer
    };

    int[] drawerIcons = new int[]{
            R.drawable.einstellungen,
            R.drawable.hilfe,
            R.drawable.about,
            R.drawable.karte
    };

    String[] drawertitles = new String[]{
            "Einstellungen",
            "Hilfe",
            "About",
            "Karte"
    };
    String[] drawersubtitles = new String[]{
            " ",
            " ",
            " ",
            "hell / dunkel"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        marked = -1;        //keine Tour ausgewählt
        parse = new XmlParser(this);

        initPager();
        initSupl();     //init SlidingUpPanelLayout
        initBtns();     //init Buttons right side, panel
        moveDrawerToTop();  //set Drawer over ActionBar
        initActionBar();    //init ActionBar
        initDrawer();       //init Drawer

        tour = new XmlParser2(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        zeichnePolyLines();
        findMyLocation();

        wuppertal = new LatLng(51.256972, 7.139341);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wuppertal, CurrentZoom));

        final GoogleMap.OnMapClickListener listener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng clickCoords) {
                for (Tour t : tour.ListTouren) {
                    if (PolyUtil.isLocationOnPath(clickCoords, t.polylines.getPoints(), true, 20)) {
                        // clicked track and marker become no alpha value
                        t.polylines.color(Color.parseColor(t.info.color));
                        for (MarkerOptions m : t.ListMarker)
                            m.alpha(1.0f);
                        // no clicked tracks and marker become alpha value
                    } else {
                        String s = t.info.color;
                        s = s.substring(1, 7);
                        s = "#30" + s; // #xx (Hex) Transparenz Stufe
                        t.polylines.color(Color.parseColor(s));
                        for (MarkerOptions m : t.ListMarker)
                            m.alpha(0.3f);
                    }
                }
                updatePolylines();
            }
        };
        mMap.setOnMapClickListener(listener);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                listener.onMapClick(marker.getPosition());
                return true;    // false: OnMarkerClick aktiv und zoomt zum Marker
            }
        });

    }

    public void findMyLocation(){
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updatePolylines();
                // define new Location
                MyLocation = location;
                pos = new LatLng(location.getLatitude(), location.getLongitude());
                // Draw new position of marker
                int id = getResources().getIdentifier("current3", "drawable", getPackageName());
                Bitmap icon = BitmapFactory.decodeResource(getResources(), id);
                CurrentMarker = new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromBitmap(icon)).anchor(0.5f,0.5f);
                mMap.addMarker(CurrentMarker);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Start GPS
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
    }

    public void updatePolylines(){
        mMap.clear();
        for(Tour t : tour.ListTouren){
            mMap.addPolyline(t.polylines);
            for(MarkerOptions m : t.ListMarker){
                mMap.addMarker(m);
            }
        }
    }

    public void zeichnePolyLines(){
        for(Tour t : tour.ListTouren){
            t.makePolylines();
            mMap.addPolyline(t.polylines);
            for(MarkerOptions m : t.ListMarker){
                mMap.addMarker(m);
            }
        }
    }


    public int addPage(int position) {
        if ((position >= 0) && (position < parse.ListTour.get(marked).stations.size())) {
            pageradapter.addFragment(position, parse.ListTour.get(marked));
            pageradapter.notifyDataSetChanged();
            return position;
        } else {
            return -1;
        }
    }



    //Durch Auswahl einer Tour wird zur Stationenübersicht gewechselt
    public void swapToViewPager(View v){

        if(pageradapter.fragments.size()!=0)
        {pageradapter.deleteStrings();
            pageradapter.fragments.clear();
            pageradapter.notifyDataSetChanged();}


        for(int i=0;i<parse.ListTour.get(marked).stations.size()-1;i++)
        {addPage(i);}

        ImageButton xbtn = (ImageButton) findViewById(R.id.btn_x);      //ActionBar Button: Right
        TextView title = (TextView) findViewById(R.id.toolbar_title);   //ActionBar Title

        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);   //Hide Slider
        xbtn.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText(parse.ListTour.get(marked).info.name);
        mPager.setVisibility(View.VISIBLE);

    }

    //Stationenübersicht schließen und zurück zur Tourenauswahl
    public void swapToSupl(){
        ImageButton xbtn = (ImageButton) findViewById(R.id.btn_x);           //ActionBar Button: Right
        TextView title = (TextView) findViewById(R.id.toolbar_title);        //ActionBar Title
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);    //Show Slider
        xbtn.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        mPager.setVisibility(View.GONE);
    }

    //Erstelle den Slider
    public void initSupl() {
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelSlideListener(onSlideListener());
        ListView lv = (ListView) findViewById(R.id.list);
        panel = (RelativeLayout) findViewById(R.id.panelhalf);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                for (RowItem item : rowItems) {     //Alle Listenelemente werden verkleinert
                    item.setSelected(false);
                }
                rowItems.get(position).setSelected(true); //Nur das Angeklickte wird vergrößert
                marked = position;
                adapter.notifyDataSetChanged();
            }
        });
        rowItems = new ArrayList<RowItem>();

        for (int i = 0; i < parse.ListTour.size(); i++) {
            RowItem items = new RowItem(parse.ListTour.get(i).info.name , parse.ListTour.get(i).info.author, parse.ListTour.get(i).info.time+ "/" + parse.ListTour.get(i).info.length, menuIcons[i], parse.ListTour.get(i).info.description);

            rowItems.add(items);
        }
        adapter = new TourenAdapter(this, rowItems, parse);
        lv.setAdapter(adapter);
    }


    public void initPager() {
        //Initialisiere Pager
        mPager = (ViewPager) findViewById(R.id.pager);
        pageradapter = new PagerAdapter(getSupportFragmentManager(), this);
        mPager.setAdapter(pageradapter);
    }


    //Initialisiere Alle Buttons in der Activity
    public void initBtns(){
        ImageButton zumstart = (ImageButton) findViewById(R.id.zumstart);       //SUPL Button bottom right
        zumstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapToViewPager(v);
            }
        });
        ImageButton imgbtn1 = (ImageButton) findViewById(R.id.x);               //SUPL Button top left
        imgbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInfo(false);
            }
        });
        ImageButton eyebtn = (ImageButton) findViewById(R.id.eyebtn);           //Circle Button
        eyebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Auge gedrueckt", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        ImageButton arrowbtn = (ImageButton) findViewById(R.id.arrowbtn);       //Top Twin Button
        arrowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigation require current Location
                if(MyLocation!=null) {
                    // Uri for google navigation
                    String start = MyLocation.getLatitude() + "," + MyLocation.getLongitude();
                    String target = wuppertal.latitude + "," + wuppertal.longitude;
                    String navigationUrl = "http://maps.google.com/maps?"
                            +"saddr="+start
                            +"&daddr="+target;

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(navigationUrl));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK&Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Standort unbekannt", Toast.LENGTH_SHORT)
                            .show();

            }
        });
        ImageButton tarbtn = (ImageButton) findViewById(R.id.tarbtn);           //Bot Twin Button
        tarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GPS-Signal ist da
                if (MyLocation != null) {
                    Toast.makeText(getApplicationContext(), "Signal da!", Toast.LENGTH_SHORT).show();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, CurrentZoom));
                }
                // GPS-Signal nicht da
                else
                    Toast.makeText(getApplicationContext(), "Kein Signal", Toast.LENGTH_SHORT)
                            .show();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    //DRAWER START
    //Trick um den Drawer über die ActionBar zu legen
    private void moveDrawerToTop() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.decor, null); // "null" is important.

        // HACK: "steal" the first child of decor view
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        LinearLayout container = (LinearLayout) drawer.findViewById(R.id.drawer_content); // This is the container we defined just now.
        container.addView(child, 0);
        drawer.findViewById(R.id.drawer).setPadding(0, getStatusBarHeight(), 0, 0);

        // Make the drawer replace the first child
        decor.addView(drawer);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getContentIdResource() {
        return getResources().getIdentifier("content", "id", "android");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        mDrawerToggle.syncState();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
       /*   int id = item.getItemId();*/

        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        actionBar = getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);     //Deaktiviert alle Buttons und setzt CostumActionBar
        View view = getLayoutInflater().inflate(R.layout.toolbar,
                null);
        android.support.v7.app.ActionBar.LayoutParams layoutParams = new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(view, layoutParams);
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);           //Vermeidet Fehler, dass CostumActionBar zu schmal wird

        initActionBarBtn();                              //Initialisiert die Knöpfe der CostumActionBar
        actionBar.setElevation(0);
    }

    public void initActionBarBtn(){
        ImageButton homebtn = (ImageButton) findViewById(R.id.homebtn);     //ActionBar Button: Right
        ImageButton xbtn = (ImageButton) findViewById(R.id.btn_x);          //ActionBar Title
        homebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(mDrawer)) {
                    mDrawerLayout.closeDrawer(mDrawer);
                } else {
                    mDrawerLayout.openDrawer(mDrawer);
                }
            }
        });
        xbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                swapToSupl();
            }
        });}

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.drawerlist);
        mDrawer = (RelativeLayout) findViewById(R.id.drawer);
        mDrawerLayout.setDrawerListener(createDrawerToggle());
        List<DrawerItem> drawerItems = new ArrayList<DrawerItem>();
        for (int i = 0; i < drawertitles.length; i++) {
            DrawerItem items = new DrawerItem(drawertitles[i], drawersubtitles[i], drawerIcons[i]);
            drawerItems.add(items);
        }
        draweradapter = new DrawerAdapter(this, drawerItems);
        mDrawerList.setAdapter(draweradapter);

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Noch nicht bearbeitet
                mDrawerLayout.closeDrawer(mDrawer);
                // FragmentManager fragmentManager = getSupportFragmentManager();
                // FragmentTransaction ftx = fragmentManager.beginTransaction();
                if (position == 0) {

                    // ftx.replace(R.id.main_content, new FragmentFirst());
                } else if (position == 1) {
                    //  ftx.replace(R.id.main_content, new FragmentSecond());

                }
                //  ftx.commit();
            }

        });

        ImageButton leftbtn = (ImageButton) findViewById(R.id.leftarrow);
        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mDrawer);
            }
        });
    }

    private DrawerLayout.DrawerListener createDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int state) {
            }
        };
        return mDrawerToggle;
    }


    //DRAWER END



    //SUPL START
    private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                if(marked>=0)
                {showInfo(true);}
                else
                {hideInfo(false);}
                panel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPanelCollapsed(View view) {
                //ändere Pfeilrichtung nach oben
                if(marked>=0)
                {showInfo(true);}
                else
                {showInfo(false);}
                for (RowItem item :rowItems){
                    item.setSelected(false);
                }
            }

            @Override
            public void onPanelExpanded(View view) {//Ändere Pfeilimage nach unten
                hideInfo(true);
                panel.setVisibility(View.GONE);
            }

            @Override
            public void onPanelAnchored(View view) {}

            @Override
            public void onPanelHidden(View view) {}
        };
    }
    //zeigt, wenn gewünscht, alle Informationen auf dem Panel an
    private void showInfo(boolean all) {
        ImageButton imgbtn1 = (ImageButton) findViewById(R.id.x);
        ImageButton imgbtn2 = (ImageButton) findViewById(R.id.zumstart);
        TextView subtext1 = (TextView) findViewById(R.id.subinfo1);
        TextView subtext2 = (TextView) findViewById(R.id.subinfo2);
        TextView tourenliste = (TextView) findViewById(R.id.tourenliste);
        ImageView up = (ImageView) findViewById(R.id.up);
        ImageView down = (ImageView) findViewById(R.id.down);


        up.setVisibility(View.VISIBLE);
        down.setVisibility(View.GONE);
        tourenliste.setVisibility(View.VISIBLE);

        if(all) {


            imgbtn1.setVisibility(View.VISIBLE);
            imgbtn2.setVisibility(View.VISIBLE);
            subtext1.setVisibility(View.VISIBLE);
            subtext2.setVisibility(View.VISIBLE);
        if(marked>=0)
        {     tourenliste.setText(parse.ListTour.get(marked).info.name);
            subtext1.setText(parse.ListTour.get(marked).info.author);
           subtext2.setText(parse.ListTour.get(marked).info.time + "/" + parse.ListTour.get(marked).info.length);
        }
        else{tourenliste.setText("Tourenliste");}}


    }

    //versteckt, wenn gewünscht, alle Informationen auf dem Panel
    private void hideInfo(boolean all){
        ImageButton imgbtn1 = (ImageButton) findViewById(R.id.x);
        ImageButton imgbtn2 = (ImageButton) findViewById(R.id.zumstart);
        TextView subtext1 = (TextView) findViewById(R.id.subinfo1);
        TextView subtext2 = (TextView) findViewById(R.id.subinfo2);
        ImageView up = (ImageView) findViewById(R.id.up);
        ImageView down = (ImageView) findViewById(R.id.down);
        TextView title = (TextView) findViewById(R.id.tourenliste);

        if(!all) {


            imgbtn1.setVisibility(View.INVISIBLE);
            imgbtn2.setVisibility(View.INVISIBLE);
            subtext1.setVisibility(View.INVISIBLE);
            subtext2.setVisibility(View.INVISIBLE);
            title.setVisibility(View.VISIBLE);
            title.setText("Tourenliste");
        }
        else{

            title.setVisibility(View.GONE);
            imgbtn1.setVisibility(View.GONE);
            imgbtn2.setVisibility(View.GONE);
            subtext1.setVisibility(View.GONE);
            subtext2.setVisibility(View.GONE);
            up.setVisibility(View.GONE);
            down.setVisibility(View.VISIBLE);
        }}



    @Override
    public void onBackPressed() {
        if(mLayout != null && mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)      //Wenn SUPL geöffnet, und zurück gedrückt wird, schließe nur SUPL
        {mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);;}
        else if(mPager.getVisibility() == View.VISIBLE)
        {swapToSupl();}
        else if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
            // GPS cancel
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }
    @Override
    public void onRestart(){
        super.onRestart();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}
