package model;

import common.PaymentTerms;

public class CorporateCustomer extends Customer {
    private long creditLimit;
    private PaymentTerms paymentTerms;
    private boolean taxExemptionStatus;

    public CorporateCustomer(int customerId, String name, String email, long phone, String address,
            long registrationDate, long creditLimit, PaymentTerms paymentTerms, boolean taxExemptionStatus) {
        super(customerId, name, email, phone, address, registrationDate, "Corporate");
        this.creditLimit = creditLimit;
        this.paymentTerms = paymentTerms;
        this.taxExemptionStatus = taxExemptionStatus;
    }

    public long getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(long creditLimit) {
        this.creditLimit = creditLimit;
    }

    public PaymentTerms getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(PaymentTerms paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public boolean getTaxExemptionStatus() {
        return taxExemptionStatus;
    }

    public void setTaxExemptionStatus(boolean taxExemptionStatus) {
        this.taxExemptionStatus = taxExemptionStatus;
    }

    @Override
    public String toString() {
        return this.customerId + " " + this.name + " " + this.email + " " + this.phone + " " + this.address + " "
                + this.registrationDate + " " + this.customerType + " " + this.creditLimit + " " + this.paymentTerms
                + " " + this.taxExemptionStatus;
    }
}
