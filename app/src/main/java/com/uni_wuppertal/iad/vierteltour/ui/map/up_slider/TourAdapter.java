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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
public class TourAdapter extends BaseExpandableListAdapter {

  private Context context;
  private View view;
  private ImageView geladen, laden;
  private TextView downloadtext;
  private Singletonint singlepage;
  private ViewGroup ownContainer;
  private List<Tour> tours;
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor e;

  @Override
  public Object getChild(int groupPosition, int childPosititon) {
    return tours.get(groupPosition).stations()
      .get(childPosititon);
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return 1;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return tours.get(groupPosition);
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }
  @Override
  public int getGroupCount() {
    return tours.size();
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  public TourAdapter( List<Tour> tours, Context context){
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    e = sharedPreferences.edit();
    this.tours = tours;
    this.context = context;
  }

  /**
   * This is being called every time the connected ListView is refreshing itself
   */
  @Override
  public View getChildView(int groupposition, final int position, boolean isLastChild, View convertView, ViewGroup parent ){
    if( convertView == null && context instanceof MapsActivity){
      LayoutInflater mInflater = (LayoutInflater) (context).getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
      convertView = mInflater.inflate( R.layout.tour_list_expand, null );
    }
    // Define the visible elements of a single item inside of our ListView
    ImageButton btnStart = (ImageButton) convertView.findViewById( R.id.zumstartlist );
    Sharp.loadResource(context.getResources(), R.raw.zum_start).into(btnStart);
    TextView txtDescription = (TextView) convertView.findViewById( R.id.addinfo );
    txtDescription.setText( tours.get(groupposition).description() );

    convertView.setBackgroundColor( Color.parseColor( tours.get(groupposition).color() ) );

    if(singlepage.INSTANCE.selectedTour()!=null && sharedPreferences.getBoolean(singlepage.INSTANCE.selectedTour().slug(), false))btnStart.setVisibility(View.VISIBLE);
    else btnStart.setVisibility(View.INVISIBLE);

    return convertView;
  }

  @Override
  public View getGroupView(final int position, boolean isExpanded,
                           View convertView, ViewGroup parent) {
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

    final Tour tour = tours.get( position );
    convertView.setBackgroundColor( Color.parseColor( tour.color() ) );

    if(sharedPreferences.getBoolean(tour.slug(), false))
    {geladen.setVisibility(View.VISIBLE);
      laden.setVisibility(View.GONE);
      downloadtext.setText("geladen");
      downloadtext.setVisibility(View.VISIBLE);}
    else{geladen.setVisibility(View.GONE);
      laden.setVisibility(View.VISIBLE);
      downloadtext.setVisibility(View.GONE);
    }
    BitmapFactory.Options options = new BitmapFactory.Options();
    Bitmap mBitmapInsurance = BitmapFactory.decodeFile(OurStorage.get(context).storagePath()+"/"+OurStorage.get(context).lookForTourFile(((MapsActivity)context).tourlist(),tour.image())+tour.image()+".png" ,options);
    imgAuthor.setImageBitmap(mBitmapInsurance);

    txtTitle.setText( tour.name() );
    txtAuthor.setText( tour.author() );
    txtTimeLength.setText( tour.time() + "/" + tour.length() );

    laden.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(context instanceof MapsActivity){
          createDownloadDialog("Wollen Sie die Tour »"+ tours.get(position).name() + "« herunterladen? Hinweis: Verwenden Sie Ihr WLAN", tour.slug(), position);

        }}});

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
    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

    RelativeLayout dialogWindow = (RelativeLayout) dialog.findViewById(R.id.dialog);
    dialogWindow.setLayoutParams(new FrameLayout.LayoutParams((int) (displayMetrics.widthPixels*0.85), FrameLayout.LayoutParams.WRAP_CONTENT));
    TextView downloadtitle = (TextView) dialog.findViewById(R.id.title_dialog);
    downloadtitle.setText("Laden der Tour");
    TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
    text.setText(txt);

    final String tourslug = slug;

    ImageButton okayButton = (ImageButton) dialog.findViewById(R.id.button_dialog);
    Sharp.loadResource(context.getResources(), R.raw.laden).into(okayButton);
    // if decline button is clicked, close the custom dialog
    okayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if(context instanceof MapsActivity)  Updater.get(((MapsActivity)context).getBaseContext()).downloadTourMedia(tourslug, context);
        dialog.dismiss();}});

    ImageButton declineButton = (ImageButton) dialog.findViewById(R.id.btn_x_dialog);
    Sharp.loadResource(context.getResources(), R.raw.beenden_dunkel).into(declineButton);
    // if decline button is clicked, close the custom dialog
    declineButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Close dialog
        dialog.dismiss();
      }});
  }

}
