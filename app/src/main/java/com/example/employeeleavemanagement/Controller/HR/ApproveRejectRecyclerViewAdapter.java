package com.example.employeeleavemanagement.Controller.HR;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeleavemanagement.Model.HR.HrApproveRejectModel;
import com.example.employeeleavemanagement.R;
import com.example.employeeleavemanagement.View.HR.HrApproveRejectDetailsActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ApproveRejectRecyclerViewAdapter extends RecyclerView.Adapter<ApproveRejectRecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<HrApproveRejectModel> hrApproveRejectModelArrayList;

    public ApproveRejectRecyclerViewAdapter(Context context, ArrayList<HrApproveRejectModel> hrApproveRejectModelArrayList) {
        this.context = context;
        this.hrApproveRejectModelArrayList = hrApproveRejectModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_leave_request, parent, false);
        return new ApproveRejectRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HrApproveRejectModel hrApproveRejectModel = hrApproveRejectModelArrayList.get(position);

        holder.usernameTextView.setText(hrApproveRejectModel.getName());
        holder.timeanddate_textView.setText(hrApproveRejectModel.getCreatedAt());
        holder.typeTextView.setText(hrApproveRejectModel.getLeaveType());
        holder.statusTextView.setText(hrApproveRejectModel.getStatus());


        // Set an OnClickListener for the RecyclerView item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the Leave Request Detail Activity
                Intent intent = new Intent(context, HrApproveRejectDetailsActivity.class);

                // Pass the selected leave request data as extras
                intent.putExtra("leaveRequestId", hrApproveRejectModel.getLeaveRequestId());
                intent.putExtra("employeeId", hrApproveRejectModel.getEmployeeId());
                intent.putExtra("name", hrApproveRejectModel.getName());
                intent.putExtra("email", hrApproveRejectModel.getEmail());
                intent.putExtra("checkNo", hrApproveRejectModel.getCheckNo());
                intent.putExtra("homephone", hrApproveRejectModel.getHomephone());
                intent.putExtra("department", hrApproveRejectModel.getDepartment());
                intent.putExtra("leaveType", hrApproveRejectModel.getLeaveType());
                intent.putExtra("startDate", hrApproveRejectModel.getStartDate());
                intent.putExtra("endDate", hrApproveRejectModel.getEndDate());
                intent.putExtra("numberOfDays", hrApproveRejectModel.getNumberOfDays());
                intent.putExtra("reason", hrApproveRejectModel.getReason());
                intent.putExtra("createdAt", hrApproveRejectModel.getCreatedAt());
                intent.putExtra("status", hrApproveRejectModel.getStatus());
                intent.putExtra("review", hrApproveRejectModel.getReview());




                // Start the Leave Request Detail Activity
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hrApproveRejectModelArrayList.size();
    }

    public void updateData(ArrayList<HrApproveRejectModel> leaveRequests) {
        this.hrApproveRejectModelArrayList.clear();
        this.hrApproveRejectModelArrayList.addAll(leaveRequests);
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
