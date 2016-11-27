package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.content.Context;

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
import android.widget.VideoView;

import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import java.io.File;
import java.util.ArrayList;

public class InformationPagerAdapter extends PagerAdapter{

  private Context mContext;
  private StationActivity infoAct;
  private Singletonint singlepage;
  private ArrayList<String> stationImagePaths;

  public InformationPagerAdapter(Context mContext, ArrayList<String> stationImagePaths , StationActivity infoAct){
    this.mContext = mContext;
    this.stationImagePaths = stationImagePaths;
    this.infoAct= infoAct;
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
  public Object instantiateItem( ViewGroup container, int position ){
    View itemView = LayoutInflater.from( mContext )
                                  .inflate( R.layout.gallerypageritem, container, false );

    ImageView imageView = (ImageView) itemView.findViewById( R.id.img_pager_item );
    ImageView imageBtn = (ImageView) itemView.findViewById( R.id.img_play_button );
    VideoView videoView = (VideoView) itemView.findViewById( R.id.vid_pager_item );
    char resources = stationImagePaths.get(position).charAt(0);     //v für video, i für image

//TODO: stationimagepaths to stationresourcepaths with video and images to show
    //TODO: HIER WURDE TMP EDITIERT
    if(resources == 'v')
    { videoView.setVideoPath( OurStorage.get(mContext).pathToFile( stationImagePaths.get(position) ) );
     /* videoView.setVisibility(View.VISIBLE);
            videoView.seekTo(100);
      imageBtn.setVisibility(View.VISIBLE);
      imageBtn.setImageResource(R.drawable.play_hell);
    imageView.setVisibility(View.GONE);*/
      imageView.setVisibility(View.VISIBLE);
      Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail( OurStorage.get(mContext).pathToFile( stationImagePaths.get(position) ),
        MediaStore.Images.Thumbnails.MINI_KIND);
      imageView.setImageBitmap(thumbnail);
      }

    else if (resources == 'i') {videoView.setVisibility(View.GONE);
      imageBtn.setVisibility(View.GONE);
      imageView.setVisibility(View.VISIBLE);
      imageView.setImageURI( Uri.fromFile( new File( OurStorage.get(mContext).pathToFile( stationImagePaths.get(position)) ) ) );}

    container.addView( itemView );

    imageView.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        infoAct.vf.setDisplayedChild(1);
        singlepage.INSTANCE.setPage(1);
      }
    });

    videoView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        infoAct.vf.setDisplayedChild(1);
        singlepage.INSTANCE.setPage(1);
      }
    });


    return itemView;
  }

  @Override
  public void destroyItem( ViewGroup container, int position, Object object ){
    container.removeView( (LinearLayout) object );
  }
}
