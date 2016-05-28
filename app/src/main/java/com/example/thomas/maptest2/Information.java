package com.example.thomas.maptest2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin on 28.12.2015.
 */

//Zur Zeit lässt sich Audio nur abspielen, wenn Pfad zu einer Datei vorhanden ist.

public class Information extends Activity implements View.OnClickListener {
    //ViewPager mPager;
    //InformationAdapter mAdapter;
    SeekBar seekbar;
    ImageButton play_button;
    MediaPlayer player;
    boolean button_status = false, finished=false;  //Variable für Status des Play-Buttons
    Handler seekHandler = new Handler();
    VideoView vid;
    TextView duration;
    double timeElapsed = 0;
    int videoId, audioId, imgId;
    String video, audio, img;
    ImageView p;
    Intent myIntent2;
    Bundle b;
    RelativeLayout layout;
    String station, farbe, autor, tourname, laenge, desc, zeit;
    TextView title, routenname, prof, info2, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        ImageButton arrdwn = (ImageButton) findViewById(R.id.arrowdown);
        arrdwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        parseData();

        System.out.println("VID: " + videoId + " \nAUD:" + audioId + "\nIMG:" + imgId);

        if(audioId!=0 || videoId!=0 || imgId!=0 )
        {getInit();}
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.map_in, R.anim.fade_out);
        if(audioId!=0)
        {player.stop();
        player.release();
        player=null;}
        if(videoId!=0)
        {vid.stopPlayback();}}

    public void parseData(){
        myIntent2 = getIntent();
        b = myIntent2.getExtras();
        station = (String) b.get("station");
        tourname = (String) b.get("name");
        autor = (String) b.get("autor");
        zeit = (String) b.get("zeit");
        laenge = (String) b.get("laenge");
        farbe = (String) b.get("farbe");
        desc = (String) b.get("desc");
        img = (String) b.get("img");
        audio = (String) b.get("audio");
        video = (String) b.get("video");
        layout = (RelativeLayout) findViewById(R.id.rellayout);
        layout.setBackgroundColor(Color.parseColor(farbe));
        title = (TextView)findViewById(R.id.stationtitle);
        title.setText(station);
        routenname = (TextView)findViewById(R.id.routenname);
        routenname.setText(tourname);
        prof = (TextView) findViewById(R.id.routeninfo1);
        prof.setText(autor);
        info2 = (TextView) findViewById(R.id.routeninfo2);
        info2.setText(zeit + "/" + laenge);
        description = (TextView) findViewById(R.id.stationenbeschreibung);
        description.setText(desc);
        videoId = getResources().getIdentifier(video, "raw", getPackageName());
        audioId = getResources().getIdentifier(audio, "raw", getPackageName());
        imgId = getResources().getIdentifier(img, "drawable", getPackageName());

    }

    public void getInit() {
        //Init
        seekbar = (SeekBar) findViewById(R.id.seek_bar);
        play_button = (ImageButton) findViewById(R.id.play_button);
        duration = (TextView)findViewById(R.id.duration);
        duration.setTextColor(Color.GRAY);
        vid = (VideoView)findViewById(R.id.videoView);
        p = (ImageView)findViewById(R.id.imageScreen);


        hide();
        if(audioId!=0)
        {
        play_button.setOnClickListener((View.OnClickListener) this);
        player = MediaPlayer.create(this, audioId);
        seekbar.getProgressDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC);
        seekbar.setMax(player.getDuration());
        seekbar.setOnSeekBarChangeListener(customSeekBarListener);
        seekbar.getThumb().mutate().setAlpha(0);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer player) {
                    finished=true;
                    seekbar.setProgress(0);
                    duration.setText("0:00");

                    player.pause();
                    play_button.setImageResource(R.drawable.play_hell);
                    button_status = false;
                }

            });


        }            //seekbar.getthumb ist pin auf der seekbar
        if(videoId!=0)
            {if(imgId!=0)
             {
              p.setImageResource(imgId);
              p.setVisibility(View.INVISIBLE);}


        vid.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + videoId));
        vid.requestFocus();
        //vid.setMediaController(new MediaController(this));
        vid.seekTo(1);
        vid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (vid.isPlaying()) {
                    vid.pause();
                    return false;
                } else {
                    vid.start();
                    return false;
                }}

        });

 /*       vid.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                vid.setZOrderOnTop(true);
                vid.setVisibility(View.GONE);
                p.setVisibility(View.VISIBLE);
            }
        });*/



    }
    else if(imgId!=0)
        {p.setImageResource(imgId);
        p.setVisibility(View.VISIBLE);
            System.out.println("test");}
        System.out.println(imgId);
    }





    Runnable run = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };


    public void seekUpdation() {
        if(player!=null && !finished)
        {
         seekbar.setProgress(player.getCurrentPosition());
         timeElapsed = player.getCurrentPosition();
         duration.setText(String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed), TimeUnit.MILLISECONDS.toSeconds((long) timeElapsed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeElapsed))));
/*      if(player.getCurrentPosition()<10000) {
            ImageView p = (ImageView)findViewById(R.id.imageView);
            p.setImageResource(R.drawable.pic1);
        }
        else {
            ImageView p = (ImageView)findViewById(R.id.imageView);
            p.setImageResource(R.drawable.pic2);
        }
*/
        seekHandler.postDelayed(run, 1);}
    }


    public SeekBar.OnSeekBarChangeListener customSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) {
                player.seekTo(progress);
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };





    //@Override
    public void onClick(View play) {
        switch (play.getId()) {
            case R.id.play_button:
                if(button_status == false) {
                    finished=false;
                    player.start();
                    play_button.setImageResource(R.drawable.stop_hell);
                    button_status = true;
                    seekUpdation();
                }
                else {
                    player.pause();
                    play_button.setImageResource(R.drawable.play_hell);
                    button_status = false;
                }
                break;
        }
    }

    public void hide()
    {if(audioId==0)
         {seekbar.setVisibility(View.GONE);
          play_button.setVisibility(View.GONE);
          duration.setVisibility(View.GONE);}

     if(videoId==0)
     {
         vid.setVisibility(View.GONE);
     }

     if(imgId==0)
     {
         p.setVisibility(View.GONE);
     }
    }



}
