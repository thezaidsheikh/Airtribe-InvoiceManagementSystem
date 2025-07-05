package main;

import java.util.Scanner;

import service.CustomerService;
import service.InvoiceService;
import service.ProductService;

public class InvoiceManagement {
    Scanner scn = new Scanner(System.in);
    ProductService productService = new ProductService(scn);
    CustomerService customerService = new CustomerService(scn);
    InvoiceService invoiceService = new InvoiceService(scn, customerService, productService);

    protected void setup() {
        productService.loadProducts();
        customerService.loadCustomers();
        invoiceService.loadInvoices();
        System.out.println("=========WELCOME TO INVOICE MANAGEMENT SYSTEM=====================");
        System.out.println("==========KINDLY SELECT THE OPTION======================\n");
        while (true) {
            // Customer related options
            System.out.println("=== CUSTOMER MANAGEMENT ===\n");
            System.out.println("1.Add Customer");
            System.out.println("2.Update Customer");
            System.out.println("3.Search Customer");
            System.out.println("4.View Customer History\n");

            // Product related options
            System.out.println("=== PRODUCT MANAGEMENT ===\n");
            System.out.println("5.Add Product");
            System.out.println("6.Update Stock");
            System.out.println("7.Search Product");
            System.out.println("8.View low stock Items");
            System.out.println("9.View All Products\n");

            // Invoice related options
            System.out.println("=== INVOICE MANAGEMENT ===\n");
            System.out.println("10.Create Invoice");
            System.out.println("11.View All Invoices");
            System.out.println("12.Exit\n");
            System.out.println("=======================================================");

            String choice = scn.nextLine();
            System.out.println("You have selected: " + choice);

            switch (choice) {
                case "1":
                    customerService.addCustomer();
                    break;
                case "2":
                    customerService.updateCustomer();
                    break;
                case "3":
                    customerService.searchCustomer();
                    break;
                case "4":
                    customerService.viewAllCustomers();
                    break;
                case "5":
                    productService.addProduct();
                    break;
                case "6":
                    productService.updateStock();
                    break;
                case "7":
                    productService.searchProduct();
                    break;
                case "8":
                    productService.viewLowStockProducts();
                    break;
                case "9":
                    productService.viewAllProducts();
                    break;
                case "10":
                    invoiceService.createInvoice();
                    break;
                case "11":
                    invoiceService.viewAllInvoices();
                    break;
                case "12":
                    System.out.println("Thank you for using the Expense Tracker. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        }
    }
}
