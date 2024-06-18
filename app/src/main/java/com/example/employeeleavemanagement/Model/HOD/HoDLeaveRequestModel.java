package com.example.employeeleavemanagement.Model.HOD;

public class HoDLeaveRequestModel {
    private String leaveRequestId;
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



    public HoDLeaveRequestModel() {
    }

    public HoDLeaveRequestModel( String name, String leaveType, String createdAt, String status) {
        this.name = name;
        this.leaveType = leaveType;
        this.createdAt = createdAt;
        this.status = status;
    }

    public HoDLeaveRequestModel(String leaveRequestId, String employeeId, String name, String email,
                                String checkNo, String homephone, String department, String leaveType,
                                String startDate, String endDate, long numberOfDays, String reason, String createdAt, String status) {
        this.leaveRequestId = leaveRequestId;
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
    }

    public String getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(String leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
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
}
