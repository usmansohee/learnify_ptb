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

public class ChapterFilesFragment extends Fragment {
    private static final String TAG = "ChapterFilesFragment";
    private NavController navController;
    private String unitName;
    private String folderPath;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chapter_files, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        if (getArguments() != null) {
            unitName = getArguments().getString("unit");
            folderPath = getArguments().getString("folder_path");
        }

        RecyclerView recyclerView = view.findViewById(R.id.chapterFilesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the list of files for this unit
        List<String> files = getFilesForUnit(folderPath);
        
        ChapterFilesAdapter adapter = new ChapterFilesAdapter(files, file -> {
            try {
                // Create the full PDF path
                String pdfPath = "classes/class_9/subjects/mathematics/chapter_wise_units/" + folderPath + "/" + file;
                
                Log.d(TAG, "PDF Path for selected file: " + pdfPath);

                Bundle bundle = new Bundle();
                bundle.putString("unit", unitName);
                bundle.putString("pdf_path", pdfPath);
                bundle.putString("file_name", file);

                Snackbar.make(view, "" + file + "", Snackbar.LENGTH_SHORT).show();
                navController.navigate(R.id.action_chapter_files_to_reading, bundle);

                Log.d(TAG, "Navigating to ReadingFragment with file: " + file);
                Log.d(TAG, "PDF path sent: " + pdfPath);

            } catch (Exception e) {
                Log.e(TAG, "Error navigating to ReadingFragment", e);
                Snackbar.make(view, "Error loading file: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
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
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Chapter Files");
        }
    }

    private void updateTitle() {
        if (getActivity() instanceof AppCompatActivity && unitName != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(unitName);
        }
    }

    private List<String> getFilesForUnit(String folderPath) {
        Log.d(TAG, "getFilesForUnit called with folderPath: " + folderPath);
        
        // Based on the folder path, return the appropriate list of files
        // This is a simplified approach - in a real app, you might scan the directory
        if (folderPath.matches(".*unit_no_1_.*")) {
            Log.d(TAG, "Matched unit 1");
            return Arrays.asList(
                "unit_no_1_real_numbers_basic_concepts.pdf",
                "unit_no_1_real_numbers_exercise_number_1_1.pdf",
                "unit_no_1_real_numbers_exercise_number_1_2.pdf",
                "unit_no_1_real_numbers_exercise_number_1_3.pdf",
                "unit_no_1_real_numbers_review_exercise_number_1.pdf",
                "unit_no_1_real_numbers_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_2_.*")) {
            Log.d(TAG, "Matched unit 2");
            return Arrays.asList(
                "unit_no_2_logarithms_basic_concepts.pdf",
                "unit_no_2_logarithms_exercise_number_2_1.pdf",
                "unit_no_2_logarithms_exercise_number_2_2.pdf",
                "unit_no_2_logarithms_exercise_number_2_3.pdf",
                "unit_no_2_logarithms_exercise_number_2_4.pdf",
                "unit_no_2_logarithms_review_exercise_number_2.pdf",
                "unit_no_2_logarithms_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_3_.*")) {
            Log.d(TAG, "Matched unit 3");
            return Arrays.asList(
                "unit_no_3_sets_and_functions_basic_concepts.pdf",
                "unit_no_3_sets_and_functions_exercise_number_3_1.pdf",
                "unit_no_3_sets_and_functions_exercise_number_3_2.pdf",
                "unit_no_3_sets_and_functions_exercise_number_3_3.pdf",
                "unit_no_3_sets_and_functions_review_exercise_number_3.pdf",
                "unit_no_3_sets_and_functions_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_4_.*")) {
            Log.d(TAG, "Matched unit 4");
            return Arrays.asList(
                "unit_no_4_factorization_and_algebraic_manipulation_basic_concepts.pdf",
                "unit_no_4_factorization_and_algebraic_manipulation_exercise_number_4_1.pdf",
                "unit_no_4_factorization_and_algebraic_manipulation_exercise_number_4_2.pdf",
                "unit_no_4_factorization_and_algebraic_manipulation_exercise_number_4_3.pdf",
                "unit_no_4_factorization_and_algebraic_manipulation_exercise_number_4_4.pdf",
                "unit_no_4_factorization_and_algebraic_manipulation_review_exercise_number_4.pdf",
                "unit_no_4_factorization_and_algebraic_manipulation_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_5_.*")) {
            Log.d(TAG, "Matched unit 5");
            return Arrays.asList(
                "unit_no_5_linear_equations_and_inequalities_basic_concepts.pdf",
                "unit_no_5_linear_equations_and_inequalities_exercise_number_5_1.pdf",
                "unit_no_5_linear_equations_and_inequalities_exercise_number_5_2.pdf",
                "unit_no_5_linear_equations_and_inequalities_review_exercise_number_5.pdf",
                "unit_no_5_linear_equations_and_inequalities_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_6_.*")) {
            Log.d(TAG, "Matched unit 6");
            return Arrays.asList(
                "unit_no_6_trigonometry_basic_concepts.pdf",
                "unit_no_6_trigonometry_exercise_number_6_1.pdf",
                "unit_no_6_trigonometry_exercise_number_6_2.pdf",
                "unit_no_6_trigonometry_exercise_number_6_3.pdf",
                "unit_no_6_trigonometry_exercise_number_6_4.pdf",
                "unit_no_6_trigonometry_exercise_number_6_5.pdf",
                "unit_no_6_trigonometry_exercise_number_6_6.pdf",
                "unit_no_6_trigonometry_review_exercise_number_6.pdf",
                "unit_no_6_trigonometry_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_7_.*")) {
            Log.d(TAG, "Matched unit 7");
            return Arrays.asList(
                "unit_no_7_coordinate_geometry_basic_concepts.pdf",
                "unit_no_7_coordinate_geometry_exercise_number_7_1.pdf",
                "unit_no_7_coordinate_geometry_exercise_number_7_2.pdf",
                "unit_no_7_coordinate_geometry_exercise_number_7_3.pdf",
                "unit_no_7_coordinate_geometry_review_exercise_number_7.pdf",
                "unit_no_7_coordinate_geometry_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_8_.*")) {
            Log.d(TAG, "Matched unit 8");
            return Arrays.asList(
                "unit_no_8_logic_basic_concepts.pdf",
                "unit_no_8_logic_exercise_number_8.pdf",
                "unit_no_8_logic_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_9_.*")) {
            Log.d(TAG, "Matched unit 9");
            return Arrays.asList(
                "unit_no_9_similar_figures_basic_concepts.pdf",
                "unit_no_9_similar_figures_exercise_number_9_1.pdf",
                "unit_no_9_similar_figures_exercise_number_9_2.pdf",
                "unit_no_9_similar_figures_exercise_number_9_3.pdf",
                "unit_no_9_similar_figures_exercise_number_9_4.pdf",
                "unit_no_9_similar_figures_review_exercise_number_9.pdf",
                "unit_no_9_similar_figures_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_10_.*")) {
            Log.d(TAG, "Matched unit 10");
            return Arrays.asList(
                "unit_no_10_graphs_of_functions_basic_concepts.pdf",
                "unit_no_10_graphs_of_functions_exercise_number_10_1.pdf",
                "unit_no_10_graphs_of_functions_exercise_number_10_2.pdf",
                "unit_no_10_graphs_of_functions_review_exercise_number_10.pdf",
                "unit_no_10_graphs_of_functions_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_11_.*")) {
            Log.d(TAG, "Matched unit 11");
            return Arrays.asList(
                "unit_no_11_loci_and_construction_basic_concepts.pdf",
                "unit_no_11_loci_and_construction_exercise_number_11_1.pdf",
                "unit_no_11_loci_and_construction_exercise_number_11_2.pdf",
                "unit_no_11_loci_and_construction_review_exercise_number_11.pdf",
                "unit_no_11_loci_and_construction_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_12_.*")) {
            Log.d(TAG, "Matched unit 12");
            return Arrays.asList(
                "unit_no_12_information_handling_basic_concepts.pdf",
                "unit_no_12_information_handling_exercise_number_12_1.pdf",
                "unit_no_12_information_handling_exercise_number_12_2.pdf",
                "unit_no_12_information_handling_review_exercise_number_12.pdf",
                "unit_no_12_information_handling_tests.pdf"
            );
        } else if (folderPath.matches(".*unit_no_13_.*")) {
            Log.d(TAG, "Matched unit 13");
            return Arrays.asList(
                "unit_no_13_probability_basic_concepts.pdf",
                "unit_no_13_probability_exercise_number_13_1.pdf",
                "unit_no_13_probability_exercise_number_13_2.pdf",
                "unit_no_13_probability_review_exercise_number_13.pdf",
                "unit_no_13_probability_tests.pdf"
            );
        }
        
        Log.e(TAG, "No match found for folderPath: " + folderPath);
        // Default empty list
        return Arrays.asList("No files found");
    }

    /**
     * Convert raw file name to user-friendly display label
     * Example: "unit_no_13_probability_basic_concepts.pdf" -> "Probability Basic Concepts"
     * Example: "unit_no_1_real_numbers_exercise_number_1_1.pdf" -> "Exercise Number 1.1"
     */
    private String getDisplayLabel(String fileName) {
        try {
            // Remove the .pdf extension
            String nameWithoutExt = fileName.replace(".pdf", "");
            
            // Remove the unit_no_X_ prefix
            String nameWithoutPrefix = nameWithoutExt.replaceAll("^unit_no_\\d+_", "");
            
            // Handle exercise numbers specifically - convert "number_X_Y" to "number X.Y"
            String nameWithExerciseNumbers = nameWithoutPrefix.replaceAll("number_(\\d+)_(\\d+)", "number $1.$2");
            
            // Replace remaining underscores with spaces
            String nameWithSpaces = nameWithExerciseNumbers.replace("_", " ");
            
            // Convert to proper case (first letter of each word capitalized)
            String[] words = nameWithSpaces.split(" ");
            StringBuilder result = new StringBuilder();
            
            for (int i = 0; i < words.length; i++) {
                if (words[i].length() > 0) {
                    // Capitalize first letter, lowercase the rest
                    String word = words[i].substring(0, 1).toUpperCase() + 
                                words[i].substring(1).toLowerCase();
                    result.append(word);
                    
                    if (i < words.length - 1) {
                        result.append(" ");
                    }
                }
            }
            
            return result.toString();
        } catch (Exception e) {
            Log.e(TAG, "Error converting fileName to display label: " + fileName, e);
            return fileName; // Return original if conversion fails
        }
    }

    interface OnFileClickListener {
        void onFileClick(String file);
    }

    private class ChapterFilesAdapter extends RecyclerView.Adapter<ChapterFilesAdapter.ViewHolder> {
        private final List<String> files;
        private final OnFileClickListener listener;

        ChapterFilesAdapter(List<String> files, OnFileClickListener listener) {
            this.files = files;
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
            String file = files.get(position);
            holder.textView.setText(getDisplayLabel(file));
            holder.itemView.setOnClickListener(v -> listener.onFileClick(file));
        }

        @Override
        public int getItemCount() {
            return files.size();
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
