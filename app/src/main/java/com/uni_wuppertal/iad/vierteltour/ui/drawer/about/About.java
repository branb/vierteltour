package com.uni_wuppertal.iad.vierteltour.ui.drawer.about;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.uni_wuppertal.iad.vierteltour.R;

/**
 * Created by Kevin-Laptop on 17.01.2017.
 * Activity only shows about text.
 */

public class About extends Activity {

  @Override
  protected void onCreate(
    @Nullable
      Bundle savedInstanceState ){
    super.onCreate( savedInstanceState );
    setContentView( R.layout.about );

    TextView textView = (TextView) findViewById(R.id.abouttext);

    String text = "Die App ViertelTour nimmt Sie mit auf informative Stadtspaziergänge, die von unterschiedlichsten Experten durch eine dichte Ökologie- und Kulturlandschaft im Wuppertaler Innenstadtraum durchgeführt werden.\nEs sind weniger die Sight-Seeing-Highlights, die hier vermittelt werden, sondern eher Orte und Dinge an denen wir täglich vorübergehen, ohne sie jedoch zu bemerken, obwohl sich zu vielen Orten unserer Stadt bemerkenswertes Wissen abrufen lässt. Man muss nur die Richtigen fragen – und das haben etwa 20 Studierende der Fakultät für Kunst und Design im Seminar Design Interaktiver Medien und Tontechnik der Bergischen Universität Wuppertal getan. \n\nLaden Sie sich die Touren am besten bevor Sie losgehen im Wlan Netz herunter. Es werden pro Tour etwa 100–200 MB Daten geladen.\n\nUm die Audio-, Bild, und Video-Daten dann zu den Touren hören und sehen zu können, müssen Sie sich vor Ort befinden!\n\nWir haben ViertelTour bewusst so konzipiert, dass die Spaziergänge tatsächlich nur erlebt werden können, wenn Sie sich genau dort befinden, wo die entsprechende Expertin oder der entsprechende Experte Sie mit auf den Weg nehmen möchte, um Wuppertal vor Ort zu erfahren.\nSie haben mit der App die Möglichkeit sich an die entsprechenden Ort navigieren zu lassen.\nWenn Sie dort angekommen sind, werden Ihnen die Daten jeweils Stationsweise zur Verfügung gestellt.\nDie Touren sind anhand nummerierter Stationen so aufgebaut, dass Sie der Expertin oder dem Experten einfach von Station zu Station folgen können.\nWir wünschen viel Spaß und hoffentlich neue Einsichten und Erfahrungen in einer hochinteressanten Stadt.\n\nProjektgruppe Design Interaktiver Medien, Bergische Universität Wuppertal, Prof. Kristian Wolf\n\n\nDie App ViertelTour entstand im Sommersemester 2015 im Projektseminar Design Interaktiver Medien unter der Leitung von Prof. Kristian Wolf. Fertiggestellt wurde sie im Wintersemester 2016/17\n\nIdee und Grundkonzept: Kristian Wolf, Laura Poplow, Nikolaus Zunner\n\nKonzeption und Screen Design: Muriel Balzer, Tamara Dreke, Franziska Györfi, Ana Ivić\n\nBild und Ton: Studierende des Seminars Licht/Ton, Design Ausdiovisueller Medien unter der Leitung von AOR Till Müller\n\nAufbereitung der Daten: Benjamin Pfingsten, Svenja Krautwurst\n\nAndroid Programmierung: Branislav Bardak, Kevin Kollek (Bachelor Thesis, Betreuung Dr. Werner Hofschuster)\n\niOS Programmierung: Noah Lotz-Hommann\n\nProjektkoordination: Dr. Christoph Rodatz\n\nWir danken allen ExpertInnen, die mit Ihrem hier preisgegebenen Wissen dazu betragen, dass die Stadt Wuppertal in vielen Facetten, die sich erst auf den zweiten Blick erschließen, erfahrbar wird.\n\nVielen Dank an die Jackstädtstiftung, die das Projekt finanziell unterstützt hat.";

    final SpannableStringBuilder str = new SpannableStringBuilder(text);      //Texteditierer, um Wörter kursiv zu schreiben
    str.setSpan(new StyleSpan(Typeface.ITALIC), 8, 19, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);        //ViertelTour
    str.setSpan(new StyleSpan(Typeface.ITALIC), 565, 592, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);     //Design Interaktiver Medien
    str.setSpan(new StyleSpan(Typeface.ITALIC), 597, 607, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);     //Tontechnik
    str.setSpan(new StyleSpan(Typeface.ITALIC), 612, 645, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);     //Bergischen Universität Wuppertal

    str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 780, 900, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    str.setSpan(new RelativeSizeSpan(1.2f), 780, 900, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

    str.setSpan(new StyleSpan(Typeface.ITALIC), 910, 922, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);        //ViertelTour
    str.setSpan(new StyleSpan(Typeface.ITALIC), 1613, 1710, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);        //Projektgruppe

    str.setSpan(new StyleSpan(Typeface.ITALIC), 1719, 1730, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);        //ViertelTour
    str.setSpan(new StyleSpan(Typeface.ITALIC), 1780, 1807, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);     //Design Interaktiver Medien

    textView.setText(str);


  }

}
