package com.uni_wuppertal.iad.vierteltour.ui.drawer.intro;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.uni_wuppertal.iad.vierteltour.R;

import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Info activity to guide the user
 */
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
    //TODO: Texts are too long. This will be changed soon
    addSlide( AppIntroFragment.newInstance( "Überschrift", "Die App ViertelTour nimmt Sie mit auf informative Stadtspaziergänge, die von unterschiedlichsten Experten durch eine dichte Ökologie- und Kulturlandschaft im Wuppertaler Innenstadtraum durchgeführt werden.\n" ,R.drawable.i, Color.parseColor( "#fbc55e") ) );
    addSlide( AppIntroFragment.newInstance( "Überschrift", "Laden Sie sich die Touren am besten bevor Sie losgehen im Wlan Netz herunter. Es werden pro Tour etwa 100–200 MB Daten geladen.\n"+"Um die Audio-, Bild, und Video-Daten dann zu den Touren hören und sehen zu können, müssen Sie sich vor Ort befinden!", R.drawable.i, Color.parseColor( "#ec6e5e") ) );
    addSlide( AppIntroFragment.newInstance( "Überschrift", "Wir haben ViertelTour bewusst so konzipiert, dass die Spaziergänge tatsächlich nur erlebt werden können, wenn Sie sich genau dort befinden, wo die entsprechende Expertin oder der entsprechende Experte Sie mit auf den Weg nehmen möchte, um Wuppertal vor Ort zu erfahren.", R.drawable.i, Color.parseColor( "#8cafdc") ) );
    addSlide( AppIntroFragment.newInstance( "Überschrift", "Sie haben mit der App die Möglichkeit sich an die entsprechenden Ort navigieren zu lassen. \n"+"Wenn Sie dort angekommen sind, werden Ihnen die Daten jeweils Stationsweise zur Verfügung gestellt.", R.drawable.i, Color.parseColor( "#fbc55e") ) );
    addSlide( AppIntroFragment.newInstance( "Überschrift", "Wir wünschen viel Spaß und hoffentlich neue Einsichten und Erfahrungen in einer hochinteressanten Stadt.\n"+"Projektgruppe Design Interaktiver Medien, Bergische Universität Wuppertal, Prof. Kristian Wolf", R.drawable.i, Color.parseColor( "#fbc55e") ) );

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
