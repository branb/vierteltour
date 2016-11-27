package com.uni_wuppertal.iad.vierteltour.ui.map;

import android.util.Log;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.Vector;

/**
 * Class that represents the list of all available tours
 *
 */
@Root( name = "tourlist" )
public class TourList{
  public TourList(){ super(); }

  @ElementList( name = "region", inline = true )
  private List<Region> regions;

  @Attribute()
  private String version;

  public String version() {
    return version;
  }

  public List<Region> regions() {
    return regions;
  }


  /**
   * Set home directories of all children of type TourListData
   *
   * @return TourList this very object itself
   */
  public TourList init( String storagePath ){
    // Set home directories for all objects
    for( Region region : regions ){
      region.home( storagePath );

      for( Area area : region.areas() ){
        area.home( region.home() );

        for( City city : area.cities() ){
          city.home( area.home() );

          for( Tour tour : city.tours() ){
            tour.home( city.home() );
          }
        }
      }
    }


    return this;
  }


  /**
   * Returns the region with the given slug
   *
   * @param slug The sanitized name of the Region
   */
  public Region region( String slug ){
    for( Region region : regions ){
      if( region.slug().equals( slug ) ){
        return region;
      }
    }

    return new Region();
  }


  /**
   * Returns the area with the given slug
   *
   * @param slug The sanitized name of the Area
   */
  public Area area( String slug ){
    for( Region region : regions ){
      for( Area area : region.areas() ){
        if( area.slug().equals( slug ) ){
          return area;
        }
      }
    }

    return new Area();
  }


  /**
   * Returns the city with the given slug
   *
   * @param slug The sanitized name of the City
   */
  public City city( String slug ){
    for( Region region : regions ){
      for( Area area : region.areas() ){
        for( City city : area.cities() ){
          if( city.slug().equals( slug ) ){
            return city;
          }
        }
      }
    }

    return new City();
  }


  /**
   * Returns the tour with the given slug
   *
   * @param slug The sanitized name of the tour
   */
  public Tour tour( String slug ){
    for( Region region : regions ){
      for( Area area : region.areas() ){
        for( City city : area.cities() ){
          for( Tour tour : city.tours() ){
            if( tour.slug().equals( slug ) ){
              return tour;
            }
          }
        }
      }
    }

    return new Tour();
  }


  // TODO: Remove this helper method. Only used during Refactoring
  public List<Tour> tours(){
    List<Tour> tours = new Vector<>();

    for( Region region : regions ){
      for( Area area : region.areas() ){
        for( City city : area.cities() ){
          for( Tour tour : city.tours() ){
            Log.d( "TourList", "Adding tour " + tour.name() );
            tours.add( tour );
          }
        }
      }
    }

    return tours;
  }
}
