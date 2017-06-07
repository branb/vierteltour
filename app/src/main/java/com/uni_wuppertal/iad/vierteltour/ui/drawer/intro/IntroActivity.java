package com.uni_wuppertal.iad.vierteltour.ui.drawer.intro;

import android.app.Activity;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;

/**
 * Created by Kevin on 25.05.2017.
 */

public class IntroActivity extends Activity {
  private ImageButton x_intro, finish;
  private ViewPager intro_pager;
  private IntroAdapter introAdapter;
  private LinearLayout pager_indicator;
  private ImageView[] dots;
  protected void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.intro );

    intro_pager = (ViewPager) findViewById(R.id.intro_pager);
    introAdapter = new IntroAdapter(this);
    intro_pager.setAdapter(introAdapter);
    intro_pager.setOnPageChangeListener(onPageChangeListener);
    intro_pager.setCurrentItem(0);



    x_intro = (ImageButton) findViewById(R.id.x_intro);
    Sharp.loadResource(getResources(), R.raw.beenden_hell).into(x_intro);
    x_intro.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });
    finish = (ImageButton) findViewById(R.id.finish);
    Sharp.loadResource(getResources(), R.raw.fertig).into(finish);
    finish.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });



    pager_indicator = (LinearLayout) findViewById(R.id.dotsLayout);
    setUiPageViewController();
    //pager_indicator
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("firstStart",false).apply();
    overridePendingTransition(0, 0);
  }

  ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {
    if(position==2)finish.setVisibility(View.VISIBLE);
    else finish.setVisibility(View.INVISIBLE);

    for (int i = 0; i < 3; i++) {
      dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem));
    }

    dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem));
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }
};

  /**
   * Manages the dots below the viewpager
   */
  private void setUiPageViewController() {
    if (introAdapter.getCount() > 1) {
      pager_indicator.removeAllViews();
      dots = new ImageView[3];

      for (int i = 0; i < 3; i++) {
        dots[i] = new ImageView(this);
        dots[i].setColorFilter(Color.parseColor("#E6EBE0"));
        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.WRAP_CONTENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(10, 0, 10, finish.getHeight()/2);
        params.gravity= Gravity.CENTER;

        pager_indicator.addView(dots[i], params);
      }

      dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem));

    }
  }

}
