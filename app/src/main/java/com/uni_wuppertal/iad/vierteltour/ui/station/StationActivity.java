package com.uni_wuppertal.iad.vierteltour.ui.station;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.gallery.GalleryMode;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.utility.Singletonint;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.ViertelTourMediaPlayer;
import com.uni_wuppertal.iad.vierteltour.utility.storage.OurStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * StationActivity for Station Overview
 */

public class StationActivity extends Activity{

  //Declare Var
  static boolean stationActivityRunning=false;
  SeekBar seekbar;        //Fortschrittsbalken
  ImageButton play_button;      //diverse Bilderbuttons
  ViertelTourMediaPlayer player;          //Media player
  Singletonint singlepage;
  int isimages=-1;
  boolean startaudio = true;  //Variable für Status des Play-Buttons
  Handler seekHandler = new Handler();;
  TextView duration;  //Textfeld
  TextView title, routenname, prof, info2, description;
  double timeElapsed = 0;
  int dotsCount;
  String video, audio;
  ArrayList<String> stationImagePaths;
  String station, farbe, autor, tourname, laenge, desc, zeit, size, number, slug, path;
  ImageView dots[], tourimage, pager_play_btn, pfeilhell;
  Intent myIntent2;
  Bundle b;
  String colorString;
  RelativeLayout layout;
  ViewPager imagePager;    //Slidebare Gallery
  StationAdapter mAdapter;
  LinearLayout pager_indicator;
  RelativeLayout gesperrt, transparentLayout;
  boolean sperrvariable=true;

  //Runnables zuständig für Aktualisierung der fortgeschrittenen Zeit der Player
  Runnable run = new Runnable(){
    @Override
    public void run(){
      seekUpdationAudio();
    }
  };

