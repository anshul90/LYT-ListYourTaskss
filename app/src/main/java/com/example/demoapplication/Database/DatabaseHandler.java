package com.example.demoapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "demoApplication";

    // Table name
    private static final String TABLE_REGISTERATION = "registeration_table";
    private static final String TABLE_PENDING_TASK = "pending_tasks_table";
    private static final String TABLE_COMPLETED_TASK = "completed_tasks_table";

    // Registerations Table Columns names
    private static final String KEY_ID = "id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String IMAGE = "image";

    // Pending/Completed Table Columns names
    private static final String TASK_KEY = "task_key";
    private static final String USER_EMAIL = "user_email";
    private static final String TASK_TITLE = "task_title";
    private static final String TASK_DESCRIPTION = "task_description";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REGISTERATION_TABLE = "CREATE TABLE " + TABLE_REGISTERATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + FIRST_NAME + " TEXT not null,"
                + LAST_NAME + " TEXT," + IMAGE + " TEXT," + EMAIL + " TEXT not null," + PASSWORD + " TEXT not null" + ")";
        db.execSQL(CREATE_REGISTERATION_TABLE);

        String CREATE_PENDING_TASKS_TABLE = "CREATE TABLE " + TABLE_PENDING_TASK + "("
                + TASK_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                TASK_DESCRIPTION + " TEXT," + USER_EMAIL + " TEXT not null," + TASK_TITLE + " TEXT not null" + ")";
        db.execSQL(CREATE_PENDING_TASKS_TABLE);

        String CREATE_COMPLETED_TASKS_TABLE = "CREATE TABLE " + TABLE_COMPLETED_TASK + "("
                + TASK_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                TASK_DESCRIPTION + " TEXT," + USER_EMAIL + " TEXT not null," + TASK_TITLE + " TEXT not null" + ")";
        db.execSQL(CREATE_COMPLETED_TASKS_TABLE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTERATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLETED_TASK);
        // Create tables again
        onCreate(db);
    }

    // Adding new Users
    public void insertNewUser(String first_name, String last_name, String email, String password, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIRST_NAME, first_name);
        values.put(LAST_NAME, last_name);
        values.put(EMAIL, email);
        values.put(PASSWORD, password);
        values.put(IMAGE, image);
        db.insert(TABLE_REGISTERATION, null, values);
        db.close();
    }

    // Adding new Task
    public void insertNewTask(String email, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_EMAIL, email);
        values.put(TASK_TITLE, title);
        values.put(TASK_DESCRIPTION, description);
        db.insert(TABLE_PENDING_TASK, null, values);
        db.close();
    }

    // Completing a Task
    public void insertCompletedTask(String email, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_EMAIL, email);
        values.put(TASK_TITLE, title);
        values.put(TASK_DESCRIPTION, description);
        db.insert(TABLE_COMPLETED_TASK, null, values);
        db.close();
    }


    // Getting All People
    public int matchRegisteration(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        int i = 0;
        Cursor res = db.rawQuery("SELECT  * FROM " + TABLE_REGISTERATION + " where email=? and password =?",
                new String[]{email.toLowerCase(), password.toLowerCase()});
        res.moveToFirst();
        i = res.getCount();
        res.close();
        return i;
    }

    public int getEmailValidation(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int i = 0;
        Cursor res = db.rawQuery("SELECT  * FROM " + TABLE_REGISTERATION + " where email=?",
                new String[]{email.toLowerCase()});
        res.moveToFirst();
        i = res.getCount();
        res.close();
        return i;
    }

    // Deleting table Registeration
    public void deleteRegisteration() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_REGISTERATION);
        db.close();
    }

    //Get Pending Tasks
    public Cursor getPendingTaskData(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor_pending = db.rawQuery(query, null);
        return cursor_pending;
    }

    // Deleting Pending Task
    public void deletePendingTask(String email, String Key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_PENDING_TASK + " where user_email=? and task_key =?",
                new String[]{email.toLowerCase(), Key});
        db.close();
    }

    // Deleting Completed Task
    public void deleteCompletedTask(String email, String Key) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_COMPLETED_TASK + " where user_email=? and task_key =?",
                new String[]{email.toLowerCase(), Key});
        db.close();
    }

    // Getting Registeration Count
    public int getRegisterationCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REGISTERATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

}
