package com.example.comp3074_groupproject_bestulator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class FourthActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> existingProjects = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fourth_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.navigation_fourth_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the toolbar as the Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set a title for the Toolbar

        getSupportActionBar().setTitle("Projects");

        // Hardcoded list projects
        existingProjects.add("Kitchen Renovation - Smith Residence");
        existingProjects.add("Bathroom Remodel - Johnson Home");
        existingProjects.add("Office Space Renovation - ABC Corp");
        existingProjects.add("New Roof Installation - Parker House");
        existingProjects.add("Basement Finishing - Locke Family");
        existingProjects.add("Living Room Expansion - Williams Apartment");
        existingProjects.add("Custom Deck Construction - Miller Estate");
        existingProjects.add("Garage Conversion - Anderson Villa");
        existingProjects.add("Full House Remodeling - Taylor Residence");
        existingProjects.add("Swimming Pool Installation - Harris Manor");
        existingProjects.add("Kitchen Renovation - Smith Residence");
        existingProjects.add("Bathroom Remodel - Johnson Home");
        existingProjects.add("Office Space Renovation - ABC Corp");
        existingProjects.add("New Roof Installation - Parker House");
        existingProjects.add("Basement Finishing - Locke Family");
        existingProjects.add("Living Room Expansion - Williams Apartment");
        existingProjects.add("Custom Deck Construction - Miller Estate");
        existingProjects.add("Garage Conversion - Anderson Villa");
        existingProjects.add("Full House Remodeling - Taylor Residence");
        existingProjects.add("Swimming Pool Installation - Harris Manor");

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, existingProjects);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu from XML
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.navigation_home) {
            startActivity(new Intent(FourthActivity.this, MainActivity.class));
            return true;
        } else if (id == R.id.navigation_second_activity) {
            startActivity(new Intent(FourthActivity.this, SecondActivity.class));
            return true;
        } else if (id == R.id.navigation_third_activity) {
            startActivity(new Intent(FourthActivity.this, ThirdActivity.class));
            return true;
        } else if (id == R.id.navigation_fourth_activity) {
            startActivity(new Intent(FourthActivity.this, FourthActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}