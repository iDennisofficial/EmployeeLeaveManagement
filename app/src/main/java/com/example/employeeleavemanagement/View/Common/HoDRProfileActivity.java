package com.example.employeeleavemanagement.View.Common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.View.Common.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class HoDRProfileActivity extends AppCompatActivity {

    String Gender, Name, PhoneNumber, Birthday, Password, Email, EmployeeID;
    MaterialTextView textViewName, textViewEmail, gender_textView, birthday_textView, phone_textView, checkno_textView;
    MaterialButton log_out_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hod_hr_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        SharedPreferences sharedPreferences = getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        // Load the employee information from SharedPreferences
        Gender = sharedPreferences.getString("gender", "");
        PhoneNumber = sharedPreferences.getString("phoneNumber", "");
        Birthday = sharedPreferences.getString("birthday", "");
        Password = sharedPreferences.getString("password", "");
        Email = sharedPreferences.getString("email", "");
        EmployeeID = sharedPreferences.getString("employeeID", "");
        Name = sharedPreferences.getString("name", "");

        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        gender_textView = findViewById(R.id.gender_textView);
        birthday_textView = findViewById(R.id.birthday_textView);
        phone_textView = findViewById(R.id.phone_textView);
        checkno_textView = findViewById(R.id.checkno_textView);

        textViewName.setText(Name);
        textViewEmail.setText(Email);
        gender_textView.setText(Gender);
        birthday_textView.setText(Birthday);
        phone_textView.setText(PhoneNumber);
        checkno_textView.setText(EmployeeID);

        log_out_button = findViewById(R.id.log_out_button);

        log_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Start the Login Activity
                Intent intent = new Intent(HoDRProfileActivity.this, LoginActivity.class);
                startActivity(intent);

                // Finish the current activity
                finish();
            }
        });
    }
}