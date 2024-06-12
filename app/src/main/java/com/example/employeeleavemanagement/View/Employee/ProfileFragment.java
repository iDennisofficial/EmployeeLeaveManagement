package com.example.employeeleavemanagement.View.Employee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.View.Common.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    String Gender, Name, PhoneNumber, Birthday, Password, Email, EmployeeID;
    MaterialTextView textViewName, textViewEmail, gender_textView,birthday_textView,phone_textView,
            checkno_textView;
    MaterialButton log_out_button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        // Load the employee information from SharedPreferences
        Gender = sharedPreferences.getString("gender", "");
        PhoneNumber = sharedPreferences.getString("phoneNumber", "");
        Birthday = sharedPreferences.getString("birthday", "");
        Password = sharedPreferences.getString("password", "");
        Email = sharedPreferences.getString("email", "");
        EmployeeID = sharedPreferences.getString("employeeID", "");
        Name = sharedPreferences.getString("name", "");

        textViewName = view.findViewById(R.id.textViewName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        gender_textView = view.findViewById(R.id.gender_textView);
        birthday_textView = view.findViewById(R.id.birthday_textView);
        phone_textView = view.findViewById(R.id.phone_textView);
        checkno_textView = view.findViewById(R.id.checkno_textView);

        textViewName.setText(Name);
        textViewEmail.setText(Email);
        gender_textView.setText(Gender);
        birthday_textView.setText(Birthday);
        phone_textView.setText(PhoneNumber);
        checkno_textView.setText(EmployeeID);


        log_out_button = view.findViewById(R.id.log_out_button);

        log_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Start the Login Activity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

                // Finish the current activity
                requireActivity().finish();
            }
        });



        return view;
    }
}