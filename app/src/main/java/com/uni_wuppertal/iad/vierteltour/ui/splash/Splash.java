package com.uni_wuppertal.iad.vierteltour.ui.splash;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.vision.text.Text;
import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.utility.ReplaceFont;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.utility.updater.UpdateListener;
import com.uni_wuppertal.iad.vierteltour.utility.updater.Updater;

import static com.google.android.gms.wearable.DataMap.TAG;

//Vorerst nicht benötigt, ist für Splashscreen zuständig, eigentlich in AndroidManifest als Launcher ausgewählt
//Aktuell nicht verwendet
public class Splash extends Activity implements UpdateListener {
  ProgressBar pbar;
  Intent myintent;
  VideoView vid;
  Boolean stop=false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash);
    ReplaceFont.replaceDefaultFont(this, "SERIF", "Bariol_Regular.ttf");   //for MainActivity
    initTypeface();                                                //only for SplashActivity

    pbar = (ProgressBar) findViewById(R.id.progressBar);
    vid = (VideoView) findViewById(R.id.videoView);
    vid.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.animation));
    vid.requestFocus();
    vid.start();


    checkForUpdates();
    //IntentLauncher launcher = new IntentLauncher();
    myintent = new Intent(this, MapsActivity.class);

    new Handler().postDelayed(new Runnable(){
      @Override
      public void run() {
        if(!stop)
        {startActivity(myintent);
        finish();}
      }
    }, 1000);
    //checkForUpdates();
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

  /**
   * Check if localVersion in SharedPreferences exists to check if tourlist was downloaded
   */

  /**
   * Check for Updates
   * If no internetconnection or no new Updates move to mainActivity
   */
  public void checkForUpdates()
  {//No Internet with first start
    if(!PreferenceManager.getDefaultSharedPreferences( getBaseContext() ).contains( "localTourdataVersion" ) && !Updater.get(getBaseContext()).isNetworkAvailable())
    {
      stop=true;
      createDialog( "Es kann keine Verbindung zum Server hergestellt werden. Bitte prüfen Sie ihre Netzwerkeinstellungen und starten Sie die Applikation neu.");
    }
    Updater.get(getBaseContext()).updateListener(this);
    Updater.get(getBaseContext()).updatesOnTourdata();}

  @Override
  public void newTourdataAvailable(){
    Updater.get( getBaseContext() ).downloadTourlist();
  }

  @Override
  public void noNewTourdataAvailable(){}

  @Override
  public void tourlistDownloaded(){
    Updater.get( getBaseContext() ).downloadTourdata();
  }

  @Override
  public void tourdataDownloaded(){

  }

  public void createDialog(String text)
  {
    // declare the dialog as a member field of your activity
    final Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.alert_dialog);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();

    TextView title = (TextView) dialog.findViewById(R.id.title_dialog);
    title.setText("Verbindungsprobleme");
    title.setTypeface(Typeface.SERIF);
    //title.setTextColor(Color.BLACK);
    TextView txt = (TextView) dialog.findViewById(R.id.text_dialog);
    txt.setText(text);
    txt.setTypeface(Typeface.SERIF);
    //txt.setTextColor(Color.BLACK);
    ImageButton declineButton = (ImageButton) dialog.findViewById(R.id.btn_x_dialog);
    Sharp.loadResource(getResources(), R.raw.beenden_dunkel).into(declineButton);
    // if decline button is clicked, close the custom dialog
    declineButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Close dialog
        dialog.dismiss();
      finish();}});
    ImageButton okayButton = (ImageButton) dialog.findViewById(R.id.button_dialog);
    Sharp.loadResource(getResources(), R.raw.schliessen).into(okayButton);
    // if decline button is clicked, close the custom dialog
    okayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {dialog.dismiss();
        finish();}});

  }
}
