package com.uni_wuppertal.iad.vierteltour.ui.map.up_slider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Tour;
import com.uni_wuppertal.iad.vierteltour.utility.Singletonint;
import com.uni_wuppertal.iad.vierteltour.utility.updater.Updater;
import com.uni_wuppertal.iad.vierteltour.utility.storage.OurStorage;

import java.util.List;

/**
 * Connects the ListView inside of the SUPL of the Maps Activity with a List<Tour>
 */
public class TourAdapter extends BaseAdapter{

  private Context context;
  private View view;
  private ImageView geladen, laden;
  private TextView downloadtext;
  private Singletonint singlepage;
  private ViewGroup ownContainer;
  private List<Tour> tours;
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor e;


  public TourAdapter( List<Tour> tours, Context context){
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    e = sharedPreferences.edit();
    this.tours = tours;
    this.context = context;
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
    if( convertView == null && context instanceof MapsActivity){
      LayoutInflater mInflater = (LayoutInflater) (context).getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
      convertView = mInflater.inflate( R.layout.touren_list_single, null );
    }


    // Define the visible elements of a single item inside of our ListView
    ImageView imgAuthor = (ImageView) convertView.findViewById( R.id.img );
    geladen = (ImageView) convertView.findViewById( R.id.geladen);
    geladen.setTag("ok"+position);
    Sharp.loadResource(context.getResources(), R.raw.ok).into(geladen);
    laden = (ImageView) convertView.findViewById( R.id.laden);
    laden.setTag("laden"+position);
    Sharp.loadResource(context.getResources(), R.raw.laden).into(laden);
    downloadtext = (TextView) convertView.findViewById(R.id.downloadtext);
    downloadtext.setTag("text"+position);
    final TextView txtTitle = (TextView) convertView.findViewById( R.id.txt );
    TextView txtAuthor = (TextView) convertView.findViewById( R.id.subtxt1 );
    TextView txtTimeLength = (TextView) convertView.findViewById( R.id.subtxt2 );
    TextView txtDescription = (TextView) convertView.findViewById( R.id.addinfo );
    ImageButton btnStart = (ImageButton) convertView.findViewById( R.id.zumstartlist );
    Sharp.loadResource(context.getResources(), R.raw.zum_start).into(btnStart);
    View divider = convertView.findViewById( R.id.divider );

    final Tour tour = tours.get( position );
    convertView.setBackgroundColor( Color.parseColor( tour.color() ) );

    if(sharedPreferences.getBoolean(tour.slug(), false))
      {geladen.setVisibility(View.VISIBLE);
        laden.setVisibility(View.GONE);
      downloadtext.setText("geladen");
      downloadtext.setVisibility(View.VISIBLE);
      btnStart.setVisibility(View.VISIBLE);}
    else{geladen.setVisibility(View.GONE);
      laden.setVisibility(View.VISIBLE);
      downloadtext.setVisibility(View.GONE);
      btnStart.setVisibility(View.INVISIBLE);
     }

    BitmapFactory.Options options = new BitmapFactory.Options();
    Bitmap mBitmapInsurance = BitmapFactory.decodeFile(OurStorage.get(context).storagePath()+"/"+OurStorage.get(context).lookForTourFile(((MapsActivity)context).tourlist(),tour.image())+tour.image()+".png" ,options);
    imgAuthor.setImageBitmap(mBitmapInsurance);
    //imgAuthor.setImageURI( Uri.fromFile(new File(OurStorage.get(context).storagePath()+"/"+OurStorage.get(context).lookForTourFile(((MapsActivity)context).tourlist(), tour.image())+tour.image()+".png")));

      laden.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(context instanceof MapsActivity){
          createDownloadDialog("Willst du die Tour "+ txtTitle.getText() + " herunterladen?\nHinweis: Verwende dein WLAN", tour.slug(), position);

        }}});

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
    else if( tour.slug().equals(singlepage.INSTANCE.selectedTour().slug()) ) {
      txtDescription.setVisibility(View.VISIBLE);
      if(sharedPreferences.getBoolean(tour.slug(), false))btnStart.setVisibility(View.VISIBLE);
      divider.setVisibility(View.VISIBLE);
      convertView.setClickable(true);
      convertView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if(context instanceof MapsActivity) ((MapsActivity)context).resetTour();
          notifyDataSetChanged();
          view.setClickable(false);

        }
      });
    }
    view = convertView;
    ownContainer=parent;

    return convertView;
  }

  public void createDownloadDialog(String txt, String slug, int position)
  {// Create custom dialog object
    final Dialog dialog = new Dialog(context);
    // Include dialog.xml file
    dialog.setContentView(R.layout.alert_dialog);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();
    // set values for custom dialog components - text, image and button
    TextView text = (TextView) dialog.findViewById(R.id.main_text);
    text.setText(txt);

    final String tourslug = slug;
    final int pos = position;

    Button okayButton = (Button) dialog.findViewById(R.id.left_btn);
    // if decline button is clicked, close the custom dialog
    okayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if(context instanceof MapsActivity)  Updater.get(((MapsActivity)context).getBaseContext()).downloadTourMedia(tourslug, context);
        dialog.dismiss();}});

    Button declineButton = (Button) dialog.findViewById(R.id.right_btn);
    // if decline button is clicked, close the custom dialog
    declineButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Close dialog
        dialog.dismiss();
      }});
  }



}
