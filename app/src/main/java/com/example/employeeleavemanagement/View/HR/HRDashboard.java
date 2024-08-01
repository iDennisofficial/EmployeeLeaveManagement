package com.example.employeeleavemanagement.View.HR;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.example.employeeleavemanagement.View.Common.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class HRDashboard extends AppCompatActivity {

    private MaterialTextView TextViewEmployeeName;

    String gender, name, phoneNumber, birthday, password, email, employeeID;
    String Gender, Name, PhoneNumber, Birthday, Password, Email, EmployeeID;

    MaterialCardView CardViewEmployeeInfo, CardViewRequests, CardViewApproved, CardViewRejected,cardViewOldLeaveRequests;
    MaterialToolbar  topAppBar;
    int PendingCount, VerifiedCount,RejectedCount;
    TextView TextViewPendingLeaveRequests, TextViewRejectedLeaveRequest, TextViewVerifiedLeaveRequests, TextViewOnLeaveRequests,
            TextViewPresentEmployees;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hr_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextViewPendingLeaveRequests = findViewById(R.id.TextViewPendingLeaveRequests);
        TextViewRejectedLeaveRequest = findViewById(R.id.TextViewRejectedLeaveRequest);
        TextViewVerifiedLeaveRequests = findViewById(R.id.TextViewVerifiedLeaveRequests);
        TextViewOnLeaveRequests = findViewById(R.id.TextViewOnLeaveRequests);
        TextViewPresentEmployees = findViewById(R.id.TextViewPresentEmployees);

        MaterialTextView txtViewDate = findViewById(R.id.TxtViewDate);
        TextViewEmployeeName = findViewById(R.id.TextViewEmployeeName);

        String formattedDate = AndroidUtil.getFormattedDate();
        txtViewDate.setText(formattedDate);

        topAppBar = findViewById(R.id.topAppBar);
        setupTopAppBar(topAppBar);

        cardViewOldLeaveRequests = findViewById(R.id.cardViewOldLeaveRequests);

        cardViewOldLeaveRequests.setOnClickListener(view -> {

            Intent intent = new Intent(HRDashboard.this, HrRequestsActivity.class);
            startActivity(intent);

        });

        CardViewEmployeeInfo = findViewById(R.id.CardViewEmployeeInfo);

        CardViewEmployeeInfo.setOnClickListener(view -> {
            Intent intent = new Intent(HRDashboard.this, ProfileActivity.class);
            startActivity(intent);

        });


        CardViewRequests = findViewById(R.id.CardViewRequests);

        CardViewRequests.setOnClickListener(view -> {
            Intent intent = new Intent(HRDashboard.this, HrRequestsActivity.class);
            startActivity(intent);

        });

        CardViewApproved = findViewById(R.id.CardViewApproved);

        CardViewApproved.setOnClickListener(view -> {

            Intent intent = new Intent(HRDashboard.this, HrApprovedLeaveRequestsActivity.class);
            startActivity(intent);
        });


        CardViewRejected = findViewById(R.id.CardViewRejected);
        CardViewRejected.setOnClickListener(view -> {

            Intent intent = new Intent(HRDashboard.this, HrRejectedLeaveRequestsActivity.class);
            startActivity(intent);

        });




        // Retrieve the oldest leave request
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView typeTextView = findViewById(R.id.typeTextView);
        TextView statusTextView = findViewById(R.id.statusTextView);


        db.collection("leaveRequests")
                .whereEqualTo("status", "Pending")
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

        SharedPreferences sharedPreferences = getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        boolean isEmployeeInfoFetched = sharedPreferences.getBoolean("isEmployeeInfoFetched", false);

        if (!isEmployeeInfoFetched) {
            fetchEmployeeInfo();
        } else {
            // Load the employee information from SharedPreferences
            loadEmployeeInfoFromSharedPreferences(TextViewEmployeeName);
        }
    }

    private void fetchEmployeeInfo() {


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

        if (textViewEmployeeName != null) {
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
                    Intent intent = new Intent(HRDashboard.this, ProfileActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }



    public void countLeaveRequests() {
        // Listen for real-time updates on Pending Leaves
        db.collection("leaveRequests")
                .whereEqualTo("status", "Pending")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Count", "Error getting documents: ", e);
                            return;
                        }
                        if (querySnapshot != null) {
                            PendingCount = querySnapshot.size();
                            TextViewPendingLeaveRequests.setText(String.valueOf(PendingCount));
                            Log.d("Count", "Number of Pending leave requests: " + PendingCount);
                        } else {
                            Log.d("Count", "No documents found");
                        }
                    }
                });

        // Listen for real-time updates on Verified Leaves
        db.collection("leaveRequests")
                .whereEqualTo("status", "Verified")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Count", "Error getting documents: ", e);
                            return;
                        }
                        if (querySnapshot != null) {
                            VerifiedCount = querySnapshot.size();
                            TextViewVerifiedLeaveRequests.setText(String.valueOf(VerifiedCount));
                            Log.d("Count", "Number of Verified leave requests: " + VerifiedCount);
                        } else {
                            Log.d("Count", "No documents found");
                        }
                    }
                });

        // Listen for real-time updates on Rejected Requests
        db.collection("leaveRequests")
                .whereEqualTo("status", "Rejected")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Count", "Error getting documents: ", e);
                            return;
                        }
                        if (querySnapshot != null) {
                            RejectedCount = querySnapshot.size();
                            TextViewRejectedLeaveRequest.setText(String.valueOf(RejectedCount));
                            Log.d("Count", "Number of Rejected leave requests: " + RejectedCount);
                        } else {
                            Log.d("Count", "No documents found");
                        }
                    }
                });

        // Listen for real-time updates on Onleave Requests
        db.collection("leaveRequests")
                .whereEqualTo("status", "Pending")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Count", "Error getting documents: ", e);
                            return;
                        }
                        if (querySnapshot != null) {
                            PendingCount = querySnapshot.size();
                            TextViewOnLeaveRequests.setText(String.valueOf(PendingCount));
                            Log.d("Count", "Number of Onleave requests: " + PendingCount);
                        } else {
                            Log.d("Count", "No documents found");
                        }
                    }
                });

        // Listen for real-time updates on Total number of employees
        db.collection("employee")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Firestore", "Error getting documents: ", e);
                            return;
                        }
                        if (querySnapshot != null) {
                            int totalEmployees = querySnapshot.size();
                            TextViewPresentEmployees.setText(String.valueOf(totalEmployees));
                            Log.d("Firestore", "Total documents: " + totalEmployees);
                        } else {
                            Log.d("Firestore", "No documents found");
                        }
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        countLeaveRequests();
    }




}