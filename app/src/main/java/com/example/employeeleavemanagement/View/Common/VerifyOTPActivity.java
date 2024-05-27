package com.example.employeeleavemanagement.View.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaos.view.PinView;
import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {

    MaterialButton BtnVerifyOTP, BtnResendOTP;
    MaterialTextView TxtViewVerifiedPhoneNo;
    PinView PinViewVerifyOTP;
    ProgressBar progressBar;

    String name, email, employeeID, password, gender, completePhoneNumber, completedBirthday, VerificationCode, userId;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Long timeoutSeconds = 60L;
    PhoneAuthProvider.ForceResendingToken ResendingToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Get the data from the previous intent
        name = getIntent().getStringExtra("Name");
        email = getIntent().getStringExtra("Email");
        employeeID = getIntent().getStringExtra("EmployeeID");
        password = getIntent().getStringExtra("Password");
        gender = getIntent().getStringExtra("Gender");
        completePhoneNumber = getIntent().getStringExtra("PhoneNumber");
        completedBirthday = getIntent().getStringExtra("Birthday");

        BtnVerifyOTP = findViewById(R.id.BtnVerifyOTP);
        TxtViewVerifiedPhoneNo = findViewById(R.id.TxtViewVerifiedPhoneNo);
        PinViewVerifyOTP = findViewById(R.id.PinViewVerifyOTP);
        progressBar = findViewById(R.id.progressBar);
        BtnResendOTP = findViewById(R.id.BtnResendOTP);


        TxtViewVerifiedPhoneNo.setText(completePhoneNumber);


        sendOTP(completePhoneNumber, false);

        BtnVerifyOTP.setOnClickListener(v -> {
            String enteredOTP = PinViewVerifyOTP.getText().toString();
            if (enteredOTP.length() == 6) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationCode, enteredOTP);
                signIn(credential);

                setInProgress(true);
            }


        });


        BtnResendOTP.setOnClickListener(v -> {

            sendOTP(completePhoneNumber, true);

        });
    }

    void sendOTP(String phoneNumber, boolean isResend) {
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(completePhoneNumber)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                signIn(phoneAuthCredential);
                                setInProgress(false);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                AndroidUtil.ShowToast(getApplicationContext(), "OTP verification failed");
                                setInProgress(false);

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                VerificationCode = s;
                                setInProgress(false);
                                ResendingToken = forceResendingToken;

                                AndroidUtil.ShowToast(getApplicationContext(), "OTP sent successfully");

                            }
                        });

        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }


    void setInProgress(boolean inProgress) {
        if (inProgress) {
            BtnVerifyOTP.setVisibility(View.INVISIBLE);
            BtnResendOTP.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            BtnVerifyOTP.setVisibility(View.VISIBLE);
            BtnResendOTP.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential) {
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Check if phone number has been used before
                    db.collection("employee").whereEqualTo("phoneNumber", completePhoneNumber)
                            .get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    if (!task1.getResult().isEmpty()) {
                                        // Phone number has been used before, show error
                                        AndroidUtil.ShowToast(getApplicationContext(), "Phone number is already in use");
                                        return;
                                    } else {
                                        // Check if email has been used before
                                        db.collection("employee").whereEqualTo("email", email)
                                                .get()
                                                .addOnCompleteListener(task11 -> {
                                                    if (task11.isSuccessful()) {
                                                        if (!task11.getResult().isEmpty()) {
                                                            // Email has been used before, show error
                                                            AndroidUtil.ShowToast(getApplicationContext(), "Email is already in use");
                                                            return;
                                                        } else {
                                                            // Phone number and email have not been used before, proceed with creating user account
                                                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task11) {
                                                                    if (task11.isSuccessful()) {

                                                                        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                                                        // The user account has been successfully created
                                                                        Map<String, Object> employeeData = getEmployeeData();
                                                                        db.collection("employee").document(userId).set(employeeData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);

                                                                                intent.putExtra("UserID", userId);
                                                                                startActivity(intent);
                                                                                finish();
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                AndroidUtil.ShowToast(getApplicationContext(), "Failed to upload data to Firestore");
                                                                            }
                                                                        });
                                                                    } else {
                                                                        // The user account creation has failed
                                                                        AndroidUtil.ShowToast(getApplicationContext(), "Failed to create user account");
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        // Error occurred while checking email
                                                        AndroidUtil.ShowToast(getApplicationContext(), "Error occurred while checking email");
                                                        return;
                                                    }
                                                });
                                    }
                                } else {
                                    // Error occurred while checking phone number
                                    AndroidUtil.ShowToast(getApplicationContext(), "Error occurred while checking phone number");
                                    return;
                                }
                            });
                } else {
                    AndroidUtil.ShowToast(getApplicationContext(), "OTP verification failed");
                }
            }

            @NonNull
            private Map<String, Object> getEmployeeData() {
                Map<String, Object> employeeData = new HashMap<>();

                // Roles and permissions
                employeeData.put("Employee", true);
                employeeData.put("HR", false);
                employeeData.put("HOD", false);

                // Personal information
                employeeData.put("gender", gender);
                employeeData.put("phoneNumber", completePhoneNumber);
                employeeData.put("birthday", completedBirthday);

                // Sensitive information should be handled carefully
                employeeData.put("password", password);

                // Group related fields together for better readability
                employeeData.put("email", email);
                employeeData.put("employeeID", employeeID);
                employeeData.put("name", name);

                return employeeData;
            }
        });
    }


    void startResendTimer() {
        BtnResendOTP.setEnabled(false);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                final String text = "Resend OTP in " + timeoutSeconds + " seconds";

                runOnUiThread(() -> {
                    BtnResendOTP.setText(text);

                    if (timeoutSeconds <= 0) {
                        timeoutSeconds = 60L;
                        timer.cancel();
                        BtnResendOTP.setEnabled(true);
                        BtnResendOTP.setText("Resend OTP");
                    }
                });
            }
        }, 0, 1000);
    }
}