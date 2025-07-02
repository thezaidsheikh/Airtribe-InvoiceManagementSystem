package model;

public class Supplier {
    private String name;
    private String contact;

    // Parameterized constructor
    public Supplier(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}
