package com.learnify.ptb.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.learnify.ptb.R;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        TextView instructionsText = view.findViewById(R.id.instructionsText);
        // add a picture to the fragment
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.welcome_image);
        
        return view;
    }
} 