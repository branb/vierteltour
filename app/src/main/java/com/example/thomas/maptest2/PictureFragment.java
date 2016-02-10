package com.example.thomas.maptest2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Kevin on 29.01.2016.
 */
public class PictureFragment extends Fragment {
  /*  private static final String ARG_PICTURE_NUMBER = "pictureNumber";
    private int imageResourceId;


    public static PictureFragment create(int pictureNumber) {
        PictureFragment fragment = new PictureFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PICTURE_NUMBER, pictureNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PictureFragment(int i) {
        imageResourceId = i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ImageView image = new ImageView(getActivity());
        image.setImageResource(imageResourceId);

        LinearLayout layout = new LinearLayout(getActivity());

        layout.setGravity(Gravity.CENTER);
        layout.addView(image);

        return layout;
    }*/
}