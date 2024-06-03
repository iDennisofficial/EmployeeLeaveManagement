package com.example.employeeleavemanagement.View.Employee;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.example.employeeleavemanagement.databinding.ActivityEmployeeMainDashboardBinding;
import com.example.employeeleavemanagement.databinding.ActivityEmployeeMainDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textview.MaterialTextView;

public class EmployeeMainDashboardActivity extends AppCompatActivity {

    private MaterialTextView TxtViewDate;

        ActivityEmployeeMainDashboardBinding activityEmployeeDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityEmployeeDashboardBinding = ActivityEmployeeMainDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityEmployeeDashboardBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(activityEmployeeDashboardBinding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        replaceFragment(new DashboardFragment() );



        activityEmployeeDashboardBinding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new DashboardFragment());
            } else if (itemId == R.id.apply) {
                replaceFragment(new ApplyLeaveFragment());
            } else if (itemId == R.id.history) {
                replaceFragment(new LeaveHistoryFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });











    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, fragment);
        fragmentTransaction.commit();
    }


}