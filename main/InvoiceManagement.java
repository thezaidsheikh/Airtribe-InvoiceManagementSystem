package main;

import java.util.Scanner;

import service.CustomerService;
import service.ProductService;

public class InvoiceManagement {
    Scanner scn = new Scanner(System.in);
    ProductService productService = new ProductService(scn);
    CustomerService customerService = new CustomerService(scn);

    protected void setup() {
        productService.loadProducts();
        customerService.loadCustomers();
        System.out.println("=========WELCOME TO EXPENSE TRACKER=====================");
        System.out.println("==========KINDLY SELECT THE OPTION======================");
        while (true) {
            // Product related options
            System.out.println("1.ADD PRODUCT");
            System.out.println("2.VIEW ALL PRODUCTS");
            System.out.println("3.UPDATE PRODUCT");
            System.out.println("4.Search Product by ID");
            System.out.println("5.Search Product by Name");
            System.out.println("6.Search Product by Category");

            // Customer related options
            System.out.println("7.Add Customer");
            System.out.println("8.View All Customers");
            System.out.println("9.Update Customer");
            System.out.println("10.Search Customer by ID");
            System.out.println("11.Search Customer by Name");
            System.out.println("12.Search Customer by Email");
            System.out.println("13.Exit");
            System.out.println("=======================================================");

            String choice = scn.nextLine();
            System.out.println("You have selected: " + choice);

            switch (choice) {
                case "1":
                    productService.addProduct();
                    break;
                case "2":
                    productService.viewAllProducts();
                    break;
                case "3":
                    productService.updateProduct();
                    break;
                case "4":
                    productService.searchProductByID();
                    break;
                case "5":
                    productService.searchProductByName();
                    break;
                case "6":
                    productService.searchProductByCategory();
                    break;
                case "7":
                    customerService.addCustomer();
                    break;
                case "8":
                    customerService.viewAllCustomers();
                    break;
                // case "9":
                // customerService.updateProduct();
                // break;
                case "10":
                    customerService.searchCustomerByID();
                    break;
                case "11":
                    customerService.searchCustomerByName();
                    break;
                case "12":
                    customerService.searchCustomerByEmail();
                    break;
                case "13":
                    System.out.println("Thank you for using the Expense Tracker. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        }
    }
}
