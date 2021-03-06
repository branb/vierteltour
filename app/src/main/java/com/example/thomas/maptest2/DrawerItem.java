package com.example.thomas.maptest2;

/**
 * Created by Kevin on 16.12.2015.
 */
public class DrawerItem {
    private String subtitle,title;
    private int icon;

    public DrawerItem(String title, String subtitle, int icon) {
        this.title = title;
        this.icon = icon;
        this.subtitle = subtitle;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public int getIcon() {return icon;}

    public void setIcon(int icon) {this.icon = icon;}

    public String getSubtitle() {return subtitle;}

    public void setSubtitle(String subtitle){this.subtitle = subtitle;}
}
