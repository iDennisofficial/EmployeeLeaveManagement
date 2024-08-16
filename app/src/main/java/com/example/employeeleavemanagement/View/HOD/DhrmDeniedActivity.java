package com.example.employeeleavemanagement.View.HOD;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.employeeleavemanagement.Controller.Employee.EmployeeLeaveRequestRecyclerViewAdapter;
import com.example.employeeleavemanagement.Model.Employee.LeaveRequest;
import com.example.employeeleavemanagement.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DhrmDeniedActivity extends AppCompatActivity {

    RecyclerView recyclerViewLeaveRequests;
    EmployeeLeaveRequestRecyclerViewAdapter employeeLeaveRequestRecyclerViewAdapter;
    FirebaseFirestore db;

    String leaveRequestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dhrm_denied);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setupTopAppBar(topAppBar);

        // Initialize the RecyclerView
        recyclerViewLeaveRequests = findViewById(R.id.recyclerViewLeaveRequests);
        recyclerViewLeaveRequests.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter
        recyclerViewLeaveRequests.setAdapter(employeeLeaveRequestRecyclerViewAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve leave requests from Firestore
        retrieveLeaveRequests();

    }

    private void retrieveLeaveRequests() {


        db.collection("leaveRequests")
                .whereEqualTo("status", "Denied")
                .whereEqualTo("department", getCurrentHODDepartment())
                .orderBy("queryTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<LeaveRequest> EmployeeleaveRequests = new ArrayList<>();
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
                            String HRreview = document.getString("hrreview");
                            String HoDreview = document.getString("hodreview");
                            String Dhrmreview = document.getString("dhrmComment");

                            // Create a LeaveRequestModel object with the extracted data
                            LeaveRequest leaveRequest = new LeaveRequest(employeeId, name, email, checkNo, homephone, department, leaveType,
                                    startDate, endDate, numberOfDays, reason, createdAt, status, HRreview, HoDreview, Dhrmreview );
                            // Add the LeaveRequestModel object to the ArrayList
                            EmployeeleaveRequests.add(leaveRequest);
                        }

                        // Initialize the adapter with the retrieved data
                        employeeLeaveRequestRecyclerViewAdapter = new EmployeeLeaveRequestRecyclerViewAdapter(this,EmployeeleaveRequests);
                        // Initialize the adapter
                        recyclerViewLeaveRequests.setAdapter(employeeLeaveRequestRecyclerViewAdapter);

                        employeeLeaveRequestRecyclerViewAdapter.notifyDataSetChanged();
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

    private void setupTopAppBar(MaterialToolbar topAppBar) {
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // or getActivity().onBackPressed(); if you're in a fragment
            }
        });
    }
}