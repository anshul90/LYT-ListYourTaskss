package com.example.demoapplication.ClassFiles;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapplication.Database.DatabaseHandler;
import com.example.demoapplication.Fragments.AboutUsFragment;
import com.example.demoapplication.Fragments.ContactUsFragment;
import com.example.demoapplication.Fragments.HomeFragment;
import com.example.demoapplication.R;
import com.example.demoapplication.UtilClass.AppPreferences;
import com.example.demoapplication.UtilClass.AppUtils;
import com.example.demoapplication.UtilClass.MyBroadcastReceiver;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static MainActivity mInstance = null;
    public DrawerLayout mDrawerLayout;
    public ImageView imageView_changeToList, imageView_changeToGrid;
    FragmentManager mFragmentManager;
    Fragment fragment = null;
    Dialog dialog;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private RelativeLayout header_layout;
    private NavigationView navigationView;

    public static MainActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        header_layout = (RelativeLayout) findViewById(R.id.header_layout);
        imageView_changeToGrid = (ImageView) findViewById(R.id.imageView_changeToGrid);
        imageView_changeToList = (ImageView) findViewById(R.id.imageView_changeToList);
        imageView_changeToList.setVisibility(View.GONE);
        imageView_changeToGrid.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        mInstance = this;
        HomeFragment fHome = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fHome).commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        imageView_changeToList.setOnClickListener(this);
        imageView_changeToGrid.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                openDialog(MainActivity.this);
            }
        });
        startAlert();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        if (menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);

        switch (menuItem.getItemId()) {

            case R.id.nMyTask:
                HomeFragment fHome = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fHome).commit();
                header_layout.setVisibility(View.VISIBLE);
                imageView_changeToGrid.setVisibility(View.VISIBLE);
                imageView_changeToList.setVisibility(View.GONE);
                break;

            case R.id.nAboutUs:
                AboutUsFragment fragAbout = new AboutUsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragAbout).commit();
                header_layout.setVisibility(View.GONE);
                break;

            case R.id.nContactUs:
                ContactUsFragment fragContact = new ContactUsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragContact).commit();
                header_layout.setVisibility(View.GONE);
                break;

            case R.id.logOut:
                AppPreferences.setIsLogin(MainActivity.this, AppUtils.ISLOGIN, false);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        if (fragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.frame_container, fragment)
                    .commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void changeLayoutManager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame_container);
        if (fragment instanceof HomeFragment) {
            ((HomeFragment) fragment).changeLayoutManager();
        } else {
            header_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_changeToGrid:
                changeLayoutManager();
                break;
            case R.id.imageView_changeToList:
                changeLayoutManager();
                break;
            default:
                break;
        }
    }

    public void openDialog(Context con) {
        // TODO Auto-generated method stub
        dialog = new Dialog(con);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.add_task_dialog);
        TextView submit_tv = (TextView) dialog.findViewById(R.id.submit_tv);
        final EditText title_editText = (EditText) dialog.findViewById(R.id.title_editText);
        final EditText description_editText = (EditText) dialog.findViewById(R.id.description_editText);
        submit_tv.setTypeface(Typeface.createFromAsset(con.getAssets(),
                "fonts/aller_regular.ttf"), Typeface.BOLD);
        title_editText.setTypeface(Typeface.createFromAsset(con.getAssets(),
                "fonts/aller_regular.ttf"));
        description_editText.setTypeface(Typeface.createFromAsset(con.getAssets(),
                "fonts/aller_regular.ttf"));
        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title_editText.getText().toString().trim().length() == 0) {
                    title_editText.setError("Please enter a title atleast.");
                } else {
                    DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
                    databaseHandler.insertNewTask(AppPreferences.getUserEmail(MainActivity.this, AppUtils.EMAIL),
                            title_editText.getText().toString().trim(),
                            description_editText.getText().toString().trim());
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.frame_container);
                    if (fragment instanceof HomeFragment) {
                        ((HomeFragment) fragment).onRefresh();
                    }
                    Toast.makeText(MainActivity.this, "Your new task has been added successfully.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
        // Setting Dialog Message
        // Setting Positive "Yes" Button
        dialog.show();
    }

    public void startAlert() {
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (6 * 1000), 60000, pendingIntent);
    }
}
