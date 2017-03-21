package com.uni_wuppertal.iad.vierteltour.ui.drawer.einstellungen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.drawer.einstellungen.Einstellungen;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Tour;
import com.uni_wuppertal.iad.vierteltour.utility.storage.OurStorage;

import java.util.List;

/**
 * Created by Kevin-Laptop on 20.01.2017.
 * Adapter for tour listview
 */

public class EinstellungenTourAdapter extends BaseAdapter {

  private List<Tour> tours;
  private Context context;


  public EinstellungenTourAdapter(List<Tour> tours, Context context){
    this.tours=tours;
    this.context = context;
    }

  @Override
  public int getCount(){return tours.size();}

  @Override
  public Object getItem( int position ){return tours.get(position);}

  @Override
  public long getItemId( int position ){return tours.indexOf(getItem( position ));}

  /**
   * This is being called every time the connected ListView is refreshing itself
   */
  @Override
  public View getView(final int position, View convertView, ViewGroup parent ){
    if(convertView==null)
    {convertView = LayoutInflater.from(context).inflate( R.layout.touren_list_single, null );}

    // Define the visible elements of a single item inside of our ListView
    ImageView imgAuthor = (ImageView) convertView.findViewById( R.id.img );
    ImageView delete = (ImageView) convertView.findViewById(R.id.downloadview);
    TextView geloescht = (TextView) convertView.findViewById(R.id.downloadtext);
    TextView txtTitle = (TextView) convertView.findViewById( R.id.txt );
    TextView txtAuthor = (TextView) convertView.findViewById( R.id.subtxt1 );
    TextView txtTimeLength = (TextView) convertView.findViewById( R.id.subtxt2 );

    Tour tour = tours.get( position );
    convertView.setBackgroundColor( Color.parseColor( tour.color() ) );

    delete.setImageResource( R.drawable.x );

    BitmapFactory.Options options = new BitmapFactory.Options();
    Bitmap mBitmapInsurance = BitmapFactory.decodeFile(OurStorage.get(context).storagePath()+"/"+OurStorage.get(context).lookForTourFile(((Einstellungen)context).tourlist(),tour.image())+tour.image()+".png" ,options);
    imgAuthor.setImageBitmap(mBitmapInsurance);

    geloescht.setText("löschen");
    geloescht.setVisibility(View.VISIBLE);
    txtTitle.setText( tour.name() );
    txtAuthor.setText( tour.author() );
    txtTimeLength.setText( tour.time() + "/" + tour.length() );

    return convertView;
  }
}
