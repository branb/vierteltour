package com.uni_wuppertal.iad.vierteltour.ui.map;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.station_pager.StationAdapter;
import com.uni_wuppertal.iad.vierteltour.ui.map.station_pager.StationFragment;


public class ShadowTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

    private ClickableViewpager mViewPager;
    private StationAdapter mAdapter;
    private Context context;
    private float mLastOffset;



    public ShadowTransformer(ClickableViewpager viewPager, StationAdapter adapter, Context context) {
        mViewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        mAdapter = adapter;
        this.context = context;
    }

  @Override
  public void transformPage(View page, float position) {

  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    int realCurrentPosition;
    int nextPosition;
    float realOffset;
    boolean goingLeft = mLastOffset > positionOffset;
    ArgbEvaluator color = new ArgbEvaluator();

    // If we're going backwards, onPageScrolled receives the last position
    // instead of the current one
    if (goingLeft) {
      realCurrentPosition = position + 1;
      nextPosition = position;
      realOffset = 1 - positionOffset;
      System.out.println("nextPosition: "+nextPosition);
      System.out.println("CurPosition: "+realCurrentPosition);
      System.out.println("Offset: "+realOffset);
    } else {
      nextPosition = position+1 ;
      realCurrentPosition = position;
      realOffset = positionOffset;

      System.out.println("nextPosition: "+nextPosition);
      System.out.println("CurPosition: "+realCurrentPosition);
      System.out.println("Offset: "+realOffset);
    }

    // Avoid crash on overscroll
    if (nextPosition > mAdapter.getCount() - 1
      || realCurrentPosition > mAdapter.getCount() - 1/* || lastPosition<0 || lastPosition < mAdapter.getCount() -1*/) {
      return;
    }

    StationFragment currentFragment = mAdapter.getItem(realCurrentPosition);

    // This might be null if a fragment is being used
    // and the views weren't created yet
    if (currentFragment != null) {
      currentFragment.getView().setScaleX((float) (1 + 0.2 * (1 - realOffset)));
      currentFragment.getView().setScaleY((float) (1 + 0.2 * (1 - realOffset)));
      if(goingLeft)currentFragment.getView().findViewById(R.id.clicklayout).setBackgroundColor((Integer) color.evaluate(positionOffset, context.getResources().getColor(R.color.grey), context.getResources().getColor(R.color.white)));
      else currentFragment.getView().findViewById(R.id.clicklayout).setBackgroundColor((Integer) color.evaluate(positionOffset, context.getResources().getColor(R.color.white), context.getResources().getColor(R.color.grey)));
    }

    StationFragment nextFragment = mAdapter.getItem(nextPosition);

    // We might be scrolling fast enough so that the next (or previous) card
    // was already destroyed or a fragment might not have been created yet
    if (nextFragment != null) {
      nextFragment.getView().setScaleX((float) (1 + 0.2 * (realOffset)));
      nextFragment.getView().setScaleY((float) (1 + 0.2 * (realOffset)));
      if(goingLeft)nextFragment.getView().findViewById(R.id.clicklayout).setBackgroundColor((Integer) color.evaluate(positionOffset, context.getResources().getColor(R.color.white), context.getResources().getColor(R.color.grey)));
        else nextFragment.getView().findViewById(R.id.clicklayout).setBackgroundColor((Integer) color.evaluate(positionOffset, context.getResources().getColor(R.color.grey) , context.getResources().getColor(R.color.white)));
    }

    mLastOffset = positionOffset;
  }

  @Override
  public void onPageSelected(int position) {
   // mAdapter.getItem(position).getView().findViewById(R.id.clicklayout).setBackgroundColor(context.getResources().getColor(R.color.white));
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }
}
