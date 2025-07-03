package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import common.InvoiceStatus;
import common.PaymentMethod;
import common.utils;

public class Invoice {

    private String invoiceId;
    private int customerId;
    private String customerName;
    private long invoiceDate;
    private long dueDate;
    private List<InvoiceItem> items;
    private long subtotal;
    private long taxAmount;
    private long discountAmount;
    private long totalAmount;
    private PaymentMethod paymentMethod;
    private InvoiceStatus status;
    private String notes;

    public Invoice() {
        this.items = new ArrayList<>();
        this.invoiceDate = utils.getEpochTime();
        this.status = InvoiceStatus.DRAFT;
    }

    public Invoice(String invoiceId, int customerId, String customerName) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.invoiceDate = utils.getEpochTime();
        this.dueDate = utils.getEpochTime() + 30 * 24 * 60 * 60; // Default 30 days payment term
        this.status = InvoiceStatus.DRAFT;
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

    public long getTotalAmount() {
        return totalAmount;
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

        for (InvoiceItem item : items) {
            this.subtotal += item.priceAfterTaxAndDiscount();
            this.taxAmount += item.getTaxAmount();
            this.discountAmount += item.getSeasonalDiscountAmount();
        }

        this.totalAmount = this.subtotal + this.taxAmount - this.discountAmount;
    }

    // public String getFormattedInvoiceDate() {
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
    // HH:mm:ss");
    // return utils.formatDate(invoiceDate);
    // }

    // public String getFormattedDueDate() {
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // return dueDate.format(formatter);
    // }

    // @Override
    // public String toString() {
    // return String.format("Invoice #%d | Customer: %s | Date: %s | Total: $%.2f |
    // Status: %s",
    // invoiceId, customerName, getFormattedInvoiceDate(), totalAmount / 100.0,
    // status);
    // }

    // Detailed invoice display
    // public String getDetailedInvoice() {
    // StringBuilder sb = new StringBuilder();
    // sb.append("===============================================\n");
    // sb.append(" INVOICE\n");
    // sb.append("===============================================\n");
    // sb.append(String.format("Invoice #: %d\n", invoiceId));
    // sb.append(String.format("Customer: %s (ID: %d)\n", customerName,
    // customerId));
    // sb.append(String.format("Invoice Date: %s\n", getFormattedInvoiceDate()));
    // sb.append(String.format("Due Date: %s\n", getFormattedDueDate()));
    // sb.append(String.format("Status: %s\n", status));
    // sb.append("===============================================\n");
    // sb.append("ITEMS:\n");
    // sb.append("-----------------------------------------------\n");

    // for (int i = 0; i < items.size(); i++) {
    // InvoiceItem item = items.get(i);
    // sb.append(String.format("%d. %s\n", i + 1, item.toString()));
    // }

    // sb.append("-----------------------------------------------\n");
    // sb.append(String.format("Subtotal: $%.2f\n", subtotal / 100.0));
    // sb.append(String.format("Tax: $%.2f\n", taxAmount / 100.0));
    // sb.append(String.format("Discount: -$%.2f\n", discountAmount / 100.0));
    // sb.append(String.format("TOTAL: $%.2f\n", totalAmount / 100.0));
    // sb.append("===============================================\n");

    // if (paymentMethod != null) {
    // sb.append(String.format("Payment Method: %s\n", paymentMethod));
    // }

    // if (notes != null && !notes.isEmpty()) {
    // sb.append(String.format("Notes: %s\n", notes));
    // }

    // return sb.toString();
    // }
}
