package com.example.employeeleavemanagement.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.employeeleavemanagement.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AndroidUtil {

     public static void ShowToast(Context context, String message)
         {
         Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
     }

    public static String getFormattedDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public static void sendNotification(Context context, String title, String message, Class<?> intentClass) {
        String channelID = "CHANNEL_ID_NOTIFICATION";

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelID);
        builder.setSmallIcon(R.drawable.eleavemoculogo) // Use the same icon for all notifications
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(context, intentClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = title;
                String channelDescription = message;
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
                channel.setDescription(channelDescription);
                notificationManager.createNotificationChannel(channel);
                Log.d("AndroidUtil", "Notification channel created successfully");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 123);
                    Log.w("AndroidUtil", "POST_NOTIFICATIONS permission not granted");
                    return;
                }
            }

            notificationManager.notify(0, builder.build());
            Log.d("AndroidUtil", "Notification sent successfully");
        } catch (Exception e) {
            Log.e("AndroidUtil", "Error creating notification channel", e);
        }
    }


}
