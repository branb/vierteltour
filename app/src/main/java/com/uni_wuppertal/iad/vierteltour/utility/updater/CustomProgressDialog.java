package com.uni_wuppertal.iad.vierteltour.utility.updater;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixplicity.sharp.Sharp;
import com.thin.downloadmanager.ThinDownloadManager;
import com.uni_wuppertal.iad.vierteltour.R;

/**
 * Created by Kevin on 23.05.2017.
 */

public class CustomProgressDialog extends ProgressDialog {
  private TextView title, text;
  private ProgressBar progressBar;
  private ImageButton btnx;
  private ThinDownloadManager downloadManager;


  public CustomProgressDialog(Context context, ThinDownloadManager dm) {
    super(context);
    downloadManager = dm;
    }

  @Override
  public void onBackPressed() {
    downloadManager.cancelAll();
    super.onBackPressed();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.custom_progress_dialog);

    title = (TextView) findViewById(R.id.progress_dialog_title);
    text = (TextView) findViewById(R.id.progress_dialog_text);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    btnx = (ImageButton) findViewById(R.id.progress_dialog_btnx);
    Sharp.loadResource(getContext().getResources(), R.raw.beenden_dunkel).into(btnx);

  }

  public void setProgress(int progress)
  {progressBar.setProgress(progress);}

  public int getProgress()
  {return progressBar.getProgress();}

  public ProgressBar getProgressBar()
  {return progressBar;}

  public void setText(String txt)
  {text.setText(txt);}

  public void setTextTitle(String title)
  {this.title.setText(title);}

  public ImageButton getBtnx()
  {return btnx;}
}
