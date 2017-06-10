package com.uni_wuppertal.iad.vierteltour.ui.map.up_slider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Tour;
import com.uni_wuppertal.iad.vierteltour.utility.storage.Singletonint;
import com.uni_wuppertal.iad.vierteltour.utility.updater.Updater;
import com.uni_wuppertal.iad.vierteltour.utility.storage.OurStorage;

import java.io.File;
import java.util.List;

/**
 * Connects the ListView inside of the SUPL of the Maps Activity with a List<Tour>
 */
public class TourAdapter extends BaseExpandableListAdapter {

  private Context context;
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
    ViewHolderChild holderchild;

    if( convertView == null && context instanceof MapsActivity){
      LayoutInflater mInflater = (LayoutInflater) (context).getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
      convertView = mInflater.inflate( R.layout.tour_list_expand, null );
      holderchild = new ViewHolderChild();
      holderchild.txtDescription = (TextView) convertView.findViewById( R.id.addinfo );
      holderchild.btnStart = (ImageButton) convertView.findViewById( R.id.zumstartlist );
    }
    else {
      holderchild = (ViewHolderChild) convertView.getTag();
    }
    // Define the visible elements of a single item inside of our ListView

    Sharp.loadResource(context.getResources(), R.raw.zum_start).into(holderchild.btnStart);

    holderchild.txtDescription.setText( tours.get(groupposition).description() );

    convertView.setBackgroundColor( Color.parseColor( tours.get(groupposition).color() ) );

    if(singlepage.INSTANCE.selectedTour()!=null && sharedPreferences.getBoolean(singlepage.INSTANCE.selectedTour().slug(), false))holderchild.btnStart.setVisibility(View.VISIBLE);
    else holderchild.btnStart.setVisibility(View.INVISIBLE);

    return convertView;
  }

  @Override
  public View getGroupView(final int position, boolean isExpanded,
                           View convertView, ViewGroup parent) {
    ViewHolderGroup holder;

    if( convertView == null && context instanceof MapsActivity){
      LayoutInflater mInflater = (LayoutInflater) (context).getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
      convertView = mInflater.inflate( R.layout.touren_list_single, null );
      holder = new ViewHolderGroup();
      holder.imgAuthor = (ImageView) convertView.findViewById( R.id.img );
      holder.geladen = (ImageView) convertView.findViewById( R.id.geladen);
      holder.laden = (ImageView) convertView.findViewById( R.id.laden);
      holder.downloadtext = (TextView) convertView.findViewById(R.id.downloadtext);
      holder.txtTitle = (TextView) convertView.findViewById( R.id.txt );
      holder.txtAuthor = (TextView) convertView.findViewById( R.id.subtxt1 );
      holder.txtTimeLength = (TextView) convertView.findViewById( R.id.subtxt2 );
      convertView.setTag(holder);
    }
    else {
      holder = (ViewHolderGroup) convertView.getTag();
    }

    // Define the visible elements of a single item inside of our ListView


    //geladen.setTag("ok"+position);
    //Sharp.loadResource(context.getResources(), R.raw.ok).into(geladen);

   // laden.setTag("laden"+position);
   // Sharp.loadResource(context.getResources(), R.raw.laden).into(laden);


    holder.downloadtext.setTag("text"+position);

    final Tour tour = tours.get( position );

    convertView.setBackgroundColor( Color.parseColor( tour.color() ) );

    if(sharedPreferences.getBoolean(tour.slug(), false))
    {
      holder.geladen.setTag("ok"+position);
      holder.laden.setImageDrawable(Sharp.loadResource(context.getResources(), R.raw.ok).getDrawable());
      holder.geladen.setVisibility(View.VISIBLE);
      holder.laden.setVisibility(View.GONE);
      holder.downloadtext.setText("geladen");
      holder.downloadtext.setVisibility(View.VISIBLE);}
    else{holder.geladen.setVisibility(View.GONE);
      holder.laden.setTag("laden"+position);
      holder.laden.setImageDrawable(Sharp.loadResource(context.getResources(), R.raw.laden).getDrawable());
      holder.laden.setVisibility(View.VISIBLE);
      holder.downloadtext.setVisibility(View.GONE);
    }
    BitmapFactory.Options options = new BitmapFactory.Options();
    Bitmap mBitmapInsurance = BitmapFactory.decodeFile(OurStorage.get(context).storagePath()+"/"+OurStorage.get(context).lookForTourFile(((MapsActivity)context).tourlist(),tour.image())+tour.image()+".png" ,options);
    holder.imgAuthor.setImageBitmap(mBitmapInsurance);

    holder.txtTitle.setText( tour.name() );
    SpannableString author = new SpannableString(tour.author() + " ");
    author.setSpan(new StyleSpan(Typeface.BOLD), 0, author.length(), 0);
    holder.txtAuthor.setText(author);
    SpannableString length = new SpannableString(tour.time() + "/" + tour.length() + " ");
    length.setSpan(new StyleSpan(Typeface.BOLD), 0, length.length(), 0);
    holder.txtTimeLength.setText(length);

    holder.laden.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(context instanceof MapsActivity){
          createDownloadDialog("Wollen Sie die Tour »"+ tours.get(position).name() + "« herunterladen? Hinweis: Verwenden Sie Ihr WLAN", tour);
        }}});

    return convertView;
  }

  public void createDownloadDialog(String txt, Tour tour)
  {// Create custom dialog object
    final Dialog dialog = new Dialog(context);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
    SpannableString dialogtext = new SpannableString(txt+" ");
    dialogtext.setSpan(new StyleSpan(Typeface.BOLD), 0, dialogtext.length(), 0);
    text.setText(dialogtext);

    final Tour selectedTour = tour;

    ImageButton okayButton = (ImageButton) dialog.findViewById(R.id.button_dialog);
    Sharp.loadResource(context.getResources(), R.raw.laden).into(okayButton);
    // if decline button is clicked, close the custom dialog
    okayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if(context instanceof MapsActivity)  Updater.get(((MapsActivity)context).getBaseContext()).downloadTourMedia(selectedTour, context);
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

  static class ViewHolderGroup {
    private TextView downloadtext;
    private ImageView geladen;
    private ImageView laden;
    private TextView txtTitle;
    private TextView txtAuthor;
    private TextView txtTimeLength;
    private ImageView imgAuthor;
  }

  static class ViewHolderChild {
    private ImageView btnStart;
    private TextView txtDescription;
  }
 /* // Using an AsyncTask to load the slow images in a background thread
  new AsyncTask<ViewHolder, Void, Bitmap>() {
    private ViewHolder v;

    @Override
    protected Bitmap doInBackground(ViewHolder... params) {
      v = params[0];
      return mFakeImageLoader.getImage();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
      super.onPostExecute(result);
      if (v.position == position) {
        // If this item hasn't been recycled already, hide the
        // progress and set and show the image
        v.progress.setVisibility(View.GONE);
        v.icon.setVisibility(View.VISIBLE);
        v.icon.setImageBitmap(result);
      }
    }
  }.execute(holder);
*/
}

