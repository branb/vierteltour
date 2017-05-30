package com.uni_wuppertal.iad.vierteltour.utility.xml;

import com.uni_wuppertal.iad.vierteltour.utility.tourlist.TourListData;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Kevin on 30.05.2017.
 */
@Root( name = "resource" )
public class Resource extends TourListData {

  public Resource()
  {super();}

  @Attribute
  private String time;

  @Attribute
  private String title;

  @Element
  private String source;



  public String time(){
    return time;
  }

  public String title(){
    return title;
  }

  public String source(){
    if(source!=null)return source;
    else return "";
  }



}
