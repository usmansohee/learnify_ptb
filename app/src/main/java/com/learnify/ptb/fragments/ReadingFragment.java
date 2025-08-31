package com.learnify.ptb.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.learnify.ptb.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReadingFragment extends Fragment {
    private static final String TAG = "ReadingFragment";
    private static final int STORAGE_PERMISSION_CODE = 1001;
    private TextView contentText;
    private PDFView pdfView;
    private FloatingActionButton jumpButton;
    private int pageCount = 0;
    private String currentPdfPath;
    private TextView pageNumberView;
    private FloatingActionButton fabPageJump;
    private int totalPageCount = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.reading_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_download) {
            downloadPdf();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contentText = view.findViewById(R.id.contentText);
        pdfView = view.findViewById(R.id.pdfView);
        jumpButton = view.findViewById(R.id.jumpButton);

        jumpButton.setOnClickListener(v -> showPagePickerDialog());

        if (getArguments() != null) {
            String pdfPath = getArguments().getString("pdf_path");
            currentPdfPath = pdfPath;
            
            Log.d(TAG, "Attempting to load PDF from path: " + pdfPath);
            
            if (pdfPath != null) {
                contentText.setVisibility(View.GONE);
                try {
                    pdfView.fromAsset(pdfPath)
                           .enableSwipe(true)
                           .enableDoubletap(true)
                           .scrollHandle(new DefaultScrollHandle(requireContext()))
                           .onLoad(new OnLoadCompleteListener() {
                               @Override
                               public void loadComplete(int nbPages) {
                                   pageCount = nbPages;
                                   jumpButton.setVisibility(View.VISIBLE);
                               }
                           })
                           .onError(new OnErrorListener() {
                               @Override
                               public void onError(Throwable t) {
                                   Log.e(TAG, "Error loading PDF: " + t.getMessage());
                                   contentText.setVisibility(View.VISIBLE);
                                   contentText.setText("Error loading PDF: " + t.getMessage());
                                   jumpButton.setVisibility(View.GONE);
                               }
                           })
                           .load();
                } catch (Exception e) {
                    Log.e(TAG, "Exception loading PDF: " + e.getMessage());
                    contentText.setVisibility(View.VISIBLE);
                    contentText.setText("Error: " + e.getMessage());
                    jumpButton.setVisibility(View.GONE);
                }
            } else {
                contentText.setVisibility(View.VISIBLE);
                contentText.setText("Error: No PDF path received");
                Log.e(TAG, "PDF path is null");
                jumpButton.setVisibility(View.GONE);
            }
        } else {
            contentText.setVisibility(View.VISIBLE);
            contentText.setText("Error: No arguments received");
            Log.e(TAG, "No arguments received");
            jumpButton.setVisibility(View.GONE);
        }
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
            String subject = getArguments().getString("subject");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(subject);
        }
    }

    private void updateTitle() {
        if (getArguments() != null) {
            String unitName = getArguments().getString("unit");
            if (unitName != null && requireActivity() instanceof AppCompatActivity) {
                ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(unitName);
            }
        }
    }

    private void showPagePickerDialog() {
        if (pageCount == 0) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_page_picker, null);
        NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);
        
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(pageCount);
        numberPicker.setValue(pdfView.getCurrentPage() + 1);
        
        builder.setTitle("Go to Page")
               .setView(dialogView)
               .setPositiveButton("Go", (dialog, which) -> {
                   int selectedPage = numberPicker.getValue() - 1;
                   pdfView.jumpTo(selectedPage);
               })
               .setNegativeButton("Cancel", null)
               .show();
    }

    private void downloadPdf() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P &&
            requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) 
            != PackageManager.PERMISSION_GRANTED) {
            
            requestPermissions(
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE
            );
            return;
        }

        if (currentPdfPath == null) {
            Snackbar.make(requireView(), "No PDF to download", Snackbar.LENGTH_LONG).show();
            return;
        }

        try {
            // Get the PDF name from the path
            String fileName = currentPdfPath.substring(currentPdfPath.lastIndexOf("/") + 1);
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = requireContext().getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    try (OutputStream output = requireContext().getContentResolver().openOutputStream(uri);
                         InputStream input = requireContext().getAssets().open(currentPdfPath)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = input.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                        }
                        showDownloadSuccessSnackbar(fileName);
                    }
                }
            } else {
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File outputFile = new File(downloadsDir, fileName);

                try (OutputStream output = new FileOutputStream(outputFile);
                     InputStream input = requireContext().getAssets().open(currentPdfPath)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                    showDownloadSuccessSnackbar(fileName);

                    // Notify system about new file
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outputFile));
                    requireContext().sendBroadcast(intent);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error downloading PDF", e);
            Snackbar.make(requireView(), "Error downloading PDF: " + e.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Retry", v -> downloadPdf())
                    .show();
        }
    }

    private void showDownloadSuccessSnackbar(String fileName) {
        Snackbar.make(requireView(), 
                     "Saved to Downloads: " + fileName,
                     Snackbar.LENGTH_LONG)
                .setAction("Open", v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                        } else {
                            uri = Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                        }
                        intent.setDataAndType(uri, "resource/folder");
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error opening Downloads folder", e);
                        Snackbar.make(requireView(), "Couldn't open Downloads folder", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadPdf();
            } else {
                Snackbar.make(requireView(), 
                    "Storage permission is required to download PDFs",
                    Snackbar.LENGTH_LONG)
                    .setAction("Grant", v -> requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE))
                    .show();
            }
        }
    }
} 