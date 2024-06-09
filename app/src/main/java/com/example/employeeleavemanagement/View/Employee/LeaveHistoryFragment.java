package com.example.employeeleavemanagement.View.Employee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.employeeleavemanagement.Controller.Employee.HistoryViewPagerAdapter;
import com.example.employeeleavemanagement.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class LeaveHistoryFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 tabviewPager;
    HistoryViewPagerAdapter historyViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leave_history, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        tabviewPager = view.findViewById(R.id.tabviewPager);

        historyViewPagerAdapter = new HistoryViewPagerAdapter(this);

        tabviewPager.setAdapter(historyViewPagerAdapter);

        // Set default background color for all tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.view.setBackgroundColor(getResources().getColor(R.color.white));
        }

        // Set the first tab to red
        Objects.requireNonNull(tabLayout.getTabAt(0)).view.setBackgroundColor(getResources().getColor(R.color.chuoyellow));

        tabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.white));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        Objects.requireNonNull(tabLayout.getTabAt(position)).view.setBackgroundColor(getResources().getColor(R.color.chuoyellow));
                        break;
                    case 1:
                        Objects.requireNonNull(tabLayout.getTabAt(position)).view.setBackgroundColor(getResources().getColor(R.color.green));
                        break;
                    case 2:
                        Objects.requireNonNull(tabLayout.getTabAt(position)).view.setBackgroundColor(getResources().getColor(R.color.chuored));
                        break;
                }

                // Add this line to select the corresponding page in the view pager
                tabviewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Reset the background color of the unselected tab
                tab.view.setBackgroundColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabviewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });

        return view;
    }
}