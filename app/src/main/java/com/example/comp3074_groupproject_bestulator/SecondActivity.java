package com.example.comp3074_groupproject_bestulator;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class SecondActivity extends AppCompatActivity implements DailyProjectAdapter.OnProjectClickListener {
    private DatabaseHelper dbHelper;
    private CalendarView calendarView;
    private TextView monthSummaryText;
    private TextView selectedDateText;
    private RecyclerView projectListRecyclerView;
    private DailyProjectAdapter adapter;
    private Map<String, List<Project>> projectsByDate;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.second_activity);

        dbHelper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        monthSummaryText = findViewById(R.id.monthSummaryText);
        selectedDateText = findViewById(R.id.selectedDateText);
        projectListRecyclerView = findViewById(R.id.projectListRecyclerView);

        // Set up RecyclerView
        projectListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DailyProjectAdapter(new ArrayList<>(), this);
        projectListRecyclerView.setAdapter(adapter);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Project Dashboard");

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.navigation_second_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadProjectDates();
        setupCalendar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProjectDates();
        updateMonthSummary(Calendar.getInstance());
    }

    private void loadProjectDates() {
        projectsByDate = new TreeMap<>();
        List<Project> projects = dbHelper.getAllProjects();

        for (Project project : projects) {
            try {
                Date endDate = dateFormat.parse(project.getEndDate());
                String dateKey = dateFormat.format(endDate);

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
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.set(year, month, dayOfMonth);

            String selectedDate = dateFormat.format(selectedCal.getTime());
            updateProjectList(selectedDate);
            updateMonthSummary(selectedCal);
        });

        // Set minimum date to today
        calendarView.setMinDate(System.currentTimeMillis() - 1000);

        // Set initial selections
        String today = dateFormat.format(new Date());
        updateProjectList(today);
        updateMonthSummary(Calendar.getInstance());
    }

    private void updateProjectList(String date) {
        selectedDateText.setText("Projects due on " + date);
        List<Project> projectsOnDate = projectsByDate.getOrDefault(date, new ArrayList<>());
        adapter.updateProjects(projectsOnDate);
    }

    private void updateMonthSummary(Calendar calendar) {
        int totalProjects = 0;
        double totalBudget = 0;
        Map<String, Integer> categoryCount = new HashMap<>();

        // Get the first and last day of the month
        Calendar firstDay = (Calendar) calendar.clone();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);

        Calendar lastDay = (Calendar) calendar.clone();
        lastDay.set(Calendar.DAY_OF_MONTH, lastDay.getActualMaximum(Calendar.DAY_OF_MONTH));

        // Count projects in the month
        for (Map.Entry<String, List<Project>> entry : projectsByDate.entrySet()) {
            try {
                Date projectDate = dateFormat.parse(entry.getKey());
                Calendar projectCal = Calendar.getInstance();
                projectCal.setTime(projectDate);

                if (!projectCal.before(firstDay) && !projectCal.after(lastDay)) {
                    List<Project> projects = entry.getValue();
                    totalProjects += projects.size();

                    for (Project project : projects) {
                        totalBudget += project.getBudget();
                        String category = project.getCategory();
                        categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Format the summary text
        StringBuilder summary = new StringBuilder();
        summary.append("Month Summary:\n");
        summary.append("Total Projects: ").append(totalProjects).append("\n");
        summary.append("Total Budget: ").append(NumberFormat.getCurrencyInstance().format(totalBudget)).append("\n");

        if (!categoryCount.isEmpty()) {
            summary.append("Categories:\n");
            for (Map.Entry<String, Integer> category : categoryCount.entrySet()) {
                summary.append("• ").append(category.getKey())
                        .append(": ").append(category.getValue())
                        .append(" projects\n");
            }
        }

        monthSummaryText.setText(summary.toString());
    }

    @Override
    public void onProjectClick(Project project) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.project_details_dialog);

        // Initialize dialog views
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
        contactText.setText(String.format("Contact:\nPhone: %s\nEmail: %s",
                project.getPhone(), project.getEmail()));
        datesText.setText(String.format("Duration: %s - %s",
                project.getStartDate(), project.getEndDate()));
        categoryText.setText(String.format("Category: %s", project.getCategory()));

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        budgetText.setText(String.format("Budget: %s", currencyFormat.format(project.getBudget())));

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
        goToCreateProject.setText("loading Creator...");
        startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
    }

    public void goToViewProjects(View v) {
        Button goToViewProjects = (Button) v;
        goToViewProjects.setText("loading List...");
        startActivity(new Intent(SecondActivity.this, FourthActivity.class));
    }
}