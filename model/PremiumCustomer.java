package model;

public class PremiumCustomer extends Customer {
    private final static int discountPercentage = 10;
    private int loyaltyPoints = 100;

    public PremiumCustomer(int customerId, String name, String email, long phone, String address,
            long registrationDate) {
        super(customerId, name, email, phone, address, registrationDate, "Premium");
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public String toString() {
        return this.customerId + " " + this.name + " " + this.email + " " + this.phone + " " + this.address + " "
                + this.registrationDate + " " + this.customerType + " " + this.discountPercentage + " "
                + this.loyaltyPoints;
    }
}
