package com.uni_wuppertal.iad.vierteltour.ui.drawer.intro;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.uni_wuppertal.iad.vierteltour.R;


/**
 * Created by Kevin on 25.05.2017.
 */

public class IntroAdapter extends PagerAdapter {
  private Context context;

  public IntroAdapter(Context mContext){
    this.context = mContext;
  }

  @Override
  public int getCount(){
    return 3;
  }

  @Override
  public boolean isViewFromObject( View view, Object object ){
    return view == object;
  }


  @Override
  public Object instantiateItem( ViewGroup container,final int position ) {
    View itemView = LayoutInflater.from( context )
      .inflate( R.layout.intro_single, container, false );

    TextView topic = (TextView)itemView.findViewById(R.id.topic);
    TextView welcome = (TextView)itemView.findViewById(R.id.welcome);
    TextView desc = (TextView)itemView.findViewById(R.id.description);
    ImageView img = (ImageView) itemView.findViewById(R.id.intro_image);

    SpannableString top = new SpannableString("Einführung ");
    top.setSpan(new StyleSpan(Typeface.BOLD), 0, top.length(), 0);
    topic.setText(top);
    if(position==0)
    { welcome.setText("Willkommen!");
      desc.setText("Diese App nimmt Sie mit auf informative Stadtspaziergänge durch die dichte Ökologie- und Kulturlandschaft der Wuppertaler Innenstadt. Begleitet werden Sie dabei von unterschiedlichen Expertinnen und Experten.");
      img.setImageResource(R.drawable.hello_01);
    }

    if(position==1)
    { welcome.setText("Standortdienste aktivieren");
      desc.setText("Wir haben ViertelTour bewusst so konzipiert, dass die Spaziergänge tatsächlich nur erlebt werden können, wenn Sie sich genau dort befinden, wo die entsprechende Expertin oder der entsprechende Experte Sie mit auf den Weg nehmen möchte. Aktivieren Sie daher bitte Ihre Standortdienste.");
      img.setImageResource(R.drawable.gps_02);}

    else if(position==2)
    { welcome.setText("Tourdaten herunterladen");
      desc.setText("Um eine Tour zu erleben, muss diese erst heruntergeladen werden. Am besten laaden Sie sich die Touren im WLAN-Netzwerk herunter bevor Sie losgehen. Es werden pro Tour etwa 50-200 MB Daten geladen.");
      img.setImageResource(R.drawable.laden_03);}

    container.addView(itemView);

    return itemView;
  }

  @Override
  public void destroyItem( ViewGroup container, int position, Object object ){
    container.removeView( (RelativeLayout) object );
  }
}
