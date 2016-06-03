package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.app.Activity;
import android.content.Intent;
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

import java.util.concurrent.TimeUnit;


public class InformationActivity extends Activity{
  //ViewPager mPager;
  //InformationAdapter mAdapter;
  SeekBar seekbar, seekbarGallery;
  ImageButton play_button, x_button, play_buttonGallery;
  MediaPlayer player;
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
  boolean button_status = false, finished = false, start = true;  //Variable für Status des Play-Buttons
  Handler seekHandler = new Handler();
  VideoView vid;
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
  TextView duration, gallerytitle, durationGallery;
  double timeElapsed = 0;
  int videoId, audioId, imgId[], page = 0, dotsCount;
  String video, audio, img;
  ImageView image, dots[];
  Intent myIntent2;
  Bundle b;
  RelativeLayout layout;
  String station, farbe, autor, tourname, laenge, desc, zeit, size, number;
  TextView title, routenname, prof, info2, description;
  OrientationEventListener changed;
  ViewFlipper vf;
  ViewPager imagePager, imagePagerGallery;
  InformationPagerAdapter mAdapter;
  LinearLayout pager_indicator;

  Runnable run = new Runnable(){
    @Override
    public void run(){
      seekUpdation();
    }
  };
  Runnable run2 = new Runnable(){
    @Override
    public void run(){
      seekUpdation2();
    }
  };

  @Override
  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.information );
    vf = (ViewFlipper) findViewById( R.id.viewFlipper );
    parseData();
    getInit();
  }

  @Override
  public void onBackPressed(){
    if( page == 0 ){
      super.onBackPressed();
      overridePendingTransition( R.anim.map_in, R.anim.fade_out );
      if( audioId != 0 ){
        player.stop();
        player.release();
        player = null;
      }
      if( videoId != 0 ){
        vid.stopPlayback();
      }
    } else if( page == 1 ){
      vf.showPrevious();
      start = false;
      vid.pause();
      page = 0;
    }
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
    img = (String) b.get( "img" );
    audio = (String) b.get( "audio" );
    video = (String) b.get( "video" );
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
    videoId = getResources().getIdentifier( video, "raw", getPackageName() );
    audioId = getResources().getIdentifier( audio, "raw", getPackageName() );

    //Temporäres einlesen mehrerer Bilder gleichzeitig
    //Später über XML Parser zu realisieren
    if( !img.isEmpty() ){
      int i = 0;
      char[] stringArray = img.toCharArray();
      String neueString = "";
      boolean erg = true;

      int count = 1;
      for( int j = 0; j < stringArray.length; j++ ){
        if( String.valueOf( stringArray[j] ).equals( "," ) ){
          count++;
        }
      }
      imgId = new int[count];


      //Temporaer
      while( erg ){
        String tmp = "";
        erg = false;
        for( int j = 0; j < stringArray.length; j++ ){
          if( String.valueOf( stringArray[j] )
                    .equals( "," ) ){
            char[] tmpArray = new char[stringArray.length - j - 1];
            for( int k = 0; k < stringArray.length - j - 1; k++ ){
              tmpArray[k] = stringArray[k + j + 1];
            }
            stringArray = tmpArray;
            erg = true;
            break;
          } else {
            tmp += String.valueOf( stringArray[j] )
                         .toString();
          }
        }
        imgId[i] = getResources().getIdentifier( tmp, "drawable", getPackageName() );
        i++;
      }
    }
  }

  public void getInit(){
    initAll();

    gallerymode();
    initOrientation();
    hide();

    if( audioId != 0 ){
      audio();
    }

    if( videoId != 0 ){
      video();
    }

    if( imgId[0] != 0 ){
      images();
    }
  }

  public void seekUpdation(){
    if( player != null && !finished ){
      seekbar.setProgress( player.getCurrentPosition() );
      timeElapsed = player.getCurrentPosition();
      duration.setText( String.format( "%d:%02d", TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ), TimeUnit.MILLISECONDS.toSeconds( (long) timeElapsed ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ) ) ) );
/*      if(player.getCurrentPosition()<10000) {
            ImageView p = (ImageView)findViewById(R.id.imageView);
            p.setImageResource(R.drawable.pic1);
        }
        else {
            ImageView p = (ImageView)findViewById(R.id.imageView);
            p.setImageResource(R.drawable.pic2);
        }
*/
      seekHandler.postDelayed( run, 100 );
    }
  }

  public void seekUpdation2(){
    if( vid != null && start ){
      seekbarGallery.setMax( vid.getDuration() );
      seekbarGallery.setProgress( vid.getCurrentPosition() );
      timeElapsed = vid.getCurrentPosition();
      durationGallery.setText( String.format( "%d:%02d", TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ), TimeUnit.MILLISECONDS.toSeconds( (long) timeElapsed ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long) timeElapsed ) ) ) );
