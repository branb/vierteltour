package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.concurrent.TimeUnit;


public class InformationActivity extends Activity{

  //Declare Var
  SeekBar seekbar, seekbarGallery;        //Fortschrittsbalken
  ImageButton play_button, x_button, play_buttonGallery;      //diverse Bilderbuttons
  ViertelTourMediaPlayer player;          //Media player
  Singletonint singlepage;                //Kann vielleicht weg
  int isimages=-1;
  boolean startaudio = true, startvideo = true;  //Variable für Status des Play-Buttons
  Handler seekHandler = new Handler();
  VideoView vid, videoplayer;                          //Videoplayer
  TextView duration, gallerytitle, gallerytitletop, durationGallery;  //diverse Textfelder
  TextView title, routenname, prof, info2, description;
  double timeElapsed = 0;
  int page = 0, dotsCount;      //page=0 normale Stationenbeschreibung, page=1 Gallery Mode
  String video, audio, stationImagePaths[];
  String station, farbe, autor, tourname, laenge, desc, zeit, size, number;
  ImageView image, dots[];
  Intent myIntent2;
  Bundle b;
  RelativeLayout layout;
  OrientationEventListener orientation;
  ViewFlipper vf;     //tauscht Stationenbeschreibung und Gallery Mode
  ViewPager imagePager, imagePagerGallery;    //Slidebare Gallery
  InformationPagerAdapter mAdapter;
  LinearLayout pager_indicator;
  RelativeLayout relGalleryBot, relGalleryTop; //Layout im Gallerymode für Informationen

  //Runnables zuständig für Aktualisierung der fortgeschrittenen Zeit der Player
  Runnable run = new Runnable(){
    @Override
    public void run(){
      seekUpdationAudio();
    }
  };
  Runnable run2 = new Runnable(){
    @Override
    public void run(){
      seekUpdationVideo();
    }
  };

