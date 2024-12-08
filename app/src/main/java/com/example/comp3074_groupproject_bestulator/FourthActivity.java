package com.example.comp3074_groupproject_bestulator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FourthActivity extends AppCompatActivity implements ProjectAdapter.OnProjectClickListener {
    private DatabaseHelper dbHelper;
    private ProjectAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fourth_activity);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Project List");

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.projectRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load and display projects
        loadProjects();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProjects(); // Reload projects when activity resumes
    }

    private void loadProjects() {
        List<Project> projects = dbHelper.getAllProjects();
        if (adapter == null) {
            adapter = new ProjectAdapter(projects, this);
            adapter.setOnDeleteClickListener(this::confirmDelete);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateProjects(projects);
        }
    }

    private void confirmDelete(Project project, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Project")
                .setMessage("Are you sure you want to delete " + project.getProjectName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> deleteProject(project, position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteProject(Project project, int position) {
        if (dbHelper.deleteProject(project.getId())) {
            adapter.removeProject(position);
            Toast.makeText(this, "Project deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error deleting project", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProjectClick(Project project) {
        showProjectDetails(project);
    }

    private void showProjectDetails(Project project) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.project_details_dialog);

        // Initialize views
        TextView projectNameTitle = dialog.findViewById(R.id.projectNameTitle);
        TextView locationText = dialog.findViewById(R.id.locationText);
        TextView clientDetailsText = dialog.findViewById(R.id.clientDetailsText);
        TextView contactText = dialog.findViewById(R.id.contactText);
        TextView datesText = dialog.findViewById(R.id.datesText);
        TextView categoryText = dialog.findViewById(R.id.categoryText);
        TextView budgetText = dialog.findViewById(R.id.budgetText);
        Button closeButton = dialog.findViewById(R.id.closeButton);

        // Set values
        projectNameTitle.setText(project.getProjectName());
        locationText.setText(String.format("Location: %s, %s", project.getCity(), project.getState()));
        clientDetailsText.setText(String.format("Client: %s", project.getClientName()));
        contactText.setText(String.format("Contact: %s\n%s", project.getPhone(), project.getEmail()));
        datesText.setText(String.format("Duration: %s - %s", project.getStartDate(), project.getEndDate()));
        categoryText.setText(String.format("Category: %s", project.getCategory()));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        budgetText.setText(String.format("Budget: %s", currencyFormat.format(project.getBudget())));

        // Set up close button
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        }
        return super.onOptionsItemSelected(item);
    }
}