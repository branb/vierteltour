package com.uni_wuppertal.iad.vierteltour.ui.drawer.intro;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;

/**
 * Created by Kevin on 25.05.2017.
 */

public class IntroActivity extends Activity {

  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.intro );

    ViewPager intro_pager = (ViewPager) findViewById(R.id.intro_pager);
    IntroAdapter introAdapter = new IntroAdapter(this);
    intro_pager.setAdapter(introAdapter);

  }
}
