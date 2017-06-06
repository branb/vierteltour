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
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.pixplicity.sharp.Sharp;
import com.uni_wuppertal.iad.vierteltour.R;
import com.uni_wuppertal.iad.vierteltour.utility.font.CustomFontTextView;

/**
 * Created by Kevin-Laptop on 17.01.2017.
 * Activity only shows about text.
 */

public class About extends AppCompatActivity {
  private ActionBar actionBar;
  private CustomFontTextView title;
  private ImageButton xbtn;
  @Override
  protected void onCreate(
    @Nullable
      Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.about );
    initActionbar();
    Typeface font2 = Typeface.createFromAsset(getAssets(), "Bariol_Bold.otf");
    CustomFontTextView titleViertelTour = (CustomFontTextView) findViewById(R.id.title_viertel);
    titleViertelTour.setText("Was ist ViertelTour?");

    CustomFontTextView textViertelTour = (CustomFontTextView) findViewById(R.id.text_viertel);

    SpannableStringBuilder vierteltour = new SpannableStringBuilder("\nDie App ViertelTour nimmt Sie mit auf informative Stadtspaziergänge durch die dichte Ökologie- und Kulturlandschaft der Wuppertaler Innenstadt. Begleitet werden Sie dabei von unterschiedlichsten Expertinnen und Experten.\n\nEs werden keine Sight-Seeing-Highlights vermittelt, sondern viel mehr Orte und Dinge an denen wir täglich vorüber gehen ohne sie zu bemerken. Zu diesen unscheinbaren Orten lässt sich bemerkenswertes Wissen abrufen − wenn man die richtigen Fragen stellt. Und das haben etwa 20 Studierende der Fakultät für Kunst und Design in den Projektseminaren Design Interaktiver Medien und Tontechnik an der Bergischen Universität Wuppertal getan.\n\nUm die Audio, Bild, und Video-Daten der jeweiligen Touren hören und sehen zu können, müssen Sie sich vor Ort befinden!\n\nAm besten laden Sie sich die Touren im WLAN-Netzwerk herunter bevor Sie losgehen. Es werden pro Tour etwa 50-200 MB Daten geladen.\n\nUm Wuppertal vor Ort zu erfahren, haben wir ViertelTour bewusst so konzipiert, dass die Spaziergänge tatsächlich nur erlebt werden können, wenn Sie sich genau dort befinden, wo die entsprechende Expertin oder der entsprechende Experte Sie mit auf den Weg nehmen möchte. Sie haben in der App die Möglichkeit sich von Ihrem Standort aus via GPS an die entsprechenden Orte navigieren zu lassen.\n\nDie Touren sind so aufgebaut, dass Sie der Expertin oder dem Experten einfach von Station zu Station folgen können. Wenn Sie dort angekommen sind, werden Ihnen die Daten der enstprechenden Station zur Verfügung gestellt.\n\nWir wünschen viel Spaß und hoffentlich neue Einsichten und Erfahrungen in einer hochinteressanten Stadt.\n\nProjektgruppe Design Interaktiver Medien, Bergische Universität Wuppertal, Prof. Kristian Wolf\n\n");

    vierteltour.setSpan (new CustomTypefaceSpan("", font2), 8,20,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    vierteltour.setSpan (new CustomTypefaceSpan("", font2), 551, 595, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    vierteltour.setSpan (new CustomTypefaceSpan("", font2), 600, 610, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    vierteltour.setSpan (new CustomTypefaceSpan("", font2), 618, 650, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    vierteltour.setSpan (new CustomTypefaceSpan("", font2), 744, 777, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    vierteltour.setSpan (new CustomTypefaceSpan("", font2), 788, 860, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    vierteltour.setSpan (new CustomTypefaceSpan("", font2), 1246, 1303, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

    textViertelTour.setText(vierteltour);

    CustomFontTextView titleMitwirkende = (CustomFontTextView) findViewById(R.id.title_mitwirkende);
    titleMitwirkende.setText("Wer hat mitgewirkt?");

    CustomFontTextView textMitwirkende = (CustomFontTextView) findViewById(R.id.text_mitwirkende);
    SpannableStringBuilder mitwirkende = new SpannableStringBuilder("\nDie App ViertelTour entstand im Sommersemester 2015 im Projektseminar Design Interaktiver Medien unter der Leitung von Prof. Kristian Wolf. Fertiggestellt wurde sie im Wintersemester 2016/2017.\n\nIdee und Grundkonzept\nKristian Wolf, Laura Poplow, Nikolaus Zunner\n\nKonzeption und Screen Design\nMuriel Balzer, Tamara Dreke, Franziska Györfi, Ana Ivić\n\nBild und Ton\nStudierende des Seminars Licht/Ton, Design Ausdiovisueller Medien unter der Leitung von AOR Till Müller\n\nAufbereitung der Daten\nBenjamin Pfingsten, Svenja Krautwurst, Jessica Schmitz\n\nAndroid Programmierung\nSoftwaretechnik-Praktikum\nMiodrag Radovanovic, Jannis Güldenpfennig, Michel Verheijen, Thomas Vogt, Branislav Bardak, Kevin Kollek\n\nBachelor-Thesis, Betreuung Dr. Werner Hofschuster\nKevin Kollek\n\niOS Programmierung\nNoah Lotz-Hommann\n\nProjektkoordination\nDr. Christoph Rodatz\n\n");


    mitwirkende.setSpan (new CustomTypefaceSpan("", font2), 196, 217, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    mitwirkende.setSpan (new CustomTypefaceSpan("", font2), 264, 292,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    mitwirkende.setSpan (new CustomTypefaceSpan("", font2), 350, 362,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    mitwirkende.setSpan (new CustomTypefaceSpan("", font2), 467, 490,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    mitwirkende.setSpan (new CustomTypefaceSpan("", font2), 547, 570,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    mitwirkende.setSpan (new UnderlineSpan(), 570, 595, 0);
    mitwirkende.setSpan (new UnderlineSpan(), 702, 751, 0);
    mitwirkende.setSpan (new CustomTypefaceSpan("", font2), 766, 785,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    mitwirkende.setSpan (new CustomTypefaceSpan("", font2), 804, 823,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

    textMitwirkende.setText(mitwirkende);


    CustomFontTextView titleDanksagung = (CustomFontTextView) findViewById(R.id.title_danksagung);
    titleDanksagung.setText("Danksagung");

    CustomFontTextView textDanksagung = (CustomFontTextView) findViewById(R.id.text_danksagung);
    SpannableStringBuilder danksagung = new SpannableStringBuilder("Wir danken allen Expertinnen und Experten, die mit ihrem hier preisgegebenen Wissen dazu beitragen, die Stadt Wuppertal in vielen, erst auf den zweiten Blick erkennbaren Facetten erfahrbar zu machen.\n\nVielen Dank an die Jackstädtstiftung, die das Projekt finanziell unterstützt hat.");

    danksagung.setSpan (new CustomTypefaceSpan("", font2), 11, 41,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    danksagung.setSpan (new CustomTypefaceSpan("", font2), 220, 237, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

    textDanksagung.setText(danksagung);

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
    title = (CustomFontTextView) findViewById(R.id.toolbar_title);  //ActionBar Title
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

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(0, 0);
  }
}
