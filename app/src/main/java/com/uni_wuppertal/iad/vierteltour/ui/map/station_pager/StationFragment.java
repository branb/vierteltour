package com.uni_wuppertal.iad.vierteltour.ui.map.station_pager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.ui.map.Station;
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.StationActivity;
import com.uni_wuppertal.iad.vierteltour.R;

import java.util.ArrayList;

public class StationFragment extends Fragment{
  private static final String ARG_PAGE_NUMBER = "pageNumber", TITLE = "title", STATIONNAME = "stationname";
  private static String TOURNAME, AUTOR, TIME, LENGHT, COLOR, SIZE;
  private static ArrayList<String> descriptions = new ArrayList<>();
  private static ArrayList<String> slug = new ArrayList<>();
  private static Bundle arguments;
  private static ArrayList<String> ztitle = new ArrayList<>();
  private static ArrayList<String> img = new ArrayList<>();
  private static ArrayList<String> aud = new ArrayList<>();
  private static ArrayList<String> vid = new ArrayList<>();
  private TextView title;
  private Station station;
  private int position;

  public static StationFragment create( int pageNumber, Tour tour ){
    StationFragment fragment = new StationFragment();
    arguments = new Bundle();
    arguments.putInt( ARG_PAGE_NUMBER, pageNumber );
    fragment.setArguments( arguments );

    Station station = tour.station( pageNumber + 1);
    ztitle.add( station.name() );
    descriptions.add( station.description() );
    img.add( station.imagesToString() );
    aud.add( station.audio() );
    vid.add( station.videosToString() );

    slug.add( station.slug() );

    TOURNAME = tour.name();
    AUTOR = tour.author();
    TIME = tour.time();
    LENGHT = tour.length();
    COLOR = tour.color();
    SIZE = "" + tour.stations().size();
    return fragment;
  }

  @Override
  public void onCreate( Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
  }

  @Override
  public View onCreateView( LayoutInflater inflater,
                            @Nullable
                            ViewGroup container,
                            @Nullable
                            Bundle savedInstanceState ){
    View rootView = inflater.inflate( R.layout.fragment_page, container, false );
    RelativeLayout btItem = (RelativeLayout) rootView.findViewById( R.id.clicklayout );

    title = (TextView) rootView.findViewById( R.id.titlefrag );

    if( arguments != null ){
      position = getArguments().getInt( ARG_PAGE_NUMBER );
      title.setText( ztitle.get( position ) );

      /*btItem.setOnClickListener( new View.OnClickListener(){
        @Override
        public void onClick( View v ){

          Intent myIntent = new Intent( getActivity(), StationActivity.class );

          //myIntent.putExtra("key", arguments.getInt(ARG_PAGE_NUMBER)); //Optional parameters
          myIntent.putExtra( "slug", slug.get(position) );
          myIntent.putExtra( "station", title.getText() );
          myIntent.putExtra( "name", TOURNAME );
          myIntent.putExtra( "autor", AUTOR );
          myIntent.putExtra( "zeit", TIME );
          myIntent.putExtra( "laenge", LENGHT );
          myIntent.putExtra( "farbe", COLOR );
          myIntent.putExtra( "desc", descriptions.get( position ) );
          myIntent.putExtra( "size", SIZE );
          myIntent.putExtra( "pos", "" + (position + 1) );
          myIntent.putExtra( "img", img.get( position ) );
          myIntent.putExtra( "audio", aud.get( position ) );
          myIntent.putExtra( "video", vid.get( position ) );



          getActivity().startActivity( myIntent );
          getActivity().overridePendingTransition( R.anim.fade_in, R.anim.map_out );
        }
      } );*/
    } else {
      btItem.setVisibility( View.GONE );
    }
    return rootView;
  }

  public void deleteStrings(){
    ztitle.clear();
    descriptions.clear();
    img.clear();
    aud.clear();
    vid.clear();
  }


}
