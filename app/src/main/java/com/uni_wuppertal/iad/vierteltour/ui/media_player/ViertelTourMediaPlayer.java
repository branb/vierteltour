package com.uni_wuppertal.iad.vierteltour.ui.media_player;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.uni_wuppertal.iad.vierteltour.utility.OurStorage;

import java.io.FileInputStream;
import java.io.IOException;

public class ViertelTourMediaPlayer extends MediaPlayer{
  private static ViertelTourMediaPlayer ourInstance;
  /**
   * Singleton-method
   *
   * @return OurPlayer a static singleton of this class
   */
  public static ViertelTourMediaPlayer getInstance( Context context){
    if( ourInstance == null ){
      ourInstance = new ViertelTourMediaPlayer( context );
    }

    return ourInstance;
  }


  private Context context;
  private VideoView videoview;
  Singletonint singlepage;
  /** The main constructor.
   *
   */
  private ViertelTourMediaPlayer( Context context ){
    this.context = context;
  }


  /**
   * Load an audio file from the application storage. The FileDescriptor approach has been
   * chosen intentionally over just giving our MediaPlayer object the file path, since this seems
   * to circumvent some file permission errors.
   *
   * @param path Path to our audio file, relative to our storage, e.g. "tours/fortschrott/station_1.mp3"
   * @return True if the file could be loaded, false else
   */
  public boolean loadAudio( String path ){

    if( isPlaying() ){
      stop();
      System.out.println("STOP");
    }
    reset();

    FileInputStream inputStream = OurStorage.getInstance( this.context ).getFile( path );

    if( inputStream == null ){
      return false;
    }

    try{
      this.setDataSource( inputStream.getFD() );
      inputStream.close();

      this.prepare();

      return true;
    } catch( IOException e ){
      Log.e( "Mediaplayer", "Failed to open '" + path + "'", e );
      return false;
    }

  }

  public boolean loadVideo( String path , VideoView video){
    String inputStream = OurStorage.getInstance( this.context ).getPathToFile( path );

    if( inputStream == null ){
      return false;
    }
      video.setVideoPath( inputStream );
      resetVideoFrame(video);
      return true;
  }



  public boolean loadGalleryVideo( String path){


    String inputStream = OurStorage.getInstance( this.context ).getPathToFile( path );

    if( inputStream == null ){
      return false;
    }

    videoview.setVideoPath( inputStream );
    return true;
  }



  public void resetVideoFrame(VideoView video)
  {
    video.seekTo(100);
  video.pause();}


  public VideoView getVideoview()
  {return videoview;}


  public void setVideoview(VideoView video)
  {videoview=video;}

  public void pos()
  {System.out.println(videoview.getCurrentPosition());
    System.out.println(singlepage.INSTANCE.getTime());
    System.out.println(singlepage.INSTANCE.getPlaying());}

}

