package com.example.employeeleavemanagement.View.Employee;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

public class EmployeeLeaveRequestActivity extends AppCompatActivity {

    MaterialTextView name_textView, email_textView, phone_textView, checkNo_textView, leaveType_textView,
            startDate_textView, endDate_textView, number_of_days_textView, reason_textView, created_at_textView,
            status_textView,department_textView, HR_review_textView, HOD_review_textView, DHRM_review_textView, hr_Review, hod_Review, dhrm_Review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_leave_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setupTopAppBar(topAppBar);

        // Retrieve the passed data from the intent extras
        String leaveRequestId = getIntent().getStringExtra("leaveRequestId");
        String employeeId = getIntent().getStringExtra("employeeId");
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String checkNo = getIntent().getStringExtra("checkNo");
        String homephone = getIntent().getStringExtra("homephone");
        String department = getIntent().getStringExtra("department");
        String leaveType = getIntent().getStringExtra("leaveType");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        long numberOfDays = getIntent().getLongExtra("numberOfDays", 0);
        String numberOfDaysString = String.valueOf(numberOfDays);
        String reason = getIntent().getStringExtra("reason");
        String createdAt = getIntent().getStringExtra("createdAt");
        String status = getIntent().getStringExtra("status");
        String HR_review = getIntent().getStringExtra("hrReview");
        String HOD_review = getIntent().getStringExtra("hodReview");
        String DHRM_review = getIntent().getStringExtra("dhrmReview");

        name_textView = findViewById(R.id.name_textView);
        email_textView = findViewById(R.id.email_textView);
        phone_textView = findViewById(R.id.phone_textView);
        checkNo_textView = findViewById(R.id.checkNo_textView);
        leaveType_textView = findViewById(R.id.leaveType_textView);
        startDate_textView = findViewById(R.id.startDate_textView);
        endDate_textView = findViewById(R.id.endDate_textView);
        number_of_days_textView = findViewById(R.id.number_of_days_textView);
        reason_textView = findViewById(R.id.reason_textView);
        created_at_textView = findViewById(R.id.created_at_textView);
        status_textView = findViewById(R.id.status_textView);
        department_textView = findViewById(R.id.department_textView);
        HR_review_textView = findViewById(R.id.HR_review_textView);
        HOD_review_textView = findViewById(R.id.HOD_review_textView);
        DHRM_review_textView = findViewById(R.id.DHRM_review_textView);

        hr_Review = findViewById(R.id.hr_Review);
        hod_Review = findViewById(R.id.hod_Review);
        dhrm_Review = findViewById(R.id.dhrm_Review);




        name_textView.setText(name);
        email_textView.setText(email);
        phone_textView.setText(homephone);
        checkNo_textView.setText(checkNo);
        department_textView.setText(department);
        leaveType_textView.setText(leaveType);
        startDate_textView.setText(startDate);
        endDate_textView.setText(endDate);
        number_of_days_textView.setText(numberOfDaysString);
        reason_textView.setText(reason);
        created_at_textView.setText(createdAt);
        status_textView.setText(status);
        // Check and set the visibility and text for HOD_review_textView
        if (HOD_review != null) {
            HOD_review_textView.setText(HOD_review);
            HOD_review_textView.setVisibility(View.VISIBLE);
        } else {
            HOD_review_textView.setVisibility(View.GONE);
            hod_Review.setVisibility(View.GONE);
        }

// Check and set the visibility and text for HR_review_textView
        if (HR_review != null) {
            HR_review_textView.setText(HR_review);
            HR_review_textView.setVisibility(View.VISIBLE);
        } else {
            HR_review_textView.setVisibility(View.GONE);
            hr_Review.setVisibility(View.GONE);
        }

// Check and set the visibility and text for DHRM_review_textView
        if (DHRM_review != null) {
            DHRM_review_textView.setText(DHRM_review);
            DHRM_review_textView.setVisibility(View.VISIBLE);
        } else {
            DHRM_review_textView.setVisibility(View.GONE);
            dhrm_Review.setVisibility(View.GONE);
        }

    }

    private void setupTopAppBar(MaterialToolbar topAppBar) {
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // or getActivity().onBackPressed(); if you're in a fragment
            }
        });
    }
}