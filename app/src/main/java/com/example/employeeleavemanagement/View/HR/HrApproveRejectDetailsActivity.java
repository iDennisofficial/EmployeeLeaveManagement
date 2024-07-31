package com.example.employeeleavemanagement.View.HR;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.Model.Common.LeaveStatus;
import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HrApproveRejectDetailsActivity extends AppCompatActivity {

    MaterialTextView name_textView, email_textView, phone_textView, checkNo_textView, leaveType_textView,
            startDate_textView, endDate_textView, number_of_days_textView, reason_textView, created_at_textView,
            status_textView, department_textView, leavebalance_textView;

    MaterialButton approve_button, reject_button;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextInputEditText review_edit_text;
    TextInputLayout review_layout;

    String leaveRequestId, employeeId;
    long numberOfDays;

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
        employeeId = getIntent().getStringExtra("employeeId");
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String checkNo = getIntent().getStringExtra("checkNo");
        String homephone = getIntent().getStringExtra("homephone");
        String department = getIntent().getStringExtra("department");
        String leaveType = getIntent().getStringExtra("leaveType");
        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        numberOfDays = getIntent().getLongExtra("numberOfDays", 0);
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
        status_textView = findViewById(R.id.status_textView);
        created_at_textView = findViewById(R.id.created_at_textView);


        department_textView = findViewById(R.id.department_textView);
        leavebalance_textView = findViewById(R.id.leavebalance_textView);

        review_edit_text = findViewById(R.id.review_edit_text);
        review_layout = findViewById(R.id.review_layout);

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


        approve_button = findViewById(R.id.approve_button);
        reject_button = findViewById(R.id.reject_button);

        // Get the leave balance
        getLeaveBalance(employeeId, leaveType);

        approve_button.setOnClickListener(v -> showConfirmationDialog("Are you sure you want to verify this leave request?", "Verified"));

        reject_button.setOnClickListener(v -> showConfirmationDialog("Are you sure you want to reject this leave request?", "Rejected"));
    }

    private void setupTopAppBar(MaterialToolbar topAppBar) {
        topAppBar.setNavigationOnClickListener(v -> onBackPressed()); // or getActivity().onBackPressed(); if you're in a fragment
    }

    private void showConfirmationDialog(String message, final String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> updateLeaveStatus(status))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateLeaveStatus(String status) {
        String review = Objects.requireNonNull(review_edit_text.getText()).toString().trim();

        if (review.isEmpty()) {
            review_layout.setError("Please enter a review");
            return;
        }
        if (leaveRequestId != null) {
            db.collection("leaveRequests").document(leaveRequestId)
                    .update("status", status, "hrreview", review)
                    .addOnSuccessListener(aVoid -> {

                        review_edit_text.setText(""); // Clear the review entered

                        review_edit_text.setVisibility(View.GONE);
                        review_layout.setVisibility(View.GONE);

                        approve_button.setEnabled(false);
                        reject_button.setEnabled(false);
                        // Update successful
                        updateLeaveStatusInLeaveStatusCollection(status);
                    })
                    .addOnFailureListener(e -> AndroidUtil.ShowToast(getApplicationContext(), "Failed to update leave status"));
        }
    }

    private void updateLeaveStatusInLeaveStatusCollection(String status) {
        if (leaveRequestId != null) {
            db.collection("leaveStatus").document(leaveRequestId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        LeaveStatus leaveStatus = documentSnapshot.toObject(LeaveStatus.class);
                        if (leaveStatus != null) {
                            boolean isHrApproved = status.equals("Verified");
                            boolean isHrRejected = status.equals("Rejected");
                            leaveStatus.setHrApproved(isHrApproved);
                            leaveStatus.setHrRejected(isHrRejected);
                            db.collection("leaveStatus").document(leaveRequestId)
                                    .set(leaveStatus)
                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Leave status updated successfully"))
                                    .addOnFailureListener(e -> Log.w(TAG, "Error updating leave status", e));
                        } else {
                            Log.w(TAG, "LeaveStatus document doesn't exist");
                        }
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error retrieving LeaveStatus document", e));
        }
    }

    private static final Map<String, String> LEAVE_TYPE_MAPPING = new HashMap<>();
    static {
        LEAVE_TYPE_MAPPING.put("Annual Leave", "annualLeave");
        LEAVE_TYPE_MAPPING.put("Sick Leave", "sickLeave");
        LEAVE_TYPE_MAPPING.put("Sabbatical Leave", "sabbaticalLeave");
        LEAVE_TYPE_MAPPING.put("Maternity Leave", "maternityLeave");
        LEAVE_TYPE_MAPPING.put("Paternity Leave", "paternityLeave");
        LEAVE_TYPE_MAPPING.put("No Pay Leave", "noPayLeave");
    }

    private void getLeaveBalance(String userId, String leaveType) {
        String mappedLeaveType = LEAVE_TYPE_MAPPING.get(leaveType);
        if (mappedLeaveType == null) {
            handleInvalidLeaveType(leaveType);
            return;
        }

        DocumentReference leaveBalanceRef = db.collection("leaveBalance").document(userId);

        leaveBalanceRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Long leaveBalance = document.getLong(mappedLeaveType);
                            if (leaveBalance != null) {
                                compareLeaveBalance(leaveBalance, numberOfDays);
                                leavebalance_textView.setText(String.valueOf(leaveBalance));
                            } else {
                                handleInvalidLeaveType(leaveType);
                            }
                        } else {
                            handleLeaveBalanceDocumentNotFoundError();
                        }
                    } else {
                        handleErrorGettingLeaveBalanceDocument(task.getException());
                    }
                });
    }

    private void compareLeaveBalance(long leaveBalance, long numberOfDays) {
        Log.d(TAG, "Comparing leave balance: " + leaveBalance + " with number of days: " + numberOfDays);

        if (numberOfDays > leaveBalance) {
            // Insufficient leave balance
            approve_button.setEnabled(false);
            Toast.makeText(this, "Insufficient leave balance. You only have " + leaveBalance + " days available.", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Insufficient leave balance: " + leaveBalance + " days available.");
        } else {
            // Sufficient leave balance
            approve_button.setEnabled(true);
            Log.d(TAG, "Sufficient leave balance.");
        }
    }

    private void handleInvalidLeaveType(String leaveType) {
        Log.e(TAG, "Invalid leave type: " + leaveType);
        Toast.makeText(this, "Invalid leave type: " + leaveType, Toast.LENGTH_SHORT).show();
    }

    private void handleLeaveBalanceDocumentNotFoundError() {
        Log.e(TAG, "Leave balance document not found");
        Toast.makeText(this, "Leave balance document not found", Toast.LENGTH_SHORT).show();
    }

    private void handleErrorGettingLeaveBalanceDocument(Exception e) {
        Log.e(TAG, "Error getting leave balance document", e);
        Toast.makeText(this, "Error getting leave balance document", Toast.LENGTH_SHORT).show();
    }
}
