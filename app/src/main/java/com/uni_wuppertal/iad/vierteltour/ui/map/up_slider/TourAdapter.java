package com.uni_wuppertal.iad.vierteltour.ui.map.up_slider;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
  private Singletonint singlepage;
  private List<Tour> tours;

  public TourAdapter( MapsActivity m, List<Tour> tours){
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
    TextView txtTitle = (TextView) convertView.findViewById( R.id.txt );
    TextView txtAuthor = (TextView) convertView.findViewById( R.id.subtxt1 );
    TextView txtTimeLength = (TextView) convertView.findViewById( R.id.subtxt2 );
    TextView txtDescription = (TextView) convertView.findViewById( R.id.addinfo );
    ImageButton btnStart = (ImageButton) convertView.findViewById( R.id.zumstartlist );
    View divider = convertView.findViewById( R.id.divider );

    Tour tour = tours.get( position );
    convertView.setBackgroundColor( Color.parseColor( tour.color() ) );

    // TODO: Insert author image
    imgAuthor.setImageResource( R.drawable.ic_drawer );
    txtTitle.setText( tour.name() );
    txtAuthor.setText( tour.author() );
    txtTimeLength.setText( tour.time() + "/" + tour.length() );
    txtDescription.setText( tour.description() );
    convertView.setClickable(false);

    if(singlepage.INSTANCE.selectedTour()==null || !tour.slug().equals(singlepage.INSTANCE.selectedTour().slug())) {
      txtDescription.setVisibility( View.GONE );
      btnStart.setVisibility( View.GONE );
      divider.setVisibility( View.GONE );

    }
    else if( tour.slug().equals( singlepage.INSTANCE.selectedTour().slug() ) ) {
      txtDescription.setVisibility(View.VISIBLE);
      btnStart.setVisibility(View.VISIBLE);
      divider.setVisibility(View.VISIBLE);
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
    return convertView;
  }
}
