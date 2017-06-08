package com.uni_wuppertal.iad.vierteltour.ui.map.station_pager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.uni_wuppertal.iad.vierteltour.utility.xml.Station;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentStatePagerAdapter {
  public ArrayList<StationFragment> fragments;

  public FragmentAdapter(FragmentManager fm ){
    super( fm );
    fragments = new ArrayList<>();
  }

  @Override
  public int getCount(){
    return fragments.size();
  }

  public void addFragment( Station station ){
    StationFragment fragment = StationFragment.create(station);
    fragments.add( fragment );
  }

  @Override
  public StationFragment getItem( int position ){
    return fragments.get( position);
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


