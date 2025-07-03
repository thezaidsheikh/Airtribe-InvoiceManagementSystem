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
            this.invoiceItems.add(new InvoiceItem(Integer.parseInt(productId), quantity, this.productDetail));
        }
        this.invoice = new Invoice(utils.generateInvoiceNumber(), this.customerDetail,
                this.invoiceItems, utils.getEpochTime(),
                utils.getEpochTime() + 30 * 24 * 60 * 60,
                InvoiceStatus.DRAFT, PaymentMethod.CASH, "");
        System.out.println("Total Amount: " + this.invoice.getFinalAmount());
        System.out.println("===== INVOICE CREATED SUCCESSFULLY ====================");
        System.out.println("=====================================");
    }

}
