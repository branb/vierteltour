package com.uni_wuppertal.iad.vierteltour.ui.drawer.einstellungen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pixplicity.sharp.Sharp;
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
    Tour deleteTours = new Tour();
    this.tours=tours;
    if(this.tours.size()>1)this.tours.add(deleteTours);
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
    System.out.println("Pos" + position + "size" + tours.size());

    // Define the visible elements of a single item inside of our ListView
    ImageView imgAuthor = (ImageView) convertView.findViewById(R.id.img);
    ImageView delete = (ImageView) convertView.findViewById(R.id.geladen);
    TextView geloescht = (TextView) convertView.findViewById(R.id.downloadtext);
    TextView txtTitle = (TextView) convertView.findViewById(R.id.txt);
    TextView txtAuthor = (TextView) convertView.findViewById(R.id.subtxt1);
    TextView txtTimeLength = (TextView) convertView.findViewById(R.id.subtxt2);


    if(tours.size()-1==position && tours.size()>1)
    {System.out.println("b");

      delete.setVisibility(View.GONE);
      imgAuthor.setVisibility(View.INVISIBLE);
      geloescht.setVisibility(View.INVISIBLE);
      txtAuthor.setVisibility(View.INVISIBLE);
      txtTimeLength.setVisibility(View.INVISIBLE);

      txtTitle.setText("Alle Touren löschen");
      RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)txtTitle.getLayoutParams();
      params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
      params.addRule(RelativeLayout.CENTER_VERTICAL);
      txtTitle.setLayoutParams(params); //causes layout update
      txtTitle.setGravity(Gravity.CENTER);
      convertView.setBackgroundColor(context.getResources().getColor(R.color.grey));
    }

    else if((tours.size()>1 && tours.size()-1!=position)||(tours.size()<=1))
    {System.out.println("a");

      Tour tour = tours.get(position);
      convertView.setBackgroundColor(Color.parseColor(tour.color()));

      delete.setVisibility(View.VISIBLE);
      imgAuthor.setVisibility(View.VISIBLE);
      geloescht.setVisibility(View.VISIBLE);
      txtAuthor.setVisibility(View.VISIBLE);
      txtTimeLength.setVisibility(View.VISIBLE);


      Sharp.loadResource(context.getResources(), R.raw.x).into(delete);

      BitmapFactory.Options options = new BitmapFactory.Options();
      Bitmap mBitmapInsurance = BitmapFactory.decodeFile(OurStorage.get(context).storagePath() + "/" + OurStorage.get(context).lookForTourFile(((Einstellungen) context).tourlist(), tour.image()) + tour.image() + ".png", options);
      imgAuthor.setImageBitmap(mBitmapInsurance);

      geloescht.setText("löschen");
      geloescht.setVisibility(View.VISIBLE);


      txtTitle.setText(tour.name());
      RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)txtTitle.getLayoutParams();
      params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      params.addRule(RelativeLayout.CENTER_VERTICAL, 0);
      txtTitle.setLayoutParams(params); //causes layout update

      txtAuthor.setText(tour.author());
      txtTimeLength.setText(tour.time() + "/" + tour.length());
    }

    return convertView;
  }
}
