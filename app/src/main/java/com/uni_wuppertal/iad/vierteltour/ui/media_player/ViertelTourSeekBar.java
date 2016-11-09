package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by Kevin-Laptop on 04.06.2016.
 */
public class ViertelTourSeekBar extends SeekBar {

 // ViertelTourMediaPlayer player;

  public ViertelTourSeekBar(Context context) {
    super(context);
  //  player = ViertelTourMediaPlayer.getInstance(context);
    //this.setOnSeekBarChangeListener(customSeekBarListener);
  }



  /*public SeekBar.OnSeekBarChangeListener customSeekBarListener = new SeekBar.OnSeekBarChangeListener(){
    @Override
    public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser ){
      if( fromUser ){
        player.seekTo( progress );
      }
    }

    @Override
    public void onStartTrackingTouch( SeekBar seekBar ){
    }

    @Override
    public void onStopTrackingTouch( SeekBar seekBar ){
    }
  };*/

/*  void setCustomUI() {

    this.setProgressDrawable(getResources().getDrawable(
      R.drawable.yourBackground));
  }
*/
}
