package com.example.comp3074_groupproject_bestulator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ThirdActivity extends AppCompatActivity {
    private EditText projectNameEdit, cityEdit, stateEdit, clientNameEdit, phoneEdit,
            emailEdit, startDateEdit, endDateEdit, categoryEdit, budgetEdit;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.third_activity);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize EditText fields
        projectNameEdit = findViewById(R.id.editText2);
        cityEdit = findViewById(R.id.editText3);
        stateEdit = findViewById(R.id.editText4);
        clientNameEdit = findViewById(R.id.editText6);
        phoneEdit = findViewById(R.id.editText7);
        emailEdit = findViewById(R.id.editText8);
        startDateEdit = findViewById(R.id.editText9);
        endDateEdit = findViewById(R.id.editText10);
        categoryEdit = findViewById(R.id.editText11);
        budgetEdit = findViewById(R.id.editText12);

        // Clear default text
        clearDefaultText();

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Project");

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.navigation_third_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up create button
        Button createButton = findViewById(R.id.button);
        createButton.setOnClickListener(v -> createProject());
    }

    private void clearDefaultText() {
        projectNameEdit.setText("");
        cityEdit.setText("");
        stateEdit.setText("");
        clientNameEdit.setText("");
        phoneEdit.setText("");
        emailEdit.setText("");
        startDateEdit.setText("");
        endDateEdit.setText("");
        categoryEdit.setText("");
        budgetEdit.setText("");
    }

    private void createProject() {
        // Validate input
        if (!validateInput()) {
            return;
        }

        try {
            // Create new project object
            Project project = new Project(
                    projectNameEdit.getText().toString(),
                    cityEdit.getText().toString(),
                    stateEdit.getText().toString(),
                    clientNameEdit.getText().toString(),
                    phoneEdit.getText().toString(),
                    emailEdit.getText().toString(),
                    startDateEdit.getText().toString(),
                    endDateEdit.getText().toString(),
                    categoryEdit.getText().toString(),
                    Double.parseDouble(budgetEdit.getText().toString())
            );

            // Save to database
            long id = dbHelper.addProject(project);

            if (id != -1) {
                Toast.makeText(this, "Project created successfully!", Toast.LENGTH_SHORT).show();
                // Navigate back to project list or clear form
                clearForm();
            } else {
                Toast.makeText(this, "Error creating project", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid budget amount", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput() {
        if (projectNameEdit.getText().toString().trim().isEmpty()) {
            projectNameEdit.setError("Project name is required");
            return false;
        }
        if (clientNameEdit.getText().toString().trim().isEmpty()) {
            clientNameEdit.setError("Client name is required");
            return false;
        }
        if (budgetEdit.getText().toString().trim().isEmpty()) {
            budgetEdit.setError("Budget is required");
            return false;
        }
        // Add more validation as needed
        return true;
    }

    private void clearForm() {
        projectNameEdit.setText("");
        cityEdit.setText("");
        stateEdit.setText("");
        clientNameEdit.setText("");
        phoneEdit.setText("");
        emailEdit.setText("");
        startDateEdit.setText("");
        endDateEdit.setText("");
        categoryEdit.setText("");
        budgetEdit.setText("");
    }

    // Your existing menu methods...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigation_home) {
            startActivity(new Intent(ThirdActivity.this, MainActivity.class));
            return true;
        } else if (id == R.id.navigation_second_activity) {
            startActivity(new Intent(ThirdActivity.this, SecondActivity.class));
            return true;
        } else if (id == R.id.navigation_third_activity) {
            startActivity(new Intent(ThirdActivity.this, ThirdActivity.class));
            return true;
        } else if (id == R.id.navigation_fourth_activity) {
            startActivity(new Intent(ThirdActivity.this, FourthActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}