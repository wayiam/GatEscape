package com.example.gatescape.models;

public class
UserData {

    String name, branch, sem, roll_no, email, phone_no, parent_no , password , UserType;

    public UserData(String name, String branch, String sem, String roll_no, String email, String phone_no,
                    String parent_no, String password , String userType) {
        this.name = name;
        this.branch = branch;
        this.sem = sem;
        this.roll_no = roll_no;
        this.email = email;
        this.phone_no = phone_no;
        this.password = password;
        this.UserType = userType;
        this.parent_no = parent_no;
    }

    public UserData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getParent_no() {
        return parent_no;
    }

    public void setParent_no(String parent_no) {
        this.parent_no = parent_no;
    }

}
