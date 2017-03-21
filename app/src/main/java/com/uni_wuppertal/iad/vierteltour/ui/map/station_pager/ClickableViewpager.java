package com.uni_wuppertal.iad.vierteltour.ui.map.station_pager;

import android.content.Context;

import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Kevin-Laptop on 07.01.2017.
 */

public class ClickableViewpager extends ViewPager {
  private OnItemClickListener mOnItemClickListener;

  public ClickableViewpager(Context context) {
    super(context);
    setup();
  }

  public ClickableViewpager(Context context, AttributeSet attrs) {
    super(context, attrs);
    setup();
  }

  /**
   * Setup for CustomViewPager
   * GestureDetector is used to filter different taps
   */
  private void setup() {
    final GestureDetector tapGestureDetector = new GestureDetector(getContext(), new TapGestureListener());

    setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        tapGestureDetector.onTouchEvent(event);
        return false;
      }
    });

    setClipToPadding(false);        //removes padding between Fragments of ViewPager
    setOffscreenPageLimit(3);       //Limit of Neighbour Fragments
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }

  public interface OnItemClickListener {
    void onItemClick(int position);
  }


  private class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
      if(mOnItemClickListener != null) {

        mOnItemClickListener.onItemClick(getCurrentItem()+1);
      }
      return true;
    }
  }
}
