package com.uni_wuppertal.iad.vierteltour.ui.drawer.einstellungen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.TypedValue;
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
    ImageView imgAuthor = (ImageView) convertView.findViewById(R.id.img);
    ImageView delete = (ImageView) convertView.findViewById(R.id.geladen);
    TextView txtTitle = (TextView) convertView.findViewById(R.id.txt);
    TextView txtAuthor = (TextView) convertView.findViewById(R.id.subtxt1);
    TextView txtTimeLength = (TextView) convertView.findViewById(R.id.subtxt2);

      Tour tour = tours.get(position);
      convertView.setBackgroundColor(Color.parseColor(tour.color()));

      delete.setVisibility(View.VISIBLE);
      imgAuthor.setVisibility(View.VISIBLE);
      txtAuthor.setVisibility(View.VISIBLE);
      txtTimeLength.setVisibility(View.VISIBLE);


      Sharp.loadResource(context.getResources(), R.raw.loeschen).into(delete);
    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, context.getResources().getDisplayMetrics()), ViewGroup.LayoutParams.WRAP_CONTENT);
    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    lp.setMargins(0,0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, context.getResources().getDisplayMetrics()),0);
    delete.setLayoutParams(lp);

    delete.setPadding(0,0,0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));


      BitmapFactory.Options options = new BitmapFactory.Options();
      Bitmap mBitmapInsurance = BitmapFactory.decodeFile(OurStorage.get(context).storagePath() + "/" + OurStorage.get(context).lookForTourFile(((Einstellungen) context).tourlist(), tour.image()) + tour.image() + ".png", options);
      imgAuthor.setImageBitmap(mBitmapInsurance);

      txtTitle.setText(tour.name());
      RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)txtTitle.getLayoutParams();
      params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
      params.addRule(RelativeLayout.CENTER_VERTICAL, 0);
      txtTitle.setLayoutParams(params); //causes layout update

      SpannableString author = new SpannableString(tour.author() + " ");
      author.setSpan(new StyleSpan(Typeface.BOLD), 0, author.length(), 0);
      txtAuthor.setText(author);
      SpannableString length = new SpannableString(tour.time() + "/" + tour.length() + " ");
      length.setSpan(new StyleSpan(Typeface.BOLD), 0, length.length(), 0);
      txtTimeLength.setText(length);

    return convertView;
  }
}
