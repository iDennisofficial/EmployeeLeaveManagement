package com.example.employeeleavemanagement.View.Common;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.example.employeeleavemanagement.View.Employee.EmployeeDashboardActivity;
import com.example.employeeleavemanagement.View.HOD.HoDDashBoard;
import com.example.employeeleavemanagement.View.HR.HRDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

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
            String email = Objects.requireNonNull(EdtTxtEmail.getText()).toString();
            String password = Objects.requireNonNull(EdtTxtPassword.getText()).toString();
            int selectedRoleId = roleGroup.getCheckedRadioButtonId();

            if (email.isEmpty() || password.isEmpty()) {
                AndroidUtil.ShowToast(this, "Please fill in all fields");
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // The user has been successfully signed in
                                FirebaseUser user = task.getResult().getUser();
                                assert user != null;
                                String userId = user.getUid();

                                // Use the user ID to retrieve user data from Firestore

                                AndroidUtil.ShowToast(LoginActivity.this, "Welcome back!");
                                db.collection("employee").document(userId).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        boolean isEmployee = document.getBoolean("Employee");
                                                        boolean isHR = document.getBoolean("HR");
                                                        boolean isHOD = document.getBoolean("HOD");

                                                        RadioButton selectedRole = findViewById(selectedRoleId);

                                                        if (selectedRole.getId() == R.id.roleEmployee) {
                                                            if (isEmployee) {
                                                                Intent intent = new Intent(LoginActivity.this, EmployeeDashboardActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                AndroidUtil.ShowToast(LoginActivity.this, "You are not authorized to access this role");
                                                            }
                                                        } else if (selectedRole.getId() == R.id.roleHr) {
                                                            if (isHR) {
                                                                Intent intent = new Intent(LoginActivity.this, HRDashboard.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                AndroidUtil.ShowToast(LoginActivity.this, "You are not authorized to access this role");
                                                            }
                                                        } else if (selectedRole.getId() == R.id.roleHod) {
                                                            if (isHOD) {
                                                                Intent intent = new Intent(LoginActivity.this, HoDDashBoard.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                AndroidUtil.ShowToast(LoginActivity.this, "You are not authorized to access this role");
                                                            }
                                                        } else {
                                                            AndroidUtil.ShowToast(LoginActivity.this, "Please select a role");
                                                        }
                                                    } else {
                                                        AndroidUtil.ShowToast(LoginActivity.this, "User not found");
                                                    }
                                                } else {
                                                    AndroidUtil.ShowToast(LoginActivity.this, "Failed to retrieve user data");
                                                }
                                            }
                                        });
                            } else {
                                AndroidUtil.ShowToast(LoginActivity.this, "Login failed");
                            }
                        }
                    });
        });
    }



    }



