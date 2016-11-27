package com.uni_wuppertal.iad.vierteltour.ui.up_slider;

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
import com.uni_wuppertal.iad.vierteltour.ui.map.Tour;

import java.util.List;

// TODO: Rename it to TourAdapter and factor the RowItem out of it. The whole point of an Adapter is to connect views with data models, and Tour (even TourOld) IS already the data model
//Die Schnittstelle zwischen Liste als xml und java mit definiertem Aussehen
public class TourenAdapter extends BaseAdapter{

  private Context context;

  private Tour selectedTour;
  private List<Tour> tours;

  public TourenAdapter( Context context, List<Tour> tours, Tour selectedTour ){
    this.context = context;
    this.tours = tours;
    this.selectedTour = selectedTour;
  }

  //zaehlt Anzahl an Zeilen in Liste
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

  //erzeugt Aussehen der Liste
  @Override
  public View getView( final int position, View convertView, ViewGroup parent ){
    if( convertView == null ){
      LayoutInflater mInflater = (LayoutInflater) context.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
      convertView = mInflater.inflate( R.layout.touren_list_single, null );
    }

    ImageView imgIcon = (ImageView) convertView.findViewById( R.id.img );
    TextView txtTitle = (TextView) convertView.findViewById( R.id.txt );
    TextView txtAuthor = (TextView) convertView.findViewById( R.id.subtxt1 );
    TextView txtTimeLength = (TextView) convertView.findViewById( R.id.subtxt2 );
    TextView txtDescription = (TextView) convertView.findViewById( R.id.addinfo );
    ImageButton startbtn = (ImageButton) convertView.findViewById( R.id.zumstartlist );
    View divider = convertView.findViewById( R.id.divider );

    //Setzt jeweilige Informationen an die richtigen Views
    Tour tour = tours.get( position );
    convertView.setBackgroundColor( Color.parseColor( tour.details().color() ) );

    // TODO: Insert author image
    imgIcon.setImageResource( R.drawable.ic_drawer );
    txtTitle.setText( tour.name() );
    txtAuthor.setText( tour.details().author() );
    txtTimeLength.setText( tour.details().time() + "/" + tour.details().length() );
    txtDescription.setText( tour.details().description() );

    if( tour.slug().equals( selectedTour.slug() ) ){
      txtDescription.setVisibility( View.VISIBLE );
      startbtn.setVisibility( View.VISIBLE );
      divider.setVisibility( View.VISIBLE );
    } else {
      txtDescription.setVisibility( View.GONE );
      startbtn.setVisibility( View.GONE );
      divider.setVisibility( View.GONE );
    }

    return convertView;
  }

  public void select( Tour tour ){
    selectedTour = tour;
  }
}
