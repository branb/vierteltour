package com.uni_wuppertal.iad.vierteltour.ui.media_player;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import java.io.File;
import java.util.ArrayList;

public class InformationPagerAdapter extends PagerAdapter{

  private Context mContext;
  private StationActivity stationActivity;
  private ArrayList<String> stationImagePaths;
  private ImageView imageView;
  private Intent gallery;
  private boolean fileAvailable=true;
  private ViertelTourMediaPlayer player;
  private Singletonint singlepage;

  public InformationPagerAdapter(Context mContext, ArrayList<String> stationImagePaths , StationActivity stationActivity){
    this.mContext = mContext;
    this.stationImagePaths = stationImagePaths;
    this.stationActivity= stationActivity;
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
    ImageView imageBtn = (ImageView) itemView.findViewById( R.id.img_play_button );
    String resources = stationImagePaths.get(position);     //v für video, i für image

//TODO: stationimagepaths to stationresourcepaths with video and images to show
    if(resources.endsWith("mp4"))
    { imageView.setVisibility(View.VISIBLE);
      if(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position))!=null)
      {Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position)),
        MediaStore.Images.Thumbnails.MINI_KIND);
      imageView.setImageBitmap(thumbnail);}
    else{imageView.setImageResource(R.drawable.i);
      fileAvailable=false;}}

    else if (resources.endsWith("jpg")) {
      imageBtn.setVisibility(View.GONE);
      imageView.setVisibility(View.VISIBLE);
      if(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position))!=null)
      {imageView.setImageURI( Uri.fromFile(new File(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position)))) );}
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
        gallery = new Intent(mContext, GalleryMode.class);
        gallery.putExtra("resources", stationImagePaths);
        gallery.putExtra("station", stationActivity.station);
        gallery.putExtra("video", stationActivity.video);
        singlepage.INSTANCE.position(position);
        stationActivity.startActivityForResult(gallery, 1);
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
}
