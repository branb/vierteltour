package com.uni_wuppertal.iad.vierteltour.ui.map.station_pager;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.hyphen.DefaultHyphenator;
import com.bluejamesbond.text.style.TextAlignment;
import com.pixplicity.sharp.Sharp;
import com.squareup.picasso.Picasso;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Station;
import com.uni_wuppertal.iad.vierteltour.utility.storage.Singletonint;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.utility.storage.OurStorage;

import java.io.File;
import java.util.ArrayList;

/**
 * single fragment for Station ViewPager within mapsActivity
 */

public class StationFragment extends Fragment{
  private static final String ARG_PAGE_NUMBER = "pageNumber";
  private static Bundle arguments;
  private static ArrayList<String> ztitle = new ArrayList<>();
  private ViewGroup ownContainer;
  private Singletonint singlepage;
  private int position;
  private View numberlayout;


//creating fragment
  public static StationFragment create( Station station ){
    StationFragment fragment = new StationFragment();
    arguments = new Bundle();
    arguments.putInt( ARG_PAGE_NUMBER, station.number() );
    fragment.setArguments( arguments );

    ztitle.add( station.name() );

    return fragment;
  }

  //destorying fragment
  @Override
  public void onDestroyView(){
    super.onDestroyView();
    ImageView i =(ImageView) ownContainer.findViewWithTag("image"+position);
    i.setImageBitmap(null);   //needs to set null otherwise buffer overflow error
    i=null;
   // fragments.get(ARG_PAGE_NUMBER).image.setImageURI(null);
    // image = null;
  }

  @Override
  public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
    View rootView = inflater.inflate( R.layout.fragment_page, container, false );
    final RelativeLayout btItem = (RelativeLayout) rootView.findViewById( R.id.clicklayout );

    TextView number = (TextView) rootView.findViewById(R.id.numbertext);
  //  DocumentView title = new DocumentView(this, DocumentView.PLAIN_TEXT);  // Support plain text
    DocumentView title = (DocumentView) rootView.findViewById( R.id.titlefrag );

    //Image Sizes of Fragment
    ImageView image = (ImageView) rootView.findViewById(R.id.imagefrag);
    ImageView icon = (ImageView) rootView.findViewById(R.id.nav_icon);
    RelativeLayout image_layout = (RelativeLayout) rootView.findViewById(R.id.transparentstation);
    Sharp.loadResource(getResources(), R.raw.ausrufezeichen).into(icon);
    //System.out.println("Height: "+getContext().getApplicationContext().getResources().getDisplayMetrics().heightPixels+"width: "+getContext().getApplicationContext().getResources().getDisplayMetrics().widthPixels);
    //System.out.println((int) (getContext().getApplicationContext().getResources().getDisplayMetrics().heightPixels*0.149));
   /* RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int) (getContext().getApplicationContext().getResources().getDisplayMetrics().heightPixels*0.149));
    lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
    lp.topMargin=(int) (getContext().getApplicationContext().getResources().getDisplayMetrics().widthPixels*0.026);
    lp.leftMargin=(int) (getContext().getApplicationContext().getResources().getDisplayMetrics().widthPixels*0.030);
    lp.rightMargin=(int) (getContext().getApplicationContext().getResources().getDisplayMetrics().widthPixels*0.030);
    image_layout.setLayoutParams(lp);
    image.setLayoutParams(lp);*/

    numberlayout = rootView.findViewById(R.id.numberlayout);

    if( arguments != null ){
      position = getArguments().getInt( ARG_PAGE_NUMBER );
      image.setTag("image"+position);
      //Change Image Dynamically
      //Gets path of small image
      String externalPath=OurStorage.get(getContext()).storagePath()+"/";
      String appPath=OurStorage.get(getContext()).lookForTourFile(((MapsActivity)getContext()).tourlist(), singlepage.INSTANCE.selectedTour().image())+"/"+singlepage.INSTANCE.selectedTour().station(position).slug()+"/";
      //Sets small image into fragment
      String file="i_"+(singlepage.INSTANCE.selectedTour().trkid()<10?"0"+singlepage.INSTANCE.selectedTour().trkid():singlepage.INSTANCE.selectedTour().trkid())+"_"+(position<10?"0"+position:position)+"_01.jpg";

      SharedPreferences getPrefs = PreferenceManager
        .getDefaultSharedPreferences( ((MapsActivity) getContext()).getBaseContext() );

      if(OurStorage.get(getContext()).pathToFile(appPath+file)!=null)
      {Picasso.with(getContext()).load(new File(externalPath+appPath+file)).into(image);}

      if(OurStorage.get(getContext()).pathToFile(appPath+file)==null || (!getPrefs.getBoolean(singlepage.INSTANCE.selectedTour().station(position).slug(), false) && !singlepage.INSTANCE.selectedTour().station(position).slug().startsWith("einleitung")))
      {icon.setVisibility(View.VISIBLE);
       image_layout.setVisibility(View.VISIBLE);}

      boolean isWaypoint=false;
      for(int i=0;i<singlepage.INSTANCE.countWaypoints().size();i++)
      {if((position-1) == singlepage.INSTANCE.countWaypoints().get(i)) {isWaypoint=true;}}

      int countnumber=0;

      if(singlepage.INSTANCE.selectedTour().station(1).slug().contains("einleitung"))
      {try{countnumber=0;
        while(singlepage.INSTANCE.countWaypoints().get(countnumber)<(position-1))countnumber++;}catch (Exception e){}}
      else
      { try{countnumber=0;
        while(singlepage.INSTANCE.countWaypoints().get(countnumber)<(position))countnumber++;}catch (Exception e){}}

      title.getDocumentLayoutParams().setHyphenator(DefaultHyphenator.getInstance(DefaultHyphenator.HyphenPattern.DE));
      title.getDocumentLayoutParams().setHyphenated(true);
      title.setText(ztitle.get( position-1 )); // Set to `true` to enable justification
      title.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
          int action = event.getAction();
          switch (action) {
            case MotionEvent.ACTION_DOWN:
              break;
            case MotionEvent.ACTION_UP:

              break;
          }
          return true;
        }});

      //Sets number layout in the right corner
      if(singlepage.INSTANCE.selectedTour().station(position).latlng()!=null && singlepage.INSTANCE.selectedTour().station(1).slug().contains("einleitung") && !isWaypoint)
      {number.setText((position-1-countnumber) + "");
      LayerDrawable bgDrawable = (LayerDrawable)numberlayout.getBackground();
      final GradientDrawable shape = (GradientDrawable)   bgDrawable.findDrawableByLayerId(R.id.shape_id);
      shape.setColor(Color.parseColor(singlepage.INSTANCE.selectedTour().color()));}
      else if(singlepage.INSTANCE.selectedTour().station(position).latlng()!=null && !isWaypoint)
      {
        number.setText((position-1-countnumber) + "");
        LayerDrawable bgDrawable = (LayerDrawable)numberlayout.getBackground();
        final GradientDrawable shape = (GradientDrawable)   bgDrawable.findDrawableByLayerId(R.id.shape_id);
        shape.setColor(Color.parseColor(singlepage.INSTANCE.selectedTour().color()));}
      else {
        numberlayout.setVisibility(View.GONE);}

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
    ownContainer=container;

    return rootView;
  }

  public View getNumberlayout() {return numberlayout;}

  public void deleteStrings(){
    ztitle.clear();
  }

}
