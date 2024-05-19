package com.example.employeeleavemanagement.Utils;

import android.content.Context;
import android.widget.Toast;

public class AndroidUtil {

     public static void ShowToast(Context context, String message)
         {
         Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
     }
}
