package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.content.Context;
import android.media.MediaPlayer;

public class ViertelTourMediaPlayer extends MediaPlayer{
  private static ViertelTourMediaPlayer ourInstance;
  /**
   * Singleton-method
   *
   * @return OurPlayer a static singleton of this class
   */
  public static ViertelTourMediaPlayer getInstance( Context context){
    if( ourInstance == null ){
      ourInstance = new ViertelTourMediaPlayer( context );
    }

    return ourInstance;
  }


  private Context context;


  /** The main constructor.
   *
   */
  private ViertelTourMediaPlayer( Context context ){
    this.context = context;
  }
}

