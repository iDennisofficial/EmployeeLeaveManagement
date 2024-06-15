package com.example.employeeleavemanagement.View.Employee;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.employeeleavemanagement.Model.Employee.LeaveRequest;
import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class ApplyLeaveFragment extends Fragment {

    String selectedLeave, selectedDepartment, employeePhone, startDate, endDate, reason, currentDateandTime;

    String Gender, Name, PhoneNumber, Birthday, Password, Email, CheckNo, UserID;

    MaterialToolbar topAppBar;

    TextInputEditText startDateEditText, endDateEditText, reasonEditText, EmployeePhoneEditText;

    Spinner leaveTypeSpinner, departmentSpinner;

    TextInputLayout departmentLayout, leaveTypeLayout, startDateLayout, endDateLayout, reasonLayout, EmployeePhoneLayout;

    MaterialButton submitButton;
    long days;

    private boolean isSubmitInProgress = false;

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
        CheckNo = sharedPreferences.getString("employeeID", "");
        Name = sharedPreferences.getString("name", "");
        UserID = sharedPreferences.getString("UID", "");


        topAppBar = view.findViewById(R.id.topAppBar);
        setupTopAppBar(topAppBar);

        leaveTypeSpinner = view.findViewById(R.id.leave_type_spinner);
        departmentSpinner = view.findViewById(R.id.department_spinner);
        departmentLayout = view.findViewById(R.id.department_layout);
        leaveTypeLayout = view.findViewById(R.id.leave_type_layout);
        startDateEditText = view.findViewById(R.id.start_date_edit_text);
        endDateEditText = view.findViewById(R.id.end_date_edit_text);
        startDateLayout = view.findViewById(R.id.start_date_layout);
        endDateLayout = view.findViewById(R.id.end_date_layout);
        reasonLayout = view.findViewById(R.id.reason_layout);
        reasonEditText = view.findViewById(R.id.reason_edit_text);
        EmployeePhoneLayout = view.findViewById(R.id.employee_phone_layout);
        EmployeePhoneEditText = view.findViewById(R.id.employee_phone_edit_text);
        submitButton = view.findViewById(R.id.submit_button);

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


        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(startDateEditText, startDateEditText, endDateEditText);
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(endDateEditText, startDateEditText, endDateEditText);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmitLeaveApplication(v);

                currentDateandTime = getCurrentTimeAndDate();

            }
        });

        return view;
    }

    private void setupTopAppBar(MaterialToolbar topAppBar) {
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed(); // or getActivity().onBackPressed(); if you're in a fragment
            }
        });

    }

    private void validateAndSubmitLeaveApplication(View view) {
        employeePhone = Objects.requireNonNull(EmployeePhoneEditText.getText()).toString().trim();
        startDate = Objects.requireNonNull(startDateEditText.getText()).toString().trim();
        endDate = Objects.requireNonNull(endDateEditText.getText()).toString().trim();
        reason = Objects.requireNonNull(reasonEditText.getText()).toString().trim();
        selectedLeave = leaveTypeSpinner.getSelectedItem().toString();
        selectedDepartment = departmentSpinner.getSelectedItem().toString();

        boolean isValid = true;


        if (employeePhone.isEmpty()) {
            EmployeePhoneLayout.setError("Please enter your number");
            isValid = false;
        } else if (!employeePhone.matches("[0-9]{10}")) {
            EmployeePhoneLayout.setError("Invalid phone number. Please enter a 10-digit number");
            isValid = false;
        } else {
            EmployeePhoneLayout.setError(null);
        }

        if (selectedDepartment.equals("Select Department")) {
            departmentLayout.setError("Please select a department");
            isValid = false;
        } else {
            departmentLayout.setError(null);
        }

        if (selectedLeave.equals("Select Leave Type")) {
            leaveTypeLayout.setError("Please select a leave type");
            isValid = false;
        } else {
            leaveTypeLayout.setError(null);
        }

        if (startDate.isEmpty()) {
            startDateLayout.setError("Please enter start date");
            isValid = false;
        } else {
            startDateLayout.setError(null);
        }

        if (endDate.isEmpty()) {
            endDateLayout.setError("Please enter end date");
            isValid = false;
        } else {
            endDateLayout.setError(null);
        }

        if (reason.isEmpty()) {
            reasonLayout.setError("Please enter reason for leave");
            isValid = false;
        } else {
            reasonLayout.setError(null);
        }

        if (isValid) {
            boolean isValidLeave = true;

            if (selectedLeave.equals("Annual Leave") && days > 28) {
                Toast.makeText(getContext(), "Annual leave cannot exceed 28 days", Toast.LENGTH_SHORT).show();
                isValidLeave = false;
            } else if (selectedLeave.equals("Sick Leave") && days > 60) {
                Toast.makeText(getContext(), "Sick leave cannot exceed 60 days", Toast.LENGTH_SHORT).show();
                isValidLeave = false;
            } else if (selectedLeave.equals("Sabbatical Leave") && days > 365) {
                Toast.makeText(getContext(), "Sabbatical leave cannot exceed 365 days", Toast.LENGTH_SHORT).show();
                isValidLeave = false;
            } else if (selectedLeave.equals("Maternity Leave") && days > 80) {
                Toast.makeText(getContext(), "Maternity leave cannot exceed 80 days", Toast.LENGTH_SHORT).show();
                isValidLeave = false;
            } else if (selectedLeave.equals("Paternity Leave") && days > 3) {
                Toast.makeText(getContext(), "Paternity leave cannot exceed 3 days", Toast.LENGTH_SHORT).show();
                isValidLeave = false;
            }

            if (isValidLeave) {

                showConfirmationDialog();

            }
        }
    }


    private void showDatePickerDialog(final TextInputEditText editText, final TextInputEditText startDateEditText, final TextInputEditText endDateEditText) {
        Calendar calendar = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }
        int year = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            year = calendar.get(Calendar.YEAR);
        }
        int month = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            month = calendar.get(Calendar.MONTH);
        }
        int day = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        Calendar finalCalendar = calendar;
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    finalCalendar.set(year, month, day);
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    editText.setText(dateFormat.format(finalCalendar.getTime()));
                }

                if (!Objects.requireNonNull(startDateEditText.getText()).toString().trim().isEmpty() && !Objects.requireNonNull(endDateEditText.getText()).toString().trim().isEmpty()) {
                    try {
                        Date startDate = dateFormat.parse(startDateEditText.getText().toString());
                        Date endDate = dateFormat.parse(endDateEditText.getText().toString());

                        assert startDate != null;
                        if (startDate.after(endDate)) {
                            Toast.makeText(getContext(), "Start date cannot be after end date", Toast.LENGTH_SHORT).show();
                            endDateEditText.setText("");
                        } else {
                            assert endDate != null;
                            long diff = endDate.getTime() - startDate.getTime();
                            days = diff / (24 * 60 * 60 * 1000);
                            Toast.makeText(getContext(), "Number of days: " + days, Toast.LENGTH_SHORT).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, year, month, day);

        datePickerDialog.show();
    }


    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to submit your leave application?" +
                " Check your information before submitting. This action is irreversible.");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isSubmitInProgress = true; // Set the flag to true
                 submitButton.setEnabled(false); // Disable the submit button
                // Submit the leave application

                //TODO: UNcomment the submit button
                submitLeaveRequest();
                Toast.makeText(getContext(), "Leave application submitted successfully", Toast.LENGTH_SHORT).show();

                // Send a notification to the employee's phone
                sendNotificationToEmployee();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isSubmitInProgress) {
            submitButton.setEnabled(true); // Re-enable the submit button
        }
    }

    private void sendNotificationToEmployee() {
        String channelID = "CHANNEL_ID_NOTIFICATION";

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(requireContext(), channelID);
        builder.setSmallIcon(R.drawable.eleavemoculogo)
                .setContentTitle("eLeMA")
                .setContentText("Leave request submitted successful")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(requireContext(), EmployeeMainDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0,
                intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = "eLeMA";
                String channelDescription = "Leave request submitted successful";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
                channel.setDescription(channelDescription);
                notificationManager.createNotificationChannel(channel);
                Log.d(TAG, "Notification channel created successfully");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) requireContext(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 123);
                    Log.w(TAG, "POST_NOTIFICATIONS permission not granted");
                    return;
                }
            }

            notificationManager.notify(0, builder.build());
            Log.d(TAG, "Notification sent successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating notification channel", e);
        }
    }


    public String getCurrentTimeAndDate() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
        return dateFormat.format(new Date());
    }

    // Submit leave request
    public void submitLeaveRequest() {
        LeaveRequest leaveRequest = getLeaveRequest();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("leaveRequests").add(leaveRequest)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Leave request submitted successfully");



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error submitting leave request", e);
                    }
                });
    }

    private LeaveRequest getLeaveRequest() {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(UserID);
        leaveRequest.setName(Name);
        leaveRequest.setEmail(Email);
        leaveRequest.setCheckNo(CheckNo);
        leaveRequest.setHomephone(employeePhone);
        leaveRequest.setDepartment(selectedDepartment);
        leaveRequest.setLeaveType(selectedLeave);
        leaveRequest.setStartDate(startDate);
        leaveRequest.setEndDate(endDate);
        leaveRequest.setNumberOfDays(days);
        leaveRequest.setReason(reason);
        leaveRequest.setCreatedAt(currentDateandTime);
        leaveRequest.setStatus("Pending"); // Set the status to "Pending" initially
        return leaveRequest;
    }

}