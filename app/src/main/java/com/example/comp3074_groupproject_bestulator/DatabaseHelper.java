package com.example.comp3074_groupproject_bestulator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ProjectManager.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_PROJECTS = "projects";

    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PROJECT_NAME = "project_name";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_CLIENT_NAME = "client_name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_END_DATE = "end_date";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_BUDGET = "budget";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROJECTS_TABLE = "CREATE TABLE " + TABLE_PROJECTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PROJECT_NAME + " TEXT,"
                + COLUMN_CITY + " TEXT,"
                + COLUMN_STATE + " TEXT,"
                + COLUMN_CLIENT_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_START_DATE + " TEXT,"
                + COLUMN_END_DATE + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_BUDGET + " REAL"
                + ")";
        db.execSQL(CREATE_PROJECTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
        onCreate(db);
    }

    // Add a new project
    public long addProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PROJECT_NAME, project.getProjectName());
        values.put(COLUMN_CITY, project.getCity());
        values.put(COLUMN_STATE, project.getState());
        values.put(COLUMN_CLIENT_NAME, project.getClientName());
        values.put(COLUMN_PHONE, project.getPhone());
        values.put(COLUMN_EMAIL, project.getEmail());
        values.put(COLUMN_START_DATE, project.getStartDate());
        values.put(COLUMN_END_DATE, project.getEndDate());
        values.put(COLUMN_CATEGORY, project.getCategory());
        values.put(COLUMN_BUDGET, project.getBudget());

        long id = db.insert(TABLE_PROJECTS, null, values);
        db.close();
        return id;
    }

    // Get all projects
    public List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PROJECTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Project project = new Project();
                project.setId(cursor.getLong(0));
                project.setProjectName(cursor.getString(1));
                project.setCity(cursor.getString(2));
                project.setState(cursor.getString(3));
                project.setClientName(cursor.getString(4));
                project.setPhone(cursor.getString(5));
                project.setEmail(cursor.getString(6));
                project.setStartDate(cursor.getString(7));
                project.setEndDate(cursor.getString(8));
                project.setCategory(cursor.getString(9));
                project.setBudget(cursor.getDouble(10));

                projectList.add(project);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return projectList;
    }
}