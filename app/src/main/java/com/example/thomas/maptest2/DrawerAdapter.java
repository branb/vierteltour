package com.example.thomas.maptest2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DrawerAdapter extends BaseAdapter{
  Context context;
  List<DrawerItem> drawerItem;

  DrawerAdapter( Context context, List<DrawerItem> drawerItem ){
    this.context = context;
    this.drawerItem = drawerItem;
  }

  //zaehlt Anzahl an Zeilen in Liste
  @Override
  public int getCount(){
    return drawerItem.size();
  }

  @Override
  public Object getItem( int position ){
    return drawerItem.get( position );
  }

  @Override
  public long getItemId( int position ){
    return drawerItem.indexOf( getItem( position ) );
  }

  //erzeugt Aussehen der Liste
  @Override
  public View getView( final int position, View convertView, ViewGroup parent ){
    if( convertView == null ){
      LayoutInflater mInflater = (LayoutInflater) context.getSystemService( Activity.LAYOUT_INFLATER_SERVICE );
      convertView = mInflater.inflate( R.layout.drawer_list_single, null );
    }

    ImageView imgIcon = (ImageView) convertView.findViewById( R.id.menuimg );
    TextView txtTitle = (TextView) convertView.findViewById( R.id.menutxt );
    TextView subtxtTitle1 = (TextView) convertView.findViewById( R.id.menusubtxt1 );

    //Setzt jeweilige Informationen an die richtigen Views
    DrawerItem row_pos = drawerItem.get( position );

    imgIcon.setImageResource( row_pos.getIcon() );
    txtTitle.setText( row_pos.getTitle() );
    subtxtTitle1.setText( row_pos.getSubtitle() );

    return convertView;

  }
}
