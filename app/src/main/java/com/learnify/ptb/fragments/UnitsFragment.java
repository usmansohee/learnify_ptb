package com.learnify.ptb.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.learnify.ptb.R;
import java.util.Arrays;
import java.util.List;

public class UnitsFragment extends Fragment {
    private static final String TAG = "UnitsFragment";
    private int classNumber;
    private String subject;
    private NavController navController;
    private List<String> mathSolutionsUnits = Arrays.asList(
            "Unit 1: Real Number",
            "Unit 2: Logarithms",
            "Unit 3: Set and Functions",
            "Unit 4: Factorisation and Algebraic Manipulation",
            "Unit 5: Linear Equations and Inequalities",
            "Unit 6: Trigonometry",
            "Unit 7: Coordinate Geometry",
            "Unit 8: Logic",
            "Unit 9: Similar Figures",
            "Unit 10: Graphs of Functions",
            "Unit 11: Loci and Construction",
            "Unit 12: Information Handling",
            "Unit 13: Probability"
    );
    private List<String> englishUnits = Arrays.asList(
            "Unit 1: Alphabet",
            "Unit 2: Phonics",
            "Unit 3: Words",
            "Unit 4: Sentences",
            "Unit 5: Reading",
            "Unit 6: Writing",
            "Unit 7: Grammar",
            "Unit 8: Vocabulary",
            "Unit 9: Stories"
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_units, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        if (getArguments() != null) {
            classNumber = getArguments().getInt("class_number");
            subject = getArguments().getString("subject");
        }

        RecyclerView recyclerView = view.findViewById(R.id.unitsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> units = subject.equals("Mathematics") ? mathSolutionsUnits : englishUnits;

        UnitsAdapter adapter = new UnitsAdapter(units, unit -> {
            try {
                // Extract unit number from the unit title (e.g., "Unit 1: Numbers" -> "1")
                String unitNumber = unit.split(":")[0].replace("Unit ", "").trim();

                // Create file path for the specific unit
                String pdfPath = "classes/class_9" +
                        "/subjects/" + subject.toLowerCase() +
                        "/units/unit_" + unitNumber + ".pdf";

                Log.d(TAG, "PDF Path for selected unit: " + pdfPath);

                Bundle bundle = new Bundle();
                bundle.putInt("class_number", classNumber);
                bundle.putString("subject", subject);
                bundle.putString("unit", unit);
                bundle.putString("pdf_path", pdfPath);

                Snackbar.make(view, "" + unit + "", Snackbar.LENGTH_SHORT).show();
                navController.navigate(R.id.action_units_to_reading, bundle);

                Log.d(TAG, "Navigating to ReadingFragment with unit: " + unit);
                Log.d(TAG, "PDF path sent: " + pdfPath);

            } catch (Exception e) {
                Log.e(TAG, "Error navigating to ReadingFragment", e);
                Snackbar.make(view, "Error loading unit: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
        recyclerView.setAdapter(adapter);
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
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Class " + classNumber + " Subjects");
        }
    }

    private void updateTitle() {
        if (getActivity() instanceof AppCompatActivity && subject != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(subject);
        }
    }

    private static class UnitsAdapter extends RecyclerView.Adapter<UnitsAdapter.ViewHolder> {
        private final List<String> units;
        private final OnUnitClickListener listener;

        interface OnUnitClickListener {
            void onUnitClick(String unit);
        }

        UnitsAdapter(List<String> units, OnUnitClickListener listener) {
            this.units = units;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_unit, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String unit = units.get(position);
            holder.textView.setText(unit);
            holder.itemView.setOnClickListener(v -> listener.onUnitClick(unit));
        }

        @Override
        public int getItemCount() {
            return units.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
            }
        }
    }
} 