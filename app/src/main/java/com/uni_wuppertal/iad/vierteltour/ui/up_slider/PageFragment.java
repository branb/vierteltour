package com.uni_wuppertal.iad.vierteltour.ui.up_slider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.ui.media_player.InformationActivity;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.TourOld;

import java.util.ArrayList;

public class PageFragment extends Fragment{
  private static final String ARG_PAGE_NUMBER = "pageNumber", TITLE = "title", STATIONNAME = "stationname";
  private static String TOURNAME, AUTOR, TIME, LENGHT, COLOR, DESCRIPTION, SIZE;
  private static ArrayList<String> descriptions = new ArrayList<>();
  private static Bundle arguments;
  private static ArrayList<String> ztitle = new ArrayList<>();
  private static ArrayList<String> img = new ArrayList<>();
  private static ArrayList<String> aud = new ArrayList<>();
  private static ArrayList<String> vid = new ArrayList<>();
  private TextView title;
  private int position;

  public static PageFragment create( int pageNumber, TourOld marked ){
    PageFragment fragment = new PageFragment();
    arguments = new Bundle();
    arguments.putInt( ARG_PAGE_NUMBER, pageNumber );
    fragment.setArguments( arguments );
    ztitle.add( marked.stations.get( pageNumber ).title );
    descriptions.add( marked.stations.get( pageNumber ).description );
    img.add( marked.stations.get( pageNumber ).image );
    aud.add( marked.stations.get( pageNumber ).audio );
    vid.add( marked.stations.get( pageNumber ).video );
    TOURNAME = marked.info.name;
    AUTOR = marked.info.author;
    TIME = marked.info.time;
    LENGHT = marked.info.length;
    COLOR = marked.info.color;
    SIZE = "" + marked.stations.size();
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

      btItem.setOnClickListener( new View.OnClickListener(){
        @Override
        public void onClick( View v ){
          Intent myIntent = new Intent( getActivity(), InformationActivity.class );

          //myIntent.putExtra("key", arguments.getInt(ARG_PAGE_NUMBER)); //Optional parameters
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


          getActivity().overridePendingTransition( R.anim.fade_in, R.anim.map_out );
          getActivity().startActivity( myIntent );
        }
      } );
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
