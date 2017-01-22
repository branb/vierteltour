package com.uni_wuppertal.iad.vierteltour.ui.map.up_slider;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.Singletonint;

import java.util.List;

/**
 * Connects the ListView inside of the SUPL of the Maps Activity with a List<Tour>
 */
public class TourAdapter extends BaseAdapter{

  private MapsActivity mapsActivity;
  private View view;
  private ImageView downloadbutton;
  private TextView downloadtext;
  private Singletonint singlepage;
  private ViewGroup ownContainer;
  private List<Tour> tours;
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor e;

  public TourAdapter( MapsActivity m, List<Tour> tours, Context context){
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    e = sharedPreferences.edit();
    this.tours = tours;
    mapsActivity = m;
  }

  @Override
  public int getCount(){
    return tours.size();
  }

  @Override
  public Object getItem( int position ){
    return tours.get( position );
  }

  @Override
  public long getItemId( int position ){
    return tours.indexOf( getItem( position ) );
  }

  /**
   * This is being called every time the connected ListView is refreshing itself
   */
  @Override
  public View getView( final int position, View convertView, ViewGroup parent ){
    if( convertView == null ){
      LayoutInflater mInflater = (LayoutInflater) mapsActivity.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
      convertView = mInflater.inflate( R.layout.touren_list_single, null );
    }


    // Define the visible elements of a single item inside of our ListView
    ImageView imgAuthor = (ImageView) convertView.findViewById( R.id.img );
    downloadbutton = (ImageView) convertView.findViewById( R.id.downloadview);
    downloadbutton.setTag("ok"+position);
    downloadtext = (TextView) convertView.findViewById(R.id.downloadtext);
    downloadtext.setTag("text"+position);
    TextView txtTitle = (TextView) convertView.findViewById( R.id.txt );
    TextView txtAuthor = (TextView) convertView.findViewById( R.id.subtxt1 );
    TextView txtTimeLength = (TextView) convertView.findViewById( R.id.subtxt2 );
    TextView txtDescription = (TextView) convertView.findViewById( R.id.addinfo );
    ImageButton btnStart = (ImageButton) convertView.findViewById( R.id.zumstartlist );
    View divider = convertView.findViewById( R.id.divider );

    final Tour tour = tours.get( position );
    convertView.setBackgroundColor( Color.parseColor( tour.color() ) );

    if(sharedPreferences.getBoolean(tour.slug(), false))
      {downloadbutton.setImageResource(R.drawable.ok);
      downloadtext.setText("geladen");
      downloadtext.setVisibility(View.VISIBLE);}
    else{downloadbutton.setImageResource(R.drawable.laden);
      downloadtext.setVisibility(View.GONE);}


    // TODO: Insert author image
    imgAuthor.setImageResource( R.drawable.ic_drawer );
    downloadbutton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        //Add downloaded Tour to prefs
        e.putBoolean( tour.slug(), true);
        e.apply();
        downloadbutton = (ImageView) ownContainer.findViewWithTag("ok"+position);
        downloadbutton.setImageResource(R.drawable.ok);
        downloadtext = (TextView) ownContainer.findViewWithTag("text"+position);
        downloadtext.setText("geladen");
        downloadtext.setVisibility(View.VISIBLE);
      }
    });
    txtTitle.setText( tour.name() );
    txtAuthor.setText( tour.author() );
    txtTimeLength.setText( tour.time() + "/" + tour.length() );
    txtDescription.setText( tour.description() );
    convertView.setClickable(false);

    if(singlepage.INSTANCE.selectedTour()==null || !tour.slug().equals(singlepage.INSTANCE.selectedTour().slug())) {
      setDownloadButtonInPosition(false);
      txtDescription.setVisibility( View.GONE );
      btnStart.setVisibility( View.GONE );
      divider.setVisibility( View.GONE );

    }
    else if( tour.slug().equals( singlepage.INSTANCE.selectedTour().slug() ) ) {
      setDownloadButtonInPosition(true);
      txtDescription.setVisibility(View.VISIBLE);
      btnStart.setVisibility(View.VISIBLE);
      divider.setVisibility(View.VISIBLE);
      mapsActivity.lv().smoothScrollToPosition(position);
      convertView.setClickable(true);
      convertView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mapsActivity.resetTour();
          notifyDataSetChanged();
          view.setClickable(false);
        }
      });
    }
    view = convertView;
    ownContainer=parent;

    return convertView;
  }

  public void setDownloadButtonInPosition(boolean open)
  {if(open)
  {RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
    lp.addRule(RelativeLayout.ABOVE, R.id.divider);
    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
    downloadbutton.setLayoutParams(lp);

    RelativeLayout.LayoutParams lptext = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    lptext.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
    lptext.addRule(RelativeLayout.ABOVE, R.id.divider);
    lptext.addRule(RelativeLayout.LEFT_OF, R.id.downloadview);
    downloadtext.setLayoutParams(lptext);

  }
  else{RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
    lp.addRule(RelativeLayout.ABOVE, 0);
    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
    downloadbutton.setLayoutParams(lp);

    RelativeLayout.LayoutParams lptext = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    lptext.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
    lptext.addRule(RelativeLayout.ABOVE, 0);
    lptext.addRule(RelativeLayout.LEFT_OF, R.id.downloadview);
    downloadtext.setLayoutParams(lptext);
  }}


}
