package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.content.Context;
import android.content.res.Configuration;
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
import android.widget.VideoView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import java.io.File;
import java.util.ArrayList;

public class GalleryPagerAdapter extends PagerAdapter {

  private Context mContext;
  private ArrayList<String> stationImagePaths;
  private ImageView imageView;
  private ViertelTourMediaPlayer player;
  private GalleryMode gallery;
  private VideoView videoView;

  public GalleryPagerAdapter(Context mContext, ArrayList<String> stationImagePaths, GalleryMode gallery){
    this.mContext = mContext;
    this.stationImagePaths = stationImagePaths;
    this.gallery = gallery;
    player = ViertelTourMediaPlayer.getInstance( gallery );
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
    ImageView imageBtn = (ImageView) itemView.findViewById( R.id.img_play_button_gallery );
    videoView = (VideoView) itemView.findViewById( R.id.vid_pager_item_gallery );
    String resources = stationImagePaths.get(position);     //v für video, i für image

//TODO: stationimagepaths to stationresourcepaths with video and images to show
    //TODO: HIER WURDE TMP EDITIERT
    if(resources.endsWith("mp4"))
    { videoView.setVideoPath(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position)));
      videoView.setVisibility(View.GONE);
      imageView.setVisibility(View.VISIBLE);
      Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position)),
        MediaStore.Images.Thumbnails.MINI_KIND);
      imageView.setImageBitmap(thumbnail);
      player.setVideoview(videoView);

    /*  videoView.setOnTouchListener( new View.OnTouchListener(){
        @Override
        public boolean onTouch( View v, MotionEvent motionEvent ){
          System.out.println("Touch");
          if( player.getVideoview().isPlaying() && gallery.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            gallery.startvideo = false;
            player.getVideoview().pause();
            gallery.play_buttonGallery.setImageResource( R.drawable.play_hell );
          }
          else if(gallery.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE && player.getVideoview() != null)
          {gallery.mediaplayerbars();
          }
          else {
            gallery.startvideo = true;
            player.getVideoview().start();
            gallery.play_buttonGallery.setImageResource( R.drawable.stop_hell );
            gallery.seekUpdationVideo();}
          return true;
        }
      });*/

    }

    else if (resources.endsWith("jpg")) {
      videoView.setVisibility(View.GONE);
      imageBtn.setVisibility(View.GONE);
      imageView.setVisibility(View.VISIBLE);
      imageView.setImageURI( Uri.fromFile( new File(OurStorage.get(mContext).pathToFile(stationImagePaths.get(position))) ) );}

    container.addView( itemView );


    imageView.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {if(gallery.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
      {gallery.mediaplayerbars();}
        else{imageView.setVisibility(View.GONE);}
      }});

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



  /* mAdapter.getImageView().setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        startvideo = true;
        player.getVideoview().setVisibility(View.VISIBLE);
        mAdapter.hideImage();
        player.getVideoview().start();
        play_buttonGallery.setImageResource( R.drawable.stop_hell );
        seekUpdationVideo();
      }
    });
*/
}
