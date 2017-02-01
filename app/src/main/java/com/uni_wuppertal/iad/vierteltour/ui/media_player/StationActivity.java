package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


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
  String station, farbe, autor, tourname, laenge, desc, zeit, size, number, slug;
  ImageView dots[];
  Intent myIntent2;
  Bundle b;
  RelativeLayout layout;
  ViewPager imagePager;    //Slidebare Gallery
  InformationPagerAdapter mAdapter;
  LinearLayout pager_indicator;
  RelativeLayout gesperrt;
  boolean sperrvariable=true;
  ImageView pfeilhell;


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
    setContentView( R.layout.information );
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
      overridePendingTransition( R.anim.map_in, R.anim.fade_out );

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

    hide();
    initAudio();
    initImages();
  }

  public void checkGPS()
  {if(PreferenceManager.getDefaultSharedPreferences( getBaseContext() ).getBoolean(slug, false))
    {sperrvariable=false;}
  }

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

    // Currently in the format "img1.jpg,img2.jpg,..."
    // TODO: Convert XML-entry to have one <image>-tag per image entry
    // TODO: Set Video and Image Resources in <Resources></Resources> in right order
//TODO: HIER WURDE TMP EDITIERT
    stationImagePaths = new ArrayList<String>();
    String imagesFromXML = (String) b.get( "img" );

    video = (String) b.get( "video" );
    System.out.println(imagesFromXML+ "\n" + video);
    if( !imagesFromXML.isEmpty() ){
      stationImagePaths = new ArrayList<String>(Arrays.asList(imagesFromXML.split("\\s*,\\s*")));
    }
    if(!video.isEmpty()){
      stationImagePaths.add(video);
    }

    //Dont show Media of not downloaded tours
    /*for(int i=stationImagePaths.size()-1;i>=0;i--)
    {if(OurStorage.get(this).pathToFile(stationImagePaths.get(i))==null)
    stationImagePaths.remove(i);}*/


    audio = (String) b.get( "audio" );


    layout = (RelativeLayout) findViewById( R.id.rellayout );
    layout.setBackgroundColor( Color.parseColor( farbe ) );
    title = (TextView) findViewById( R.id.stationtitle );
    title.setText( station + "  (" + number + "/" + size + ")" );
    routenname = (TextView) findViewById( R.id.routenname );
    routenname.setText( tourname );
    prof = (TextView) findViewById( R.id.routeninfo1 );
    prof.setText( autor );
    info2 = (TextView) findViewById( R.id.routeninfo2 );
    info2.setText( zeit + "/" + laenge );
    description = (TextView) findViewById( R.id.stationenbeschreibung );
    description.setText( desc );
    singlepage.INSTANCE.position(Integer.parseInt(number)-1);
  }

  public void initAll(){//Init
    ImageButton arrdwn = (ImageButton) findViewById( R.id.arrowdown );
    arrdwn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        onBackPressed();
      }
    });
    seekbar = (SeekBar) findViewById( R.id.seek_bar );
    play_button = (ImageButton) findViewById( R.id.play_button );
    duration = (TextView) findViewById( R.id.duration );
    duration.setTextColor( Color.GRAY );
    gesperrt = (RelativeLayout) findViewById(R.id.gesperrt);
    pfeilhell = (ImageView) findViewById(R.id.pfeilhell);
    imagePager = (ViewPager) findViewById( R.id.ImagePager );
    mAdapter = new InformationPagerAdapter( this, stationImagePaths, this);
    pager_indicator = (LinearLayout) findViewById( R.id.viewPagerCountDots );
    imagePager.setAdapter( mAdapter );
    seekbar.setOnSeekBarChangeListener(customSeekBarListener);


  }


  //Audioupdater
  public void seekUpdationAudio(){
    if( player != null && startaudio ){

      seekbar.setProgress( player.getCurrentPosition() );
      timeElapsed = player.getCurrentPosition();

      duration.setText( String.format( "%d:%02d", TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ), TimeUnit.MILLISECONDS.toSeconds( (long) timeElapsed ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ) ) ) );
      seekHandler.postDelayed( run, 100 );
    }
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1) {
      if(resultCode == RESULT_OK){
        imagePager.setCurrentItem(singlepage.INSTANCE.position());
        if(!audio.contains(".mp3") || !player.isPlaying())
        {startaudio=false;
          play_button.setImageResource( R.drawable.play_hell );}}
    }
  }


  //zeigt nur Ressourcen an, die vorhanden sind
  public void hide(){
    if( !audio.contains(".mp3") || sperrvariable || OurStorage.get(this).pathToFile(audio)==null){
      seekbar.setVisibility( View.GONE );
      play_button.setVisibility( View.GONE );
      duration.setVisibility( View.GONE );
    }

    if( (stationImagePaths.size() == 0 && video.isEmpty()) || sperrvariable ){
      imagePager.setVisibility( View.GONE );
    }

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



  private void setUiPageViewController(){

    dotsCount = mAdapter.getCount();
    dots = new ImageView[dotsCount];

    for( int i = 0; i < dotsCount; i++ ){
      dots[i] = new ImageView( this );
      dots[i].setImageDrawable( getResources().getDrawable( R.drawable.nonselecteditem ) );

      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      );

      params.setMargins( 4, 0, 4, 0 );

      pager_indicator.addView( dots[i], params );
    }

    dots[0].setImageDrawable( getResources().getDrawable( R.drawable.selecteditem ) );


  }



  public void initAudio(){
    if( !audio.contains(".mp3") || OurStorage.get(getApplicationContext()).pathToFile(audio)==null){
      play_button.setVisibility(View.GONE);
      seekbar.setVisibility(View.GONE);
      duration.setVisibility(View.GONE);
      return;
    }
    player = ViertelTourMediaPlayer.getInstance( this );



    //number soll später mit id ersetzt werden, leider wurde id bis jetzt noch nicht gesetzt
    //Wenn die gleiche Station geöffnet wird, soll audio nicht neu geladen werden
    if(singlepage.INSTANCE.getId() != Integer.parseInt(number))
    { player.loadAudio( audio );
      singlepage.INSTANCE.setId(Integer.parseInt(number));}



    else if(player.isPlaying())
    {startaudio = true;
      play_button.setImageResource( R.drawable.stop_hell );
      seekUpdationAudio();}



    //CustomKlasse Seekbar
    seekbar.getProgressDrawable().setColorFilter( Color.GRAY, PorterDuff.Mode.SRC );
    seekbar.setMax( player.getDuration() );
  //  seekbar.getThumb().mutate().setAlpha( 0 );//seekbar.getthumb ist pin auf der seekbar

    seekbar.setProgress( player.getCurrentPosition() );
    timeElapsed = player.getCurrentPosition();

    duration.setText( String.format( "%d:%02d", TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ), TimeUnit.MILLISECONDS.toSeconds( (long) timeElapsed ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ) ) ) );


    player.setOnCompletionListener( new MediaPlayer.OnCompletionListener(){
      @Override
      public void onCompletion( MediaPlayer player ){
        player.seekTo(0);
        startaudio = false;
        play_button.setImageResource(R.drawable.play_hell);

        if(stationActivityRunning){Intent background = new Intent(getApplicationContext(), Stationbeendet.class);

        if(size.equals(number)){background.putExtra("vergleich", 1);}
        else {background.putExtra("vergleich", 0);}
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
              play_button.setImageResource( R.drawable.stop_hell );
              seekUpdationAudio();
            } else {
              startaudio=false;
              player.pause();
              play_button.setImageResource( R.drawable.play_hell );
            }break;}}});
  }


  public ViewPager getImagePager()
  {return imagePager;}

  public void initImages(){
    if( stationImagePaths.size() == 0 ){
      return;
    }

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

}
