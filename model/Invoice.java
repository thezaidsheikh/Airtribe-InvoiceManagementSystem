package model;

import java.util.ArrayList;
import java.util.List;

import common.InvoiceStatus;
import common.PaymentMethod;
import common.utils;

public class Invoice {

    private String invoiceId;
    private int customerId;
    private String customerName;
    private Customer customerDetail;
    private long invoiceDate = utils.getEpochTime();
    private long dueDate = utils.getEpochTime() + 30 * 24 * 60 * 60; // Default 30 days payment term
    private List<InvoiceItem> items;
    private long subtotal;
    private long taxAmount;
    private long discountAmount;
    private long finalAmount;
    private PaymentMethod paymentMethod;
    private InvoiceStatus status = InvoiceStatus.DRAFT;
    private String notes;

    public Invoice() {
        this.items = new ArrayList<>();
        this.invoiceDate = utils.getEpochTime();
        this.status = InvoiceStatus.DRAFT;
    }

    public Invoice(String invoiceId, Customer customerDetail, List<InvoiceItem> items, long invoiceDate,
            long dueDate, InvoiceStatus status, PaymentMethod paymentMethod, String notes) {
        this.invoiceId = invoiceId;
        this.customerId = customerDetail.getCustomerId();
        this.customerName = customerDetail.getName();
        this.items = items;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.customerDetail = customerDetail;
    }

    // Getters and Setters
    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(long invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public void addItem(InvoiceItem item) {
        this.items.add(item);
        calculateTotals();
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            calculateTotals();
        }
    }

    public long getSubtotal() {
        return subtotal;
    }

    public long getTaxAmount() {
        return taxAmount;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public long getFinalAmount() {
        this.calculateTotals();
        // if(this.customerDetail instanceof PremiumCustomer) {
        // this.finalAmount = this.finalAmount * 0.9;
        // }
        return this.finalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public InvoiceStatus getStatus() {
        return this.status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Calculate totals based on items
    public void calculateTotals() {
        this.subtotal = 0;
        this.taxAmount = 0;
        this.discountAmount = 0;

        for (InvoiceItem item : this.items) {
            System.out.println("Items: " + item.getProductDetail().toString());
            this.subtotal += item.getProductDetail().getBasePrice();
            this.taxAmount += item.getProductDetail().getTaxAmount();
            this.discountAmount += item.getProductDetail().getSeasonalDiscountAmount();
        }

        this.finalAmount = this.subtotal + this.taxAmount - this.discountAmount;
    }
}
