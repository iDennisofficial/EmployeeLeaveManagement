package com.example.employeeleavemanagement.Utils;


public class RetrieveEmployee {
    private String uid;
    private String name;
    private String email;
    private String employeeID;
    private String phoneNumber;
    private String birthday;
    private String gender;
    private String password;

    public RetrieveEmployee() {}

    public RetrieveEmployee(String uid, String name, String email, String employeeID, String phoneNumber, String birthday, String gender, String password) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.employeeID = employeeID;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.gender = gender;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
