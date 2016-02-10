package com.example.thomas.maptest2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
public class Information extends Activity implements View.OnClickListener {
    //ViewPager mPager;
    //InformationAdapter mAdapter;
    SeekBar seekbar;
    ImageButton play_button;
    MediaPlayer player;
    boolean button_status = false;  //Variable für Status des Play-Buttons
    Handler seekHandler = new Handler();
    VideoView vid;
    TextView duration;
    double timeElapsed = 0;
    int videoId;
    int audioId;
    int imgId;
    String video;
    String audio;
    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);




        Intent myIntent2 = getIntent();
        Bundle b = myIntent2.getExtras();


        String station = (String) b.get("station");
        String tourname = (String) b.get("name");
        String Autor = (String) b.get("autor");
        String zeit = (String) b.get("zeit");
        String laenge = (String) b.get("laenge");
        String farbe = (String) b.get("farbe");
        String desc = (String) b.get("desc");
        img = (String) b.get("img");
        audio = (String) b.get("audio");
        video = (String) b.get("video");

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rellayout);
        layout.setBackgroundColor(Color.parseColor(farbe));

        TextView title = (TextView)findViewById(R.id.stationtitle);
        title.setText(station);

        TextView routenname = (TextView)findViewById(R.id.routenname);
        routenname.setText(tourname);

        TextView Prof = (TextView) findViewById(R.id.routeninfo1);
        Prof.setText(Autor);

        TextView info2 = (TextView) findViewById(R.id.routeninfo2);
        info2.setText(zeit + "/" + laenge);

        TextView description = (TextView) findViewById(R.id.stationenbeschreibung);
        description.setText(desc);

        videoId = getResources().getIdentifier(video, "raw", getPackageName());
        audioId = getResources().getIdentifier(audio, "raw", getPackageName());
        imgId = getResources().getIdentifier(img, "drawable", getPackageName());


        ImageButton arrdwn = (ImageButton) findViewById(R.id.arrowdown);
        arrdwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        getInit();
        seekUpdation();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.map_in, R.anim.fade_out);
        player.stop();
        vid.stopPlayback();
    }

    public void getInit() {


        seekbar = (SeekBar) findViewById(R.id.seek_bar);
        play_button = (ImageButton) findViewById(R.id.play_button);
        play_button.setOnClickListener((View.OnClickListener) this);
        player = MediaPlayer.create(this, audioId);
        seekbar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        seekbar.setMax(player.getDuration());
        seekbar.setOnSeekBarChangeListener(customSeekBarListener);
        seekbar.getThumb().mutate().setAlpha(0);

        ImageView p = (ImageView)findViewById(R.id.imageView);
        p.setImageResource(imgId);


        vid = (VideoView)findViewById(R.id.videoView);
        vid.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + videoId));
        vid.requestFocus();
        if(vid.getDuration() <= 2000) {
        vid.setVisibility(View.GONE);
        }
        duration = (TextView)findViewById(R.id.textView);





    }



    Runnable run = new Runnable() {


        @Override
        public void run() {
            seekUpdation();
        }

    };






    public void seekUpdation() {
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
        seekHandler.postDelayed(run, 1);

    }


    public SeekBar.OnSeekBarChangeListener customSeekBarListener = new SeekBar.OnSeekBarChangeListener() {




        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {



            if(fromUser)
            {
                player.seekTo(progress);
                vid.seekTo(progress);
            }




        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    //@Override
    public void onClick(View play) {


        ImageButton t = (ImageButton) findViewById(R.id.play_button);
        switch (play.getId()) {
            case R.id.play_button:
                if(button_status == false) {
                    //Toast.makeText(getApplicationContext(), "Playing...", Toast.LENGTH_SHORT).show();
                    vid.start();
                    player.start();
                    t.setImageResource(R.drawable.stop_hell);
                    button_status = true;
                }
                else
                {
                    if(vid.isPlaying()){
                        vid.pause();
                    }
                    player.pause();
                    t.setImageResource(R.drawable.play_hell);
                    //Toast.makeText(getApplicationContext(), "Paused...", Toast.LENGTH_SHORT).show();
                    button_status = false;
                }

                break;

        }

    }

}