/*      if(player.getCurrentPosition()<10000) {
            ImageView p = (ImageView)findViewById(R.id.imageView);
            p.setImageResource(R.drawable.pic1);
        }
        else {
            ImageView p = (ImageView)findViewById(R.id.imageView);
            p.setImageResource(R.drawable.pic2);
        }
*/
      seekHandler.postDelayed( run2, 100 );
    }
  }


  //@Override

  public void hide(){
    if( audioId == 0 ){
      seekbar.setVisibility( View.GONE );
      play_button.setVisibility( View.GONE );
      duration.setVisibility( View.GONE );
    }

    if( videoId == 0 ){
      vid.setVisibility( View.GONE );
    }

    if( imgId[0] == 0 ){
      imagePager.setVisibility( View.GONE );
    }
  }

 /*   @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "PORTRAIT",
                    Toast.LENGTH_LONG).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "LANDSCAPE",
                    Toast.LENGTH_LONG).show();
        }

    }
*/

  public void initOrientation(){//Landscape/Portrait change
    changed = new OrientationEventListener( this, SensorManager.SENSOR_DELAY_NORMAL ){
      @Override
      public void onOrientationChanged( int arg0 ){
        if( arg0 >= 90 && arg0 <= 270 ){
          Toast.makeText( getApplicationContext(), "PORTRAIT", Toast.LENGTH_LONG )
               .show();
        }
      }
    };
    if( changed.canDetectOrientation() ){
      changed.enable();
    }
  }


  public void gallerymode(){
    gallerytitle.setText( station );
    x_button.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        vid.pause();
        start = false;
        page = 0;
        vf.showPrevious();
      }
    });


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
    vid = (VideoView) findViewById( R.id.videoViewGallery );
    // image = (ImageView)findViewById(R.id.imageScreen);
    gallerytitle = (TextView) findViewById( R.id.titleGallery );
    x_button = (ImageButton) findViewById( R.id.x_button );
    seekbarGallery = (SeekBar) findViewById( R.id.seek_barGallery );
    play_buttonGallery = (ImageButton) findViewById( R.id.play_buttonGallery );
    durationGallery = (TextView) findViewById( R.id.durationGallery );
    imagePager = (ViewPager) findViewById( R.id.ImagePager );
    mAdapter = new InformationPagerAdapter( this, imgId );
    pager_indicator = (LinearLayout) findViewById( R.id.viewPagerCountDots );
    imagePager.setAdapter( mAdapter );
    imagePagerGallery = (ViewPager) findViewById( R.id.ImagePagerGallery );
    imagePagerGallery.setAdapter( mAdapter );
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
    play_button.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View play ){
        switch( play.getId() ){
          case R.id.play_button:
            if( !button_status ){
              finished = false;
              player.start();
              play_button.setImageResource( R.drawable.stop_hell );
              button_status = true;
              seekUpdation();
            } else {
              player.pause();
              play_button.setImageResource( R.drawable.play_hell );
              button_status = false;
            }
            break;
        }

      }
    } );
    player = MediaPlayer.create( this, audioId );
    seekbar.getProgressDrawable()
           .setColorFilter( Color.GRAY, PorterDuff.Mode.SRC );
    seekbar.setMax( player.getDuration() );
    seekbar.setOnSeekBarChangeListener( customSeekBarListener );
    seekbar.getThumb()
           .mutate()
           .setAlpha( 0 );//seekbar.getthumb ist pin auf der seekbar
    player.setOnCompletionListener( new MediaPlayer.OnCompletionListener(){
      @Override
      public void onCompletion( MediaPlayer player ){
        finished = true;
        seekbar.setProgress( 0 );
        duration.setText( "0:00" );

        player.pause();
        play_button.setImageResource( R.drawable.play_hell );
        button_status = false;
      }

    });

  }

  public void video(){
    vid.setVideoURI( Uri.parse( "android.resource://" + getPackageName() + "/" + videoId ) );
    vid.requestFocus();
    //vid.setMediaController(new MediaController(this));
    play_buttonGallery.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        if( vid.isPlaying() ){
          start = false;
          vid.pause();
          play_buttonGallery.setImageResource( R.drawable.play_hell );
        } else {
          start = true;
          vid.start();
          play_buttonGallery.setImageResource( R.drawable.stop_hell );
          seekUpdation2();
        }
      }
    });

    seekbarGallery.setOnSeekBarChangeListener( customSeekBarListener2 );
    vid.setOnTouchListener( new View.OnTouchListener(){
      @Override
      public boolean onTouch( View v, MotionEvent motionEvent ){
        if( vid.isPlaying() ){
          start = false;
          vid.pause();
          play_buttonGallery.setImageResource( R.drawable.play_hell );
          return false;
        } else {
          start = true;
          vid.start();
          play_buttonGallery.setImageResource( R.drawable.stop_hell );
          seekUpdation2();
          return false;
        }
      }
    });

     /*       vid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                vid.setZOrderOnTop(true);
                vid.setVisibility(View.GONE);
                p.setVisibility(View.VISIBLE);
            }
        });*/

       /* image.setVisibility(View.VISIBLE);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vf.showNext();
                play_button.setImageResource(R.drawable.play_hell);
                button_status = false;
                finished=true;
                if(player.isPlaying())player.pause();
                page=1;
                start=true;
                vid.start();
                seekUpdation2();
            }
        });*/
  }

  public void images(){
    imagePager.setCurrentItem( 0 );

    if( imgId.length > 1 ){
      imagePager.setOnPageChangeListener( new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels ){
        }

        @Override
        public void onPageSelected( int position ){
          for( int i = 0; i < dotsCount; i++ ){
            dots[i].setImageDrawable( getResources().getDrawable( R.drawable.nonselecteditem ) );
          }

          dots[position].setImageDrawable( getResources().getDrawable( R.drawable.selecteditem ) );
        }

        @Override
        public void onPageScrollStateChanged( int state ){
        }
      });
      setUiPageViewController();

    }
  }
}
