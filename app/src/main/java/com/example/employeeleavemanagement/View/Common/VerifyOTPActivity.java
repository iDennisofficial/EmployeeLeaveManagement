package com.example.employeeleavemanagement.View.Common;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.View.Employee.EmployeeDashboardActivity;
import com.google.android.material.button.MaterialButton;

public class VerifyOTPActivity extends AppCompatActivity {

    MaterialButton BtnVerifyOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BtnVerifyOTP = findViewById(R.id.BtnVerifyOTP);
        BtnVerifyOTP.setOnClickListener(v -> {
            Intent intent = new Intent(VerifyOTPActivity.this, EmployeeDashboardActivity.class);
            startActivity(intent);

        });
    }
}