  @Override
  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.information );

    parseData();      //übergibt Daten von MapsActivity
    getInit();        //Initialisierung
   }

  @Override
  protected void onDestroy()
  {super.onDestroy();
   orientation.disable();
    if( !video.isEmpty() ){
      vid.stopPlayback();
      vid=null;
      startvideo=false;
    }}

  @Override
  public void onBackPressed(){
    if( page == 0 ){
      startaudio=false;

      super.onBackPressed();
      overridePendingTransition( R.anim.map_in, R.anim.fade_out );
      singlepage.INSTANCE.reset();
      if(player.isPlaying()==true)
      {MapsActivity.audiobar.setVisibility(View.VISIBLE);}
      else
      {MapsActivity.audiobar.setVisibility(View.GONE);}
    }

     else if( page == 1 ){
      vf.setDisplayedChild(0);
      startvideo = false;
      vid.pause();
      page = 0;
      videoplayer.setVisibility(View.VISIBLE);
      if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_PORTRAIT)
      { singlepage.INSTANCE.setPage(0);
        if( !video.isEmpty() )
        {singlepage.INSTANCE.setTime(vid.getCurrentPosition());
          singlepage.INSTANCE.setPlaying(vid.isPlaying());}
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);}
    }
  }

  public void getInit(){
    initAll();

    gallerymode();
    initOrientation();
    hide();

    if( !audio.isEmpty() ){audio();}

    if( !video.isEmpty() ){video();}

    if( stationImagePaths.length != 0 ){images();}
  }


  public void parseData(){
    myIntent2 = getIntent();
    b = myIntent2.getExtras();
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

    String imagesFromXML = (String) b.get( "img" );

    if( !imagesFromXML.isEmpty() ){
      this.stationImagePaths = imagesFromXML.split( "," );
    } else {
      this.stationImagePaths = new String[0];
    }


    audio = (String) b.get( "audio" );
    video = (String) b.get( "video" );


    page = singlepage.INSTANCE.getPage();

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
  }

  public void initAll(){//Init
    vf = (ViewFlipper) findViewById( R.id.viewFlipper );
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
    vid = (VideoView) findViewById( R.id.videoViewGallery );
    videoplayer = (VideoView) findViewById(R.id.videoView);
    image = (ImageView)findViewById(R.id.imageScreen);
    gallerytitle = (TextView) findViewById( R.id.titleGallery );
    gallerytitletop = (TextView) findViewById(R.id.titleGalleryTop);
    x_button = (ImageButton) findViewById( R.id.x_button );
    seekbarGallery = (SeekBar) findViewById( R.id.seek_barGallery );
    play_buttonGallery = (ImageButton) findViewById( R.id.play_buttonGallery );
    durationGallery = (TextView) findViewById( R.id.durationGallery );
    relGalleryBot = (RelativeLayout) findViewById(R.id.relativeBot);
    relGalleryTop = (RelativeLayout) findViewById(R.id.relativeTop);
    imagePager = (ViewPager) findViewById( R.id.ImagePager );
    mAdapter = new InformationPagerAdapter( this, stationImagePaths, this);
    pager_indicator = (LinearLayout) findViewById( R.id.viewPagerCountDots );
    imagePager.setAdapter( mAdapter );
    imagePagerGallery = (ViewPager) findViewById( R.id.ImagePagerGallery );
    imagePagerGallery.setAdapter( mAdapter );
    seekbar.setOnSeekBarChangeListener(customSeekBarListener);
    seekbarGallery.setOnSeekBarChangeListener(customSeekBarListener);


  }


  //Audioupdater
  public void seekUpdationAudio(){
    if( player != null && startaudio ){
      System.out.println("111");

      seekbar.setProgress( player.getCurrentPosition() );
      timeElapsed = player.getCurrentPosition();

      duration.setText( String.format( "%d:%02d", TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ), TimeUnit.MILLISECONDS.toSeconds( (long) timeElapsed ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ) ) ) );
      seekHandler.postDelayed( run, 100 );
    }
  }

  //Videoupdater
  public void seekUpdationVideo(){
    if( vid != null && startvideo ){
      System.out.println("222");
      seekbarGallery.setMax( vid.getDuration() );
      seekbarGallery.setProgress( vid.getCurrentPosition() );
      timeElapsed = vid.getCurrentPosition();

      durationGallery.setText( String.format( "%d:%02d", TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ), TimeUnit.MILLISECONDS.toSeconds( (long) timeElapsed ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ) ) ) );

      seekHandler.postDelayed( run2, 100 );
    }
  }


  //zeigt nur Ressourcen an, die vorhanden sind
  public void hide(){
    if( audio.isEmpty() ){
      seekbar.setVisibility( View.GONE );
      play_button.setVisibility( View.GONE );
      duration.setVisibility( View.GONE );
    }

    if( video.isEmpty() ){
      vid.setVisibility( View.INVISIBLE );
    }

    if( stationImagePaths.length == 0 ){
      imagePager.setVisibility( View.GONE );
      imagePagerGallery.setVisibility(View.GONE);
    }
  }


  public void gallerymode(){
    gallerytitle.setText( station );
    if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_PORTRAIT)
    {gallerytitletop.setText( station );
      gallerytitletop.setVisibility(View.VISIBLE);
    gallerytitle.setVisibility(View.GONE);}


    x_button.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        onBackPressed();
      }
    });

    if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_PORTRAIT || page == 1)
    {page=1;
     vf.setDisplayedChild(1);}
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

  public void audio(){
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
        startaudio = false;
        seekbar.setProgress(0);
        duration.setText("0:00");
        play_button.setImageResource(R.drawable.play_hell);
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


//TODO: Videoplayer auslagern und abändern
  public void video(){

    vid.setVideoPath( OurStorage.getInstance( this).getPathToFile( video ) );
    vid.requestFocus();
    vid.setVisibility(View.VISIBLE);
    showGalleryVideoBar();
    if(singlepage.INSTANCE.getPlaying() && singlepage.INSTANCE.getTime()>0)
    {play_buttonGallery.setImageResource(R.drawable.stop_hell);

      vid.seekTo((int) singlepage.INSTANCE.getTime());
      vid.start();
      startvideo=true;
    seekUpdationVideo();}
    else if(singlepage.INSTANCE.getTime()>0)
    {play_buttonGallery.setImageResource(R.drawable.play_hell);
      vid.seekTo((int) singlepage.INSTANCE.getTime());
    startvideo=false;}
    play_buttonGallery.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        if( vid.isPlaying() ){
          startvideo = false;
          vid.pause();
          play_buttonGallery.setImageResource( R.drawable.play_hell );
        } else {
          startvideo = true;
          vid.start();
          play_buttonGallery.setImageResource( R.drawable.stop_hell );
          seekUpdationVideo();
        }
      }
    });

    seekbarGallery.setOnSeekBarChangeListener( customSeekBarListener2 );
    vid.setOnTouchListener( new View.OnTouchListener(){
      @Override
      public boolean onTouch( View v, MotionEvent motionEvent ){
        if( vid.isPlaying() && getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
          startvideo = false;
          vid.pause();
          play_buttonGallery.setImageResource( R.drawable.play_hell );

        }
        else if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE && vid != null)
        {mediaplayerbars();
           }
        else {
          startvideo = true;
          vid.start();
          play_buttonGallery.setImageResource( R.drawable.stop_hell );
          seekUpdationVideo();}
        return false;
      }
    });

            vid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                play_buttonGallery.setImageResource(R.drawable.play_hell);
                startvideo=false;
                durationGallery.setText("0:00");
                seekbarGallery.setProgress(0);
            }
        });


    //TODO: Change Image dynamically to Video //AFTER VIDEO CHANGED AND XML UPDATED + Some bugs flipping between gallery and station
        videoplayer.setVisibility(View.VISIBLE);
        videoplayer.setVideoPath( OurStorage.getInstance( this).getPathToFile( video ) );
        videoplayer.seekTo(100);
        image.setVisibility(View.VISIBLE);
        image.setImageResource(R.drawable.play_hell);
        videoplayer.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View view, MotionEvent motionEvent) {

                vf.setDisplayedChild(1);
                play_button.setImageResource(R.drawable.play_hell);
                startaudio=false;
                if(player.isPlaying())
                player.pause();
                videoplayer.setVisibility(View.GONE);
                page=1;
                startvideo=true;
                vid.seekTo((int) singlepage.INSTANCE.getTime());
                play_buttonGallery.setImageResource(R.drawable.stop_hell);
                vid.start();

                seekbarGallery.setProgress((int) singlepage.INSTANCE.getTime());
                seekUpdationVideo();

            return false;
            }
        });
  }

  public void images(){
    if( stationImagePaths.length > 1 ){
      isimages=0;
        imagePager.setOnPageChangeListener(pagechangelisten);
        imagePagerGallery.setOnPageChangeListener(pagechangelisten);
      setUiPageViewController();
    }
  }



  public void mediaplayerbars()
  {if (relGalleryBot.getVisibility() == View.VISIBLE) {
    Animation slide1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide1_down);
    Animation slide2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide2_up);
    relGalleryBot.startAnimation(slide1);
    relGalleryTop.startAnimation(slide2);
    relGalleryBot.setVisibility(View.GONE);
    relGalleryTop.setVisibility(View.GONE);

  }
  else if(relGalleryBot.getVisibility() == View.GONE){
    Animation slide1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide1_up);
    Animation slide2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide2_down);
    relGalleryBot.startAnimation(slide1);
    relGalleryTop.startAnimation(slide2);
    relGalleryBot.setVisibility(View.VISIBLE);
    relGalleryTop.setVisibility(View.VISIBLE);
  }}

  public void showGalleryVideoBar()
  {seekbarGallery.setVisibility(View.VISIBLE);
    play_buttonGallery.setVisibility(View.VISIBLE);
    durationGallery.setVisibility(View.VISIBLE);}







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

  public SeekBar.OnSeekBarChangeListener customSeekBarListener2 = new SeekBar.OnSeekBarChangeListener(){
    @Override
    public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser ){
      if( fromUser ){
        vid.seekTo( progress );
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

  //EIGENE KLASSE Orientation, zusätzlich muss geprüft werden, ob bildschirm gedreht werden darf und empfindlichkeit anpassung
  public void initOrientation(){//Landscape/Portrait change
    orientation = new OrientationEventListener( this, SensorManager.SENSOR_DELAY_NORMAL ){
      @Override
      public void onOrientationChanged( int arg0 ){
        arg0= arg0%360;

//TODO: Check orientation with variables and permission of orientation  //AFTER VIDEOPLAYER CHANGE
        if( arg0>=87 && arg0<=93  && page==1 ){

          singlepage.INSTANCE.setPage(1);
          if( !video.isEmpty() )
          {singlepage.INSTANCE.setTime(vid.getCurrentPosition());
            singlepage.INSTANCE.setPlaying(vid.isPlaying());}
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }


        else if(arg0==180){}

        else if(arg0>=267  && arg0<=273 && page==1){

          singlepage.INSTANCE.setPage(1);
          if( !video.isEmpty() )
          {singlepage.INSTANCE.setTime(vid.getCurrentPosition());
            singlepage.INSTANCE.setPlaying(vid.isPlaying());}
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        else if((arg0>=357 || arg0<=3) && getResources().getConfiguration().orientation!= Configuration.ORIENTATION_PORTRAIT)
        {

          singlepage.INSTANCE.setPage(1);
          if( !video.isEmpty())
          {singlepage.INSTANCE.setTime(vid.getCurrentPosition());
            singlepage.INSTANCE.setPlaying(vid.isPlaying());}
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
      }
    };
    if( orientation.canDetectOrientation() ){
      orientation.enable();
    }
  }
//Orientation end

  //ViewPager.OnPageChangeListener
  ViewPager.OnPageChangeListener pagechangelisten = new ViewPager.OnPageChangeListener(){
    @Override
    public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ){

    }

    @Override
    public void onPageSelected( int position ){
      isimages=position;
      imagePagerGallery.setCurrentItem(position);
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
