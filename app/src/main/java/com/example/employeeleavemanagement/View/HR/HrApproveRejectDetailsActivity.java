package com.example.employeeleavemanagement.View.HR;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.Model.Common.LeaveStatus;
import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HrApproveRejectDetailsActivity extends AppCompatActivity {

    MaterialTextView name_textView, email_textView, phone_textView, checkNo_textView, leaveType_textView,
            startDate_textView, endDate_textView, number_of_days_textView, reason_textView, created_at_textView,
            status_textView, review_textView,department_textView;

    MaterialButton approve_button, reject_button;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String leaveRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hr_approve_reject_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setupTopAppBar(topAppBar);

        // Retrieve the passed data from the intent extras
        leaveRequestId = getIntent().getStringExtra("leaveRequestId");
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
        String review = getIntent().getStringExtra("review");

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
        review_textView = findViewById(R.id.review_textView);
        department_textView = findViewById(R.id.department_textView);



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
        review_textView.setText(review);

        approve_button = findViewById(R.id.approve_button);
        reject_button = findViewById(R.id.reject_button);

        approve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog("Are you sure you want to approve this leave request?", "Approved");
            }
        });

        reject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog("Are you sure you want to reject this leave request?", "Rejected");
            }
        });


    }


    private void setupTopAppBar(MaterialToolbar topAppBar) {
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // or getActivity().onBackPressed(); if you're in a fragment
            }
        });
    }

    private void showConfirmationDialog(String message, final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateLeaveStatus(status);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateLeaveStatus(String status) {

        assert leaveRequestId!= null;
        db.collection("leaveRequests").document(leaveRequestId)
                .update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update successful
                        updateLeaveStatusInLeaveStatusCollection(status);
                    }
                })
                .addOnFailureListener(e -> AndroidUtil.ShowToast(getApplicationContext(), "Failed to update leave status"));
    }

    private void updateLeaveStatusInLeaveStatusCollection(String status) {

        assert leaveRequestId!= null;
        db.collection("leaveStatus").document(leaveRequestId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    LeaveStatus leaveStatus = documentSnapshot.toObject(LeaveStatus.class);
                    if (leaveStatus!= null) {
                        boolean isHrApproved = status.equals("Approved");
                        boolean isHrRejected = status.equals("Rejected");
                        leaveStatus.setHrApproved(isHrApproved);
                        leaveStatus.setHrRejected(isHrRejected);
                        db.collection("leaveStatus").document(leaveRequestId)
                                .set(leaveStatus)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Leave status updated successfully");
                                    }
                                })
                                .addOnFailureListener(e -> Log.w(TAG, "Error updating leave status", e));
                    } else {
                        // Handle the case where the LeaveStatus document doesn't exist
                        Log.w(TAG, "LeaveStatus document doesn't exist");
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error retrieving LeaveStatus document", e));
    }
}