package com.example.employeeleavemanagement.Model.Common;

public class LeaveStatus {

    private String leaveRequestId;
    private boolean hodReviewed;
    private boolean hrApproved;
    private boolean hrRejected;

    public LeaveStatus(String leaveRequestId, boolean hodReviewed, boolean hrApproved, boolean hrRejected) {
        this.leaveRequestId = leaveRequestId;
        this.hodReviewed = hodReviewed;
        this.hrApproved = hrApproved;
        this.hrRejected = hrRejected;
    }

    public String getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(String leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public boolean isHodReviewed() {
        return hodReviewed;
    }

    public void setHodReviewed(boolean hodReviewed) {
        this.hodReviewed = hodReviewed;
    }

    public boolean isHrApproved() {
        return hrApproved;
    }

    public void setHrApproved(boolean hrApproved) {
        this.hrApproved = hrApproved;
    }

    public boolean isHrRejected() {
        return hrRejected;
    }

    public void setHrRejected(boolean hrRejected) {
        this.hrRejected = hrRejected;
    }
}
