package com.example.employeeleavemanagement.View.Employee;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.example.employeeleavemanagement.View.Common.ProfileActivity;
import com.example.employeeleavemanagement.View.HOD.HoDDashBoard;
import com.example.employeeleavemanagement.View.HR.HRDashboard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class DashboardFragment extends Fragment {

    MaterialTextView TxtViewDate, TextViewEmployeeName, TextViewEmployeeCheckNo,
            TextViewUsedAnnual, TextViewUsedSick, TextViewUsedSabbatical, TextViewUsedMaternity, TextViewUsedPaternity, TextViewUsedNoPay;

    String gender, name, phoneNumber, birthday, password, email, employeeID, department;
    String Gender, Name, PhoneNumber, Birthday, Password, Email, EmployeeID, Department;

    MaterialToolbar topAppBar;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        TxtViewDate = view.findViewById(R.id.TxtViewDate);
        TextViewEmployeeName = view.findViewById(R.id.TextViewEmployeeName);
        TextViewEmployeeCheckNo = view.findViewById(R.id.TextViewEmployeeCheckNo);

        String formattedDate = AndroidUtil.getFormattedDate();
        TxtViewDate.setText(formattedDate);

        topAppBar = view.findViewById(R.id.topAppBar);
        setupTopAppBar(topAppBar);

        TextViewUsedAnnual = view.findViewById(R.id.TextViewUsedAnnual);
        TextViewUsedSick = view.findViewById(R.id.TextViewUsedSick);
        TextViewUsedSabbatical = view.findViewById(R.id.TextViewUsedSabbatical);
        TextViewUsedMaternity = view.findViewById(R.id.TextViewUsedMaternity);
        TextViewUsedPaternity = view.findViewById(R.id.TextViewUsedPaternity);
        TextViewUsedNoPay = view.findViewById(R.id.TextViewUsedNoPay);




        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        boolean isEmployeeInfoFetched = sharedPreferences.getBoolean("isEmployeeInfoFetched", false);

        if (!isEmployeeInfoFetched) {
            fetchEmployeeInfo();
        } else {
            // Load the employee information from SharedPreferences
            loadEmployeeInfoFromSharedPreferences(TextViewEmployeeName, TextViewEmployeeCheckNo);
        }


        // Retrieve the oldest leave request
        TextView usernameTextView = view.findViewById(R.id.usernameTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        TextView typeTextView = view.findViewById(R.id.typeTextView);
        TextView statusTextView = view.findViewById(R.id.statusTextView);


        db.collection("leaveRequests")
                .whereEqualTo("employeeId", uid)
                .orderBy("queryTime", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(taskOldest -> {
                    if (taskOldest.isSuccessful() && !taskOldest.getResult().isEmpty()) {
                        QueryDocumentSnapshot oldestDocument = (QueryDocumentSnapshot) taskOldest.getResult().getDocuments().get(0);
                        String oldestEmployeeName = oldestDocument.getString("name");
                        String oldestLeaveType = oldestDocument.getString("leaveType");
                        String oldestStartDate = oldestDocument.getString("createdAt");
                        String oldestStatus = oldestDocument.getString("status");

                        // Assuming these are the TextViews where you display the employee details
                        usernameTextView.setText(oldestEmployeeName);
                        dateTextView.setText(oldestStartDate);
                        typeTextView.setText(oldestLeaveType);
                        statusTextView.setText(oldestStatus);

                    // Check if the status is "Approved", "Denied", "Qualified", or "Unqualified" before sending the notification
                        if ("Approved".equals(oldestStatus) || "Denied".equals(oldestStatus) ||
                                "Qualified".equals(oldestStatus) || "Unqualified".equals(oldestStatus)) {
                            // Make sure to pass the actual activity class you want to open with the notification
                            AndroidUtil.sendNotification(getContext(), "Leave Information", "Your Leave Application has been " + oldestStatus, getActivity().getClass());
                        }

                    } else {
                        // Enhanced error handling
                        if (taskOldest.getException() != null) {
                            Log.e(TAG, "Error retrieving oldest leave request", taskOldest.getException());
                        } else {
                            Log.e(TAG, "No matching leave requests found.");
                        }
                        AndroidUtil.ShowToast(requireContext(), "Not getting the values");
                    }
                });

        fetchLeaveBalances();




        return view;
    }

    private void fetchEmployeeInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching employee information...");
        progressDialog.show();

        db.collection("employee").document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        progressDialog.dismiss();
                        if (documentSnapshot.exists()) {
                            gender = documentSnapshot.getString("gender");
                            phoneNumber = documentSnapshot.getString("phoneNumber");
                            birthday = documentSnapshot.getString("birthday");
                            password = documentSnapshot.getString("password");
                            email = documentSnapshot.getString("email");
                            employeeID = documentSnapshot.getString("employeeID");
                            name = documentSnapshot.getString("name");
                            department = documentSnapshot.getString("department");

                            // Store the employee information in SharedPreferences
                            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("gender", gender);
                            editor.putString("phoneNumber", phoneNumber);
                            editor.putString("birthday", birthday);
                            editor.putString("password", password);
                            editor.putString("email", email);
                            editor.putString("employeeID", employeeID);
                            editor.putString("name", name);
                            editor.putString("department", department);
                            editor.putString("UID", uid);
                            editor.putBoolean("isEmployeeInfoFetched", true);
                            editor.apply();

                            loadEmployeeInfoFromSharedPreferences(TextViewEmployeeName, TextViewEmployeeCheckNo);
                        } else {
                            Log.d(TAG, "No employee information found");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.w(TAG, "Error fetching employee information", e);
                    }
                });
    }

    private void loadEmployeeInfoFromSharedPreferences(MaterialTextView textViewEmployeeName, MaterialTextView textViewEmployeeCheckNo) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        Gender = sharedPreferences.getString("gender", "");
        PhoneNumber = sharedPreferences.getString("phoneNumber", "");
        Birthday = sharedPreferences.getString("birthday", "");
        Password = sharedPreferences.getString("password", "");
        Email = sharedPreferences.getString("email", "");
        EmployeeID = sharedPreferences.getString("employeeID", "");
        Name = sharedPreferences.getString("name", "");
        Department = sharedPreferences.getString("department", "");

        if (textViewEmployeeName != null) {
            textViewEmployeeName.setText(Name);
        }
        if (textViewEmployeeCheckNo != null) {
            textViewEmployeeCheckNo.setText(EmployeeID);
        }
    }

    private void setupTopAppBar(MaterialToolbar topAppBar) {
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed(); // or getActivity().onBackPressed(); if you're in a fragment
            }
        });

    }

    private void fetchLeaveBalances() {
        db.collection("leaveBalance").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the current leave balances from Firestore as long
                        Long annualLeaveLong = documentSnapshot.getLong("annualLeave");
                        Long maternityLeaveLong = documentSnapshot.getLong("maternityLeave");
                        Long noPayLeaveLong = documentSnapshot.getLong("noPayLeave");
                        Long paternityLeaveLong = documentSnapshot.getLong("paternityLeave");
                        Long sabbaticalLeaveLong = documentSnapshot.getLong("sabbaticalLeave");
                        Long sickLeaveLong = documentSnapshot.getLong("sickLeave");

                        // Log the fetched values
                        Log.d(TAG, "Annual Leave: " + annualLeaveLong);
                        Log.d(TAG, "Maternity Leave: " + maternityLeaveLong);
                        Log.d(TAG, "No Pay Leave: " + noPayLeaveLong);
                        Log.d(TAG, "Paternity Leave: " + paternityLeaveLong);
                        Log.d(TAG, "Sabbatical Leave: " + sabbaticalLeaveLong);
                        Log.d(TAG, "Sick Leave: " + sickLeaveLong);

                        // Convert the fetched values to long, or handle null values
                        long annualLeave = annualLeaveLong != null ? annualLeaveLong : 0;
                        long maternityLeave = maternityLeaveLong != null ? maternityLeaveLong : 0;
                        long noPayLeave = noPayLeaveLong != null ? noPayLeaveLong : 0;
                        long paternityLeave = paternityLeaveLong != null ? paternityLeaveLong : 0;
                        long sabbaticalLeave = sabbaticalLeaveLong != null ? sabbaticalLeaveLong : 0;
                        long sickLeave = sickLeaveLong != null ? sickLeaveLong : 0;

                        // Define the maximum leave limits
                        final long MAX_ANNUAL_LEAVE = 28L;
                        final long MAX_MATERNITY_LEAVE = 84L;
                        final long MAX_NO_PAY_LEAVE = 365L;
                        final long MAX_PATERNITY_LEAVE = 7L;
                        final long MAX_SABBATICAL_LEAVE = 365L;
                        final long MAX_SICK_LEAVE = 180L;

                        // Calculate the remaining leave balances
                        long remainingAnnualLeave = MAX_ANNUAL_LEAVE - annualLeave;
                        long remainingMaternityLeave = MAX_MATERNITY_LEAVE - maternityLeave;
                        long remainingNoPayLeave = MAX_NO_PAY_LEAVE - noPayLeave;
                        long remainingPaternityLeave = MAX_PATERNITY_LEAVE - paternityLeave;
                        long remainingSabbaticalLeave = MAX_SABBATICAL_LEAVE - sabbaticalLeave;
                        long remainingSickLeave = MAX_SICK_LEAVE - sickLeave;

                        // Log the remaining balances
                        Log.d(TAG, "Remaining Annual Leave: " + remainingAnnualLeave);
                        Log.d(TAG, "Remaining Maternity Leave: " + remainingMaternityLeave);
                        Log.d(TAG, "Remaining No Pay Leave: " + remainingNoPayLeave);
                        Log.d(TAG, "Remaining Paternity Leave: " + remainingPaternityLeave);
                        Log.d(TAG, "Remaining Sabbatical Leave: " + remainingSabbaticalLeave);
                        Log.d(TAG, "Remaining Sick Leave: " + remainingSickLeave);

                        // Update the TextViews with the remaining balances
                        TextViewUsedAnnual.setText(String.valueOf(remainingAnnualLeave));
                        TextViewUsedMaternity.setText(String.valueOf(remainingMaternityLeave));
                        TextViewUsedNoPay.setText(String.valueOf(remainingNoPayLeave));
                        TextViewUsedPaternity.setText(String.valueOf(remainingPaternityLeave));
                        TextViewUsedSabbatical.setText(String.valueOf(remainingSabbaticalLeave));
                        TextViewUsedSick.setText(String.valueOf(remainingSickLeave));
                    } else {
                        Log.d(TAG, "No leave balance data found for UID: " + uid);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error fetching leave balances", e);
                    AndroidUtil.ShowToast(requireContext(), "Error fetching leave balances");
                });
    }



    private String getCurrentEmployeeID() {
        // Get the ID of the current user from SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("UID", "");
    }

}