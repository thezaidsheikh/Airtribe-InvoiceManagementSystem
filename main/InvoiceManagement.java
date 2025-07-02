package main;

import java.util.Scanner;

import service.ProductService;

public class InvoiceManagement {
    Scanner scn = new Scanner(System.in);
    ProductService productService = new ProductService(scn);

    protected void setup() {
        productService.loadProducts();
        System.out.println("=========WELCOME TO EXPENSE TRACKER=====================");
        System.out.println("==========KINDLY SELECT THE OPTION======================");
        while (true) {
            System.out.println("1.ADD PRODUCT");
            System.out.println("2.VIEW ALL PRODUCTS");
            System.out.println("3.Search Product");
            System.out.println("4.Set Prices for Premium Customer");
            System.out.println("5.UPDATE PRODUCT");

            System.out.println("6.Add Customer");
            System.out.println("7.View All Customers");
            System.out.println("8.Update Customer");
            System.out.println("9.Exit");
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
                case "9":
                    System.out.println("Thank you for using the Expense Tracker. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        }
    }
}
