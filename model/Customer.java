package model;

import java.util.Date;

public class Customer {

    protected int customerId;
    protected String name;
    protected String email;
    protected long phone;
    protected String address;
    protected long registrationDate;
    protected String customerType;

    public Customer() {
    }

    public Customer(int customerId, String name, String email, long phone, String address, long registrationDate,
            String customerType) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = registrationDate;
        this.customerType = customerType;
    }

    public int getCustomerId() {
        return customerId;
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

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getRegistrationDate() {
        return registrationDate;
    }

    public String getCustomerType() {
        return customerType;
    }

    @Override
    public String toString() {
        return this.customerId + " " + this.name + " " + this.email + " " + this.phone + " " + this.address + " "
                + this.registrationDate + " " + this.customerType;
    }
}
