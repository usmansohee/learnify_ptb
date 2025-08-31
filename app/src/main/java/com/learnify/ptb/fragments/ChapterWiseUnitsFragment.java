package com.learnify.ptb.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.learnify.ptb.R;

import java.util.Arrays;
import java.util.List;

public class ChapterWiseUnitsFragment extends Fragment {
    private static final String TAG = "ChapterWiseUnitsFragment";
    private NavController navController;
    
    private List<String> chapterWiseUnits = Arrays.asList(
            "Unit 1: Real Numbers",
            "Unit 2: Logarithms", 
            "Unit 3: Sets and Functions",
            "Unit 4: Factorization and Algebraic Manipulation",
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chapter_wise_units, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        RecyclerView recyclerView = view.findViewById(R.id.chapterWiseUnitsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ChapterWiseUnitsAdapter adapter = new ChapterWiseUnitsAdapter(chapterWiseUnits, unit -> {
            try {
                // Extract unit number from the unit title (e.g., "Unit 1: Real Numbers" -> "1")
                String unitNumber = unit.split(":")[0].replace("Unit ", "").trim();
                
                // Create folder path for the specific unit
                String folderPath = "unit_no_" + unitNumber + "_" + getUnitFolderName(unit);
                
                Log.d(TAG, "Unit: " + unit);
                Log.d(TAG, "Unit Number: " + unitNumber);
                Log.d(TAG, "Unit Name: " + unit.split(":")[1].trim());
                Log.d(TAG, "Generated Folder Path: " + folderPath);
                
                Bundle bundle = new Bundle();
                bundle.putString("unit", unit);
                bundle.putString("folder_path", folderPath);

                Snackbar.make(view, "" + unit + "", Snackbar.LENGTH_SHORT).show();
                navController.navigate(R.id.action_chapter_wise_units_to_chapter_files, bundle);

                Log.d(TAG, "Navigating to ChapterFilesFragment with unit: " + unit);
                Log.d(TAG, "Folder path sent: " + folderPath);

            } catch (Exception e) {
                Log.e(TAG, "Error navigating to ChapterFilesFragment", e);
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
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Chapter-wise Units");
        }
    }

    private void updateTitle() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Units");
        }
    }

    private String getUnitFolderName(String unit) {
        // Convert unit names to folder names (lowercase, replace spaces with underscores)
        String unitName = unit.split(":")[1].trim();
        return unitName.toLowerCase().replace(" ", "_");
    }

    /**
     * Convert unit name to user-friendly display label
     * Example: "Unit 13: Probability" -> "Unit 13: Probability" (already clean)
     */
    private String getUnitDisplayLabel(String unit) {
        // The unit names are already in a good format, just return as is
        return unit;
    }

    interface OnUnitClickListener {
        void onUnitClick(String unit);
    }

    private class ChapterWiseUnitsAdapter extends RecyclerView.Adapter<ChapterWiseUnitsAdapter.ViewHolder> {
        private final List<String> units;
        private final OnUnitClickListener listener;



        ChapterWiseUnitsAdapter(List<String> units, OnUnitClickListener listener) {
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
            // Display user-friendly label but keep original unit name for functionality
            String displayLabel = getUnitDisplayLabel(unit);
            holder.textView.setText(displayLabel);
            holder.itemView.setOnClickListener(v -> listener.onUnitClick(unit));
        }

        @Override
        public int getItemCount() {
            return units.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
            }
        }
    }
}
