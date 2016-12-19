package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.uni_wuppertal.iad.vierteltour.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin on 24.11.2016.
 */

public class GalleryMode extends Activity {
  ViewPager imagePagerGallery;
  ViertelTourMediaPlayer player;
  Singletonint singlepage;
  ImageButton play_buttonGallery, x_button;
  SeekBar seekbarGallery;
  VideoView videoplayerGallery;
  TextView gallerytitle, gallerytitletop, durationGallery;
  GalleryPagerAdapter mAdapter2;
  RelativeLayout relGalleryBot, relGalleryTop, stationbeendet;
  Boolean startvideo = true;
  double timeElapsedGallery = 0;
  Handler seekHandlerGallery = new Handler();
  OrientationEventListener orientation;
  int isimages=-1;
  Bundle gallerybundle;
  Intent galleryIntent;
  ArrayList<String> res;
  String video, station;

  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.gallerymode );


    initAll();
    initOrientation();
    gallerymode();
  }

  public void onBackPressed() {
    Intent intent = new Intent();

    if( !video.isEmpty() ){
      player.getVideoview().pause();
      mAdapter2.showImage(imagePagerGallery.getCurrentItem());
      startvideo = false;
      play_buttonGallery.setImageResource( R.drawable.play_hell );
      durationGallery.setText("0:00");
      seekbarGallery.setProgress(0);
      player.getVideoview().seekTo(0);      //Verursacht anfangssound nochmal
      if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_PORTRAIT)
      {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);}
    }
    setResult(RESULT_OK, intent);
    finish();
  }

  @Override
  protected void onDestroy()
  {super.onDestroy();
    orientation.disable();
    if( !video.isEmpty() ){
      singlepage.INSTANCE.setTime(player.getVideoview().getCurrentPosition());
      singlepage.INSTANCE.setPlaying(player.getVideoview().isPlaying());
      startvideo=false;
    }}

