package com.uni_wuppertal.iad.vierteltour.ui.map.station_pager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.ui.map.ClickableViewpager;
import com.uni_wuppertal.iad.vierteltour.ui.map.Station;
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.Singletonint;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.StationActivity;
import com.uni_wuppertal.iad.vierteltour.R;

import java.util.ArrayList;

public class StationFragment extends Fragment{
  private static final String ARG_PAGE_NUMBER = "pageNumber";
  private static Bundle arguments;
  private static ArrayList<String> ztitle = new ArrayList<>();
  private TextView title;
  private Singletonint singlepage;
  private int position;

  public static StationFragment create( Station station ){
    StationFragment fragment = new StationFragment();
    arguments = new Bundle();
    arguments.putInt( ARG_PAGE_NUMBER, station.number() );
    fragment.setArguments( arguments );

    ztitle.add( station.name() );

    return fragment;
  }

  @Override
  public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
    View rootView = inflater.inflate( R.layout.fragment_page, container, false );
    RelativeLayout btItem = (RelativeLayout) rootView.findViewById( R.id.clicklayout );


    title = (TextView) rootView.findViewById( R.id.titlefrag );

    if( arguments != null ){
      position = getArguments().getInt( ARG_PAGE_NUMBER );
      System.out.println(position);
      title.setText( ztitle.get( position-1 ) );

      btItem.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
          singlepage.INSTANCE.onfragmentclicked(true);
          singlepage.INSTANCE.selectedOldStation(singlepage.INSTANCE.selectedStation());
          singlepage.INSTANCE.selectedStation(singlepage.INSTANCE.selectedTour().station(position+1));
          return false;
        }
      } );
    } else {
      btItem.setVisibility( View.GONE );
    }
    return rootView;
  }

  public void deleteStrings(){
    ztitle.clear();
  }

}
