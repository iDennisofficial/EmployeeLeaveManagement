package com.example.employeeleavemanagement.Utils;

import android.content.Context;
import android.widget.Toast;

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


    public static void sendNotification(String employeeId, String notificationMessage) {
        // Use FCM or another notification service to send the notification
        // For example, using FCM:
        String notificationTitle = "Leave Request Update";
        String notificationBody = notificationMessage;

      //  FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(employeeId + "@fcm.googleapis.com")
          //      .setMessageId(UUID.randomUUID().toString())
          //      .addData("title", notificationTitle)
          //      .addData("body", notificationBody)
            //    .build());
    }
}
