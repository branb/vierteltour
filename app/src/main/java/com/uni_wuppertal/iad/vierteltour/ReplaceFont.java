package com.uni_wuppertal.iad.vierteltour;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public class ReplaceFont{
  public static void replaceDefaultFont( Context context, String old_Font, String Asset_Font ){
    final Typeface customFontTypeface = Typeface.createFromAsset( context.getAssets(), Asset_Font );
    replaceFont( old_Font, customFontTypeface );
  }

  public static void replaceFont( String old_Font, Typeface customFontTypeface ){
    try{
      final Field myfield = Typeface.class.getDeclaredField( old_Font );
      myfield.setAccessible( true );
      myfield.set( null, customFontTypeface );
    } catch( NoSuchFieldException e ){
      e.printStackTrace();
    } catch( IllegalAccessException e ){
      e.printStackTrace();
    }
  }
}
