package com.example.gatescape.models;

public class RequestInfo {

    String reason;
    UserData user;
    Long createdAt;
    Boolean approve = false;

    public RequestInfo() {

    }

    public RequestInfo(String reason, UserData user, Long createdAt, Boolean approve) {
        this.reason = reason;
        this.user = user;
        this.createdAt = createdAt;
        this.approve = approve;
    }


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public Boolean getApprove() {
        return approve;
    }

    public void setApprove(Boolean approve) {
        this.approve = approve;
    }

}
