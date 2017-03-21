package com.uni_wuppertal.iad.vierteltour.ui.map.Marker;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.uni_wuppertal.iad.vierteltour.R;

/**
 * Created by Kevin-Laptop on 12.01.2017.
 * Stellt ein nicht vorhandenes InfoFenster in den Vordergrund mit dem Marker, damit der Marker hervorgehoben wird.
 */

public class MapWindowAdapter implements GoogleMap.InfoWindowAdapter {
  private Context context = null;

  public MapWindowAdapter(Context context) {
    this.context = context;
  }

  // Hack to prevent info window from displaying: use a 0dp/0dp frame
  @Override
  public View getInfoWindow(Marker marker) {
    View v = ((Activity) context).getLayoutInflater().inflate(R.layout.no_info_window, null);
    return v;
  }

  @Override
  public View getInfoContents(Marker marker) {
    return null;
  }
}
