package com.uni_wuppertal.iad.vierteltour.ui.map.station_pager;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;

import java.util.ArrayList;
import java.util.List;

public class StationAdapter extends FragmentPagerAdapter{
  public List<StationFragment> fragments = new ArrayList<StationFragment>();
  private LayoutInflater mInflater;

  public StationAdapter( FragmentManager fm, Context context ){
    super( fm );
    mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
  }

  @Override
  public int getCount(){
    return fragments.size();
  }

  public StationFragment addFragment( int position, Tour tour ){
    StationFragment xfragment = StationFragment.create( position, tour );
    fragments.add( position, xfragment );
    notifyDataSetChanged();
    return xfragment;
  }

  @Override
  public StationFragment getItem( int position ){
    return fragments.get( position );
  }

  @Override
  public float getPageWidth( int position ){
    return 0.4f;
  }

  @Override
  public int getItemPosition( Object object ){
    return POSITION_NONE;
  }

  public void deleteStrings(){
    fragments.get( 0 )
             .deleteStrings();
  }

}


