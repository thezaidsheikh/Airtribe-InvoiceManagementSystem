package model;

public class PremiumCustomer extends Customer {
    private final static int discount = 10;
    private int loyaltyPoints = 100;

    public PremiumCustomer(String name, String email, String phone, String address) {
        super(name, email, phone, address);
    }

    public int getDiscount() {
        return discount;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}
