package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import common.PaymentTerms;
import common.utils;
import model.CorporateCustomer;
import model.Customer;
import model.PremiumCustomer;

public class CustomerService {
    private List<Customer> customers = new ArrayList<>();
    private final Scanner scanner;

    public CustomerService(Scanner scanner) {
        this.scanner = scanner;
    }

    // Load customers from file on app startup
    public void loadCustomers() {
        this.customers = utils.readData("./db/customers.txt").map(line -> {
            String[] parts = line.split(" ");
            int customerId = Integer.parseInt(parts[0]);
            String name = parts[1];
            String email = parts[2];
            long phone = Long.parseLong(parts[3]);
            String address = parts[4];
            long registrationDate = Long.parseLong(parts[5]);
            String customerType = parts[6];
            if (customerType.equals("Corporate")) {
                long creditLimit = Long.parseLong(parts[7]);
                String paymentTerms = parts[8];
                boolean taxExemptionStatus = Boolean.parseBoolean(parts[9]);
                return new CorporateCustomer(customerId, name, email, phone, address, registrationDate, creditLimit,
                        PaymentTerms.valueOf(paymentTerms.toUpperCase()), taxExemptionStatus);
            } else if (customerType.equals("Premium")) {
                return new PremiumCustomer(customerId, name, email, phone, address, registrationDate);
            } else if (customerType.equals("Regular")) {
                return new Customer(customerId, name, email, phone, address, registrationDate, "Regular");
            } else {
                return null;
            }
        }).collect(Collectors.toList());
    }

    // Add new Customer to the application
    public void addCustomer() {
        System.out.println("========================== ADD CUSTOMER =============================");
        System.out.print("Enter Customer Name (mandatory) - (min 15 characters): ");
        String name = this.scanner.nextLine().split("\\s+")[0];
        if (name.isEmpty() || name.length() > 15) {
            System.out.println("Error: Customer name is not valid");
            return;
        }

        System.out.print("Enter Customer Contact (mandatory) - (10 digits): ");
        long contact = Long.parseLong(this.scanner.nextLine().split("\\s+")[0]);
        if (String.valueOf(contact).length() != 10) {
            System.out.println("Error: Customer contact must be 10 digits");
            return;
        }

        System.out.print("Enter Customer Email (mandatory) - (contains @): ");
        String email = this.scanner.nextLine().split("\\s+")[0];
        if (email.isEmpty() || !email.contains("@")) {
            System.out.println("Error: Customer email is invalid");
            return;
        }

        System.out.print("Enter Customer Address (mandatory) - (max 50 characters): ");
        String address = this.scanner.nextLine().split("\\s+")[0];
        if (address.isEmpty() || address.length() > 50) {
            System.out.println("Error: Customer address cannot be empty");
            return;
        }

        System.out.println(
                "Select the type of customer: \n1. Premium Customer\n2. Corporate Customer\n3. Regular Customer");
        String customerType = this.scanner.nextLine().split("\\s+")[0];
        if (customerType.equals("1")) {
            PremiumCustomer premiumCustomer = new PremiumCustomer(utils.generateId(4), name, email, contact,
                    address, utils.getEpochTime());
            this.customers.add(premiumCustomer);
        } else if (customerType.equals("2")) {
            System.out.print("Enter the credit limit (mandatory) - (greater than 0): ");
            long creditLimit = Long.parseLong(this.scanner.nextLine().split("\\s+")[0]);
            if (creditLimit <= 0) {
                System.out.println("Error: Credit limit is not valid");
                return;
            }

            System.out.print("Enter the payment terms (mandatory) - (NET_30, NET_60, NET_90): ");
            String paymentTerms = this.scanner.nextLine().split("\\s+")[0];
            if (!paymentTerms.equals("NET_30") && !paymentTerms.equals("NET_60") && !paymentTerms.equals("NET_90")) {
                System.out.println("Error: Payment terms is not valid");
                return;
            }

            System.out.print("Enter the tax exemption status: ");
            boolean taxExemptionStatus = this.scanner.nextBoolean();

            PaymentTerms paymentTermsEnum = PaymentTerms.valueOf(paymentTerms.toUpperCase());

            CorporateCustomer corporateCustomer = new CorporateCustomer(utils.generateId(4), name, email, contact,
                    address, utils.getEpochTime(), creditLimit, paymentTermsEnum, taxExemptionStatus);
            this.customers.add(corporateCustomer);
        } else if (customerType.equals("3")) {
            Customer customer = new Customer(utils.generateId(4), name, email, contact,
                    address, utils.getEpochTime(), "Regular");
            this.customers.add(customer);
        } else {
            System.out.println("Error: Invalid customer type");
            return;
        }

        utils.saveData("./db/customers.txt", this.customers);
        System.out.println("===== CUSTOMER ADDED SUCCESSFULLY ====================");
        System.out.println("=====================================");
    }

