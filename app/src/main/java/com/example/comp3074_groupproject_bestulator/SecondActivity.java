package com.example.comp3074_groupproject_bestulator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SecondActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private CalendarView calendarView;
    private TextView projectListText;
    private Map<String, List<Project>> projectsByDate;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.second_activity);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize date formatter
        dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        projectListText = findViewById(R.id.projectListText);

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.navigation_second_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load projects and set up calendar
        loadProjectDates();
        setupCalendar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProjectDates(); // Reload projects when returning to this activity
    }

    private void loadProjectDates() {
        projectsByDate = new HashMap<>();
        List<Project> projects = dbHelper.getAllProjects();

        for (Project project : projects) {
            try {
                // Parse the end date string to get a Date object
                Date endDate = dateFormat.parse(project.getEndDate());
                String dateKey = dateFormat.format(endDate);

                // Add project to the list for this date
                if (!projectsByDate.containsKey(dateKey)) {
                    projectsByDate.put(dateKey, new ArrayList<>());
                }
                projectsByDate.get(dateKey).add(project);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Create a Calendar instance for the selected date
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);

            // Format the date to match our stored format
            String selectedDate = dateFormat.format(calendar.getTime());

            // Update the text view with projects due on the selected date
            updateProjectList(selectedDate);
        });

        // Set minimum date to today
        calendarView.setMinDate(System.currentTimeMillis() - 1000);

        // Set initial project list to today's date
        String today = dateFormat.format(new Date());
        updateProjectList(today);
    }

    private void updateProjectList(String date) {
        List<Project> projectsOnDate = projectsByDate.get(date);

        if (projectsOnDate != null && !projectsOnDate.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Projects due on ").append(date).append(":\n\n");

            for (Project project : projectsOnDate) {
                sb.append("• ").append(project.getProjectName())
                        .append(" (").append(project.getClientName()).append(")\n");
            }

            projectListText.setText(sb.toString());
            projectListText.setVisibility(View.VISIBLE);
        } else {
            projectListText.setText("No projects due on " + date);
        }
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
            startActivity(new Intent(SecondActivity.this, MainActivity.class));
            return true;
        } else if (id == R.id.navigation_second_activity) {
            startActivity(new Intent(SecondActivity.this, SecondActivity.class));
            return true;
        } else if (id == R.id.navigation_third_activity) {
            startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
            return true;
        } else if (id == R.id.navigation_fourth_activity) {
            startActivity(new Intent(SecondActivity.this, FourthActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToCreateProject(View v) {
        Button goToCreateProject = (Button) v;
        goToCreateProject.setText("changing...");
        startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
    }

    public void goToViewProjects(View v) {
        Button goToViewProjects = (Button) v;
        goToViewProjects.setText("changing...");
        startActivity(new Intent(SecondActivity.this, FourthActivity.class));
    }
}