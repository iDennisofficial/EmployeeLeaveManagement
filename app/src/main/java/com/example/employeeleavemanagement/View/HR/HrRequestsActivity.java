package com.example.employeeleavemanagement.View.HR;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeleavemanagement.Controller.HOD.LeaveRequestReviewedRecyclerViewAdapter;
import com.example.employeeleavemanagement.Controller.HR.ApproveRejectRecyclerViewAdapter;
import com.example.employeeleavemanagement.Model.HOD.HoDLeaveRequestModel;
import com.example.employeeleavemanagement.Model.HR.HrApproveRejectModel;
import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.View.HOD.HoDReviewedRequestsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HrRequestsActivity extends AppCompatActivity {

    RecyclerView recyclerViewApproveReject;
    ApproveRejectRecyclerViewAdapter approveRejectRecyclerViewAdapter;
    FirebaseFirestore db;

    String leaveRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hr_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setupTopAppBar(topAppBar);

        // Initialize the RecyclerView
        recyclerViewApproveReject = findViewById(R.id.recyclerViewApproveReject);
        recyclerViewApproveReject.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter
        approveRejectRecyclerViewAdapter = new ApproveRejectRecyclerViewAdapter(HrRequestsActivity.this, new ArrayList<>());
        recyclerViewApproveReject.setAdapter(approveRejectRecyclerViewAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve leave requests from Firestore
        retrieveLeaveRequests();

    }

    private void retrieveLeaveRequests() {
        db.collection("leaveRequests")
                .whereEqualTo("status", "Reviewed")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<HrApproveRejectModel> leaveRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            leaveRequestId = document.getId(); // Get the ID of the leave request document

                            // Extract the data from the document
                            String employeeId = document.getString("employeeId");
                            String name = document.getString("name");
                            String email = document.getString("email");
                            String checkNo = document.getString("checkNo");
                            String homephone = document.getString("homephone");
                            String department = document.getString("department");
                            String leaveType = document.getString("leaveType");
                            String startDate = document.getString("startDate");
                            String endDate = document.getString("endDate");
                            long numberOfDays = document.getLong("numberOfDays");
                            String reason = document.getString("reason");
                            String createdAt = document.getString("createdAt");
                            String status = document.getString("status");
                            String review = document.getString("review");

                            //  AndroidUtil.ShowToast(getApplicationContext(),"The review is " + review );

                            // Create a HoDLeaveRequestModel object with the extracted data
                            HrApproveRejectModel hrApproveRejectModel = new HrApproveRejectModel(leaveRequestId,
                                    employeeId, name, email, checkNo, homephone, department, leaveType, startDate,
                                    endDate, numberOfDays, reason, createdAt, status, review);

                            // Add the HoDLeaveRequestModel object to the ArrayList
                            leaveRequests.add(hrApproveRejectModel);
                        }

                        // Update the adapter with the retrieved data
                        approveRejectRecyclerViewAdapter.updateData(leaveRequests);
                        approveRejectRecyclerViewAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error retrieving leave requests", task.getException());
                    }
                });
    }

    private void setupTopAppBar(MaterialToolbar topAppBar) {
        topAppBar.setNavigationOnClickListener(v -> {
            onBackPressed(); // or getActivity().onBackPressed(); if you're in a fragment
        });

    }
}