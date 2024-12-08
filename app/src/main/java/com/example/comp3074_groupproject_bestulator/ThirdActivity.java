package com.example.comp3074_groupproject_bestulator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ThirdActivity extends AppCompatActivity {
    private EditText projectNameEdit, cityEdit, stateEdit, clientNameEdit, phoneEdit,
            emailEdit, startDateEdit, endDateEdit, categoryEdit, budgetEdit;
    private Spinner categorySpinner;
    private DatabaseHelper dbHelper;
    private Calendar startCalendar, endCalendar;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.third_activity);

        // Initialize calendars and date formatter
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

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
        budgetEdit = findViewById(R.id.editText12);

        // Initialize and setup Spinner
        setupCategorySpinner();

        // Clear default text
        clearDefaultText();

        // Set up date pickers
        setupDatePickers();

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.create_project);

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

    private void setupCategorySpinner() {
        try {
            categorySpinner = findViewById(R.id.categorySpinner);
            String[] categories = {"Construction", "Renovation", "Design", "Maintenance"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    categories
            );

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error setting up category spinner", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearDefaultText() {
        projectNameEdit.setText("");
        cityEdit.setText("");
        stateEdit.setText("");
        clientNameEdit.setText("");
        phoneEdit.setText("");
        emailEdit.setText("");
        categorySpinner.setSelection(0);
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
                    categorySpinner.getSelectedItem().toString(),
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
        return true;
    }

    private void clearForm() {
        projectNameEdit.setText("");
        cityEdit.setText("");
        stateEdit.setText("");
        clientNameEdit.setText("");
        phoneEdit.setText("");
        emailEdit.setText("");
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startDateEdit.setText(dateFormatter.format(startCalendar.getTime()));
        endDateEdit.setText(dateFormatter.format(endCalendar.getTime()));
        categorySpinner.setSelection(0);
        budgetEdit.setText("");
    }

    private void setupDatePickers() {
        // Make date EditTexts non-editable but clickable
        startDateEdit.setFocusable(false);
        endDateEdit.setFocusable(false);

        // Create date picker dialogs
        DatePickerDialog.OnDateSetListener startDateListener = (view, year, month, day) -> {
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH, month);
            startCalendar.set(Calendar.DAY_OF_MONTH, day);
            startDateEdit.setText(dateFormatter.format(startCalendar.getTime()));

            // Update end date minimum to start date
            endCalendar.setTimeInMillis(Math.max(endCalendar.getTimeInMillis(), startCalendar.getTimeInMillis()));
            endDateEdit.setText(dateFormatter.format(endCalendar.getTime()));
        };

        DatePickerDialog.OnDateSetListener endDateListener = (view, year, month, day) -> {
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH, month);
            endCalendar.set(Calendar.DAY_OF_MONTH, day);
            endDateEdit.setText(dateFormatter.format(endCalendar.getTime()));
        };

        // Set click listeners for date fields
        startDateEdit.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    ThirdActivity.this,
                    startDateListener,
                    startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH),
                    startCalendar.get(Calendar.DAY_OF_MONTH)
            );
            // Set minimum date to today
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dialog.show();
        });

        endDateEdit.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    ThirdActivity.this,
                    endDateListener,
                    endCalendar.get(Calendar.YEAR),
                    endCalendar.get(Calendar.MONTH),
                    endCalendar.get(Calendar.DAY_OF_MONTH)
            );
            // Set minimum date to start date
            dialog.getDatePicker().setMinDate(startCalendar.getTimeInMillis());
            dialog.show();
        });

        // Set initial dates
        startDateEdit.setText(dateFormatter.format(startCalendar.getTime()));
        endDateEdit.setText(dateFormatter.format(endCalendar.getTime()));
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