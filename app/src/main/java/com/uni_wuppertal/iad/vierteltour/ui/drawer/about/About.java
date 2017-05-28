package com.uni_wuppertal.iad.vierteltour.ui.drawer.about;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;

/**
 * Created by Kevin-Laptop on 17.01.2017.
 * Activity only shows about text.
 */

public class About extends AppCompatActivity {
  private ActionBar actionBar;
  private TextView title;
  private ImageButton xbtn;
  @Override
  protected void onCreate(
    @Nullable
      Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.about );
    initActionbar();
    TextView titleViertelTour = (TextView) findViewById(R.id.title_viertel);
    titleViertelTour.setText("Was ist ViertelTour?");

    TextView textViertelTour = (TextView) findViewById(R.id.text_viertel);

    textViertelTour.setText(Html.fromHtml( "<br />"
      + "Die App "+ "<b>" + "ViertelTour" + "</b>" + " nimmt Sie mit auf informative Stadtspaziergänge durch die dichte Ökologie- und Kulturlandschaft der Wuppertaler Innenstadt. Begleitet werden Sie dabei von unterschiedlichsten Expertinnen und Experten."
      + "<br />" + "<br />" + "Es werden keine Sight-Seeing-Highlights vermittelt, sondern viel mehr Orte und Dinge an denen wir täglich vorüber gehen ohne sie zu bemerken. Zu diesen unscheinbaren Orten lässt sich bemerkenswertes Wissen abrufen − wenn man die richtigen Fragen stellt."
      +"Und das haben etwa 20 Studierende der Fakultät für Kunst und Design in den " + "<b>" +"Projektseminaren Design Interaktiver Medien"+ "</b>" + " und " + "<b>" + "Tontechnik" + "</b>" + " an der " + "<b>" + "Bergischen Universität Wuppertal" + "</b>" + " getan."
      + "<br />" + "<br />" + " Um die Audio, Bild, und Video-Daten der jeweiligen Touren hören und sehen zu können," + "<b>" + " müssen Sie sich vor Ort befinden!" + "</b>" + "<br />" + "<br />"
      + "Am besten" + "<b>" + " laden Sie sich die Touren im WLAN-Netzwerk herunter bevor Sie losgehen." + "</b>" + " Es werden pro Tour etwa 50-200 MB Daten geladen." + "<br />" + "<br />"
      + "Um Wuppertal vor Ort zu erfahren, haben wir ViertelTour bewusst so konzipiert, dass die Spaziergänge tatsächlich nur erlebt werden können, wenn Sie sich genau dort befinden, wo die entsprechende Expertin oder der entsprechende Experte Sie mit auf den Weg nehmen möchte."
      +" Sie haben in der App die Möglichkeit sich von Ihrem Standort aus "+"<b>"+"via GPS an die entsprechenden Orte navigieren zu lassen."+"</b>"+"<br />"+"<br />"
      + "Die Touren sind so aufgebaut, dass Sie der Expertin oder dem Experten einfach von Station zu Station folgen könen. Wenn Sie dort angekommen sind, werden Ihnen die Daten der enstprechenden Station zur Verfügung gestellt."+ "<br />" + "<br />"
      + "Wir wünschen viel Spaß und hoffentlich neue Einsichten und Erfahrungen in einer hochinteressanten Stadt."+ "<br />" + "<br />" + "Projektgruppe Design Interaktiver Medien, Bergische Universität Wuppertal, Prof. Kristian Wolf"+ "<br />" + "<br />"
    ));

    TextView titleMitwirkende = (TextView) findViewById(R.id.title_mitwirkende);
    titleMitwirkende.setText("Wer hat mitgewirkt?");

    TextView textMitwirkende = (TextView) findViewById(R.id.text_mitwirkende);

