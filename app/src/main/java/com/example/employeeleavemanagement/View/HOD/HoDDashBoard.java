package com.example.employeeleavemanagement.View.HOD;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.example.employeeleavemanagement.View.Common.ProfileActivity;
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

import java.util.Objects;

public class HoDDashBoard extends AppCompatActivity {


    private MaterialTextView TextViewEmployeeName;

    String gender, name, phoneNumber, birthday, password, email, employeeID, department;
    String Gender, Name, PhoneNumber, Birthday, Password, Email, EmployeeID, Department;

    MaterialCardView CardViewEmployeeInfo, CardViewRequests, CardViewReviewed;
    MaterialToolbar topAppBar;

    FirebaseFirestore db = FirebaseFirestore.getInstance();




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

        MaterialTextView txtViewDate = findViewById(R.id.TxtViewDate);
        TextViewEmployeeName = findViewById(R.id.TextViewEmployeeName);


        String formattedDate = AndroidUtil.getFormattedDate();
        txtViewDate.setText(formattedDate);

        topAppBar = findViewById(R.id.topAppBar);
        setupTopAppBar(topAppBar);

        CardViewRequests = findViewById(R.id.CardViewRequests);


        CardViewRequests.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HoDRequestsActivity.class);
            startActivity(intent);
        });

        CardViewReviewed = findViewById(R.id.CardViewReviewed);

        CardViewReviewed.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HoDReviewedRequestsActivity.class);
            startActivity(intent);
        });



        CardViewEmployeeInfo = findViewById(R.id.CardViewEmployeeInfo);

        CardViewEmployeeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HoDDashBoard.this, ProfileActivity.class);

                startActivity(intent);

            }
        });





        SharedPreferences sharedPreferences = getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        boolean isEmployeeInfoFetched = sharedPreferences.getBoolean("isEmployeeInfoFetched", false);

        if (!isEmployeeInfoFetched) {
            fetchEmployeeInfo();
        } else {
            // Load the employee information from SharedPreferences
            loadEmployeeInfoFromSharedPreferences(TextViewEmployeeName);
        }

        // Retrieve the oldest leave request
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView typeTextView = findViewById(R.id.typeTextView);
        TextView statusTextView = findViewById(R.id.statusTextView);

        String department = getCurrentHODDepartment();
        Log.d(TAG, "Current HOD Department: " + department + "is not empty");


        db.collection("leaveRequests")
                .whereEqualTo("status", "Pending")
                .whereEqualTo("department", getCurrentHODDepartment())
                .orderBy("queryTime", Query.Direction.ASCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(taskOldest -> {
                    if (taskOldest.isSuccessful() && !taskOldest.getResult().isEmpty()) {
                        QueryDocumentSnapshot oldestDocument = (QueryDocumentSnapshot) taskOldest.getResult().getDocuments().get(0);
                        String oldestEmployeeName = oldestDocument.getString("name");
                        String oldestLeaveType = oldestDocument.getString("leaveType");
                        String oldestStartDate = oldestDocument.getString("createdAt");
                        String oldestStatus = oldestDocument.getString("status");

                        // Set the text of the TextViews
                        usernameTextView.setText(oldestEmployeeName);
                        dateTextView.setText(oldestStartDate);
                        typeTextView.setText(oldestLeaveType);
                        statusTextView.setText(oldestStatus);
                    } else {
                        // Enhanced error handling
                        if (taskOldest.getException() != null) {
                            Log.e(TAG, "Error retrieving oldest leave request", taskOldest.getException());
                        } else {
                            Log.e(TAG, "No matching leave requests found.");
                        }
                        AndroidUtil.ShowToast(getApplicationContext(), "Not getting the values");
                    }
                });






    }

    private void fetchEmployeeInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        final ProgressDialog progressDialog = new ProgressDialog(this);
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
                            SharedPreferences sharedPreferences = getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("gender", gender);
                            editor.putString("phoneNumber", phoneNumber);
                            editor.putString("birthday", birthday);
                            editor.putString("password", password);
                            editor.putString("email", email);
                            editor.putString("employeeID", employeeID);
                            editor.putString("name", name);
                            editor.putString("department", department);
                            editor.putBoolean("isEmployeeInfoFetched", true);
                            editor.apply();

                            loadEmployeeInfoFromSharedPreferences(TextViewEmployeeName);
                        } else {
                            Log.d("TAG", "No employee information found");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.w("TAG", "Error fetching employee information", e);
                    }
                });
    }

    private void loadEmployeeInfoFromSharedPreferences(MaterialTextView textViewEmployeeName) {
        SharedPreferences sharedPreferences = getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        Gender = sharedPreferences.getString("gender", "");
        PhoneNumber = sharedPreferences.getString("phoneNumber", "");
        Birthday = sharedPreferences.getString("birthday", "");
        Password = sharedPreferences.getString("password", "");
        Email = sharedPreferences.getString("email", "");
        EmployeeID = sharedPreferences.getString("employeeID", "");
        Name = sharedPreferences.getString("name", "");
        Department = sharedPreferences.getString("department", "");

        if (textViewEmployeeName!= null) {
            textViewEmployeeName.setText(Name);
        }

    }

    private void setupTopAppBar(MaterialToolbar topAppBar) {
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // or getActivity().onBackPressed(); if you're in a fragment
            }
        });

        topAppBar.setOnMenuItemClickListener(new MaterialToolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.Notification) {
                    AndroidUtil.ShowToast(getApplicationContext(), "Notification soon to be added");

                    return true;
                } else if (item.getItemId() == R.id.Account) {
                    Intent intent = new Intent(HoDDashBoard.this, ProfileActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private String getCurrentHODDepartment() {
        // Get the department of the current HOD from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("department", "");
    }
}