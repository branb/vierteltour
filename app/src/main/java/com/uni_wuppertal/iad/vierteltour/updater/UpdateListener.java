package com.uni_wuppertal.iad.vierteltour.updater;


import android.content.Context;

/**
 * Adds a couple of callbacks to a class to react on various update related events
 */
public interface UpdateListener{

  void newTourdataAvailable(Context context);

  void noNewTourdataAvailable();

  void tourlistDownloaded(Context context);

  void tourdataDownloaded();
}
