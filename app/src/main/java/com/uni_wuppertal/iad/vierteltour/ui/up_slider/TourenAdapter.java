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

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.XmlParser;

import java.util.List;

//Die Schnittstelle zwischen Liste als xml und java mit definiertem Aussehen
public class TourenAdapter extends BaseAdapter{

  Context context;
  List<RowItem> rowItem;
  XmlParser parser;

  private SlidingUpPanelLayout mLayout;

  public TourenAdapter( Context context, List<RowItem> rowItem, XmlParser parse ){
    this.context = context;
    this.rowItem = rowItem;
    this.parser = parse;
  }

  //zaehlt Anzahl an Zeilen in Liste
  @Override
  public int getCount(){
    return rowItem.size();
  }

  @Override
  public Object getItem( int position ){
    return rowItem.get( position );
  }

  @Override
  public long getItemId( int position ){
    return rowItem.indexOf( getItem( position ) );
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
    TextView subtxtTitle1 = (TextView) convertView.findViewById( R.id.subtxt1 );
    TextView subtxtTitle2 = (TextView) convertView.findViewById( R.id.subtxt2 );
    TextView addInfo = (TextView) convertView.findViewById( R.id.addinfo );
    ImageButton startbtn = (ImageButton) convertView.findViewById( R.id.zumstartlist );
    View divider = convertView.findViewById( R.id.divider );

    //Setzt jeweilige Informationen an die richtigen Views
    convertView.setBackgroundColor( Color.parseColor( parser.ListTouren.get( position ).info.color ) );
    RowItem row_pos = rowItem.get( position );

    imgIcon.setImageResource( row_pos.getIcon() );
    txtTitle.setText( row_pos.getTitle() );
    subtxtTitle1.setText( row_pos.getSubtitle1() );
    subtxtTitle2.setText( row_pos.getSubtitle2() );
    addInfo.setText( row_pos.getAddinfo() );

    if( row_pos.isSelected() ){
      addInfo.setVisibility( View.VISIBLE );
      startbtn.setVisibility( View.VISIBLE );
      divider.setVisibility( View.VISIBLE );
    } else {
      addInfo.setVisibility( View.GONE );
      startbtn.setVisibility( View.GONE );
      divider.setVisibility( View.GONE );
    }

    return convertView;
  }

}
