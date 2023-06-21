package com.example.bottomnavigation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.legacy.content.WakefulBroadcastReceiver;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String desiredTime = "13:16";

        String[] timeParts = desiredTime.split(":");
        int desiredHour = Integer.parseInt(timeParts[0]);
        int desiredMinute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        if (currentHour == desiredHour && currentMinute == desiredMinute) {
            // Schedule the notification using AlarmManager
            scheduleNotification(context);
        }
    }

    private void scheduleNotification(Context context) {
        // Intent for the NotificationService
        //Intent serviceIntent = new Intent(context, NotificationService.class);
        Intent serviceIntent = new Intent(context.getApplicationContext(), NotificationService.class);

        // PendingIntent to start the service
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the desired time for the notification
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 13); // Replace with desired hour
        calendar.set(Calendar.MINUTE, 0); // Replace with desired minute

        // Schedule the notification using AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
}
