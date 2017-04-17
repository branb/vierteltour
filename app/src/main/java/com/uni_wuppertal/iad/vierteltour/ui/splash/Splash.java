package com.uni_wuppertal.iad.vierteltour.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.utility.ReplaceFont;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;

import static com.google.android.gms.wearable.DataMap.TAG;

//Vorerst nicht benötigt, ist für Splashscreen zuständig, eigentlich in AndroidManifest als Launcher ausgewählt
//Aktuell nicht verwendet
public class Splash extends Activity {
  ProgressBar pbar;
  Intent myintent;
  VideoView vid;
  int progress = 0;
  Handler h = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
   // this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
    //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar
    setContentView(R.layout.splash);
    ReplaceFont.replaceDefaultFont(this, "SERIF", "Bariol_Regular.ttf");   //for MainActivity
    initTypeface();                                                //only for SplashActivity

    pbar = (ProgressBar) findViewById(R.id.progressBar);
    vid = (VideoView) findViewById(R.id.videoView);
    vid.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.animation));
    vid.requestFocus();
    vid.start();

    //IntentLauncher launcher = new IntentLauncher();
    myintent = new Intent(this, MapsActivity.class);

    new Handler().postDelayed(new Runnable(){
      @Override
      public void run() {
        startActivity(myintent);
        finish();
      }
    }, 1000);

   // launcher.start();
  }

  private void initTypeface() {
    Typeface font = Typeface.createFromAsset(getAssets(), "Bariol_Regular.ttf");
    TextView tv = (TextView) findViewById(R.id.textView);
    tv.setTypeface(font);
    TextView tv2 = (TextView) findViewById(R.id.textView2);
    tv2.setTypeface(font);
    TextView tv3 = (TextView) findViewById(R.id.textView3);
    tv3.setTypeface(font);
  }

  protected void onDestroy() {
    super.onDestroy();
  }


  private class IntentLauncher extends Thread {
    @Override
    /**
     * Sleep for some time and than start new activity.
     */
    public void run() {
      try {
        vid = (VideoView) findViewById(R.id.videoView);
        vid.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.animation));
        vid.requestFocus();
        vid.start();
        // Sleeping
        Thread.sleep(20 * 1000);
      } catch (Exception e) {
        Log.e(TAG, e.getMessage());
      }

      // Start main activity
      Intent intent = new Intent(Splash.this, MapsActivity.class);
      Splash.this.startActivity(intent);
      Splash.this.finish();
    }
  }
}