    public void updateCustomer() {
        System.out.println("========================== UPDATE CUSTOMER =============================");

        System.out.print("Enter Customer ID: ");
        int customerId = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
        int customerIndex = IntStream.range(0, this.customers.size())
                .filter(i -> this.customers.get(i).getCustomerId() == customerId)
                .findFirst().orElse(-1);
        if (customerIndex == -1) {
            System.out.println("Error: Customer not found");
            System.out.println("=====================================");
            return;
        }
        Customer customer = this.customers.get(customerIndex);

        System.out.print("Enter the new name (current name: " + customer.getName()
                + ") (press enter to skip - min 15 characters): ");
        String name = this.scanner.nextLine().split("\\s+")[0];
        if (!name.isEmpty() && name.length() > 15) {
            System.out.println("Error: Customer name is not valid");
            return;
        }
        if (!name.isEmpty()) {
            customer.setName(name);
        }

        System.out.print("Enter the new email (current email: " + customer.getEmail()
                + ") (press enter to skip - contains @): ");
        String email = this.scanner.nextLine().split("\\s+")[0];
        if (!email.isEmpty() && !email.contains("@")) {
            System.out.println("Error: Customer email is invalid");
            return;
        }
        if (!email.isEmpty()) {
            customer.setEmail(email);
        }

        System.out.print(
                "Enter the new phone (current phone: " + customer.getPhone() + ") (press enter to skip - 10 digits): ");
        String phone = this.scanner.nextLine().split("\\s+")[0];
        if (!phone.isEmpty() && phone.length() != 10) {
            System.out.println("Error: Customer phone is invalid");
            return;
        }
        if (!phone.isEmpty()) {
            customer.setPhone(Long.parseLong(phone));
        }

        System.out.print(
                "Enter the new address (current address: " + customer.getAddress()
                        + ") (press enter to skip - max 50 characters): ");
        String address = this.scanner.nextLine().split("\\s+")[0];
        if (!address.isEmpty() && address.length() > 50) {
            System.out.println("Error: Customer address is invalid");
            return;
        }
        if (!address.isEmpty()) {
            customer.setAddress(address);
        }

        this.customers.set(customerIndex, customer);
        utils.saveData("./db/customers.txt", this.customers);
        System.out.println("===== CUSTOMER UPDATED SUCCESSFULLY ====================");
        System.out.println("=====================================");
    }

    public void searchCustomer() {
        System.out.println("========================== SEARCH CUSTOMER =============================");
        System.out.println("1.Search Customer by ID");
        System.out.println("2.Search Customer by Name");
        System.out.println("3.Search Customer by Email");
        String value = this.scanner.nextLine().split("\\s+")[0];
        switch (value) {
            case "1":
                searchCustomerByID();
                break;
            case "2":
                searchCustomerByName();
                break;
            case "3":
                searchCustomerByEmail();
                break;
            default:
                System.out.println("Error: Invalid choice");
                break;
        }
    }

    public void searchCustomerByID() {
        System.out.println("========================== SEARCH CUSTOMER =============================");
        System.out.print("Enter Customer ID: ");
        int customerId = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
        List<Customer> customer = this.customers.stream().filter(p -> p.getCustomerId() == customerId)
                .collect(Collectors.toList());
        if (customer.size() == 0) {
            System.out.println("Error: Customer not found");
            System.out.println("=====================================");
            return;
        }
        showCustomerList(customer);
    }

