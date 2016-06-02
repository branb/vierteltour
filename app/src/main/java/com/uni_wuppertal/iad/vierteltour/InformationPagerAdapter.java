package com.uni_wuppertal.iad.vierteltour;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InformationPagerAdapter extends PagerAdapter{

  private Context mContext;
  private int[] mResources;

  public InformationPagerAdapter( Context mContext, int[] mResources ){
    this.mContext = mContext;
    this.mResources = mResources;
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

    ImageView imageView = (ImageView) itemView.findViewById( R.id.img_pager_item );
    imageView.setImageResource( mResources[position] );

    container.addView( itemView );

    return itemView;
  }

  @Override
  public void destroyItem( ViewGroup container, int position, Object object ){
    container.removeView( (LinearLayout) object );
  }
}
