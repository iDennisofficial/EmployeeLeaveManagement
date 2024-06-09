package com.example.employeeleavemanagement.Controller.Employee;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.employeeleavemanagement.View.Employee.ApprovedFragment;
import com.example.employeeleavemanagement.View.Employee.CancelledFragment;
import com.example.employeeleavemanagement.View.Employee.LeaveHistoryFragment;
import com.example.employeeleavemanagement.View.Employee.WaitingFragment;

public class HistoryViewPagerAdapter extends FragmentStateAdapter {
    public HistoryViewPagerAdapter(@NonNull LeaveHistoryFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new WaitingFragment();

            case 1:
                return  new ApprovedFragment();

            case 2:
                return  new CancelledFragment();

            default:
                return  new WaitingFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
