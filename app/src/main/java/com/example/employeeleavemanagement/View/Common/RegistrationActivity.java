package com.example.employeeleavemanagement.View.Common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {


    MaterialButton BtnContinue;
    MaterialTextView TxtViewSignUpHere;
    // Get references to the TextInputEditTexts
    TextInputEditText edtTxtName;
    TextInputEditText edtTxtEmail;
    TextInputEditText edtTxtEmployeeID;
    TextInputEditText edtTxtPassword;
    TextInputEditText edtTxtRepeatPassword;

    Spinner departmentSpinner;


    TextInputLayout departmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BtnContinue = findViewById(R.id.BtnContinue);
        // Get references to the TextInputEditTexts
        edtTxtName = findViewById(R.id.EdtTxtName);
        edtTxtEmail = findViewById(R.id.EdtTxtEmail);
        edtTxtEmployeeID = findViewById(R.id.EdtTxtEmployeeID);
        edtTxtPassword = findViewById(R.id.EdtTxtPassword);
        edtTxtRepeatPassword = findViewById(R.id.EdtTxtRepeatPassword);


        departmentSpinner = findViewById(R.id.department_spinner);
        departmentLayout = findViewById(R.id.department_layout);


        ArrayAdapter<CharSequence> departmentadapter = ArrayAdapter.createFromResource(this,
                R.array.fbis_department, android.R.layout.simple_spinner_item);
        departmentadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentadapter);

        departmentSpinner.setPrompt("Department");


        BtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onContinueButtonClicked();
            }
        });

        TxtViewSignUpHere = findViewById(R.id.TxtViewSignUpHere);
        TxtViewSignUpHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



    }

    public boolean validateRegistrationInput(EditText nameEdtTxt, EditText emailEdtTxt, EditText employeeIdEdtTxt, EditText passwordEdtTxt, EditText repeatPasswordEdtTxt, Spinner  departmentSpinner) {
        // Perform checks on the values entered by the user
        String name = nameEdtTxt.getText().toString().trim();
        String email = emailEdtTxt.getText().toString().trim();
        String employeeID = employeeIdEdtTxt.getText().toString().trim();
        String password = passwordEdtTxt.getText().toString().trim();
        String repeatPassword = repeatPasswordEdtTxt.getText().toString().trim();
        String selectedDepartment = departmentSpinner.getSelectedItem().toString();

        // Check for empty fields
        if (TextUtils.isEmpty(name)) {
            nameEdtTxt.setError("Please enter your name");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            emailEdtTxt.setError("Please enter your email");
            return false;
        }
        if (TextUtils.isEmpty(employeeID)) {
            employeeIdEdtTxt.setError("Please enter your employee ID");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEdtTxt.setError("Please enter a password");
            return false;
        }
        if (TextUtils.isEmpty(repeatPassword)) {
            repeatPasswordEdtTxt.setError("Please repeat your password");
            return false;
        }

        if (selectedDepartment.equals("Select Department")) {
            departmentLayout.setError("Please select a department");
              return false;
        } else {
            departmentLayout.setError(null);
        }

        // Check for valid email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdtTxt.setError("Please enter a valid email address");
            return false;
        }

        // Check for name without numbers
        if (name.matches(".*\\d+.*")) {
            nameEdtTxt.setError("Name should not contain numbers");
            return false;
        }

        // Check for employee ID format
        if (employeeID.length()!= 14 ||!employeeID.matches("\\d+")) {
            employeeIdEdtTxt.setError("Please enter a valid Check Number");
            return false;
        }

        // Check for password with alphanumeric characters
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            passwordEdtTxt.setError("Password should contain alphanumeric characters and be between 8 and 20 characters long");
            return false;
        }

        // Check for password match
        if (!password.equals(repeatPassword)) {
            repeatPasswordEdtTxt.setError("Passwords do not match");
            return false;
        }

        // If all checks pass, return true
        return true;
    }



    // Create a separate method to handle the validation and data saving
    private void onContinueButtonClicked() {
        if (validateRegistrationInput(edtTxtName, edtTxtEmail, edtTxtEmployeeID, edtTxtPassword, edtTxtRepeatPassword, departmentSpinner)) {
            Intent intent = new Intent(RegistrationActivity.this, RegistrationSecondScreenActivity.class);
            intent.putExtra("Name", Objects.requireNonNull(edtTxtName.getText()).toString());
            intent.putExtra("Email", Objects.requireNonNull(edtTxtEmail.getText()).toString());
            intent.putExtra("EmployeeID", Objects.requireNonNull(edtTxtEmployeeID.getText()).toString());
            intent.putExtra("Password", Objects.requireNonNull(edtTxtPassword.getText()).toString());
            intent.putExtra("Department", Objects.requireNonNull(departmentSpinner.getSelectedItem().toString()));
            AndroidUtil.ShowToast(this, departmentSpinner.getSelectedItem().toString());
            startActivity(intent);
        }
    }
}
