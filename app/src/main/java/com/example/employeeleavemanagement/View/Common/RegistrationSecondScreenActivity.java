package com.example.employeeleavemanagement.View.Common;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.employeeleavemanagement.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.hbb20.CountryCodePicker;


import java.text.SimpleDateFormat;
import java.util.Locale;

public class RegistrationSecondScreenActivity extends AppCompatActivity {

    MaterialButton BtnContinue;
    String gender;
    String completedBirthday;
    String completePhoneNumber;
    MaterialTextView TxtViewSignUpHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration_second_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TxtViewSignUpHere = findViewById(R.id.TxtViewSignUpHere);

        TxtViewSignUpHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationSecondScreenActivity.this, LoginActivity.class);
            }
        });

        BtnContinue = findViewById(R.id.BtnContinue);
        BtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the data from the previous intent
                String name = getIntent().getStringExtra("Name");
                String email = getIntent().getStringExtra("Email");
                String employeeID = getIntent().getStringExtra("EmployeeID");
                String password = getIntent().getStringExtra("Password");
                String department = getIntent().getStringExtra("Department");
                Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);

                if (validateInputs()) {
                    intent.putExtra("Name", name);
                    intent.putExtra("Email", email);
                    intent.putExtra("EmployeeID", employeeID);
                    intent.putExtra("Password", password);
                    intent.putExtra("Department", department);
                    intent.putExtra("Gender", gender);
                    intent.putExtra("PhoneNumber", completePhoneNumber);
                    intent.putExtra("Birthday", completedBirthday);


                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private boolean validateInputs() {
        // Get the selected gender
        gender = "";
        RadioGroup radioGroupGender;
        radioGroupGender = findViewById(R.id.radioGroupGender);
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedId == -1) {
            // Set error message for the first radio button in the group
            RadioButton radioButton = radioGroupGender.findViewById(R.id.radioButtonMale);
            radioButton.setError("Please select a gender");
            return false;
        } else {
            if (selectedId == R.id.radioButtonMale) {
                gender = "Male";
            } else if (selectedId == R.id.radioButtonFemale) {
                gender = "Female";
            }

            // Reset the error message for the first radio button in the group
            RadioButton radioButton = radioGroupGender.findViewById(R.id.radioButtonMale);
            radioButton.setError(null);
        }


        // Get the birthdate
        DatePicker datepickerBirthday;
        datepickerBirthday = findViewById(R.id.datepickerBirthday);
        int age = getAge(datepickerBirthday);

        // Check if the person is eligible to register
        if (age < 18) {
            Toast.makeText(this, "You are not eligible to register", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            // Get the complete date
            int day = datepickerBirthday.getDayOfMonth();
            int month = datepickerBirthday.getMonth();
            int year = datepickerBirthday.getYear();

            // Create a Calendar instance and set the date
            Calendar calendar = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                calendar = Calendar.getInstance();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                calendar.set(year, month, day);
            }

            // Format the date as a string
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            completedBirthday = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                completedBirthday = dateFormat.format(calendar.getTime());
            }

            // Use the completedBirthday string as needed
            Log.d("DatePicker", "Completed birthday: " + completedBirthday);
        }


        // Get the selected country code
        CountryCodePicker countryCodePicker;
        countryCodePicker = findViewById(R.id.countryCodePicker);
        String countryCode = countryCodePicker.getSelectedCountryCode();

        // Get the phone number
        EditText EdtTxtPhoneNo;
        EdtTxtPhoneNo = findViewById(R.id.EdtTxtPhoneNo);
        String phoneNumber = EdtTxtPhoneNo.getText().toString();

        if (phoneNumber.isEmpty()) {
            EdtTxtPhoneNo.setError("Please enter a phone number");
            return false;
        }


        // Modify the phone number
        phoneNumber = phoneNumber.replaceAll("^0+", ""); // Remove any zero from the start
        if (phoneNumber.startsWith("+")) {
            phoneNumber = phoneNumber.substring(1); // Remove the plus sign
            if (phoneNumber.length() >= 12) {
                phoneNumber = phoneNumber.substring(0, 3); // Remove the country code
            }
        }

        // Generate a complete valid phone number
        completePhoneNumber = "+" + countryCode + phoneNumber;

        // Display the complete valid phone number
        Toast.makeText(this,  completePhoneNumber, Toast.LENGTH_SHORT).show();

        return true;
    }

    private static int getAge(DatePicker datePickerBirthday) {
        Calendar now = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            now = Calendar.getInstance();
        }
        int currentYear = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentYear = now.get(Calendar.YEAR);
        }

        int birthYear = datePickerBirthday.getYear();
        int birthMonth = datePickerBirthday.getMonth();
        int birthDay = datePickerBirthday.getDayOfMonth();

        Calendar birthdate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            birthdate = Calendar.getInstance();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            birthdate.set(birthYear, birthMonth, birthDay);
        }

        int age = currentYear - birthYear;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (now.before(birthdate)) {
                age--;
            }
        }

        return age;
    }
}