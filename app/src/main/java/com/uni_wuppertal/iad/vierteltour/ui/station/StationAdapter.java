package com.uni_wuppertal.iad.vierteltour.ui.station;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;

import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.gallery.GalleryMode;
import com.uni_wuppertal.iad.vierteltour.ui.map.MapsActivity;
import com.uni_wuppertal.iad.vierteltour.utility.Singletonint;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.ViertelTourMediaPlayer;
import com.uni_wuppertal.iad.vierteltour.utility.storage.OurStorage;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Station;

import java.io.File;
import java.util.ArrayList;

/**
 * Adapter for ViewPager in StationActivity
 */
public class StationAdapter extends PagerAdapter{

  private Context mContext;
  private ArrayList<String> stationImagePaths;
  private ImageView imageView, background;
  private Intent gallery;
  private boolean fileAvailable=true;
  private ViertelTourMediaPlayer player;
  private Singletonint singlepage;

  public StationAdapter(Context mContext, ArrayList<String> stationImagePaths){
    this.mContext = mContext;
    this.stationImagePaths = stationImagePaths;
  }

  @Override
  public int getCount(){
    return stationImagePaths.size();
  }

  @Override
  public boolean isViewFromObject( View view, Object object ){
    return view == object;
  }


  @Override
  public Object instantiateItem( ViewGroup container,final int position ){
    View itemView = LayoutInflater.from( mContext )
      .inflate( R.layout.pageritem, container, false );

    imageView = (ImageView) itemView.findViewById( R.id.img_pager_item );
    background = (ImageView) itemView.findViewById( R.id.img_pager_background);
   // ImageView imageBtn = (ImageView) itemView.findViewById( R.id.img_play_button );
    String resources = stationImagePaths.get(position);     //v für video, i für image

//TODO: stationimagepaths to stationresourcepaths with video and images to show
    if(resources.endsWith("mp4"))
    { imageView.setVisibility(View.VISIBLE);

      if(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position))!=null)
      {Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position)),
        MediaStore.Images.Thumbnails.MINI_KIND);
      imageView.setImageBitmap(thumbnail);
        background.setVisibility(View.VISIBLE);
        //imageBtn.setVisibility(View.VISIBLE);
      }
    else{imageView.setImageResource(R.drawable.i);
      fileAvailable=false;}}

    else if (resources.endsWith("jpg")) {
    //  imageBtn.setVisibility(View.GONE);
      imageView.setVisibility(View.VISIBLE);
      if(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position))!=null)
      {imageView.setImageURI( Uri.fromFile(new File(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position)))) );
        background.setVisibility(View.VISIBLE);}
      else{imageView.setImageResource(R.drawable.i);
        fileAvailable=false;}
  }

    container.addView( itemView );

    if(fileAvailable)
    {imageView.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        startGallery();
      }
    });
      background.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          startGallery();
        }
      });}

    return itemView;
  }

  @Override
  public void destroyItem( ViewGroup container, int position, Object object ){
    container.removeView( (RelativeLayout) object );
  }

  public void showImage()
  {imageView.setVisibility(View.VISIBLE);}

  public void hideImage()
  {imageView.setVisibility(View.GONE);}

  public ImageView getImageView()
  {return imageView;}

  public void startGallery()
  {((MapsActivity)mContext).startGallery();}
}
