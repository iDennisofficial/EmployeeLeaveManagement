package com.example.employeeleavemanagement.View.Employee;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.employeeleavemanagement.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class ApplyLeaveFragment extends Fragment {

    String selectedLeave, selectedDepartment;

    String Gender, Name, PhoneNumber, Birthday, Password, Email, EmployeeID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_leave, container, false);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("EmployeeInfo", Context.MODE_PRIVATE);
        // Load the employee information from SharedPreferences
        Gender = sharedPreferences.getString("gender", "");
        PhoneNumber = sharedPreferences.getString("phoneNumber", "");
        Birthday = sharedPreferences.getString("birthday", "");
        Password = sharedPreferences.getString("password", "");
        Email = sharedPreferences.getString("email", "");
        EmployeeID = sharedPreferences.getString("employeeID", "");
        Name = sharedPreferences.getString("name", "");

        TextInputEditText startDateEditText = view.findViewById(R.id.start_date_edit_text);
        TextInputEditText endDateEditText = view.findViewById(R.id.end_date_edit_text);

        Spinner leaveTypeSpinner = view.findViewById(R.id.leave_type_spinner);
        Spinner departmentSpinner = view.findViewById(R.id.department_spinner);
        TextInputLayout department_layout = view.findViewById(R.id.department_layout);
        TextInputLayout leaveTypeLayout = view.findViewById(R.id.leave_type_layout);

        ArrayAdapter<CharSequence> leavetypeadapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.leave_types, android.R.layout.simple_spinner_item);
        leavetypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leaveTypeSpinner.setAdapter(leavetypeadapter);

        ArrayAdapter<CharSequence> departmentadapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.fbis_department, android.R.layout.simple_spinner_item);
        departmentadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentadapter);


        leaveTypeSpinner.setPrompt("Leave Type");
        departmentSpinner.setPrompt("Department");

        leaveTypeLayout.setError("Please select a Leave Type");
        department_layout.setError("Please select a department");

        leaveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    leaveTypeLayout.setError(null);
                } else {
                    leaveTypeLayout.setError("Please select a Leave Type");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                leaveTypeLayout.setError("Please select a Leave Type");
            }
        });

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    department_layout.setError(null);
                } else {
                    department_layout.setError("Please select a department");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                department_layout.setError("Please select a department");
            }
        });

        return view;
    }
}