public void initAll()
{
  res = new ArrayList<String>();
  galleryIntent = getIntent();
  gallerybundle = galleryIntent.getExtras();
  res = (ArrayList<String>) gallerybundle.get("resources");
  video = (String) gallerybundle.get("video");
  station = (String) gallerybundle.get("station");

  x_button = (ImageButton) findViewById( R.id.x_button );
  seekbarGallery = (SeekBar) findViewById( R.id.seek_barGallery );
  play_buttonGallery = (ImageButton) findViewById( R.id.play_buttonGallery );
  imagePagerGallery = (ViewPager) findViewById( R.id.ImagePagerGallery );

  gallerytitle = (TextView) findViewById( R.id.titleGallery );
  gallerytitletop = (TextView) findViewById(R.id.titleGalleryTop);
  durationGallery = (TextView) findViewById( R.id.durationGallery );
  relGalleryBot = (RelativeLayout) findViewById(R.id.relativeBot);
  relGalleryTop = (RelativeLayout) findViewById(R.id.relativeTop);
  stationbeendet = (RelativeLayout) findViewById(R.id.stationbeendet);
  videoplayerGallery = (VideoView) findViewById( R.id.vid_pager_item_gallery );
  mAdapter2 = new GalleryPagerAdapter(this, res, this);
  imagePagerGallery.setAdapter( mAdapter2 );
  imagePagerGallery.setCurrentItem(singlepage.INSTANCE.getPosition());
  seekbarGallery.setOnSeekBarChangeListener(customSeekBarListener);
  player = ViertelTourMediaPlayer.getInstance( this );
  images();
}

  public void gallerymode(){
    gallerytitle.setText( station );
    if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_PORTRAIT)
    {gallerytitletop.setText( station );
      gallerytitletop.setVisibility(View.VISIBLE);
      gallerytitle.setVisibility(View.GONE);}
    if(res.get(singlepage.INSTANCE.getPosition()).contains("v"))
    {showGalleryVideoBar();}
    else
    {hideGalleryVideoBar();}


    x_button.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        onBackPressed();
      }
    });

    if(!video.isEmpty())video();
  }


  Runnable run2 = new Runnable(){
    @Override
    public void run(){
      seekUpdationVideo();
    }
  };

  //Videoupdater
  public void seekUpdationVideo(){
    if( player.getVideoview() != null && startvideo ){
      //System.out.println("222");
      seekbarGallery.setMax( player.getVideoview().getDuration() );
      seekbarGallery.setProgress( player.getVideoview().getCurrentPosition() );
      timeElapsedGallery = player.getVideoview().getCurrentPosition();

      durationGallery.setText( String.format( "%d:%02d", TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsedGallery ), TimeUnit.MILLISECONDS.toSeconds( (long) timeElapsedGallery ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsedGallery ) ) ) );

      seekHandlerGallery.postDelayed( run2, 100 );
    }
  }


  //TODO: Videoplayer auslagern und abändern
  public void video(){
    // if(player.getVideoview()SOURCE != SOURCE aktuelle Videoview (Wenn die Quellen unterschiedlich sind))
   // player.setVideoview(videoplayerGallery);
    player.loadGalleryVideo(video);
    //videoplayer.setVisibility(View.VISIBLE);
    //player.getVideoview().setVisibility(View.VISIBLE);

  /*  if(singlepage.INSTANCE.getPlaying() && singlepage.INSTANCE.getTime()>0)
    {play_buttonGallery.setImageResource(R.drawable.stop_hell);
      player.getVideoview().seekTo((int) singlepage.INSTANCE.getTime());
      player.getVideoview().start();
      startvideo=true;
      seekUpdationVideo();}
    else if(singlepage.INSTANCE.getTime()>0)
      {play_buttonGallery.setImageResource(R.drawable.play_hell);
        player.getVideoview().seekTo((int) singlepage.INSTANCE.getTime());
     startvideo=false;}*/


    play_buttonGallery.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){


        if( player.getVideoview().isPlaying() ){
          pauseVideoplay();
        } else {

          startVideoplay();
        }
      }
    });

    seekbarGallery.setOnSeekBarChangeListener( customSeekBarListener2 );



    player.getVideoview().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        System.out.println("FINISH");
        stopVideoplay();
      }
    });


    //TODO: Change Image dynamically to Video //AFTER VIDEO CHANGED AND XML UPDATED

    /*    player.getVideoview().setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View view, MotionEvent motionEvent) {

                vf.setDisplayedChild(1);
                play_button.setImageResource(R.drawable.play_hell);
                startaudio=false;
                if(player.isPlaying())
                player.pause();
                videoplayer.setVisibility(View.GONE);
                player.getVideoview().setVisibility(View.VISIBLE);
                singlepage.INSTANCE.setPage(1);
                startvideo=true;
                player.getVideoview().seekTo((int) singlepage.INSTANCE.getTime());
                play_buttonGallery.setImageResource(R.drawable.stop_hell);
                player.getVideoview().start();

                seekbarGallery.setProgress((int) singlepage.INSTANCE.getTime());
                seekUpdationVideo();
            System.out.println("KLICK");
            return false;
            }
        });*/
  }

  public void startVideoplay()
  {if(player.isPlaying())player.pause();
    startvideo = true;
    player.getVideoview().setVisibility(View.VISIBLE);
    mAdapter2.hideImage(imagePagerGallery.getCurrentItem());
    player.getVideoview().start();
    play_buttonGallery.setImageResource( R.drawable.stop_hell );
    seekUpdationVideo();}

  public void pauseVideoplay()
  {startvideo = false;
    player.getVideoview().pause();
    play_buttonGallery.setImageResource( R.drawable.play_hell );}

  public void stopVideoplay()
  {startvideo = false;
    player.getVideoview().pause();
    play_buttonGallery.setImageResource( R.drawable.play_hell );
    mAdapter2.showImage(imagePagerGallery.getCurrentItem());
    player.getVideoview().setVisibility(View.GONE);
    durationGallery.setText("0:00");
    seekbarGallery.setProgress(0);}

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

  public void hideBars()
  {if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
  {  if(relGalleryBot.getVisibility() == View.VISIBLE)
  {Animation slide1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide1_down);
    relGalleryBot.startAnimation(slide1);
    relGalleryBot.setVisibility(View.GONE);}
  if(relGalleryTop.getVisibility() == View.VISIBLE)
  {Animation slide2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide2_up);
    relGalleryTop.startAnimation(slide2);
    relGalleryTop.setVisibility(View.GONE);}}
  }

  public void showGalleryVideoBar()
  {seekbarGallery.setVisibility(View.VISIBLE);
    play_buttonGallery.setVisibility(View.VISIBLE);
    durationGallery.setVisibility(View.VISIBLE);}

  public void hideGalleryVideoBar()
  {seekbarGallery.setVisibility(View.GONE);
    play_buttonGallery.setVisibility(View.GONE);
    durationGallery.setVisibility(View.GONE);}

  public void imageBar()
  {if (relGalleryTop.getVisibility() == View.VISIBLE) {
    Animation slide2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide2_up);
    relGalleryTop.startAnimation(slide2);
    relGalleryTop.setVisibility(View.GONE);}
  else if(relGalleryBot.getVisibility() == View.GONE){
    Animation slide2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide2_down);
    relGalleryTop.startAnimation(slide2);
    relGalleryTop.setVisibility(View.VISIBLE);}
  }

  public void images(){
    if( res.size() > 1 || !video.isEmpty() ){
      isimages=0;
      imagePagerGallery.setOnPageChangeListener(pagechangelisten2);
    }
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

  public SeekBar.OnSeekBarChangeListener customSeekBarListener2 = new SeekBar.OnSeekBarChangeListener(){
    @Override
    public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser ){
      if( fromUser ){
        player.getVideoview().seekTo( progress );
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
        if( arg0>=87 && arg0<=93 ){

          if( !video.isEmpty() )
          {singlepage.INSTANCE.setTime(player.getVideoview().getCurrentPosition());
            singlepage.INSTANCE.setPlaying(player.getVideoview().isPlaying());}
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }


        else if(arg0==180){}

        else if(arg0>=267  && arg0<=273){

          if( !video.isEmpty() )
          {singlepage.INSTANCE.setTime(player.getVideoview().getCurrentPosition());
            singlepage.INSTANCE.setPlaying(player.getVideoview().isPlaying());}
          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        else if((arg0>=357 || arg0<=3) && getResources().getConfiguration().orientation!= Configuration.ORIENTATION_PORTRAIT)
        {

          if( !video.isEmpty())
          {singlepage.INSTANCE.setTime(player.getVideoview().getCurrentPosition());
            singlepage.INSTANCE.setPlaying(player.getVideoview().isPlaying());}
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
  ViewPager.OnPageChangeListener pagechangelisten2 = new ViewPager.OnPageChangeListener(){
    @Override
    public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ){

    }
    @Override
    public void onPageSelected( int position ){
      if(player.getVideoview().isPlaying())
      {stopVideoplay();
      if(position < mAdapter2.getCount()) mAdapter2.showImage(position+1);
      if(position > 0) mAdapter2.showImage(position-1);}      //reset the Neighbours image

      hideBars();

      singlepage.INSTANCE.setPosition(position);


      isimages=singlepage.INSTANCE.getPosition();
      imagePagerGallery.setCurrentItem(singlepage.INSTANCE.getPosition());

      if(res.get(singlepage.INSTANCE.getPosition()).endsWith("mp4"))
      {player.getVideoview().setVideoPath(getExternalFilesDir( null ) +"/" + res.get(position));
        showGalleryVideoBar();}
      else
      {hideGalleryVideoBar();}

    }

    @Override
    public void onPageScrollStateChanged( int state ){}
  };
  //Viewpager.onPageChangeListener end


}
