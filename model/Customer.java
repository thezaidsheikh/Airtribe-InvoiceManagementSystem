package model;

import java.util.Date;

import common.utils;

public class Customer {

    private int customerId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Date registrationDate;

    public Customer() {
    }

    protected Customer(String name, String email, String phone, String address) {
        this.customerId = utils.generateId(4);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = new Date();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
