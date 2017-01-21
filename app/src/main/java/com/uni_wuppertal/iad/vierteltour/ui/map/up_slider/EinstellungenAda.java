package com.uni_wuppertal.iad.vierteltour.ui.map.up_slider;

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

/**
 * Created by Kevin-Laptop on 20.01.2017.
 */

public class EinstellungenAda extends BaseAdapter {

  private String[] values;
  private Context context;

  public EinstellungenAda(Context context, String[] values){
    this.context = context;
    this.values=values;
  }

  @Override
  public int getCount(){return values.length;}

  @Override
  public Object getItem( int position ){return values[position];}

  @Override
  public long getItemId( int position ){return values.hashCode();}

  /**
   * This is being called every time the connected ListView is refreshing itself
   */
  @Override
  public View getView(final int position, View convertView, ViewGroup parent ){
    if(convertView==null)
    {convertView = LayoutInflater.from(context).inflate( R.layout.einstellungen_single_item, null );}

    // Define the visible elements of a single item inside of our ListView
    TextView txtEinstellungen = (TextView) convertView.findViewById( R.id.einstellungentext );

    txtEinstellungen.setText( values[position] );

    return convertView;
  }
}