  @Override
  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.stationactivity );
    stationActivityRunning=true;
    parseData();      //übergibt Daten von MapsActivity
    getInit();        //Initialisierung
   }

  @Override
  protected void onDestroy()
  {super.onDestroy();
    singlepage.INSTANCE.position(0);
    stationActivityRunning=false;
  }

  @Override
  public void onBackPressed(){
    Intent intent = new Intent();
      startaudio=false;

      super.onBackPressed();
      //overridePendingTransition( R.anim.map_in, R.anim.fade_out );

      if(player != null)    //If no audio exists, player == null and error will show up
      {if(player.isPlaying())
      {RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        MapsActivity.audiobar.setLayoutParams(layoutParams);}
      else
      {RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      MapsActivity.audiobar.setLayoutParams(layoutParams);}
    }
    setResult(RESULT_OK, intent);
    finish();
  }

  public void getInit(){
    initAll();

    checkGPS();

    setVisibility();
    initAudio();
    initImages();
  }

  /**
   * Checks with SharedPreferences, if the station can be shown
   */
  public void checkGPS()
  {SharedPreferences prefs =
    PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    if(PreferenceManager.getDefaultSharedPreferences( getBaseContext() ).getBoolean(slug, false) || slug.startsWith("einleitung"))
    {sperrvariable=false;}

    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
      @Override
      public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(PreferenceManager.getDefaultSharedPreferences( getBaseContext() ).getBoolean(slug, false) || slug.startsWith("einleitung"))
        {sperrvariable=false;
          setVisibility();}
      }
    };
    prefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
  }

  /**
   * Method is parsing all saving all information got from previous activity
   */
  public void parseData(){
    myIntent2 = getIntent();
    b = myIntent2.getExtras();
    slug = (String) b.get( "slug" );
    station = (String) b.get( "station" );
    tourname = (String) b.get( "name" );
    autor = (String) b.get( "autor" );
    zeit = (String) b.get( "zeit" );
    laenge = (String) b.get( "laenge" );
    farbe = (String) b.get( "farbe" );
    desc = (String) b.get( "desc" );
    size = (String) b.get( "size" );
    number = (String) b.get( "pos" );
    path = (String) b.get("path");

    // Currently in the format "img1.jpg,img2.jpg,..."
    // TODO: Convert XML-entry to have one <image>-tag per image entry
    // TODO: Set Video and Image Resources in <Resources></Resources> in right order
    stationImagePaths = new ArrayList<String>();
    String imagesFromXML = (String) b.get( "img" );

    video = (String) b.get( "video" );
    if( !imagesFromXML.isEmpty() ){
      stationImagePaths = new ArrayList<String>(Arrays.asList(imagesFromXML.split("\\s*,\\s*")));
    }
    if(!video.isEmpty()){
      stationImagePaths.add(0,video);
    }

    audio = (String) b.get( "audio" );

    layout = (RelativeLayout) findViewById( R.id.rellayout );
    layout.setBackgroundColor( Color.parseColor( farbe ) );
    title = (TextView) findViewById( R.id.stationtitle );
    title.setText( station + "  (" + (Integer.parseInt(number)) + "/" + (Integer.parseInt(size)) + ")" );
    if(slug.contains("einleitung"))title.setText("Einleitung");
    routenname = (TextView) findViewById( R.id.routenname );
    routenname.setText( tourname );
    tourimage = (ImageView) findViewById(R.id.routenbild);
    tourimage.setImageURI( Uri.fromFile(new File(path+singlepage.INSTANCE.selectedTour().image()+".png")));
    prof = (TextView) findViewById( R.id.routeninfo1 );
    prof.setText( autor );
    info2 = (TextView) findViewById( R.id.routeninfo2 );
    info2.setText( zeit + "/" + laenge );
    description = (TextView) findViewById( R.id.stationenbeschreibung );
    description.setText( desc );
    singlepage.INSTANCE.position(Integer.parseInt(number)-1);
  }

  public void initAll(){//Initialisation
    RelativeLayout arrdwn = (RelativeLayout) findViewById( R.id.arrow_down );

    arrdwn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        onBackPressed();
      }
    });
    seekbar = (SeekBar) findViewById( R.id.seek_bar );
    pager_play_btn = (ImageView) findViewById(R.id.pager_play_button);
    play_button = (ImageButton) findViewById( R.id.play_button );
    duration = (TextView) findViewById( R.id.duration );
    transparentLayout = (RelativeLayout) findViewById(R.id.transparent_layout);
    transparentLayout.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        onBackPressed();
      }
    });
    gesperrt = (RelativeLayout) findViewById(R.id.gesperrt);
    pfeilhell = (ImageView) findViewById(R.id.pfeilhell);
    imagePager = (ViewPager) findViewById( R.id.ImagePager );
    imagePager.setOffscreenPageLimit(5);
    mAdapter = new StationAdapter( this, stationImagePaths);
    pager_indicator = (LinearLayout) findViewById( R.id.viewPagerCountDots );
    imagePager.setAdapter( mAdapter );
    seekbar.setOnSeekBarChangeListener(customSeekBarListener);
    setImageResource(true);


  }


  /**
   * Updating Audio seekbar and textview
   */
  public void seekUpdationAudio(){
    if( player != null && startaudio ){

      seekbar.setProgress( player.getCurrentPosition() );
      timeElapsed = player.getCurrentPosition();

      duration.setText( String.format( "%d:%02d", TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ), TimeUnit.MILLISECONDS.toSeconds( (long) timeElapsed ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ) ) ) );
      seekHandler.postDelayed( run, 100 );
    }
  }
  /**
   * Feedback of Gallery Activity
   */
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1) {
      if(resultCode == RESULT_OK){
        imagePager.setCurrentItem(singlepage.INSTANCE.position());
        if(player != null)
        {if(!audio.contains(".mp3") || !player.isPlaying())
        {startaudio=false;
          setImageResource( true );}}}
    }
  }


  /**
   * Filters available layout
   */
  public void setVisibility(){
    if( !audio.contains(".mp3") || sperrvariable || OurStorage.get(this).pathToFile(audio)==null){
      seekbar.setVisibility( View.GONE );
      play_button.setVisibility( View.GONE );
      duration.setVisibility( View.GONE );
    }
    else{seekbar.setVisibility( View.VISIBLE );
      play_button.setVisibility( View.VISIBLE );
      duration.setVisibility( View.VISIBLE );}

    if( (stationImagePaths.size() == 0 && video.isEmpty()) || sperrvariable ){
      imagePager.setVisibility( View.GONE );
    }else{imagePager.setVisibility( View.VISIBLE );}

    if(sperrvariable)
    {gesperrt.setVisibility(View.VISIBLE);
      pfeilhell.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          sperrvariable=false;
          gesperrt.setVisibility(View.GONE);
          if(audio.contains(".mp3") && OurStorage.get(getApplicationContext()).pathToFile(audio)!=null)
          {seekbar.setVisibility( View.VISIBLE );
            play_button.setVisibility( View.VISIBLE );
            duration.setVisibility( View.VISIBLE );}
          if(!(stationImagePaths.size() == 0 && video.isEmpty()))
          {imagePager.setVisibility( View.VISIBLE );}
        }
      });
    }
    else{gesperrt.setVisibility(View.GONE);}
  }


  /**
   * Manages the dots below the viewpager
   */
  private void setUiPageViewController(){
  if(mAdapter.getCount()>1) {
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
  public void initAudio(){
    if( !audio.contains(".mp3") || OurStorage.get(getApplicationContext()).pathToFile(audio)==null){
      singlepage.INSTANCE.isAudio(false);
      play_button.setVisibility(View.GONE);
      seekbar.setVisibility(View.GONE);
      duration.setVisibility(View.GONE);
      return;
    }
    player = ViertelTourMediaPlayer.getInstance( this );
    singlepage.INSTANCE.isAudio(true);


    //number soll später mit id ersetzt werden, leider wurde id bis jetzt noch nicht gesetzt
    //Wenn die gleiche Station geöffnet wird, soll audio nicht neu geladen werden
    if(singlepage.INSTANCE.getId() != Integer.parseInt(number))
    { player.loadAudio( audio );
      singlepage.INSTANCE.setId(Integer.parseInt(number));}



    else if(player.isPlaying())
    {startaudio = true;
      setImageResource( false );
      seekUpdationAudio();}



    //CustomKlasse Seekbar

    seekbar.setMax( player.getDuration() );
    seekbar.setProgress( player.getCurrentPosition() );
    timeElapsed = player.getCurrentPosition();

    duration.setText( String.format( "%d:%02d", TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ), TimeUnit.MILLISECONDS.toSeconds( (long) timeElapsed ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ) ) ) );


    player.setOnCompletionListener( new MediaPlayer.OnCompletionListener(){
      @Override
      public void onCompletion( MediaPlayer player ){
        player.seekTo(0);
        startaudio = false;
        setImageResource( true );
        System.out.println(path);
        if(stationActivityRunning){
        Intent background = new Intent(getApplicationContext(), Stationbeendet.class);
        if(size.equals(number)){background.putExtra("vergleich", 1);}
        else {background.putExtra("vergleich", 0);}
          background.putExtra("pfad", path);
        startActivity(background);
        duration.setText("0:00");
        seekbar.setProgress(0);}
        else
        { RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0);
          layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
          MapsActivity.audiobar.setLayoutParams(layoutParams);
        }
      }

    });



    play_button.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View play ){

        switch( play.getId() ){
          case R.id.play_button:
            if( !player.isPlaying() ){
              startaudio = true;
              player.start();
              setImageResource( false );
              seekUpdationAudio();
            } else {
              startaudio=false;
              player.pause();
              setImageResource( true );
            }break;}}});
  }

  /**
   * Initializes images
   */
  public void initImages(){
    if( stationImagePaths.size() == 0 ){
      return;
    }
    pager_play_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent gallery = new Intent(getApplicationContext(), GalleryMode.class);
        gallery.putExtra("resources", stationImagePaths);
        gallery.putExtra("station", station);
        gallery.putExtra("video", video);
        gallery.putExtra("pfad", path);
        gallery.putExtra("size", size);
        gallery.putExtra("number", number);
        startActivityForResult(gallery, 1);
      }
    });
    isimages=0;
    imagePager.setOnPageChangeListener(pagechangelisten);
    setUiPageViewController();
  }

  //Custom Class Seekbar start
  public SeekBar.OnSeekBarChangeListener customSeekBarListener = new SeekBar.OnSeekBarChangeListener(){
    @Override
    public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser ){
      if( fromUser ){
        player.seekTo( progress );
      }
    }

    @Override
    public void onStartTrackingTouch( SeekBar seekBar ){
    }

    @Override
    public void onStopTrackingTouch( SeekBar seekBar ){
    }
  };
  //Custom Class Seekbar stop

  //ViewPager.OnPageChangeListener
  ViewPager.OnPageChangeListener pagechangelisten = new ViewPager.OnPageChangeListener(){
    @Override
    public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ){

    }

    @Override
    public void onPageSelected( int position ){
      isimages=position;
      imagePager.setCurrentItem(position);

      for( int i = 0; i < dotsCount; i++ ){
        dots[i].setImageDrawable( getResources().getDrawable( R.drawable.nonselecteditem ) );
      }

      dots[position].setImageDrawable( getResources().getDrawable( R.drawable.selecteditem ) );
    }

    @Override
    public void onPageScrollStateChanged( int state ){
    }
  };
  //Viewpager.onPageChangeListener end

  /**
   * calculates white or black color on seekbar in dependence of tour color
   * @param play
     */
  public void setImageResource(boolean play)
  {
    int red = Integer.valueOf( farbe.substring( 1, 3 ), 16 );
    int green = Integer.valueOf( farbe.substring( 3, 5 ), 16 );
    int blue = Integer.valueOf( farbe.substring( 5, 7 ), 16 );
    if ((red*0.299 + green*0.587 + blue*0.114) > 186)
    {
      duration.setTextColor(Color.parseColor("#353535"));
      colorString="#353535";
      seekbar.getProgressDrawable().setColorFilter(
        Color.parseColor("#353535"), android.graphics.PorterDuff.Mode.SRC_IN);
      seekbar.getThumb().setColorFilter(Color.parseColor("#353535"), android.graphics.PorterDuff.Mode.SRC_IN);
      if(play) play_button.setImageResource(R.drawable.play_dunkel);
    else play_button.setImageResource(R.drawable.stop_dunkel);}
    else{duration.setTextColor(Color.parseColor("#E6EBE0"));
      colorString="#E6EBE0";
      seekbar.getProgressDrawable().setColorFilter(
        Color.parseColor("#E6EBE0"), android.graphics.PorterDuff.Mode.SRC_IN);

      seekbar.getThumb().setColorFilter(Color.parseColor("#E6EBE0"), android.graphics.PorterDuff.Mode.SRC_IN);
      if(play)play_button.setImageResource(R.drawable.play_hell);
    else play_button.setImageResource(R.drawable.stop_hell);}
}}
