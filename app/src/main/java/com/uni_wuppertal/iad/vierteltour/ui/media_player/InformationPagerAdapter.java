package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.content.Context;
import android.content.res.Configuration;

import android.net.Uri;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import java.io.File;

public class InformationPagerAdapter extends PagerAdapter{

  private Context mContext;
  private InformationActivity infoAct;
  private Singletonint singlepage;
  private String[] stationImagePaths;

  public InformationPagerAdapter( Context mContext, String[] stationImagePaths , InformationActivity infoAct){
    this.mContext = mContext;
    this.stationImagePaths = stationImagePaths;
    this.infoAct= infoAct;
  }

  @Override
  public int getCount(){
    return stationImagePaths.length;
  }

  @Override
  public boolean isViewFromObject( View view, Object object ){
    return view == object;
  }

  @Override
  public Object instantiateItem( ViewGroup container, int position ){
    View itemView = LayoutInflater.from( mContext )
                                  .inflate( R.layout.gallerypageritem, container, false );

    ImageView imageView = (ImageView) itemView.findViewById( R.id.img_pager_item );
    VideoView videoView = (VideoView) itemView.findViewById( R.id.vid_pager_item );
    //TODO:Erkenne ob video oder image und zeige jeweils nur video oder image an
    imageView.setImageURI( Uri.fromFile( new File(OurStorage.getInstance(mContext).getPathToFile(stationImagePaths[position])) ) );

    if(true){videoView.setVisibility(View.VISIBLE);
            videoView.seekTo(100);
      imageView.setImageResource(R.drawable.play_hell);
    imageView.setVisibility(View.VISIBLE);}

    else {imageView.setVisibility(View.VISIBLE);}

    container.addView( itemView );

    imageView.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        infoAct.vf.setDisplayedChild(1);
        singlepage.INSTANCE.setPage(1);
      }
    });

    videoView.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        infoAct.vf.setDisplayedChild(1);
        singlepage.INSTANCE.setPage(1);
        return false;
      }
    });


    return itemView;
  }

  @Override
  public void destroyItem( ViewGroup container, int position, Object object ){
    container.removeView( (LinearLayout) object );
  }
}
