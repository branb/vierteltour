package com.uni_wuppertal.iad.vierteltour.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.ui.media_player.ViertelTourMediaPlayer;
import com.uni_wuppertal.iad.vierteltour.ui.station.Stationbeendet;
import com.uni_wuppertal.iad.vierteltour.utility.storage.Singletonint;
import com.uni_wuppertal.iad.vierteltour.utility.storage.OurStorage;
import com.uni_wuppertal.iad.vierteltour.utility.xml.Resource;

import java.io.File;
import java.util.ArrayList;

/**
 * Adapter for ViewPager in Gallery
 */

public class GalleryPagerAdapter extends PagerAdapter {

  private Context mContext;
  private ArrayList<Resource> stationImagePaths;
  private SubsamplingScaleImageView imageView;
  private ImageView play, background;
  private ViertelTourMediaPlayer player;
  private VideoView videoView;
  private ViewGroup ownContainer;
  private int STATION_BEENDET=1;
  private Singletonint singlepage;

  public GalleryPagerAdapter(Context mContext, ArrayList<Resource> stationImagePaths){
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

    play = (ImageView) itemView.findViewById(R.id.img_play_button_gallery);
    play.setTag("play"+position);
    background = (ImageView) itemView.findViewById(R.id.img_pager_background_gallery);
    background.setTag("background"+position);
    imageView = (SubsamplingScaleImageView) itemView.findViewById( R.id.img_pager_item_gallery );
    imageView.setTag("image" + position);
    imageView.setParallelLoadingEnabled(true);
    //imageBtn = (ImageView) itemView.findViewById( R.id.img_play_button_gallery );
    //imageBtn.setTag("button" + position);
    videoView = (VideoView) itemView.findViewById( R.id.vid_pager_item_gallery );
    videoView.setTag("video" + position);
    final Resource resources = stationImagePaths.get(position);     //v für video, i für image

    //Layout for Videos
    if(resources.getSource().endsWith("mp4"))
    {
      Sharp.loadResource(mContext.getResources(), R.raw.play_hell).into(play);

      videoView.setVisibility(View.GONE);
      play.setVisibility(View.VISIBLE);
      background.setVisibility(View.VISIBLE);
      imageView.setVisibility(View.VISIBLE);
      if(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position).getSource())!=null)
      {videoView.setVideoPath(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position).getSource()));
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position).getSource()),
        MediaStore.Images.Thumbnails.MINI_KIND);
        if(thumbnail!=null)imageView.setImage(ImageSource.bitmap(thumbnail));
        }
      else{imageView.setImage(ImageSource.resource(R.drawable.i));}


      videoView.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
         if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && videoView != null)
          {((GalleryMode)mContext).mediaplayerbars();}

          return false;
        }
      });

      videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
          ((GalleryMode)mContext).stopVideoplay();
          if(!singlepage.INSTANCE.isAudio()){
            Intent background = new Intent((mContext).getApplicationContext(), Stationbeendet.class);
            if(((GalleryMode)mContext).size==(((GalleryMode)mContext).number)){background.putExtra("vergleich", 1);}
            else {background.putExtra("vergleich", 0);}
            ((GalleryMode)mContext).startActivityForResult(background, STATION_BEENDET);
            ((GalleryMode)mContext).overridePendingTransition(0, 0);
          }
          showVideoThumbnail(position);
        }
      });

    }
    //Layout for Images
    else if (resources.getSource().endsWith("jpg")) {
      play.setVisibility(View.GONE);
      background.setVisibility(View.GONE);
      videoView.setVisibility(View.GONE);
      imageView.setVisibility(View.VISIBLE);
      if(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position).getSource())!=null)
      {
        imageView.setImage(ImageSource.uri( Uri.fromFile(new File(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position).getSource()))) ));
      }
      else{imageView.setImage(ImageSource.resource(R.drawable.i));}}



    play.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        hideVideoThumbnail(position);
        ((GalleryMode)mContext).startVideoplay();
      }
    });

    container.addView( itemView );
    ownContainer = container;

    imageView.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if(resources.getSource().endsWith("mp4")){
          if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
          {((GalleryMode)mContext).mediaplayerbars();}
        else{
            play.setVisibility(View.GONE);
            background.setVisibility(View.GONE);
            ((GalleryMode)mContext).startVideoplay();}

        } else if (resources.getSource().endsWith("jpg")) {
          if(mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
          {((GalleryMode)mContext).imageBar();}
        }

      }});
    if(singlepage.INSTANCE.position()==position && resources.getSource().endsWith("mp4"))
    {hideVideoThumbnail(position);
      ((GalleryMode)mContext).startVideoplay();}



    return itemView;
  }


  @Override
  public void destroyItem( ViewGroup container, int position, Object object ){
    SubsamplingScaleImageView i = (SubsamplingScaleImageView) container.findViewWithTag("image"+position);
    i.recycle();
    container.removeView( (RelativeLayout) object );
  }

  //Images can only be found with GalleryActivity by using their tags
  public void showImage(int position)
  {//System.out.println(ownContainer.findViewWithTag("image" + position));

    ownContainer.findViewWithTag("image" + position).setVisibility(View.VISIBLE);
   // if(stationImagePaths.get(position).endsWith("mp4"))
   //ownContainer.findViewWithTag("button" + position).setVisibility(View.VISIBLE);
  }

  public void hideImage(int position)
  {ownContainer.findViewWithTag("image" + position).setVisibility(View.GONE);
  // ownContainer.findViewWithTag("button" + position).setVisibility(View.GONE);
  }

  public void showVideoThumbnail(int position)
  {ownContainer.findViewWithTag("image" + position).setVisibility(View.VISIBLE);
    ownContainer.findViewWithTag("play" + position).setVisibility(View.VISIBLE);
    ownContainer.findViewWithTag("background" + position).setVisibility(View.VISIBLE);}

  public void hideVideoThumbnail(int position)
  {ownContainer.findViewWithTag("image" + position).setVisibility(View.GONE);
    ownContainer.findViewWithTag("play" + position).setVisibility(View.GONE);
    ownContainer.findViewWithTag("background" + position).setVisibility(View.GONE);}

  public VideoView videoView(int position)
  {
    return (VideoView) ownContainer.findViewWithTag("video" + position);}

  public ViewGroup container()
  {return ownContainer;}

  public SubsamplingScaleImageView getImageView()
  {return imageView;}

  public void unzoomImageView(int position)
  {SubsamplingScaleImageView i = (SubsamplingScaleImageView) ownContainer.findViewWithTag("image"+position);
    i.recycle();
    if(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position).getSource())!=null)
    {i.setImage(ImageSource.uri( Uri.fromFile(new File(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position).getSource()))) ));}}
}
