package com.example.employeeleavemanagement.View.Employee;

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

    String selectedValue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ApplyLeaveFragment() {
        // Required empty public constructor
    }


    public static ApplyLeaveFragment newInstance(String param1, String param2) {
        ApplyLeaveFragment fragment = new ApplyLeaveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!= null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_leave, container, false);

        TextInputEditText startDateEditText = view.findViewById(R.id.start_date_edit_text);
        TextInputEditText endDateEditText = view.findViewById(R.id.end_date_edit_text);

        Spinner leaveTypeSpinner = view.findViewById(R.id.leave_type_spinner);
        TextInputLayout leaveTypeLayout = view.findViewById(R.id.leave_type_layout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.leave_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leaveTypeSpinner.setAdapter(adapter);

        leaveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Display error when the user selects the first item (prompt)
                    leaveTypeLayout.setError("Please select a leave type");
                } else {
                    // Get the selected value and remove error
                    selectedValue = parent.getItemAtPosition(position).toString();
                    leaveTypeLayout.setError(null);
                    // Do something with the selected value
                    Toast.makeText(getActivity(), "Selected value: " + selectedValue, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do something when the user does not select any item
            }
        });

        return view;
    }
}