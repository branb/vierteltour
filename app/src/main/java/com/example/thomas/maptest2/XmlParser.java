package com.example.thomas.maptest2;

import android.support.v4.app.FragmentActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

/**
 * Created by thomas on 26.01.16.
 */
public class XmlParser {
    public int resource;

    public String name;
    public String author;
    public String description;
    public String length;
    public String time;
    public String image;
    public String color;
    public String id;
    public String title;
    public String number;
    public String video;
    public String audio;
    public String coordinates;

    public Tour tour;
    public List<Tour> ListTour;
    public Station station;
    public List<Station> stations;
    public StationInfo stationInfo;

    public XmlPullParser parser;
    public XmlPullParserFactory parserFactory;
    public InputStream inputStream;
    public String text;

    public XmlParser(FragmentActivity context){
        stations = new Vector<>();
        ListTour = new Vector<>();
        inputStream = context.getResources().openRawResource(R.raw.tour1);
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            parser = parserFactory.newPullParser();
            parser.setInput(inputStream,null);

            int eventType = parser.getEventType();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                if(eventType==XmlPullParser.START_TAG){
                    switch (parser.getName()) {
                        case ("tour"):
                            tour = new Tour();
                            stations = new Vector<>();
                            break;
                        case ("info"):
                            stationInfo = new StationInfo();
                            break;
                        case ("station"):
                            station = new Station();
                            break;
                        default:
                            break;
                    }
                }
                else if(eventType==XmlPullParser.TEXT){
                    text = parser.getText();
                }
                else if(eventType==XmlPullParser.END_TAG){
                    switch (parser.getName()){
                        case ("name"):
                            name = text;
                            break;
                        case ("author"):
                            author = text;
                            break;
                        case ("description"):
                            description = text;
                            break;
                        case ("length"):
                            length = text;
                            break;
                        case ("time"):
                            time = text;
                            break;
                        case ("image"):
                            image = text;
                            break;
                        case ("color"):
                            color = text;
                            break;
                        case ("id"):
                            id = text;
                            break;
                        case ("title"):
                            title = text;
                            break;
                        case ("number"):
                            number = text;
                            break;
                        case ("video"):
                            video = text;
                            break;
                        case ("audio"):
                            audio = text;
                            break;
                        case ("coordinates"):
                            coordinates = text;
                            break;

                        // Konstruktor StationInfo
                        case ("info"):
                            stationInfo = new StationInfo(name,author,description,length,time,image,color);
                            break;

                        // Konstruktor Station
                        case ("station"):
                            station = new Station(id,title,number,description,image,video,audio,coordinates);
                            stations.add(station);
                            break;

                        // Konstruktor Tour
                        case ("tour"):
                            tour = new Tour(stationInfo,stations);
                            ListTour.add(tour);
                            //System.out.println(tour.info.name);
                        default:
                            break;
                    }

                }
                eventType = parser.next();
            }
        }
        catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public Tour getTour(){
        return tour;
    }

    public void printAll(){
        for(Tour t : ListTour ){
            System.out.println(t);
            for(Station s : t.stations){
                System.out.println(s);
            }
        }

    }
}
