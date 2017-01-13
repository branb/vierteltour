package com.uni_wuppertal.iad.vierteltour.ui.map;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.uni_wuppertal.iad.vierteltour.ui.map.station_pager.StationAdapter;
import com.uni_wuppertal.iad.vierteltour.ui.map.station_pager.StationFragment;


public class ShadowTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

    private ClickableViewpager mViewPager;
    private StationAdapter mAdapter;
    private float mLastOffset;
    private boolean mScalingEnabled;

    public ShadowTransformer(ClickableViewpager viewPager, StationAdapter adapter) {
        mViewPager = viewPager;
        //viewPager.addOnPageChangeListener(this);
        mAdapter = adapter;
    }

    public void enableScaling(boolean enable) {
        if (mScalingEnabled && !enable) {
            // shrink main card
            StationFragment currentFragment = mAdapter.getItem(mViewPager.getCurrentItem());
            if (currentFragment != null) {
            //    currentFragment.setPadding(mViewPager.getCurrentItem(),0,0,(int)(2*120 * mViewPager.getResources().getDisplayMetrics().density + 0.5f),0);
             // currentFragment.animate().scaleX(1);
            }
        }else if(!mScalingEnabled && enable){
            // grow main card
          StationFragment currentFragment = mAdapter.getItem(mViewPager.getCurrentItem());
            if (currentFragment != null) {
            //  currentFragment.animate().scaleY(1.1f);
            //  currentFragment.animate().scaleX(1.1f);
            }
        }

        mScalingEnabled = enable;
    }

    @Override
    public void transformPage(View page, float position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int realCurrentPosition;
        int nextPosition;
      //  float baseElevation = mAdapter.getBaseElevation();
        float realOffset;
        boolean goingLeft = mLastOffset > positionOffset;

        // If we're going backwards, onPageScrolled receives the last position
        // instead of the current one
        if (goingLeft) {
            realCurrentPosition = position + 1;
            nextPosition = position;
            realOffset = 1 - positionOffset;
        } else {
            nextPosition = position + 1;
            realCurrentPosition = position;
            realOffset = positionOffset;
        }

        // Avoid crash on overscroll
        if (nextPosition > mAdapter.getCount() - 1
                || realCurrentPosition > mAdapter.getCount() - 1) {
            return;
        }

      StationFragment currentFragment = mAdapter.getItem(realCurrentPosition);

        // This might be null if a fragment is being used
        // and the views weren't created yet
        if (currentFragment != null) {
            if (mScalingEnabled) {
          //    currentFragment.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
          //    currentFragment.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
            }
        }

        StationFragment nextFragment = mAdapter.getItem(nextPosition);

        // We might be scrolling fast enough so that the next (or previous) card
        // was already destroyed or a fragment might not have been created yet
        if (nextFragment != null) {
            if (mScalingEnabled) {
              //nextFragment.setScaleX((float) (1 + 0.1 * (realOffset)));
             // nextFragment.setScaleY((float) (1 + 0.1 * (realOffset)));
            }
        }

        mLastOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
