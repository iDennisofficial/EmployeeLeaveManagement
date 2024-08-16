package com.example.employeeleavemanagement.View.Employee;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employeeleavemanagement.Controller.Employee.EmployeeLeaveRequestRecyclerViewAdapter;
import com.example.employeeleavemanagement.Model.Employee.LeaveRequest;
import com.example.employeeleavemanagement.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ApprovedFragment extends Fragment {

    RecyclerView recyclerViewLeaveRequests;
    EmployeeLeaveRequestRecyclerViewAdapter employeeLeaveRequestRecyclerViewAdapter;
    FirebaseFirestore db;

    String leaveRequestId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_approved, container, false);



        // Initialize the RecyclerView
        recyclerViewLeaveRequests = view.findViewById(R.id.recyclerViewLeaveRequests);
        recyclerViewLeaveRequests.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize the adapter
        recyclerViewLeaveRequests.setAdapter(employeeLeaveRequestRecyclerViewAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve leave requests from Firestore
        retrieveLeaveRequests();

        return view;
    }

    private void retrieveLeaveRequests() {


        db.collection("leaveRequests")
                .whereEqualTo("status", "Approved")
                .whereEqualTo("employeeId", getCurrentEmployeeID())
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
                        employeeLeaveRequestRecyclerViewAdapter = new EmployeeLeaveRequestRecyclerViewAdapter(requireContext(),EmployeeleaveRequests);
                        // Initialize the adapter
                        recyclerViewLeaveRequests.setAdapter(employeeLeaveRequestRecyclerViewAdapter);

                        employeeLeaveRequestRecyclerViewAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Error retrieving leave requests", task.getException());
                    }
                });
    }

    private String getCurrentEmployeeID() {
        // Get the ID of the current user from SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("UID", "");
    }
}