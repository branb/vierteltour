package com.uni_wuppertal.iad.vierteltour.ui.drawer.intro;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.station_pager.StationFragment;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Station;

import java.util.ArrayList;

/**
 * Created by Kevin on 25.05.2017.
 */

public class IntroAdapter extends FragmentStatePagerAdapter{
  public ArrayList<IntroFragment> fragments;

  public IntroAdapter(FragmentManager fm ){
    super( fm );
    fragments = new ArrayList<>();
  }

  @Override
  public int getCount(){
    return fragments.size();
  }

  public void addFragment( int position ){
    IntroFragment fragment = IntroFragment.create(position);
    fragments.add( fragment );
  }

  @Override
  public IntroFragment getItem( int position ){
    return fragments.get( position);
  }

  @Override
  public int getItemPosition( Object object ){
    return POSITION_NONE;
  }

}
