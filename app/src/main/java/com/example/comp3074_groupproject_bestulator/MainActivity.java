package com.example.comp3074_groupproject_bestulator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar as the Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set a title for the Toolbar
        getSupportActionBar().setTitle("Home");



//        EdgeToEdge.enable(this);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

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
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            return true;
        } else if (id == R.id.navigation_second_activity) {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
            return true;
        } else if (id == R.id.navigation_third_activity) {
            startActivity(new Intent(MainActivity.this, ThirdActivity.class));
            return true;
        } else if (id == R.id.navigation_fourth_activity) {
            startActivity(new Intent(MainActivity.this, FourthActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void goToDashboard(View v) {


        Button goToScreen2 = (Button) v;
        goToScreen2.setText("changing...");

        Intent intent = new Intent(MainActivity.this, SecondActivity.class);

        startActivity(intent);
    }
}