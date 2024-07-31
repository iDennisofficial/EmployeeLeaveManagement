package com.example.employeeleavemanagement.View.Common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.example.employeeleavemanagement.View.Employee.EmployeeMainDashboardActivity;
import com.example.employeeleavemanagement.View.HOD.HoDDashBoard;
import com.example.employeeleavemanagement.View.HR.HRDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private MaterialButton BtnSignin;
    private MaterialTextView TxtViewSignUpHere;
    private TextInputEditText EdtTxtEmail, EdtTxtPassword;
    private RadioGroup roleGroup;
    private RadioButton roleEmployee, roleHr, roleHod;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;





    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TxtViewSignUpHere = findViewById(R.id.TxtViewSignUpHere);

        TxtViewSignUpHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });



        EdtTxtEmail = findViewById(R.id.EdtTxtEmail);
        EdtTxtPassword = findViewById(R.id.EdtTxtPassword);
        BtnSignin = findViewById(R.id.BtnSignin);
        roleGroup = findViewById(R.id.roleGroup);
        roleEmployee = findViewById(R.id.roleEmployee);
        roleHr = findViewById(R.id.roleHr);
        roleHod = findViewById(R.id.roleHod);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        BtnSignin.setOnClickListener(v -> {

            SharedPreferences sharedPreferences = this.getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();


            String email = Objects.requireNonNull(EdtTxtEmail.getText()).toString();
            String password = Objects.requireNonNull(EdtTxtPassword.getText()).toString();
            int selectedRoleId = roleGroup.getCheckedRadioButtonId();



            // Check if email is empty
            if (TextUtils.isEmpty(email)) {
                EdtTxtEmail.setError("Please enter an email address");
                return;
            }

// Check if email is in correct pattern
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                EdtTxtEmail.setError("Please enter a valid email address");
                return;
            }

// Clear the error message for the email field
            EdtTxtEmail.setError(null);

// Check if password is empty
            if (TextUtils.isEmpty(password)) {
                EdtTxtPassword.setError("Please enter a password");
                return;
            }

// Check if a radio button is selected
            if (selectedRoleId == -1) {
                AndroidUtil.ShowToast(this, "Please select a role");
                return;
            }

            // Show the ProgressBar and disable the sign-in button
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            BtnSignin.setEnabled(false);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // The user has been successfully signed in
                                FirebaseUser user = task.getResult().getUser();
                                assert user != null;
                                String userId = user.getUid();

                                progressBar.setVisibility(View.GONE);

                                // Use the user ID to retrieve user data from Firestore



                                db.collection("employee").document(userId).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        boolean isEmployee = Boolean.TRUE.equals(document.getBoolean("Employee"));
                                                        boolean isHR = Boolean.TRUE.equals(document.getBoolean("HR"));
                                                        boolean isHOD = Boolean.TRUE.equals(document.getBoolean("HOD"));

                                                        RadioButton selectedRole = findViewById(selectedRoleId);

                                                        if (selectedRole.getId() == R.id.roleEmployee) {
                                                            if (isEmployee) {
                                                                Intent intent = new Intent(LoginActivity.this, EmployeeMainDashboardActivity.class);
                                                                AndroidUtil.ShowToast(LoginActivity.this, "Welcome back!");
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                progressBar.setVisibility(View.GONE);
                                                                AndroidUtil.ShowToast(LoginActivity.this, "You are not authorized to access this role");
                                                            }
                                                        } else if (selectedRole.getId() == R.id.roleHr) {
                                                            if (isHR) {
                                                                Intent intent = new Intent(LoginActivity.this, HRDashboard.class);
                                                                AndroidUtil.ShowToast(LoginActivity.this, "Welcome back!");
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                progressBar.setVisibility(View.GONE);
                                                                AndroidUtil.ShowToast(LoginActivity.this, "You are not authorized to access this role");
                                                            }
                                                        } else if (selectedRole.getId() == R.id.roleHod) {
                                                            if (isHOD) {
                                                                Intent intent = new Intent(LoginActivity.this, HoDDashBoard.class);
                                                                AndroidUtil.ShowToast(LoginActivity.this, "Welcome back!");
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                progressBar.setVisibility(View.GONE);
                                                                AndroidUtil.ShowToast(LoginActivity.this, "You are not authorized to access this role");
                                                            }
                                                        } else {
                                                            progressBar.setVisibility(View.GONE);
                                                            AndroidUtil.ShowToast(LoginActivity.this, "Please select a role");
                                                        }
                                                    } else {
                                                        progressBar.setVisibility(View.GONE);
                                                        AndroidUtil.ShowToast(LoginActivity.this, "User not found");
                                                    }
                                                } else {
                                                    progressBar.setVisibility(View.GONE);
                                                    AndroidUtil.ShowToast(LoginActivity.this, "Failed to retrieve user data");
                                                }
                                            }
                                        });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                AndroidUtil.ShowToast(LoginActivity.this, "Login failed");
                            }
                        }
                    });
        });
    }



    }



