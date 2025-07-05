package model;

import java.util.ArrayList;
import java.util.List;

import common.InvoiceStatus;
import common.PaymentMethod;
import common.PaymentTerms;
import common.utils;

public class Invoice {

    private String invoiceId;
    private int customerId;
    private String customerName;
    private Customer customerDetail;
    private long invoiceDate = utils.getEpochTime();
    private long dueDate = utils.getDueDate(utils.getEpochTime(), PaymentTerms.NET_30);
    private List<InvoiceItem> items;
    private long subtotal;
    private long taxAmount;
    private long discountAmount;
    private long finalAmount;
    private PaymentMethod paymentMethod;
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    public Invoice() {
        this.items = new ArrayList<>();
        this.invoiceDate = utils.getEpochTime();
        this.status = InvoiceStatus.DRAFT;
    }

    public Invoice(String invoiceId, Customer customerDetail, List<InvoiceItem> items) {
        this.invoiceId = invoiceId;
        this.customerId = customerDetail.getCustomerId();
        this.customerName = customerDetail.getName();
        this.items = items;
        this.customerDetail = customerDetail;
        this.calculateTotals();
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
        return this.subtotal;
    }

    public void setSubtotal(long subtotal) {
        this.subtotal = subtotal;
    }

    public long getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(long taxAmount) {
        this.taxAmount = taxAmount;
    }

    public long getDiscountAmount() {
        return this.discountAmount;
    }

    public void setDiscountAmount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public long getFinalAmount() {
        return this.finalAmount;
    }

    public void setFinalAmount(long finalAmount) {
        this.finalAmount = finalAmount;
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

    // Calculate totals based on items
    public void calculateTotals() {
        this.subtotal = 0;
        this.taxAmount = 0;
        this.discountAmount = 0;

        for (InvoiceItem item : this.items) {
            this.subtotal += (item.getProductDetail().getBasePrice() * item.getQuantity());
            this.taxAmount += (item.getProductDetail().getTaxAmount() * item.getQuantity());
            this.discountAmount += (item.getProductDetail().getSeasonalDiscountAmount() * item.getQuantity());
        }

        this.finalAmount = this.subtotal + this.taxAmount - this.discountAmount;
    }

    @Override
    public String toString() {
        return this.getInvoiceId() + " " + this.getCustomerId() + " " + this.getCustomerName() + " "
                + this.getInvoiceDate()
                + " " + this.getDueDate() + " " + this.getItems() + " " + this.getSubtotal()
                + " " + this.getTaxAmount() + " " + this.getDiscountAmount() + " " + this.getFinalAmount() + " "
                + this.getPaymentMethod() + " " + this.getStatus();
    }
}
