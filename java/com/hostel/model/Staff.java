package com.hostel.model;

public class Staff {
    private int staffId;
    private String name;
    private String role;
    private String phone;
    private String email;

    public Staff(int staffId, String name, String role, String phone, String email) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.email = email;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
