package com.uni_wuppertal.iad.vierteltour.utility.tourlist;

import org.simpleframework.xml.Attribute;

public class TourListData{

  @Attribute
  private String name = "";

  @Attribute
  private String slug = "";

  private String home;


  /**
   * Returns the name of the data node
   *
   * @return String The name of the data node
   */
  public String name(){
    return name;
  }


  /**
   * Returns the sanitized name of the data node
   *
   * @return String The sanitized name of the data node
   */
  public String slug(){
    if(slug!=null)
    return slug;
    else
      return null;
  }


  /**
   * Returns the full path to the home directory
   *
   * @return String Absolute path to the home directory
   */
  public String home(){
    return home + "/";
  }


  /**
   * Sets the home directory. It will be the combination of the parentPath and the slug.
   *
   * Example:
   * parentPath: /storage/emulated/0/Android/data/com.uni_wuppertal.iad.vierteltour/files
   * slug:       vierteltour
   *
   * Home directory will then become:
   * /storage/emulated/0/Android/data/com.uni_wuppertal.iad.vierteltour/files/vierteltour
   *
   *
   * @return TourListData this very object
   */
  public TourListData home( String parentPath ){
    home = parentPath + "/" + slug;

    return this;
  }

}
