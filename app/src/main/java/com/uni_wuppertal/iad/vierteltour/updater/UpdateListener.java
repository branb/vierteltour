package com.uni_wuppertal.iad.vierteltour.updater;


/**
 * Adds a couple of callbacks to a class to react on various update related events
 */
public interface UpdateListener{

  void newTourdataAvailable();

  void noNewTourdataAvailable();

  void tourlistDownloaded();

  void tourdataDownloaded();
}
