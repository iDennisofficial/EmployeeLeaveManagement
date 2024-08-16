package com.example.employeeleavemanagement.Model.Employee;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

public class LeaveRequest {
    private String employeeId;
    private String name;
    private String email;
    private String checkNo;
    private String homephone;
    private String department;
    private String leaveType;
    private String startDate;
    private String endDate;
    private long numberOfDays;
    private String reason;
    private String createdAt;
    private String status;
    private Timestamp queryTime;
    private String hrReview;
    private String hodReview;
    private String dhrmReview;

    public LeaveRequest() {
    }

    public LeaveRequest(String employeeId, String name, String email, String checkNo, String homephone,
                        String department, String leaveType, String startDate, String endDate, long numberOfDays,
                        String reason, String createdAt, String status, String hrReview, String hodReview, String dhrmReview) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.checkNo = checkNo;
        this.homephone = homephone;
        this.department = department;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.reason = reason;
        this.createdAt = createdAt;
        this.status = status;
        this.hrReview = hrReview;
        this.hodReview = hodReview;
        this.dhrmReview = dhrmReview;
    }


    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCheckNo() {
        return checkNo;
    }

    public void setCheckNo(String checkNo) {
        this.checkNo = checkNo;
    }

    public String getHomephone() {
        return homephone;
    }

    public void setHomephone(String homephone) {
        this.homephone = homephone;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(long numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Timestamp queryTime) {
        this.queryTime = queryTime;
    }

    public String getHrReview() {
        return hrReview;
    }

    public void setHrReview(String hrReview) {
        this.hrReview = hrReview;
    }

    public String getHodReview() {
        return hodReview;
    }

    public void setHodReview(String hodReview) {
        this.hodReview = hodReview;
    }

    public String getDhrmReview() {
        return dhrmReview;
    }

    public void setDhrmReview(String dhrmReview) {
        this.dhrmReview = dhrmReview;
    }
}
