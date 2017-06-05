package com.uni_wuppertal.iad.vierteltour.utility.xml;

import android.os.Parcel;
import android.os.Parcelable;

import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourListData;

import org.simpleframework.xml.Attribute;

/**
 * Created by Kevin-Laptop on 04.06.2017.
 */

public class Description extends TourListData{

  public Description()
  {super();}

  @Attribute ( required = false )
  private String stopover;

  @Attribute ( required = false )
  private String text;

  @Attribute ( required = false )
  private String note;

  public String stopover()
  {if(stopover != null && !stopover.isEmpty())return stopover;
  else return "";}

  public String text()
  {if(text != null && !text.isEmpty())return text;
  else return "";}

  public String note()
  {if(note!=null && !note.isEmpty())return note;
  else return "";}
}
