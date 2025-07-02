package model;

public class CorporateCustomer extends Customer {
    private long creditLimit;
    private String paymentTerms;
    private String taxExemptionStatus;

    public CorporateCustomer(String name, String email, String phone, String address) {
        super(name, email, phone, address);
    }

    public long getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(long creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getTaxExemptionStatus() {
        return taxExemptionStatus;
    }

    public void setTaxExemptionStatus(String taxExemptionStatus) {
        this.taxExemptionStatus = taxExemptionStatus;
    }

}