    textMitwirkende.setText(Html.fromHtml( "<br />"
      + "Die App ViertelTour entstand im Sommersemester 2015 im Projektseminar Design Interaktiver Medien unter der Leitung von Prof. Kristian Wolf. Fertiggestellt wurde sie im Wintersemester 2016/2017."+"<br />" +"<br />"
      +"<b>"+"Idee und Grundkonzept"+"</b>"+"<br />" +"Kristian Wolf, Laura Poplow, Nikolaus Zunner"+"<br />"+"<br />"+"<b>"+"Konzeption und Screen Design"+"</b>"+"<br />"+"Muriel Balzer, Tamara Dreke, Franziska Györfi, Ana Ivić"+"<br />"+"<br />"
      +"<b>"+"Bild und Ton"+"</b>"+"<br />"+"Studierende des Seminars Licht/Ton, Design Ausdiovisueller Medien unter der Leitung von AOR Till Müller"+"<br />"+"<br />"+"<b>"+"Aufbereitung der Daten"+"</b>"+"<br />"+"Benjamin Pfingsten, Svenja Krautwurst, Jessica Schmitz"
      +"<br />"+"<br />"+"<b>" + "Android Programmierung" + "</b>" +"<br />" +"<u>"+"Softwaretechnik-Praktikum"+"</u>"+"<br />"+"Miodrag Radovanovic, Jannis Güldenpfennig, Michel Verheijen, Thomas Vogt, Branislav Bardak, Kevin Kollek"+"<br />"+"<br />"+"<u>"
      +"Bachelor-Thesis, Betreuung Dr. Werner Hofschuster"+"</u>"+"<br />" + "Kevin Kollek"+"<br />"+"<br />"
      +"<b>"+"iOS Programmierung"+"</b>"+"<br />"+"Noah Lotz-Hommann"+"<br />"+"<br />"+"<b>"+"Projektkoordination"+"</b>"+"<br />"+"Dr. Christoph Rodatz"+"<br />"+"<br />"));


    TextView titleDanksagung = (TextView) findViewById(R.id.title_danksagung);
    titleDanksagung.setText("Danksagung");

    TextView textDanksagung = (TextView) findViewById(R.id.text_danksagung);
    textDanksagung.setText(Html.fromHtml( "<br />" + "Wir danken " + "<b>" + "allen Expertinnen und Experten" + "</b>" + ", die mit ihrem hier preisgegebenen Wissen dazu beitragen, die Stadt Wupertal in vielen, erst auf den zweiten Blick erkennbaren Facetten erfahrbar zu machen."
      +"<br />"+"<br />"+ "Vielen Dank an die "+"<b>" + "Jackstädtstiftung" + "</b>" + ", die das Projekt finanziell unterstützt hat."
    ));

   /*



    Wir danken allen Expertinnen und Experten, die mit ihrem hier preisgegebenen Wissen dazu beitragen, die Stadt Wupertal in vielen, erst auf den zweiten Blick erkennbaren Facetten erfahrbar zu machen.

      Vielen Dank an die Jackstädtstiftung, die das Projekt finanziell unterstützt hat."
*/
  }
  public void initActionbar()
  {actionBar = getSupportActionBar();

    actionBar.setDisplayShowHomeEnabled( false );
    actionBar.setDisplayShowTitleEnabled( false );
    actionBar.setDisplayHomeAsUpEnabled( false );
    actionBar.setHomeButtonEnabled( false );
    actionBar.setDisplayShowCustomEnabled( true );     //Deaktiviert alle Buttons und setzt CostumActionBar
    View view = getLayoutInflater().inflate( R.layout.toolbar, null );
    android.support.v7.app.ActionBar.LayoutParams layoutParams = new android.support.v7.app.ActionBar.LayoutParams( android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT, android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT );
    actionBar.setCustomView( view, layoutParams );
    Toolbar parent = (Toolbar) view.getParent();
    parent.setContentInsetsAbsolute( 0, 0 );           //Vermeidet Fehler, dass CostumActionBar zu schmal wird
    actionBar.setElevation( 0 );


    xbtn = (ImageButton) findViewById(R.id.btn_x);      //ActionBar Button: Right
    title = (TextView) findViewById(R.id.toolbar_title);  //ActionBar Title
    title.setText("About");
    xbtn.setOnClickListener( new View.OnClickListener(){
      @Override
      public void onClick( View v ){
        onBackPressed();
      }
    });
    Sharp.loadResource(getResources(), R.raw.beenden_dunkel).into(xbtn);

    title.setVisibility(View.VISIBLE);
    xbtn.setVisibility(View.VISIBLE);
    title.setTypeface(Typeface.MONOSPACE);

  }
}
