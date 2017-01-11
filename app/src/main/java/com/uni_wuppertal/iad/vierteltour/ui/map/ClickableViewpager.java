package com.uni_wuppertal.iad.vierteltour.ui.map;

import android.content.Context;

import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.uni_wuppertal.iad.vierteltour.ui.media_player.Singletonint;

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

  private void setup() {
    final GestureDetector tapGestureDetector = new GestureDetector(getContext(), new TapGestureListener());

    setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        tapGestureDetector.onTouchEvent(event);
        return false;
      }
    });

    setClipToPadding(false);
    setOffscreenPageLimit(3);
  }

  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }

  public interface OnItemClickListener {
    void onItemClick(int position);
  }

 /* @Override
  private void scrollToItem(int item, boolean smoothScroll, int velocity,
                            boolean dispatchSelected) {
    final ItemInfo curInfo = infoForPosition(item);
    int destX = 0;
    if (curInfo != null) {
      final int width = getClientWidth();
      destX = (int) (width * Math.max(mFirstOffset,
        Math.min(curInfo.offset, mLastOffset)));
    }
    if (smoothScroll) {
      smoothScrollTo(destX, 0, velocity);
      if (dispatchSelected) {
        dispatchOnPageSelected(item);
      }
    } else {
      if (dispatchSelected) {
        dispatchOnPageSelected(item);
      }
      completeScroll(false);
      scrollTo(destX, 0);
      pageScrolled(destX);
    }
  }*/


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
