package com.uni_wuppertal.iad.vierteltour.ui.map;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.station_pager.StationAdapter;
import com.uni_wuppertal.iad.vierteltour.ui.map.station_pager.StationFragment;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.Singletonint;


public class ShadowTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

    private ClickableViewpager mViewPager;
    private StationAdapter mAdapter;
    private float mLastOffset;
    private Singletonint singlepage;
    private Context context;



    public ShadowTransformer(ClickableViewpager viewPager, StationAdapter adapter, Context context) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
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
    int lastPosition;
    float realOffset;
    boolean goingLeft = mLastOffset > positionOffset;
    ArgbEvaluator color = new ArgbEvaluator();

    // If we're going backwards, onPageScrolled receives the last position
    // instead of the current one
    if (goingLeft) {
      lastPosition = position + 2;
      realCurrentPosition = position + 1;
      nextPosition = position;
      realOffset = 1 - positionOffset;

    } else {
      lastPosition = position - 1;
      nextPosition = position+1 ;
      realCurrentPosition = position;
      realOffset = positionOffset;

    }
    if(position==mAdapter.getCount()-1 && positionOffset==0)
    {mAdapter.getItem(position).getView().findViewById(R.id.clicklayout).setBackgroundColor(context.getResources().getColor(R.color.whitewhite));
      mAdapter.getItem(position).getView().setScaleX(1.2f);
      mAdapter.getItem(position).getView().setScaleY(1.2f);}


    // Avoid crash on overscroll
    if (nextPosition > mAdapter.getCount() - 1
      || realCurrentPosition > mAdapter.getCount() -1 ) {
      return;
    }
    if(lastPosition>=0 && lastPosition<mAdapter.getCount())
    {StationFragment lastFragment = mAdapter.getItem(lastPosition);
    if(lastFragment.getView()!=null)
    {lastFragment.getView().setScaleX(1);
     lastFragment.getView().setScaleY(1);
     lastFragment.getView().findViewById(R.id.clicklayout).setBackgroundColor(context.getResources().getColor(R.color.grey));}
    }


    StationFragment currentFragment = mAdapter.getItem(realCurrentPosition);

    // This might be null if a fragment is being used
    // and the views weren't created yet
    if (currentFragment.getView() != null) {
      currentFragment.getView().setScaleX((float) (1 + 0.2 * (1 - realOffset)));
      currentFragment.getView().setScaleY((float) (1 + 0.2 * (1 - realOffset)));
      if(goingLeft)currentFragment.getView().findViewById(R.id.clicklayout).setBackgroundColor((Integer) color.evaluate(positionOffset, context.getResources().getColor(R.color.grey), context.getResources().getColor(R.color.whitewhite)));
      else currentFragment.getView().findViewById(R.id.clicklayout).setBackgroundColor((Integer) color.evaluate(positionOffset, context.getResources().getColor(R.color.whitewhite), context.getResources().getColor(R.color.grey)));
    }

    StationFragment nextFragment = mAdapter.getItem(nextPosition);

    // We might be scrolling fast enough so that the next (or previous) card
    // was already destroyed or a fragment might not have been created yet
    if (nextFragment.getView() != null) {
      nextFragment.getView().setScaleX((float) (1 + 0.2 * (realOffset)));
      nextFragment.getView().setScaleY((float) (1 + 0.2 * (realOffset)));
      if(goingLeft)nextFragment.getView().findViewById(R.id.clicklayout).setBackgroundColor((Integer) color.evaluate(positionOffset, context.getResources().getColor(R.color.whitewhite), context.getResources().getColor(R.color.grey)));
        else nextFragment.getView().findViewById(R.id.clicklayout).setBackgroundColor((Integer) color.evaluate(positionOffset, context.getResources().getColor(R.color.grey) , context.getResources().getColor(R.color.whitewhite)));
    }

    mLastOffset = positionOffset;
  }

  @Override
  public void onPageSelected(int position) {

    mAdapter.getItem(position).getView().findViewById(R.id.clicklayout).setBackgroundColor(context.getResources().getColor(R.color.whitewhite));
    mAdapter.getItem(position).getView().setScaleX(1.2f);
    mAdapter.getItem(position).getView().setScaleY(1.2f);

    if(position+1<mAdapter.getCount() && mAdapter.getItem(position+1).getView()!=null){mAdapter.getItem(position+1).getView().findViewById(R.id.clicklayout).setBackgroundColor(context.getResources().getColor(R.color.grey));
    mAdapter.getItem(position+1).getView().setScaleX(1f);
    mAdapter.getItem(position+1).getView().setScaleY(1f);}

    if(position-1>0 && mAdapter.getItem(position-1).getView()!=null){mAdapter.getItem(position-1).getView().findViewById(R.id.clicklayout).setBackgroundColor(context.getResources().getColor(R.color.grey));
    mAdapter.getItem(position-1).getView().setScaleX(1f);
    mAdapter.getItem(position-1).getView().setScaleY(1f);}

    ((MapsActivity)context).selectStation(singlepage.INSTANCE.selectedTour().station(position+1));
    singlepage.INSTANCE.onfragmentclicked(-1);
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }
}
