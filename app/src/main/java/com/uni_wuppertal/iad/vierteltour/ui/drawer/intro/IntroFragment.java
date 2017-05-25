package com.uni_wuppertal.iad.vierteltour.ui.drawer.intro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;

/**
 * Created by Kevin on 25.05.2017.
 */

public class IntroFragment extends Fragment {

  public static IntroFragment create(int position){
    IntroFragment fragment = new IntroFragment();
    /*arguments = new Bundle();
    arguments.putInt( ARG_PAGE_NUMBER, station.number() );
    fragment.setArguments( arguments );*/
    return fragment;
  }

  @Override
  public void onDestroyView(){super.onDestroyView();}

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
    View itemView = inflater.inflate( R.layout.intro_single, container, false );
    TextView topic = (TextView)itemView.findViewById(R.id.topic);
    TextView welcome = (TextView)itemView.findViewById(R.id.welcome);
    TextView desc = (TextView)itemView.findViewById(R.id.description);
    ImageView img = (ImageView) itemView.findViewById(R.id.intro_image);
    ImageButton skip = (ImageButton) itemView.findViewById(R.id.skip);
    topic.setText("Einführung");
    if(position==0)
    {
      welcome.setText("Willkommen!");
      desc.setText("Diese App nimmt Sie mit auf informative Stadtspaziergänge durch die dichte Ökologie- und Kulturlandschaft der Wuppertaler Innenstadt. Begleitet werden Sie dabei von unterschiedlichen Expertinnen und Experten.");
      img.setImageResource(R.drawable.hello_01);
      Sharp.loadResource(context.getResources(), R.raw.skip).into(skip);}

    if(position==1)
    { welcome.setText("Standortdienste aktivieren");
      desc.setText("Wir haben ViertelTour bewusst so konzipiert, dass die Spaziergänge tatsächlich nur erlebt werden können, wenn Sie sich genau dort befinden, wo die entsprechende Expertin oder der entsprechende Experte Sie mit auf den Weg nehmen möchte. Aktivieren Sie daher bitte Ihre Standortdienste.");
      img.setImageResource(R.drawable.gps_02);
      Sharp.loadResource(context.getResources(), R.raw.skip).into(skip);}

    else if(position==2)
    {welcome.setText("Tourdaten herunterladen");
      desc.setText("Um eine Tour zu erleben, muss diese erst heruntergeladen werden. Am besten laaden Sie sich die Touren im WLAN-Netzwerk herunter bevor Sie losgehen. Es werden pro Tour etwa 50-200 MB Daten geladen.");
      img.setImageResource(R.drawable.laden_03);
      Sharp.loadResource(context.getResources(), R.raw.fertig).into(skip);}

    return itemView;
  }

}
