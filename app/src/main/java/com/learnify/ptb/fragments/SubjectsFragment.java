package com.learnify.ptb.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.learnify.ptb.R;
import com.google.android.material.snackbar.Snackbar;

public class SubjectsFragment extends Fragment {
    private static final String TAG = "SubjectsFragment";
    private int classNumber;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subjects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        navController = Navigation.findNavController(view);

        if (getArguments() != null) {
            classNumber = getArguments().getInt("class_number");
        }

        // Set up the Chapter-wise Units button
        Button chapterWiseUnitsButton = view.findViewById(R.id.chapterWiseUnitsButton);
        chapterWiseUnitsButton.setOnClickListener(v -> {
            // Navigate to chapter-wise units fragment
            navController.navigate(R.id.action_subjects_to_chapter_wise_units);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");
        }
    }

    private void updateTitle() {
        if (getActivity() instanceof AppCompatActivity && classNumber > 0) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Class " + classNumber);
        }
    }

    private void handleSubjectClick(String subject) {
        try {
            Log.d(TAG, "Subject clicked: " + subject);
            
            String url = "";
            String title = "";
            
            if (subject.equals("Mathematics English")) {
                url = "https://www.dropbox.com/scl/fi/5u70qb17uml4oop3kf8hy/Math-9-EM.pdf?rlkey=p4bh3t1m8hf6gnmeu0mct10tg&st=5z02m2sv&dl=0";
                title = "Maths 9th (English)";
            } else if (subject.equals("Mathematics Urdu")) {
                url = "https://www.dropbox.com/scl/fi/elohxxz0k7560kwgdwsfm/Math-9-UM.pdf?rlkey=j1csn50gwjdly3p2qdcfa59k3&e=1&st=mdu9314f&dl=0";
                title = "Maths 9th (Urdu)";
            }

            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            bundle.putString("title", title);
            
            navController.navigate(R.id.action_subjects_to_webview, bundle);
                
        } catch (Exception e) {
            Log.e(TAG, "Error handling subject click", e);
           Snackbar.make(requireView(), "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }
} 