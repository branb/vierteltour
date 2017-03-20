package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin on 24.11.2016.
 */

public class GalleryMode extends Activity {
  ViewPager imagePagerGallery;
  ViertelTourMediaPlayer player;
  Singletonint singlepage;
  ImageButton play_buttonGallery, x_button, x_button_bar, play_buttonGallery_bar;
  SeekBar seekbarGallery, seekbarGallery_bar;
  TextView gallerytitle, gallerytitletop, durationGallery, durationGallery_bar;
  GalleryPagerAdapter mAdapter2;
  RelativeLayout relGalleryBot, relGalleryTop;
  Boolean startvideo = true;
  double timeElapsedGallery = 0;
  Handler seekHandlerGallery = new Handler();
  int isimages=-1;
  Bundle gallerybundle;
  Intent galleryIntent;
  ArrayList<String> res;
  String video, station, path;

  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.gallerymode );

    initAll();
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
      player.getVideoview().seekTo(0);
      if(getResources().getConfiguration().orientation!= Configuration.ORIENTATION_PORTRAIT)
      {setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);}
    }
    setResult(RESULT_OK, intent);
    finish();
  }

public void initAll()
{
  res = new ArrayList<String>();
  galleryIntent = getIntent();
  gallerybundle = galleryIntent.getExtras();
  res = (ArrayList<String>) gallerybundle.get("resources");
  video = (String) gallerybundle.get("video");
  station = (String) gallerybundle.get("station");
  path = (String) gallerybundle.get("path");

  x_button = (ImageButton) findViewById( R.id.x_button );
  x_button_bar = (ImageButton) findViewById( R.id.x_button_bar );
  seekbarGallery = (SeekBar) findViewById( R.id.seek_barGallery );
  seekbarGallery_bar = (SeekBar) findViewById( R.id.seek_barGallery_bar );
  play_buttonGallery = (ImageButton) findViewById( R.id.play_buttonGallery );
  play_buttonGallery_bar = (ImageButton) findViewById( R.id.play_buttonGallery_bar );
  imagePagerGallery = (ViewPager) findViewById( R.id.ImagePagerGallery );
  gallerytitle = (TextView) findViewById( R.id.titleGallery );
  gallerytitletop = (TextView) findViewById(R.id.titleGalleryTop_bar);
  durationGallery = (TextView) findViewById( R.id.durationGallery );
  durationGallery_bar = (TextView) findViewById( R.id.durationGallery_bar );
  relGalleryBot = (RelativeLayout) findViewById(R.id.relativeBot);
  relGalleryTop = (RelativeLayout) findViewById(R.id.relativeTop);
  mAdapter2 = new GalleryPagerAdapter(this, res);
  imagePagerGallery.setAdapter( mAdapter2 );
  imagePagerGallery.setCurrentItem(singlepage.INSTANCE.position());
  imagePagerGallery.setOffscreenPageLimit(5);
  player = ViertelTourMediaPlayer.getInstance( this );
  images();
}

  /**
   * Used when switching between portrait and landscape mode
   * @param newConfig
     */
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    // Checks the orientation of the screen
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      x_button.setVisibility(View.GONE);
      gallerytitle.setVisibility(View.GONE);
      gallerytitletop.setText(station);
      hideGalleryVideoBar();
      ViewGroup.LayoutParams params = imagePagerGallery.getLayoutParams();
      params.height = ViewPager.LayoutParams.MATCH_PARENT;
      imagePagerGallery.setLayoutParams(params);

    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
      relGalleryBot.setVisibility(View.GONE);
      relGalleryTop.setVisibility(View.GONE);
      x_button.setVisibility(View.VISIBLE);
      gallerytitle.setVisibility(View.VISIBLE);
      if(res.get(singlepage.INSTANCE.position()).endsWith("mp4"))showGalleryVideoBar();

      ViewGroup.LayoutParams params = imagePagerGallery.getLayoutParams();
      params.height = calculateDP(300);
      imagePagerGallery.setLayoutParams(params);
    }
  }
