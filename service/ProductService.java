package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import common.ProductCategory;
import common.utils;
import model.DigitalService;
import model.PhysicalProduct;
import model.Product;

public class ProductService {
    private List<Product> products = new ArrayList<>();
    private final Scanner scanner;

    public ProductService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void loadProducts() {
        this.products = utils.readData("./db/products.txt").map(line -> {
            String[] parts = line.split(" ");
            int productId = Integer.parseInt(parts[0]);
            String name = parts[1];
            String category = parts[2];
            long basePrice = Long.parseLong(parts[3]);
            int taxRate = Integer.parseInt(parts[4]);
            int seasonalDiscount = Integer.parseInt(parts[5]);
            String productType = parts[6];
            if (productType.equals("PhysicalProduct")) {
                int stockQuantity = Integer.parseInt(parts[7]);
                int reorderLevel = Integer.parseInt(parts[8]);
                String supplierName = parts[9];
                long supplierContact = Long.parseLong(parts[10]);
                return new PhysicalProduct(productId, name, ProductCategory.valueOf(category), basePrice, taxRate,
                        seasonalDiscount, stockQuantity, reorderLevel, supplierName, supplierContact);
            } else {
                return new DigitalService(productId, name, ProductCategory.valueOf(category), basePrice, taxRate,
                        seasonalDiscount);
            }
        }).collect(Collectors.toList());
    }

    public void addProduct() {
        System.out.println("========================== ADD PRODUCT =============================");

        System.out.print("Enter Product Name: ");
        String name = this.scanner.nextLine().split("\\s+")[0];
        String cat;
        while (true) {
            System.out.print("Enter Product Category (Electronics, Clothing, Books, Food): ");
            cat = this.scanner.nextLine().split("\\s+")[0];
            if (!cat.equals(ProductCategory.Electronics.name()) && !cat.equals(ProductCategory.Clothing.name())
                    && !cat.equals(ProductCategory.Books.name()) && !cat.equals(ProductCategory.Food.name())) {
                System.out.println("Invalid category. Please try again.");
                continue;
            }
            break;
        }

        System.out.print("ENTER THE BASE PRICE: ");
        long basePrice = Long.parseLong(this.scanner.nextLine().split("\\s+")[0]);

        System.out.print("ENTER THE TAX RATE: ");
        int taxRate = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);

        System.out.print("ENTER THE SEASONAL DISCOUNT: ");
        int seasonalDiscount = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);

        System.out.println("Select the type of product: \n1. Physical Product\n2. Digital Service\n");
        String productType = this.scanner.nextLine().split("\\s+")[0];

        if (productType.equals("1")) {
            System.out.print("ENTER THE STOCK QUANTITY: ");
            int stockQuantity = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);

            System.out.print("ENTER THE REORDER LEVEL: ");
            int reorderLevel = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);

            System.out.print("ENTER THE SUPPLIER Name: ");
            String supplierName = this.scanner.nextLine().split("\\s+")[0];

            System.out.print("ENTER THE SUPPLIER Contact: ");
            long supplierContact = Long.parseLong(this.scanner.nextLine().split("\\s+")[0]);

            PhysicalProduct physicalProduct = new PhysicalProduct(utils.generateId(4), name,
                    ProductCategory.valueOf(cat),
                    basePrice, taxRate,
                    seasonalDiscount, stockQuantity, reorderLevel,
                    supplierName, supplierContact);
            this.products.add(physicalProduct);
        } else {
            DigitalService digitalService = new DigitalService(utils.generateId(4), name,
                    ProductCategory.valueOf(cat),
                    basePrice, taxRate, seasonalDiscount);
            this.products.add(digitalService);
        }
        utils.saveData("./db/products.txt", this.products);
        System.out.println("===== PRODUCT ADDED SUCCESSFULLY ====================");
        System.out.println("=====================================");
    }

    public void viewAllProducts() {
        System.out.println("========================== VIEW ALL PRODUCTS =============================");
        if (this.products.size() == 0) {
            System.out.println("NO PRODUCTS YET");
            System.out.println("=====================================");
            return;
        }
        System.out.println(
                "PRODUCT ID\tNAME\tCATEGORY\tBASE PRICE\tTAX RATE\tSEASONAL DISCOUNT\tPRODUCT TYPE\tStock Quantity\tReorder Level\tSupplier Name\tSupplier Contact");
        for (int i = 0; i < this.products.size(); i++) {
            if (this.products.get(i) instanceof PhysicalProduct) {
                PhysicalProduct physicalProduct = (PhysicalProduct) this.products.get(i);
                System.out.println(physicalProduct.getProductId() + "\t" + physicalProduct.getName() + "\t"
                        + physicalProduct.getCategory() + "\t" + physicalProduct.getBasePrice() + "\t"
                        + physicalProduct.getTaxRate() + "\t" + physicalProduct.getSeasonalDiscount() + "\t"
                        + physicalProduct.getProductType() + "\t" + physicalProduct.getStockQuantity() + "\t"
                        + physicalProduct.getReorderLevel() + "\t" + physicalProduct.getSupplierName() + "\t"
                        + physicalProduct.getSupplierContact());
            } else {
                System.out.println(this.products.get(i).getProductId() + "\t" + this.products.get(i).getName() + "\t"
                        + this.products.get(i).getCategory() + "\t" + this.products.get(i).getBasePrice() + "\t"
                        + this.products.get(i).getTaxRate() + "\t" + this.products.get(i).getSeasonalDiscount() + "\t"
                        + this.products.get(i).getProductType());
            }
        }
        System.out.println("=====================================");
    }
}
