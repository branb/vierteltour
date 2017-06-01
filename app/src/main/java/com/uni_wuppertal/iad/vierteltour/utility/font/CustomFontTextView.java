package com.uni_wuppertal.iad.vierteltour.utility.font;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFontTextView extends TextView {

  public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

  public CustomFontTextView(Context context, AttributeSet attrs) {
    super(context, attrs);

    applyCustomFont(context, attrs);
  }

  public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    applyCustomFont(context, attrs);
  }

  private void applyCustomFont(Context context, AttributeSet attrs) {
    int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

    Typeface customFont = selectTypeface(context, textStyle);
    setTypeface(customFont);
  }

  private Typeface selectTypeface(Context context, int textStyle) {
        /*
        * information about the TextView textStyle:
        * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
        */
    switch (textStyle) {
      case Typeface.BOLD: // bold
        return FontCache.getTypeface("Bariol_Bold.otf", context);

      case Typeface.ITALIC: // italic
        return FontCache.getTypeface("Bariol_Thin_Italic.otf", context);

      case Typeface.NORMAL: // regular
      default:
        return FontCache.getTypeface("Bariol_Regular.ttf", context);
    }}
  }
