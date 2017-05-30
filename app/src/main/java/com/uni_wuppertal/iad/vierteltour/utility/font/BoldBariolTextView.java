package com.uni_wuppertal.iad.vierteltour.utility.font;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Replacing Default Font with Bariol Font
 */

public class BoldBariolTextView extends TextView{

  public BoldBariolTextView(Context context) {
    super(context);
    Typeface face=Typeface.createFromAsset(context.getAssets(), "Bariol_Bold.otf");
    this.setTypeface(face);
  }

  public BoldBariolTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Typeface face=Typeface.createFromAsset(context.getAssets(), "Bariol_Bold.otf");
    this.setTypeface(face);
  }

  protected void onDraw (Canvas canvas) {
    super.onDraw(canvas);


  }

}