    public void searchCustomerByName() {
        System.out.println("========================== SEARCH CUSTOMER =============================");
        System.out.print("Enter Customer Name: ");
        String name = this.scanner.nextLine().split("\\s+")[0];
        List<Customer> customer = this.customers.stream().filter(p -> p.getName().equals(name))
                .collect(Collectors.toList());
        if (customer.size() == 0) {
            System.out.println("Error: Customer not found");
            System.out.println("=====================================");
            return;
        }
        showCustomerList(customer);
    }

    public void searchCustomerByEmail() {
        System.out.println("========================== SEARCH CUSTOMER =============================");
        System.out.print("Enter Customer Email: ");
        String email = this.scanner.nextLine().split("\\s+")[0];
        List<Customer> customer = this.customers.stream().filter(p -> p.getEmail().equals(email))
                .collect(Collectors.toList());
        if (customer.size() == 0) {
            System.out.println("Error: Customer not found");
            System.out.println("=====================================");
            return;
        }
        showCustomerList(customer);
    }

    public void viewAllCustomers() {
        System.out.println("========================== VIEW ALL CUSTOMERS =============================");
        showCustomerList(this.customers);
    }

    private void showCustomerList(List<Customer> customersList) {
        System.out.println("Result -\n");
        if (customersList.size() == 0) {
            System.out.println("Error: NO CUSTOMERS YET");
            System.out.println("=====================================");
            return;
        }
        String[] headers = { "CUSTOMER ID", "NAME", "EMAIL", "PHONE", "ADDRESS", "REGISTRATION DATE", "CUSTOMER TYPE",
                "DISCOUNT PERCENTAGE", "LOYALTY POINTS", "CREDIT LIMIT", "PAYMENT TERMS", "TAX EXEMPTION STATUS" };

        // Calculate max width for each column
        int[] colWidths = new int[headers.length];
        colWidths[0] = headers[0].length();
        colWidths[1] = headers[1].length();
        colWidths[2] = headers[2].length();
        colWidths[3] = headers[3].length();
        colWidths[4] = headers[4].length();
        colWidths[5] = headers[5].length();
        colWidths[6] = headers[6].length();
        colWidths[7] = headers[7].length();
        colWidths[8] = headers[8].length();
        colWidths[9] = headers[9].length();
        colWidths[10] = headers[10].length();
        colWidths[11] = headers[11].length();

        for (Customer customer : customersList) {
            if (customer instanceof PremiumCustomer) {
                PremiumCustomer premiumCustomer = (PremiumCustomer) customer;
                colWidths[0] = Math.max(colWidths[0], String.valueOf(premiumCustomer.getCustomerId()).length());
                colWidths[1] = Math.max(colWidths[1], premiumCustomer.getName().length());
                colWidths[2] = Math.max(colWidths[2], premiumCustomer.getEmail().length());
                colWidths[3] = Math.max(colWidths[3], String.valueOf(premiumCustomer.getPhone()).length());
                colWidths[4] = Math.max(colWidths[4], premiumCustomer.getAddress().length());
                colWidths[5] = Math.max(colWidths[5],
                        utils.convertEpochToDateTime(premiumCustomer.getRegistrationDate()).length());
                colWidths[6] = Math.max(colWidths[6], premiumCustomer.getCustomerType().length());
                colWidths[7] = Math.max(colWidths[7], String.valueOf(premiumCustomer.getDiscountPercentage()).length());
                colWidths[8] = Math.max(colWidths[8], String.valueOf(premiumCustomer.getLoyaltyPoints()).length());
            } else if (customer instanceof CorporateCustomer) {
                CorporateCustomer corporateCustomer = (CorporateCustomer) customer;
                colWidths[0] = Math.max(colWidths[0], String.valueOf(corporateCustomer.getCustomerId()).length());
                colWidths[1] = Math.max(colWidths[1], corporateCustomer.getName().length());
                colWidths[2] = Math.max(colWidths[2], corporateCustomer.getEmail().length());
                colWidths[3] = Math.max(colWidths[3], String.valueOf(corporateCustomer.getPhone()).length());
                colWidths[4] = Math.max(colWidths[4], corporateCustomer.getAddress().length());
                colWidths[5] = Math.max(colWidths[5],
                        utils.convertEpochToDateTime(corporateCustomer.getRegistrationDate()).length());
                colWidths[6] = Math.max(colWidths[6], corporateCustomer.getCustomerType().length());
                colWidths[9] = Math.max(colWidths[9], String.valueOf(corporateCustomer.getCreditLimit()).length());
                colWidths[10] = Math.max(colWidths[10], corporateCustomer.getPaymentTerms().toString().length());
                colWidths[11] = Math.max(colWidths[11],
                        String.valueOf(corporateCustomer.getTaxExemptionStatus()).length());
            } else {
                Customer regularCustomer = (Customer) customer;
                colWidths[0] = Math.max(colWidths[0], String.valueOf(regularCustomer.getCustomerId()).length());
                colWidths[1] = Math.max(colWidths[1], regularCustomer.getName().length());
                colWidths[2] = Math.max(colWidths[2], regularCustomer.getEmail().length());
                colWidths[3] = Math.max(colWidths[3], String.valueOf(regularCustomer.getPhone()).length());
                colWidths[4] = Math.max(colWidths[4], regularCustomer.getAddress().length());
                colWidths[5] = Math.max(colWidths[5],
                        utils.convertEpochToDateTime(regularCustomer.getRegistrationDate()).length());
                colWidths[6] = Math.max(colWidths[6], regularCustomer.getCustomerType().length());
            }
        }

        // Build format string
        String format = String.format(
                "%%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds%n",
                colWidths[0], colWidths[1], colWidths[2], colWidths[3], colWidths[4], colWidths[5], colWidths[6],
                colWidths[7], colWidths[8], colWidths[9], colWidths[10], colWidths[11]);

        // Print header
        System.out.printf(format, (Object[]) headers);
        System.out.println();

        // Print rows
        for (Customer customer : customersList) {
            if (customer instanceof PremiumCustomer) {
                PremiumCustomer premiumCustomer = (PremiumCustomer) customer;
                System.out.printf(format,
                        String.valueOf(premiumCustomer.getCustomerId()),
                        premiumCustomer.getName(),
                        premiumCustomer.getEmail(),
                        String.valueOf(premiumCustomer.getPhone()),
                        premiumCustomer.getAddress(),
                        utils.convertEpochToDateTime(premiumCustomer.getRegistrationDate()),
                        premiumCustomer.getCustomerType(),
                        String.valueOf(premiumCustomer.getDiscountPercentage()),
                        String.valueOf(premiumCustomer.getLoyaltyPoints()),
                        "-", "-", "-");
            } else if (customer instanceof CorporateCustomer) {
                CorporateCustomer corporateCustomer = (CorporateCustomer) customer;
                System.out.printf(format,
                        String.valueOf(corporateCustomer.getCustomerId()),
                        corporateCustomer.getName(),
                        corporateCustomer.getEmail(),
                        String.valueOf(corporateCustomer.getPhone()),
                        corporateCustomer.getAddress(),
                        utils.convertEpochToDateTime(corporateCustomer.getRegistrationDate()),
                        corporateCustomer.getCustomerType(),
                        "-",
                        "-",
                        String.valueOf(corporateCustomer.getCreditLimit()),
                        corporateCustomer.getPaymentTerms().toString(),
                        String.valueOf(corporateCustomer.getTaxExemptionStatus()));
            } else {
                Customer regularCustomer = (Customer) customer;
                System.out.printf(format,
                        String.valueOf(regularCustomer.getCustomerId()),
                        regularCustomer.getName(),
                        regularCustomer.getEmail(),
                        String.valueOf(regularCustomer.getPhone()),
                        regularCustomer.getAddress(),
                        utils.convertEpochToDateTime(regularCustomer.getRegistrationDate()),
                        regularCustomer.getCustomerType(),
                        "-", "-", "-", "-", "-");
            }
        }
        System.out.println("\n===================================== END CUSTOMER LIST =============================\n");
    }

    public Customer getCustomerById(int customerId) {
        return this.customers.stream().filter(p -> p.getCustomerId() == customerId).findFirst().orElse(null);
    }

}
