package com.uni_wuppertal.iad.vierteltour.ui.map.station_pager;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uni_wuppertal.iad.vierteltour.ui.map.Station;
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;

import java.util.ArrayList;
import java.util.List;

public class StationAdapter extends FragmentStatePagerAdapter {
  public List<StationFragment> fragments;

  public StationAdapter( FragmentManager fm ){
    super( fm );
    fragments = new ArrayList<>();
  }

  @Override
  public int getCount(){
    return fragments.size();
  }

  public View getFragmentViewAt(int position) {return fragments.get(position).getView();}

  public void addFragment( Station station ){
    StationFragment fragment = StationFragment.create(station);
    fragments.add( fragment );
  }

  @Override
  public StationFragment getItem( int position ){
    return fragments.get( position );
  }

 /* @Override
  public float getPageWidth( int position ){
    return 0.4f;
  }*/

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    Object fragment = super.instantiateItem(container, position);
    fragments.set(position, (StationFragment) fragment);
    return fragment;
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


