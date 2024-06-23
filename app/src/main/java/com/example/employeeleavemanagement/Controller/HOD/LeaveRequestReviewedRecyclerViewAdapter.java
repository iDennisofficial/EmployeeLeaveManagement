package com.example.employeeleavemanagement.Controller.HOD;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeleavemanagement.Model.HOD.HoDLeaveRequestModel;
import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.Utils.AndroidUtil;
import com.example.employeeleavemanagement.View.HOD.HoDReviewedLeaveRequestDetailActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class LeaveRequestReviewedRecyclerViewAdapter extends RecyclerView.Adapter<LeaveRequestReviewedRecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<HoDLeaveRequestModel> hoDLeaveRequestModelArrayList;

    public LeaveRequestReviewedRecyclerViewAdapter(Context context, ArrayList<HoDLeaveRequestModel> hoDLeaveRequestModelArrayList) {
        this.context = context;
        this.hoDLeaveRequestModelArrayList = hoDLeaveRequestModelArrayList;
    }

    @NonNull
    @Override
    public LeaveRequestReviewedRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_leave_request, parent, false);
        return new LeaveRequestReviewedRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveRequestReviewedRecyclerViewAdapter.ViewHolder holder, int position) {
        HoDLeaveRequestModel hoDLeaveRequestModel = hoDLeaveRequestModelArrayList.get(position);

        holder.usernameTextView.setText(hoDLeaveRequestModel.getName());
        holder.timeanddate_textView.setText(hoDLeaveRequestModel.getCreatedAt());
        holder.typeTextView.setText(hoDLeaveRequestModel.getLeaveType());
        holder.statusTextView.setText(hoDLeaveRequestModel.getStatus());

        // Set an OnClickListener for the RecyclerView item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the Leave Request Detail Activity
                Intent intent = new Intent(context, HoDReviewedLeaveRequestDetailActivity.class);

                // Pass the selected leave request data as extras
                intent.putExtra("leaveRequestId", hoDLeaveRequestModel.getLeaveRequestId());
                intent.putExtra("employeeId", hoDLeaveRequestModel.getEmployeeId());
                intent.putExtra("name", hoDLeaveRequestModel.getName());
                intent.putExtra("email", hoDLeaveRequestModel.getEmail());
                intent.putExtra("checkNo", hoDLeaveRequestModel.getCheckNo());
                intent.putExtra("homephone", hoDLeaveRequestModel.getHomephone());
                intent.putExtra("department", hoDLeaveRequestModel.getDepartment());
                intent.putExtra("leaveType", hoDLeaveRequestModel.getLeaveType());
                intent.putExtra("startDate", hoDLeaveRequestModel.getStartDate());
                intent.putExtra("endDate", hoDLeaveRequestModel.getEndDate());
                intent.putExtra("numberOfDays", hoDLeaveRequestModel.getNumberOfDays());
                intent.putExtra("reason", hoDLeaveRequestModel.getReason());
                intent.putExtra("createdAt", hoDLeaveRequestModel.getCreatedAt());
                intent.putExtra("status", hoDLeaveRequestModel.getStatus());
                intent.putExtra("review", hoDLeaveRequestModel.getReview());

                //TODO: Make sure that the review is showing appropriately, because it is not showing

                AndroidUtil.ShowToast(context,"The review is " + hoDLeaveRequestModel.getReview());

                // Start the Leave Request Detail Activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hoDLeaveRequestModelArrayList.size();
    }

    public void updateData(ArrayList<HoDLeaveRequestModel> leaveRequests) {
        this.hoDLeaveRequestModelArrayList.clear();
        this.hoDLeaveRequestModelArrayList.addAll(leaveRequests);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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