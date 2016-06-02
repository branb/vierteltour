package com.example.thomas.maptest2;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter{
  List<PageFragment> fragments = new ArrayList<PageFragment>();
  private LayoutInflater mInflater;

  public PagerAdapter( FragmentManager fm, Context context ){
    super( fm );
    mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
  }

  @Override
  public int getCount(){
    return fragments.size();
  }

  public PageFragment addFragment( int position, Tour tour ){
    PageFragment xfragment = PageFragment.create( position, tour );
    fragments.add( position, xfragment );
    notifyDataSetChanged();
    return xfragment;
  }

  @Override
  public PageFragment getItem( int position ){
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


