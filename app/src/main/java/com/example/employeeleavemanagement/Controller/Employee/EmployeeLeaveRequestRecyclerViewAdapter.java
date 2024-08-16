package com.example.employeeleavemanagement.Controller.Employee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeleavemanagement.Controller.HOD.LeaveRequestRecyclerViewAdapter;
import com.example.employeeleavemanagement.Model.Employee.LeaveRequest;
import com.example.employeeleavemanagement.Model.HOD.HoDLeaveRequestModel;
import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.View.Employee.EmployeeLeaveRequestActivity;
import com.example.employeeleavemanagement.View.HOD.HoDLeaveRequestDetailActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class EmployeeLeaveRequestRecyclerViewAdapter extends RecyclerView.Adapter<EmployeeLeaveRequestRecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<LeaveRequest> EmployeeleaveRequestsArrayList;

    public EmployeeLeaveRequestRecyclerViewAdapter(Context context, ArrayList<LeaveRequest> employeeleaveRequestsArrayList) {
        this.context = context;
        this.EmployeeleaveRequestsArrayList = employeeleaveRequestsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_leave_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaveRequest leaveRequestModel = EmployeeleaveRequestsArrayList.get(position);

        holder.usernameTextView.setText(leaveRequestModel.getName());
        holder.timeanddate_textView.setText(leaveRequestModel.getCreatedAt());
        holder.typeTextView.setText(leaveRequestModel.getLeaveType());
        holder.statusTextView.setText(leaveRequestModel.getStatus());

        holder.itemView.setOnClickListener(view -> {
            // Create an Intent to start the Leave Request Detail Activity
            Intent intent = new Intent(context, EmployeeLeaveRequestActivity.class);

            // Pass the selected leave request data as extras
            intent.putExtra("employeeId", leaveRequestModel.getEmployeeId());
            intent.putExtra("name", leaveRequestModel.getName());
            intent.putExtra("email", leaveRequestModel.getEmail());
            intent.putExtra("checkNo", leaveRequestModel.getCheckNo());
            intent.putExtra("homephone", leaveRequestModel.getHomephone());
            intent.putExtra("department", leaveRequestModel.getDepartment());
            intent.putExtra("leaveType", leaveRequestModel.getLeaveType());
            intent.putExtra("startDate", leaveRequestModel.getStartDate());
            intent.putExtra("endDate", leaveRequestModel.getEndDate());
            intent.putExtra("numberOfDays", leaveRequestModel.getNumberOfDays());
            intent.putExtra("reason", leaveRequestModel.getReason());
            intent.putExtra("createdAt", leaveRequestModel.getCreatedAt());
            intent.putExtra("status", leaveRequestModel.getStatus());
            intent.putExtra("hrReview", leaveRequestModel.getHrReview());
            intent.putExtra("hodReview", leaveRequestModel.getHodReview());
            intent.putExtra("dhrmReview", leaveRequestModel.getDhrmReview());

            // Start the Leave Request Detail Activity
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return EmployeeleaveRequestsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        MaterialCardView cardViewRecent;
        MaterialTextView usernameTextView, timeanddate_textView, typeTextView, statusTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewRecent = itemView.findViewById(R.id.cardViewRecent);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            timeanddate_textView = itemView.findViewById(R.id.dateTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }
}


