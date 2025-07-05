package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import common.InvoiceStatus;
import common.PaymentMethod;
import common.utils;
import model.CorporateCustomer;
import model.Customer;
import model.Invoice;
import model.InvoiceItem;
import model.PhysicalProduct;
import model.PremiumCustomer;
import model.Product;

public class InvoiceService {
    private Customer customerDetail;
    private Product productDetail;
    private List<InvoiceItem> invoiceItems = new ArrayList<>();
    private List<Invoice> invoices = new ArrayList<>();
    private Invoice invoice;
    private final Scanner scanner;
    private final CustomerService customerService;
    private final ProductService productService;

    public InvoiceService(Scanner scanner, CustomerService customerService, ProductService productService) {
        this.scanner = scanner;
        this.customerService = customerService;
        this.productService = productService;
    }

    public void loadInvoices() {
        this.invoices = utils.readData("./db/invoices.txt").map(line -> {

            String lineItems = line.subSequence(line.indexOf(" [") + 2, line.indexOf("] ")).toString();
            line = line.replace("[" + lineItems + "] ", "");
            String[] items = lineItems.split(",");
            List<InvoiceItem> invoiceItems = new ArrayList<>();
            for (String item : items) {
                String[] parts = item.split(" ");
                int productId = Integer.parseInt(parts[0]);
                int quantity = Integer.parseInt(parts[1]);
                long price = Long.parseLong(parts[2]);
                Product product = this.productService.getProductById(productId);
                invoiceItems.add(new InvoiceItem(productId, quantity, price, product));
            }
            String[] parts = line.split(" ");
            String invoiceId = parts[0];
            int customerId = Integer.parseInt(parts[1]);
            String customerName = parts[2];
            long invoiceDate = Long.parseLong(parts[3]);
            long dueDate = Long.parseLong(parts[4]);
            long subtotal = Long.parseLong(parts[5]);
            long taxAmount = Long.parseLong(parts[6]);
            long discountAmount = Long.parseLong(parts[7]);
            long finalAmount = Long.parseLong(parts[8]);
            PaymentMethod paymentMethod = PaymentMethod.valueOf(parts[9]);
            InvoiceStatus status = InvoiceStatus.valueOf(parts[10]);

            Customer customer = this.customerService.getCustomerById(customerId);
            Invoice invoice = new Invoice(invoiceId, customer, invoiceItems);
            invoice.setInvoiceDate(invoiceDate);
            invoice.setDueDate(dueDate);
            invoice.setSubtotal(subtotal);
            invoice.setTaxAmount(taxAmount);
            invoice.setDiscountAmount(discountAmount);
            invoice.setFinalAmount(finalAmount);
            invoice.setPaymentMethod(paymentMethod);
            invoice.setStatus(status);

            return invoice;

        }).collect(Collectors.toList());
    }

    public void createInvoice() {
        System.out.println("========================== CREATE INVOICE =============================");
        System.out.print("Enter Customer ID: ");
        int customerId = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
        this.customerDetail = this.customerService.getCustomerById(customerId);
        if (this.customerDetail == null) {
            System.out.println("Error: Customer not found");
            System.out.println("=====================================");
            return;
        }

        long totalInvoiceItemPrice = 0;
        int totalQuantity = 0;
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
                System.out.println("Error: Product not found");
                System.out.println("=====================================");
                return;
            }

            System.out.println("Enter quantity: ");
            int quantity = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
            if (quantity <= 0) {
                System.out.println("Error: Quantity must be greater than 0");
                System.out.println("=====================================");
                continue;
            }
            if (this.productDetail instanceof PhysicalProduct) {
                PhysicalProduct physicalProduct = (PhysicalProduct) this.productDetail;
                if (quantity > physicalProduct.getStockQuantity()) {
                    System.out.println("Error: Quantity is greater than stock quantity");
                    System.out.println("=====================================");
                    continue;
                }
            }
            long price = this.productDetail.priceAfterTaxAndDiscount() * quantity;
            totalInvoiceItemPrice += price;
            totalQuantity += quantity;
            InvoiceItem invoiceItem = new InvoiceItem(Integer.parseInt(productId), quantity, price, this.productDetail);
            this.invoiceItems.add(invoiceItem);
        }

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

        this.invoice.setPaymentMethod(PaymentMethod.NULL);
        if (this.customerDetail instanceof CorporateCustomer) {
            CorporateCustomer corporateCustomer = (CorporateCustomer) this.customerDetail;
            if (totalInvoiceItemPrice > corporateCustomer.getCreditLimit()) {
                System.out.println("Error: Credit limit exceeded");
                System.out.println("=====================================");
                return;
            }
            if (corporateCustomer.getTaxExemptionStatus()) {
                long finalAmount = this.invoice.getFinalAmount() - this.invoice.getTaxAmount();
                this.invoice.setTaxAmount(0);
                this.invoice.setFinalAmount(finalAmount);
            }
            if (totalQuantity > 100) {
                long discountAmount = utils.getDiscountAmount(this.invoice.getFinalAmount(),
                        10);
                long finalAmount = this.invoice.getFinalAmount() - discountAmount;
                this.invoice.setDiscountAmount(this.invoice.getDiscountAmount() + discountAmount);
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

    public void viewAllInvoices() {
        System.out.println("========================== VIEW ALL PRODUCTS =============================");
        showInvoiceItems(this.invoices);
    }

    private void showInvoiceItems(List<Invoice> invoices) {
        System.out.println("Result -\n");
        if (invoices.size() == 0) {
            System.out.println("Error: NO INVOICES YET");
            System.out.println("=====================================");
            return;
        }

        for (Invoice invoice : invoices) {
            System.out.println(invoice.toString());
        }
    }

}
