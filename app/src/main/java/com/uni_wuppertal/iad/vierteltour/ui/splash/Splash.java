package com.uni_wuppertal.iad.vierteltour.ui.splash;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.utility.storage.Singletonint;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.utility.updater.UpdateListener;
import com.uni_wuppertal.iad.vierteltour.utility.updater.Updater;

//Vorerst nicht benötigt, ist für Splashscreen zuständig, eigentlich in AndroidManifest als Launcher ausgewählt
//Aktuell nicht verwendet
public class Splash extends Activity implements UpdateListener {
  ProgressBar pbar;
  Intent myintent;
  VideoView vid;
  Boolean stop=false;
  Singletonint singlepage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash);
   // initTypeface();                                                //only for SplashActivity
    singlepage.INSTANCE.restart(true);      //On First Start

    pbar = (ProgressBar) findViewById(R.id.progressBar);
    /*vid = (VideoView) findViewById(R.id.videoView);
    vid.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.animation));
    vid.requestFocus();
    vid.start();*/

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
      != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
      != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
      != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }
    else{
    myintent = new Intent(this, MapsActivity.class);

    try{checkForUpdates();}
    catch(Exception e){
      Toast.makeText(this, "Die Tourdaten konnten nicht geladen werden", Toast.LENGTH_LONG);}
    //IntentLauncher launcher = new IntentLauncher();



    //checkForUpdates();
    startActivity(myintent);}

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == 101)  {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        myintent = new Intent(this, MapsActivity.class);

        try{checkForUpdates();}
        catch(Exception e){
          Toast.makeText(this, "Die Tourdaten konnten nicht geladen werden", Toast.LENGTH_LONG);}
        //IntentLauncher launcher = new IntentLauncher();



        //checkForUpdates();
        startActivity(myintent);
      }
      else{Toast.makeText(this, "Die ViertelTour App funktioniert nur mit erteilten Berechtigungen.", Toast.LENGTH_LONG).show();}
    }
  }
 /* private void initTypeface() {
    Typeface font = Typeface.createFromAsset(getAssets(), "Bariol_Regular.ttf");
    TextView tv = (TextView) findViewById(R.id.textView);
    tv.setTypeface(font);
    TextView tv2 = (TextView) findViewById(R.id.textView2);
    tv2.setTypeface(font);
    TextView tv3 = (TextView) findViewById(R.id.textView3);
    tv3.setTypeface(font);
  }*/

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
  public void noNewTourdataAvailable(){
    new Handler().postDelayed(new Runnable(){
    @Override
    public void run() {
      if(!stop && singlepage.INSTANCE.restart())
      { Splash.this.startActivity(myintent);
        Splash.this.overridePendingTransition(0, 0);
        Splash.this.finish();
        Splash.this.overridePendingTransition(0, 0);
        singlepage.INSTANCE.restart(false);}
    }
  }, 1);}

  @Override
  public void tourlistDownloaded(){
    Updater.get( getBaseContext() ).downloadTourdata();
  }

  @Override
  public void tourdataDownloaded(){
    singlepage.INSTANCE.versionUpdate(false);

    new Handler().postDelayed(new Runnable(){
      @Override
      public void run() {
        if(!stop && singlepage.INSTANCE.restart())
        { Splash.this.startActivity(myintent);
          Splash.this.overridePendingTransition(0, 0);
          Splash.this.finish();
          Splash.this.overridePendingTransition(0, 0);
          singlepage.INSTANCE.restart(false);}
      }
    }, 1); }

  public void createDialog(String text)
  {
    // declare the dialog as a member field of your activity
    final Dialog dialog = new Dialog(this);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.alert_dialog);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();

    TextView title = (TextView) dialog.findViewById(R.id.title_dialog);
    title.setText("Verbindungsprobleme");
    title.setTextColor(getResources().getColor(R.color.black));
    TextView txt = (TextView) dialog.findViewById(R.id.text_dialog);
    SpannableString dialog_text = new SpannableString(text + " ");
    dialog_text.setSpan(new StyleSpan(Typeface.BOLD), 0, dialog_text.length(), 0);
    txt.setTextColor(getResources().getColor(R.color.black));
    txt.setText(dialog_text);
    ImageButton okayButton = (ImageButton) dialog.findViewById(R.id.button_dialog);
    Sharp.loadResource(getResources(), R.raw.schliessen).into(okayButton);
    // if decline button is clicked, close the custom dialog
    okayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {dialog.dismiss();
        finish();}});

  }
}
