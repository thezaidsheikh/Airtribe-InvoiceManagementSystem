package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import common.InvoiceStatus;
import common.PaymentMethod;
import common.utils;
import model.CorporateCustomer;
import model.Customer;
import model.Invoice;
import model.InvoiceItem;
import model.PremiumCustomer;
import model.Product;

public class InvoiceService {
    private Customer customerDetail;
    private Product productDetail;
    private List<InvoiceItem> invoiceItems = new ArrayList<>();
    private Invoice invoice;
    private final Scanner scanner;
    private final CustomerService customerService;
    private final ProductService productService;

    public InvoiceService(Scanner scanner, CustomerService customerService, ProductService productService) {
        this.scanner = scanner;
        this.customerService = customerService;
        this.productService = productService;
    }

    public void createInvoice() {
        System.out.println("========================== CREATE INVOICE =============================");
        System.out.print("Enter Customer ID: ");
        int customerId = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
        this.customerDetail = this.customerService.getCustomerById(customerId);
        if (this.customerDetail == null) {
            System.out.println("Customer not found");
            System.out.println("=====================================");
            return;
        }

        long totalInvoiceItemPrice = 0;
        System.out.println("Please add at least one product to create an invoice");

        while (true) {
            if (this.invoiceItems.size() > 0) {
                System.out.println("You have added " + this.invoiceItems.size()
                        + " products. Press enter to finish adding products");
            }
            System.out.print("Enter Product ID: ");
            String productId = this.scanner.nextLine().split("\\s+")[0];

            if (this.invoiceItems.size() > 0 && productId.isEmpty()) {
                break;
            } else if (productId.isEmpty()) {
                continue;
            }
            this.productDetail = this.productService.getProductById(Integer.parseInt(productId));
            if (this.productDetail == null) {
                System.out.println("Product not found");
                System.out.println("=====================================");
                return;
            }
            System.out.println("Enter quantity: ");
            int quantity = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than 0");
                System.out.println("=====================================");
                continue;
            }
            long price = this.productDetail.priceAfterTaxAndDiscount() * quantity;
            totalInvoiceItemPrice += price;
            InvoiceItem invoiceItem = new InvoiceItem(Integer.parseInt(productId), quantity, price, this.productDetail);
            this.invoiceItems.add(invoiceItem);
        }
        // System.out.println("Payment Method: \n1. CASH\n2. CREDIT CARD\n3. DEBIT
        // CARD\n4. UPI\n5. NET BANKING\n");
        // int paymentMethod = scanner.nextInt();
        // if (paymentMethod > 5 || paymentMethod < 1) {
        // System.out.println("Invalid payment method");
        // System.out.println("=====================================");
        // return;
        // }

        this.invoice = new Invoice(utils.generateInvoiceNumber(),
                this.customerDetail,
                this.invoiceItems);

        System.out.println("Invoice Number: " + this.invoice.getInvoiceId());
        System.out.println("Customer Name: " + this.invoice.getCustomerName());
        System.out.println("Customer ID: " + this.invoice.getCustomerId());
        System.out.println("Invoice Date: " + utils.convertEpochToDateTime(this.invoice.getInvoiceDate()));
        System.out.println("Due Date: " + utils.convertEpochToDateTime(this.invoice.getDueDate()));
        System.out.println("Payment Method: " + this.invoice.getPaymentMethod());
        System.out.println("Status: " + this.invoice.getStatus());
        System.out.println("Subtotal: " + this.invoice.getSubtotal());
        System.out.println("Tax Amount: " + this.invoice.getTaxAmount());
        System.out.println("Discount Amount: " + this.invoice.getDiscountAmount());
        System.out.println("Final Amount: " + this.invoice.getFinalAmount());

        if (this.customerDetail instanceof CorporateCustomer) {
            CorporateCustomer corporateCustomer = (CorporateCustomer) this.customerDetail;
            if (corporateCustomer.getCreditLimit() > totalInvoiceItemPrice) {
                System.out.println("Credit limit exceeded");
                System.out.println("=====================================");
                return;
            }
            if (corporateCustomer.getTaxExemptionStatus()) {
                long finalAmount = this.invoice.getFinalAmount() - this.invoice.getTaxAmount();
                this.invoice.setTaxAmount(0);
                this.invoice.setFinalAmount(finalAmount);
            }
            long dueDate = utils.getDueDate(this.invoice.getInvoiceDate(),
                    corporateCustomer.getPaymentTerms());
            this.invoice.setDueDate(dueDate);
        } else if (this.customerDetail instanceof PremiumCustomer) {
            PremiumCustomer premiumCustomer = (PremiumCustomer) this.customerDetail;
            long discountAmount = utils.getDiscountAmount(this.invoice.getFinalAmount(),
                    premiumCustomer.getDiscountPercentage());
            long finalAmount = this.invoice.getFinalAmount() - discountAmount;
            this.invoice.setDiscountAmount(this.invoice.getDiscountAmount() + discountAmount);
            this.invoice.setFinalAmount(finalAmount);
            this.invoice.setDueDate(0);
        }

        System.out.println("Subtotal: " + this.invoice.getSubtotal());
        System.out.println("Discount Amount: " + this.invoice.getDiscountAmount());
        System.out.println("Tax Amount: " + this.invoice.getTaxAmount());
        System.out.println("Final Amount: " + this.invoice.getFinalAmount());
        utils.saveData("./db/invoices.txt", List.of(this.invoice));
        System.out.println("===== INVOICE CREATED SUCCESSFULLY ====================");
        System.out.println("=====================================");
        this.invoiceItems.clear();

    }

}
