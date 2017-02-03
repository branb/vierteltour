package com.uni_wuppertal.iad.vierteltour.ui.intro;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.uni_wuppertal.iad.vierteltour.R;

import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro{
  @Override
  protected void onCreate(
    @Nullable
    Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );

    // Add your slide fragments here.
    // AppIntro will automatically generate the dots indicator and buttons.
  //    addSlide(firstFragment);
  //    addSlide(secondFragment);
  //    addSlide(thirdFragment);
  //    addSlide(fourthFragment);

    // Instead of fragments, you can also use our default slide
    // Just set a title, description, background and image. AppIntro will do the rest.
    addSlide( AppIntroFragment.newInstance( "Überschrift", "Weit hinten, hinter den Wortbergen, fern der Länder Vokalien und Konsonantien leben die Blindtexte.", R.drawable.i, Color.parseColor( "#fbc55e") ) );
    addSlide( AppIntroFragment.newInstance( "Überschrift", "Weit hinten, hinter den Wortbergen, fern der Länder Vokalien und Konsonantien leben die Blindtexte.", R.drawable.i, Color.parseColor( "#ec6e5e") ) );
    addSlide( AppIntroFragment.newInstance( "Überschrift", "Weit hinten, hinter den Wortbergen, fern der Länder Vokalien und Konsonantien leben die Blindtexte.", R.drawable.i, Color.parseColor( "#8cafdc") ) );
    addSlide( AppIntroFragment.newInstance( "Überschrift", "Weit hinten, hinter den Wortbergen, fern der Länder Vokalien und Konsonantien leben die Blindtexte.", R.drawable.i, Color.parseColor( "#d1e096") ) );

    // OPTIONAL METHODS
    // Override bar/separator color.
//    setBarColor( Color.parseColor( "#3F51B5" ) );
//    setSeparatorColor( Color.parseColor( "#2196F3" ) );

    // Hide Skip/Done button.
//    showSkipButton( true );
    setProgressButtonEnabled( true );

    // Turn vibration on and set intensity.
    // NOTE: you will probably need to ask VIBRATE permission in Manifest.
    // setVibrate( true );
    setVibrateIntensity( 30 );


    setSkipText( "Überspringen" );
    setDoneText( "Fertig" );
  }

  // Do something when users tap on Skip button.
  @Override
  public void onSkipPressed( Fragment currentFragment ){
    super.onSkipPressed( currentFragment );
    finish();
  }

  // Do something when users tap on Done button.
  @Override
  public void onDonePressed( Fragment currentFragment ){
    super.onDonePressed( currentFragment );
    finish();
  }

  // Do something when the slide changes.
  @Override
  public void onSlideChanged(
    @Nullable
    Fragment oldFragment,
    @Nullable
    Fragment newFragment ){
    super.onSlideChanged( oldFragment, newFragment );
  }

  @Override
  public void finish(){
    //  Remember to not start the intro again
    PreferenceManager.getDefaultSharedPreferences( getBaseContext() )
                     .edit()
                     .putBoolean( "firstStart", false )
                     .apply();

    super.finish();
  }
}
