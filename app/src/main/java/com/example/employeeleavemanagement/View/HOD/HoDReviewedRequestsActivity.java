package com.example.employeeleavemanagement.View.HOD;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeleavemanagement.Controller.HOD.LeaveRequestReviewedRecyclerViewAdapter;
import com.example.employeeleavemanagement.Model.HOD.HoDLeaveRequestModel;
import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HoDReviewedRequestsActivity extends AppCompatActivity {

    RecyclerView recyclerViewReviewedLeaveRequests;
    LeaveRequestReviewedRecyclerViewAdapter leaveRequestReviewedRecyclerViewAdapter;
    FirebaseFirestore db;

    String leaveRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hod_reviewed_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the RecyclerView
        recyclerViewReviewedLeaveRequests = findViewById(R.id.recyclerViewLeaveRequests);
        recyclerViewReviewedLeaveRequests.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter
        leaveRequestReviewedRecyclerViewAdapter = new LeaveRequestReviewedRecyclerViewAdapter(HoDReviewedRequestsActivity.this, new ArrayList<>());
        recyclerViewReviewedLeaveRequests.setAdapter(leaveRequestReviewedRecyclerViewAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve leave requests from Firestore
        retrieveLeaveRequests();
    }

    private void retrieveLeaveRequests() {
        db.collection("leaveRequests")
                .whereEqualTo("status", "Reviewed")
                .whereEqualTo("department", getCurrentHODDepartment())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<HoDLeaveRequestModel> leaveRequests = new ArrayList<>();
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
                            HoDLeaveRequestModel hoDLeaveRequestModel = new HoDLeaveRequestModel(leaveRequestId,
                                    employeeId, name, email, checkNo, homephone, department, leaveType, startDate,
                                    endDate, numberOfDays, reason, createdAt, status, review);

                            // Add the HoDLeaveRequestModel object to the ArrayList
                            leaveRequests.add(hoDLeaveRequestModel);
                        }

                        // Update the adapter with the retrieved data
                        leaveRequestReviewedRecyclerViewAdapter.updateData(leaveRequests);
                        leaveRequestReviewedRecyclerViewAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error retrieving leave requests", task.getException());
                    }
                });
    }

    private String getCurrentHODDepartment() {
        // Get the department of the current HOD from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("department", "");
    }
}