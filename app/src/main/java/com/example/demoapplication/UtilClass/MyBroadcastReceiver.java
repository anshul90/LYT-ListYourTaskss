package com.example.demoapplication.UtilClass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Settings;
import android.support.v4.app.TaskStackBuilder;

import com.example.demoapplication.ClassFiles.SplashScreenActivity;
import com.example.demoapplication.Database.DatabaseHandler;
import com.example.demoapplication.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyBroadcastReceiver extends BroadcastReceiver {
    NotificationManager manager;
    Notification myNotication;
    DatabaseHandler databaseHandler;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AppPreferences.getIsLogin(context, AppUtils.ISLOGIN)) {
            String email = "";
            List<String> tasksTitlesList = null;
            List<String> tasksIdsList = null;
            if (tasksTitlesList != null) {
                tasksTitlesList.clear();
                tasksTitlesList = null;
                tasksTitlesList = new ArrayList<>();
            } else tasksTitlesList = new ArrayList<>();
            if (tasksIdsList != null) {
                tasksIdsList.clear();
                tasksIdsList = null;
                tasksIdsList = new ArrayList<>();
            } else tasksIdsList = new ArrayList<>();
            databaseHandler = new DatabaseHandler(context);
            Cursor cursor = databaseHandler.getPendingTaskData("SELECT * FROM " + "pending_tasks_table");
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    email = cursor.getString(2);
                    if (email.equals(AppPreferences.getUserEmail(context, AppUtils.EMAIL))) {
                        if (!tasksIdsList.contains(cursor.getString(0))) {
                            tasksIdsList.add(cursor.getString(0));
                            tasksTitlesList.add(cursor.getString(3));
                        }

                    }
                }
                if (email != null && !email.isEmpty()) {
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);
                    manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    Notification.Builder builder = new Notification.Builder(context);

                    builder.setAutoCancel(false);
                    builder.setTicker("this is ticker text");
                    builder.setContentTitle("Please complete yout Pending Task");
                    builder.setContentText("" + tasksTitlesList.toString().replace("[", "").replace("]", ""));
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                    builder.setContentIntent(pendingIntent);
                    builder.setOngoing(false);
                    builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
                    //  builder.setSubText("This is subtext...");   //API level 16
                    //     builder.setNumber(100);
                    builder.setAutoCancel(true);
                    Intent resultIntent = new Intent(context, SplashScreenActivity.class);
                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(resultPendingIntent);
                    if (tasksTitlesList.size() > 0) {
                        builder.build();
                        myNotication = builder.getNotification();
                        manager.notify(11, myNotication);
                    }
                }

            }
        }
    }
}