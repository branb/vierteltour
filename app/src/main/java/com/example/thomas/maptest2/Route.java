package com.example.thomas.maptest2;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.RawRes;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by thomas on 22.11.15.
 */
public class Route {
    int routenPunkte;
    List<LatLng> route;
    List<LatLng> stationen;
    InputStream is;
    BufferedReader br;
    String line;
    String delimiter = ",";
    StringTokenizer tok;
    PolylineOptions lines;
    public int color;
    List<MarkerOptions> markerList;
    MarkerOptions marker;




    public Route(FragmentActivity fa, int raw){
        //System.out.println("XXXXXXXXXXXXXXX");
        is = fa.getResources().openRawResource(raw);
        br = new BufferedReader(new InputStreamReader(is));
        lines = new PolylineOptions();

        try {
            line = br.readLine();
            /*tok = new StringTokenizer(line, delimiter);
            rot = Integer.parseInt(tok.nextToken());
            grün = Integer.parseInt(tok.nextToken());
            blau = Integer.parseInt(tok.nextToken());
            lines.color(Color.rgb(rot,grün,blau));*/
            color = Color.parseColor(line);
            lines.color(color);
            line = br.readLine();
            route = new Vector<>();
            while(line != null && !line.equals("marker:")) {
                tok = new StringTokenizer(line, delimiter);
                Double lat = Double.parseDouble(tok.nextToken());
                Double lng = Double.parseDouble(tok.nextToken());
                route.add(new LatLng(lat,lng));
                lines.add(new LatLng(lat,lng));
                line = br.readLine();
            }

            line = br.readLine();
            stationen = new Vector<>();
            markerList = new Vector<>();
            while(line != null) {
                tok = new StringTokenizer(line, delimiter);
                Double lat = Double.parseDouble(tok.nextToken());
                Double lng = Double.parseDouble(tok.nextToken());
                stationen.add(new LatLng(lat,lng));
                marker = new MarkerOptions();
                marker.position(new LatLng(lat, lng));
                //System.out.println(String.copyValueOf(color));

                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                markerList.add(marker);
                line = br.readLine();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
