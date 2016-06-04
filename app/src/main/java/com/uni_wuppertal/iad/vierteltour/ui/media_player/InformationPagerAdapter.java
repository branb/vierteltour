package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.uni_wuppertal.iad.vierteltour.R;

public class InformationPagerAdapter extends PagerAdapter{

  private Context mContext;
  private int[] mResources;
  private InformationActivity infoAct;

  public InformationPagerAdapter( Context mContext, int[] mResources , InformationActivity infoAct){
    this.mContext = mContext;
    this.mResources = mResources;
    this.infoAct= infoAct;
  }

  @Override
  public int getCount(){
    return mResources.length;
  }

  @Override
  public boolean isViewFromObject( View view, Object object ){
    return view == object;
  }

  @Override
  public Object instantiateItem( ViewGroup container, int position ){
    View itemView = LayoutInflater.from( mContext )
                                  .inflate( R.layout.gallerypageritem, container, false );

    int pos = position;
    ImageView imageView = (ImageView) itemView.findViewById( R.id.img_pager_item );
    imageView.setImageResource( mResources[position] );

    container.addView( itemView );

    imageView.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {infoAct.vf.showNext();
        infoAct.page=1;
      }
    });


    return itemView;
  }

  @Override
  public void destroyItem( ViewGroup container, int position, Object object ){
    container.removeView( (LinearLayout) object );
  }
}
