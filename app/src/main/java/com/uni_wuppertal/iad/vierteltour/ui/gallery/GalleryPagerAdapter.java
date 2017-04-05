package com.uni_wuppertal.iad.vierteltour.ui.gallery;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.ViertelTourMediaPlayer;
import com.uni_wuppertal.iad.vierteltour.utility.Singletonint;
import com.uni_wuppertal.iad.vierteltour.utility.storage.OurStorage;

import java.io.File;
import java.util.ArrayList;

/**
 * Adapter for ViewPager in Gallery
 */

public class GalleryPagerAdapter extends PagerAdapter {

  private Context mContext;
  private ArrayList<String> stationImagePaths;
  private ImageView imageView/*, imageBtn*/;
  private ViertelTourMediaPlayer player;
  private VideoView videoView;
  private ViewGroup ownContainer;
  private Singletonint singlepage;

  public GalleryPagerAdapter(Context mContext, ArrayList<String> stationImagePaths){
    this.mContext = mContext;
    this.stationImagePaths = stationImagePaths;
    player = ViertelTourMediaPlayer.getInstance( mContext );
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
  public Object instantiateItem(ViewGroup container, final int position ){
    View itemView = LayoutInflater.from( mContext )
      .inflate( R.layout.gallerypageritem, container, false );

    imageView = (ImageView) itemView.findViewById( R.id.img_pager_item_gallery );
    imageView.setTag("image" + position);
    //imageBtn = (ImageView) itemView.findViewById( R.id.img_play_button_gallery );
    //imageBtn.setTag("button" + position);
    videoView = (VideoView) itemView.findViewById( R.id.vid_pager_item_gallery );
    videoView.setTag("video" + position);
    final String resources = stationImagePaths.get(position);     //v für video, i für image

    //Layout for Videos
    if(resources.endsWith("mp4"))
    { videoView.setVisibility(View.GONE);
      imageView.setVisibility(View.VISIBLE);
      if(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position))!=null)
      {videoView.setVideoPath(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position)));
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position)),
        MediaStore.Images.Thumbnails.MINI_KIND);
        imageView.setImageBitmap(thumbnail);
        //imageBtn.setVisibility(View.VISIBLE);
        player.setVideoview(videoView);}
      else{imageView.setImageResource(R.drawable.i);}


      videoView.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
         if(mContext.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE && player.getVideoview() != null)
          {((GalleryMode)mContext).mediaplayerbars();}

          return false;
        }
      });

    /*  videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
          ((GalleryMode)mContext).stopVideoplay();
        }
      });*/

    }
    //Layout for Images
    else if (resources.endsWith("jpg")) {
      videoView.setVisibility(View.GONE);
     // imageBtn.setVisibility(View.GONE);
      imageView.setVisibility(View.VISIBLE);
      System.out.println(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position)));
      if(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position))!=null)
      {imageView.setImageURI( Uri.fromFile(new File(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position)))) );}
      else{imageView.setImageResource(R.drawable.i);}}

    container.addView( itemView );
    ownContainer = container;

   /* imageBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ((GalleryMode)mContext).startVideoplay();
      }
    });*/

    imageView.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if(resources.endsWith("mp4")){
          if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
          {((GalleryMode)mContext).mediaplayerbars();}
        else{((GalleryMode)mContext).startVideoplay();}

        } else if (resources.endsWith("jpg")) {
          if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
          {((GalleryMode)mContext).imageBar();}
        }

      }});
    if(singlepage.INSTANCE.position()==position && resources.endsWith("mp4"))
      ((GalleryMode)mContext).startVideoplay();

    return itemView;
  }


  @Override
  public void destroyItem( ViewGroup container, int position, Object object ){
    container.removeView( (RelativeLayout) object );
  }

  //Images can only be found with GalleryAcivity by using their tags
  public void showImage(int position)
  {System.out.println(ownContainer.findViewWithTag("image" + position));

    ownContainer.findViewWithTag("image" + position).setVisibility(View.VISIBLE);
   // if(stationImagePaths.get(position).endsWith("mp4"))
   //ownContainer.findViewWithTag("button" + position).setVisibility(View.VISIBLE);
  }

  public void hideImage(int position)
  {ownContainer.findViewWithTag("image" + position).setVisibility(View.GONE);
  // ownContainer.findViewWithTag("button" + position).setVisibility(View.GONE);
  }

  public VideoView videoView(int position)
  {
    return (VideoView) ownContainer.findViewWithTag("video" + position);}

  public ViewGroup container()
  {return ownContainer;}

  public ImageView getImageView()
  {return imageView;}

}
