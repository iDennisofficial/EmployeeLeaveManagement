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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class DashboardFragment extends Fragment {

    MaterialTextView TxtViewDate, TextViewEmployeeName, TextViewEmployeeCheckNo;

    String gender, name, phoneNumber, birthday, password, email, employeeID;
    String Gender, Name, PhoneNumber, Birthday, Password, Email, EmployeeID;


    MaterialToolbar topAppBar;

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




        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        boolean isEmployeeInfoFetched = sharedPreferences.getBoolean("isEmployeeInfoFetched", false);

        if (!isEmployeeInfoFetched) {
            fetchEmployeeInfo();
        } else {
            // Load the employee information from SharedPreferences
            loadEmployeeInfoFromSharedPreferences(TextViewEmployeeName, TextViewEmployeeCheckNo);
        }

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
}