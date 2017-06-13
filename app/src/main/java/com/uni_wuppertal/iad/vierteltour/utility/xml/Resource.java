package com.uni_wuppertal.iad.vierteltour.utility.xml;

import android.os.Parcel;
import android.os.Parcelable;

import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourListData;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Kevin on 30.05.2017.
 */
@Root( name = "resource" )
public class Resource extends TourListData implements Parcelable {

  public Resource()
  {super();}

  @Attribute ( required = false )
  private String time;

  @Attribute ( required = false )
  private String title;

  @Attribute
  private String source;



  public String time(){
    if(time==null) return "";
    return time;
  }

  public String title(){
    if(title==null) return "";
    return title;
  }

  public String source(){
    if(source==null)return "";
    else if(source.contains(home())) {return source;}
    else {return home() + source;}
  }

  public void setSource(String newSource)
  {source=newSource;}

  public String getSource()
  {return source;}

  public Resource(Parcel in){
    this.source = in.readString();
    this.title = in.readString();
    this.time = in.readString();
  }


  public int describeContents(){
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.source);
    dest.writeString(this.title);
    dest.writeString(this.time);

  }
  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public Resource createFromParcel(Parcel in) {
      return new Resource(in);
    }

    public Resource[] newArray(int size) {
      return new Resource[size];
    }

  };

}