/**
  * Layout Setup
 */
  public void gallerymode(){
    gallerytitle.setText( station );

    if ( getResources().getConfiguration().orientation  == Configuration.ORIENTATION_LANDSCAPE) {   //do in Landscape mode
      x_button.setVisibility(View.GONE);                                //Hide Layout except Image/Video
      gallerytitle.setVisibility(View.GONE);
      gallerytitletop.setText(station);
      hideGalleryVideoBar();
      ViewGroup.LayoutParams params = imagePagerGallery.getLayoutParams();      //Resize Viewpager of Image/Video
      params.height = ViewPager.LayoutParams.MATCH_PARENT;
      imagePagerGallery.setLayoutParams(params);}
    else if (getResources().getConfiguration().orientation  == Configuration.ORIENTATION_PORTRAIT){
      relGalleryBot.setVisibility(View.GONE);
      relGalleryTop.setVisibility(View.GONE);
      x_button.setVisibility(View.VISIBLE);
      gallerytitle.setVisibility(View.VISIBLE);
      if(res.get(singlepage.INSTANCE.position()).endsWith("mp4")){showGalleryVideoBar();}

      ViewGroup.LayoutParams params = imagePagerGallery.getLayoutParams();//Resize Viewpager of Image/Video
      params.height = calculateDP(300);
      imagePagerGallery.setLayoutParams(params);
    }

    if(res.get(singlepage.INSTANCE.position()).endsWith("mp4") && getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
    {showGalleryVideoBar();}
    else
    {hideGalleryVideoBar();}


    x_button.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        onBackPressed();
      }
    });
    x_button_bar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });

    if(!video.isEmpty())video();

    //White Color for Seekbar and Thumb
    seekbarGallery.getProgressDrawable().setColorFilter(
      Color.parseColor("#E6EBE0"), android.graphics.PorterDuff.Mode.SRC_IN);
    seekbarGallery.getThumb().setColorFilter(Color.parseColor("#E6EBE0"), android.graphics.PorterDuff.Mode.SRC_IN);
    seekbarGallery_bar.getProgressDrawable().setColorFilter(
      Color.parseColor("#E6EBE0"), android.graphics.PorterDuff.Mode.SRC_IN);
    seekbarGallery_bar.getThumb().setColorFilter(Color.parseColor("#E6EBE0"), android.graphics.PorterDuff.Mode.SRC_IN);
  }

  /**
   * Thread to refresh Progress of Seekbar in the background
   */
  Runnable run2 = new Runnable(){
    @Override
    public void run(){
      seekUpdationVideo();
    }
  };

  /**
   * Updating Video seekbar and textview
   */
  public void seekUpdationVideo(){
    if( player.getVideoview() != null && startvideo ){
      seekbarGallery.setMax( player.getVideoview().getDuration() );
      seekbarGallery_bar.setMax( player.getVideoview().getDuration() );
      seekbarGallery.setProgress( player.getVideoview().getCurrentPosition() );
      seekbarGallery_bar.setProgress( player.getVideoview().getCurrentPosition() );

      timeElapsedGallery = player.getVideoview().getCurrentPosition();

      durationGallery.setText( String.format( "%d:%02d", TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsedGallery ), TimeUnit.MILLISECONDS.toSeconds( (long) timeElapsedGallery ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsedGallery ) ) ) );
      durationGallery_bar.setText( String.format( "%d:%02d", TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsedGallery ), TimeUnit.MILLISECONDS.toSeconds( (long) timeElapsedGallery ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsedGallery ) ) ) );

      seekHandlerGallery.postDelayed( run2, 100 );
    }
  }


  /**
   * Used if viewpager contains video
   */
  public void video(){

    seekbarGallery.setOnSeekBarChangeListener( customSeekBarListenerVideo );
    seekbarGallery_bar.setOnSeekBarChangeListener( customSeekBarListenerVideo );

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

    play_buttonGallery_bar.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){


        if( player.getVideoview().isPlaying() ){
          pauseVideoplay();
        } else {

          startVideoplay();
        }
      }
    });

    //After Video is finished, use this method
    player.getVideoview().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        stopVideoplay();
    /*    if(station.contains("einleitung")){Intent background = new Intent(getApplicationContext(), Stationbeendet.class);

          background.putExtra("vergleich", 0);
          background.putExtra("pfad", path);
          startActivity(background);
         }*/
      }});

    startVideoplay();
  }

  /**
   * Stops Audio, Starts Video, Managing all Buttons
   */
  public void startVideoplay()
  {if(player.isPlaying())player.pause();
    startvideo = true;
    player.getVideoview().setVisibility(View.VISIBLE);
    try{mAdapter2.hideImage(imagePagerGallery.getCurrentItem());}catch(Exception e){}
    player.getVideoview().start();
    play_buttonGallery.setImageResource( R.drawable.stop_hell );
    play_buttonGallery_bar.setImageResource( R.drawable.stop_hell );
    seekUpdationVideo();}

  /**
   * Pausing video and set buttons
   */
  public void pauseVideoplay()
  {startvideo = false;
    player.getVideoview().pause();
    play_buttonGallery.setImageResource( R.drawable.play_hell );
    play_buttonGallery_bar.setImageResource( R.drawable.play_hell );}

  /**
   * Stops video and sets to 0
   */
  public void stopVideoplay()
  {startvideo = false;
    player.getVideoview().pause();
    play_buttonGallery.setImageResource( R.drawable.play_hell );
    play_buttonGallery_bar.setImageResource(R.drawable.play_hell);
    mAdapter2.showImage(imagePagerGallery.getCurrentItem());
    player.getVideoview().setVisibility(View.GONE);
    durationGallery_bar.setText("0:00");
    seekbarGallery_bar.setProgress(0);
    durationGallery.setText("0:00");
    seekbarGallery.setProgress(0);}

  /**
   * Is Used to show/hide the Bars in Landscapemode on top and bottom of screen
   */
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

  /**
   * Hides single Bar if shown
   */
  public void hideBars()
  {if(relGalleryBot.getVisibility() == View.VISIBLE)
  {Animation slide1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide1_down);
    relGalleryBot.startAnimation(slide1);
    relGalleryBot.setVisibility(View.GONE);}
  if(relGalleryTop.getVisibility() == View.VISIBLE)
  {Animation slide2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide2_up);
    relGalleryTop.startAnimation(slide2);
    relGalleryTop.setVisibility(View.GONE);}
  }

  /**
   * Shows custom videoseekbar
   */
  public void showGalleryVideoBar()
  {seekbarGallery.setVisibility(View.VISIBLE);
    play_buttonGallery.setVisibility(View.VISIBLE);
    durationGallery.setVisibility(View.VISIBLE);}

  /**
   * Hides custom videoseekbar
   */
  public void hideGalleryVideoBar()
  {seekbarGallery.setVisibility(View.GONE);
    play_buttonGallery.setVisibility(View.GONE);
    durationGallery.setVisibility(View.GONE);}

  /**
   *
   */
  public void imageBar()
  {if (relGalleryTop.getVisibility() == View.VISIBLE) {
    Animation slide2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide2_up);
    relGalleryTop.startAnimation(slide2);
    relGalleryTop.setVisibility(View.GONE);}
  else if(relGalleryTop.getVisibility() == View.GONE){
    Animation slide2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide2_down);
    relGalleryTop.startAnimation(slide2);
    relGalleryTop.setVisibility(View.VISIBLE);}
  }

  /**
   * calculates sizes for ViewPager depending on pixels and size on used screen
   * @param pixel
   * @return
     */
  public int calculateDP(int pixel)
  {DisplayMetrics metrics = getResources().getDisplayMetrics();
    float dp = pixel;
    float fpixels = metrics.density * dp;
    return (int) (fpixels + 0.5f);}

  public void images(){
    if( res.size() > 1 || !video.isEmpty() ){
      isimages=0;
      imagePagerGallery.setOnPageChangeListener(pagechangelisten2);
    }
  }

  //Custom Class Seekbar start

  public SeekBar.OnSeekBarChangeListener customSeekBarListenerVideo = new SeekBar.OnSeekBarChangeListener(){
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

  //ViewPager.OnPageChangeListener
  ViewPager.OnPageChangeListener pagechangelisten2 = new ViewPager.OnPageChangeListener(){
    @Override
    public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ){

    }
    //If Page is Selected, stop last videoplay and reset Neighbours
    @Override
    public void onPageSelected( int position ){
      if(player.getVideoview().isPlaying())
      {stopVideoplay();
      if(position < mAdapter2.getCount()) mAdapter2.showImage(position);
      if(position > 0) mAdapter2.showImage(position-1);}      //reset the Neighbours image

      hideBars();

      singlepage.INSTANCE.position(position);   //Updating position


      isimages=singlepage.INSTANCE.position();
      imagePagerGallery.setCurrentItem(singlepage.INSTANCE.position());

      if(res.get(singlepage.INSTANCE.position()).endsWith("mp4") && getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE && res.get(position)!=null)
      { player.getVideoview().setVideoPath(getExternalFilesDir( null ) +"/" + res.get(position));
        showGalleryVideoBar();}
      else
      {hideGalleryVideoBar();}

      if(res.get(singlepage.INSTANCE.position()).endsWith("mp4")) startVideoplay();
    }

    @Override
    public void onPageScrollStateChanged( int state ){}
  };
  //Viewpager.onPageChangeListener end


}
