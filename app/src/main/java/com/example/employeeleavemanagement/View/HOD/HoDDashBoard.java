package com.example.employeeleavemanagement.View.HOD;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.google.android.material.textview.MaterialTextView;

public class HoDDashBoard extends AppCompatActivity {

    private MaterialTextView TxtViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hod_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TxtViewDate = findViewById(R.id.TxtViewDate);
        String formattedDate = AndroidUtil.getFormattedDate();
        TxtViewDate.setText(formattedDate);
    }
}