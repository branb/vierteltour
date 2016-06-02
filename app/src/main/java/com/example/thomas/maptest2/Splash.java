package com.example.thomas.maptest2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

//Vorerst nicht benötigt, ist für Splashscreen zuständig, eigentlich in AndroidManifest als Launcher ausgewählt

public class Splash extends Activity{
  ProgressBar pbar;
  VideoView vid;
  int progress = 0;
  Handler h = new Handler();

  @Override
  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.splash );
    ReplaceFont.replaceDefaultFont( this, "SERIF", "Bariol_Regular.ttf" );   //for MainActivity
    initTypeface();                                                //only for SplashActivity

    pbar = (ProgressBar) findViewById( R.id.progressBar );
    vid = (VideoView) findViewById( R.id.videoView );
    vid.setVideoURI( Uri.parse( "android.resource://" + getPackageName() + "/" + R.raw.animation ) );
    vid.requestFocus();
    vid.start();

    Thread t = new Thread( new Runnable(){
      @Override
      public void run(){
        for( int j = 0; j < 5; j++ ){
          progress += 20;
          h.post( new Runnable(){
            @Override
            public void run(){
              pbar.setProgress( progress );
              if( progress == pbar.getMax() ){
                vid.pause();
                Intent i = new Intent( getBaseContext(), MapsActivity.class );
                startActivity( i );
                finish();
              }
            }
          } );
          try{
            Thread.sleep( 3000 );       //3 seconds
          } catch( InterruptedException e ){
            e.printStackTrace();
          }
        }
      }
    } );
    t.start();
  }

  private void initTypeface(){
    Typeface font = Typeface.createFromAsset( getAssets(), "Bariol_Regular.ttf" );
    TextView tv = (TextView) findViewById( R.id.textView );
    tv.setTypeface( font );
    TextView tv2 = (TextView) findViewById( R.id.textView2 );
    tv2.setTypeface( font );
    TextView tv3 = (TextView) findViewById( R.id.textView3 );
    tv3.setTypeface( font );
  }

  protected void onDestroy(){
    super.onDestroy();
  }


}
