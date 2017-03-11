package com.uni_wuppertal.iad.vierteltour.ui.map.station_pager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.ui.map.ClickableViewpager;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.ui.map.Station;
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.Singletonint;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.StationActivity;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import java.io.File;
import java.util.ArrayList;

public class StationFragment extends Fragment{
  private static final String ARG_PAGE_NUMBER = "pageNumber";
  private static Bundle arguments;
  private static ArrayList<String> ztitle = new ArrayList<>();
  private ViewGroup ownContainer;
  private Singletonint singlepage;
  private int position;
  private View view;



  public static StationFragment create( Station station ){
    StationFragment fragment = new StationFragment();
    arguments = new Bundle();
    arguments.putInt( ARG_PAGE_NUMBER, station.number() );
    fragment.setArguments( arguments );

    ztitle.add( station.name() );

    return fragment;
  }

  @Override
  public void onDestroyView(){
    super.onDestroyView();
    ImageView i =(ImageView) ownContainer.findViewWithTag("image"+position);
    i.setImageBitmap(null);
    i=null;
   // fragments.get(ARG_PAGE_NUMBER).image.setImageURI(null);
   // image = null;
  }

  @Override
  public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
    View rootView = inflater.inflate( R.layout.fragment_page, container, false );
    RelativeLayout btItem = (RelativeLayout) rootView.findViewById( R.id.clicklayout );

    TextView number = (TextView) rootView.findViewById(R.id.numbertext);
    TextView title = (TextView) rootView.findViewById( R.id.titlefrag );
    ImageView image = (ImageView) rootView.findViewById(R.id.imagefrag);

    View numberlayout = rootView.findViewById(R.id.numberlayout);

    if( arguments != null ){
      position = getArguments().getInt( ARG_PAGE_NUMBER );
      image.setTag("image"+position);
      //Change Image Dynamically
      String externalPath=OurStorage.get(getContext()).storagePath()+"/";
      String appPath=OurStorage.get(getContext()).lookForTourFile(((MapsActivity)getContext()).tourlist(), singlepage.INSTANCE.selectedTour().image())+"/"+singlepage.INSTANCE.selectedTour().station(position).slug()+"/";
      String file="i_"+(singlepage.INSTANCE.selectedTour().trkid()<10?"0"+singlepage.INSTANCE.selectedTour().trkid():singlepage.INSTANCE.selectedTour().trkid())+"_"+(position<10?"0"+position:position)+"_01_400.jpg";

      SharedPreferences getPrefs = PreferenceManager
        .getDefaultSharedPreferences( ((MapsActivity) getContext()).getBaseContext() );

      if(OurStorage.get(getContext()).pathToFile(appPath+file)!=null && (getPrefs.getBoolean(singlepage.INSTANCE.selectedTour().station(position).slug(), false) || singlepage.INSTANCE.selectedTour().station(position).slug().startsWith("einleitung")))
      {BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap mBitmapInsurance = BitmapFactory.decodeFile(externalPath+appPath+file ,options);
        image.setImageBitmap(mBitmapInsurance);}

      title.setText( ztitle.get( position-1 ) );
      if(singlepage.INSTANCE.selectedTour().station(position).latlng()!=null)
      {number.setText((position-1) + "");
      LayerDrawable bgDrawable = (LayerDrawable)numberlayout.getBackground();
      final GradientDrawable shape = (GradientDrawable)   bgDrawable.findDrawableByLayerId(R.id.shape_id);
      shape.setColor(Color.parseColor(singlepage.INSTANCE.selectedTour().color()));}
      else numberlayout.setVisibility(View.GONE);


      btItem.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
          singlepage.INSTANCE.onfragmentclicked(position);
          return false;
        }
      } );
    } else {
      btItem.setVisibility( View.GONE );
    }

    view = rootView;
    ownContainer=container;

    return rootView;
  }

  public void deleteStrings(){
    ztitle.clear();
  }

}
