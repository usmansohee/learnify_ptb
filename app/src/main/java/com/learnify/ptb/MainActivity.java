package com.learnify.ptb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    // Constants for URLs
    private static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.learnify.ptb";
    private static final String PLAY_STORE_RATE_URL = "https://play.google.com/store/apps/details?id=com.learnify.ptb&pli=1";
    private static final String PLAY_STORE_DEVELOPER_URL = "https://play.google.com/store/apps/developer?id=Learnify+Dev";
    private static final String FEEDBACK_EMAIL = "illuminatorsstudyclub92@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        // Set up the NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();

        // Set up the AppBarConfiguration
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.subjectsFragment,
                R.id.readingFragment,
                R.id.webViewFragment,
                R.id.chapterWiseUnitsFragment,
                R.id.chapterFilesFragment
        )
        .setDrawerLayout(drawerLayout)
        .build();

        // Set up the toolbar with the NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        try {
            if (id == R.id.nav_class9) {
                Bundle args = new Bundle();
                args.putInt("class_number", 9);
                
                // Clear the back stack before navigating
                navController.popBackStack(R.id.homeFragment, false);
                navController.navigate(R.id.action_home_to_subjects, args);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_share_app) {
                shareApp();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_send_feedback) {
                sendFeedback();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_rate_app) {
                rateApp();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_other_apps) {
                openOtherApps();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_exit) {
                exitApp();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    
    /**
     * Share the app with other users
     */
    private void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing Math learning app: " + PLAY_STORE_URL);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to share app", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Open email app to send feedback
     */
    private void sendFeedback() {
        try {
            // Try to open Gmail directly first
            Intent gmailIntent = new Intent(Intent.ACTION_SENDTO);
            gmailIntent.setData(Uri.parse("mailto:" + FEEDBACK_EMAIL));
            gmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for " + getString(R.string.app_name));
            gmailIntent.putExtra(Intent.EXTRA_TEXT, "Hi,\n\nI would like to provide feedback about your app.\n\n");
            
            // This will only show email apps, not general share apps
            startActivity(gmailIntent);
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to open email app: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Open Play Store rating page
     */
    private void rateApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_RATE_URL));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to open Play Store", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Open Play Store developer page
     */
    private void openOtherApps() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_DEVELOPER_URL));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to open Play Store", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Exit the app
     */
    private void exitApp() {
        finish();
        System.exit(0);
    }
}