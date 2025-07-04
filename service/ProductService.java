package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        System.out.print("Enter Product Name (mandatory - max 15 characters): ");
        String name = this.scanner.nextLine().split("\\s+")[0];
        while (name.isEmpty() || name.length() > 15) {
            System.out.println("Product name is invalid. Please try again.");
            name = this.scanner.nextLine().split("\\s+")[0];
        }
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

        System.out.print("Enter Base Price (mandatory): ");
        long basePrice = Long.parseLong(this.scanner.nextLine().split("\\s+")[0]);
        if (basePrice <= 0) {
            System.out.println("Base price is invalid. Please try again.");
            return;
        }

        System.out.print("Enter Tax Rate (mandatory): ");
        int taxRate = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
        if (taxRate < 0) {
            System.out.println("Tax rate is invalid. Please try again.");
            return;
        }

        System.out.print("Enter Seasonal Discount (mandatory): ");
        int seasonalDiscount = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
        if (seasonalDiscount < 0) {
            System.out.println("Seasonal discount is invalid. Please try again.");
            return;
        }

        System.out.println("Select the type of product: \n1. Physical Product\n2. Digital Service\n");
        String productType = this.scanner.nextLine().split("\\s+")[0];
        while (!productType.equals("1") && !productType.equals("2")) {
            System.out.println("Invalid product type. Please try again.");
            return;
        }

        if (productType.equals("1")) {
            System.out.print("Enter Stock Quantity (mandatory): ");
            int stockQuantity = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
            if (stockQuantity <= 0) {
                System.out.println("Stock quantity is invalid. Please try again.");
                return;
            }

            System.out.print("Enter Reorder Level (mandatory): ");
            int reorderLevel = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
            if (reorderLevel <= 0) {
                System.out.println("Reorder level is invalid. Please try again.");
                return;
            }

            System.out.print("Enter Supplier Name (mandatory): ");
            String supplierName = this.scanner.nextLine().split("\\s+")[0];
            if (supplierName.isEmpty()) {
                System.out.println("Supplier name is invalid. Please try again.");
                return;
            }

            System.out.print("Enter Supplier Contact (mandatory - 10 digits): ");
            long supplierContact = Long.parseLong(this.scanner.nextLine().split("\\s+")[0]);
            if (String.valueOf(supplierContact).length() != 10) {
                System.out.println("Supplier contact must be 10 digits");
                return;
            }

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
        showProductList(this.products);
    }

    private void showProductList(List<Product> productsList) {
        System.out.println("Result -\n");
        if (productsList.size() == 0) {
            System.out.println("NO PRODUCTS YET");
            System.out.println("=====================================");
            return;
        }

        String[] headers = { "PRODUCT ID", "NAME", "CATEGORY", "BASE PRICE", "TAX RATE", "SEASONAL DISCOUNT",
                "PRODUCT TYPE", "Price After Tax", "Price After Discount", "Price After Tax and Discount",
                "Stock Quantity", "Reorder Level", "Supplier Name", "Supplier Contact" };

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
        colWidths[12] = headers[12].length();
        colWidths[13] = headers[13].length();

        for (Product product : productsList) {
            if (product instanceof PhysicalProduct) {
                PhysicalProduct physicalProduct = (PhysicalProduct) product;
                colWidths[0] = Math.max(colWidths[0], String.valueOf(physicalProduct.getProductId()).length());
                colWidths[1] = Math.max(colWidths[1], physicalProduct.getName().length());
                colWidths[2] = Math.max(colWidths[2], physicalProduct.getCategory().name().length());
                colWidths[3] = Math.max(colWidths[3], String.valueOf(physicalProduct.getBasePrice()).length());
                colWidths[4] = Math.max(colWidths[4], String.valueOf(physicalProduct.getTaxRate()).length());
                colWidths[5] = Math.max(colWidths[5], String.valueOf(physicalProduct.getSeasonalDiscount()).length());
                colWidths[6] = Math.max(colWidths[6], physicalProduct.getProductType().length());
                colWidths[7] = Math.max(colWidths[7], String.valueOf(physicalProduct.priceAfterTax()).length());
                colWidths[8] = Math.max(colWidths[8], String.valueOf(physicalProduct.priceAfterDiscount()).length());
                colWidths[9] = Math.max(colWidths[9],
                        String.valueOf(physicalProduct.priceAfterTaxAndDiscount()).length());
                colWidths[10] = Math.max(colWidths[10], String.valueOf(physicalProduct.getStockQuantity()).length());
                colWidths[11] = Math.max(colWidths[11], String.valueOf(physicalProduct.getReorderLevel()).length());
                colWidths[12] = Math.max(colWidths[12], physicalProduct.getSupplierName().length());
                colWidths[13] = Math.max(colWidths[13], String.valueOf(physicalProduct.getSupplierContact()).length());
            } else {
                DigitalService digitalService = (DigitalService) product;
                colWidths[0] = Math.max(colWidths[0], String.valueOf(digitalService.getProductId()).length());
                colWidths[1] = Math.max(colWidths[1], digitalService.getName().length());
                colWidths[2] = Math.max(colWidths[2], digitalService.getCategory().name().length());
                colWidths[3] = Math.max(colWidths[3], String.valueOf(digitalService.getBasePrice()).length());
                colWidths[4] = Math.max(colWidths[4], String.valueOf(digitalService.getTaxRate()).length());
                colWidths[5] = Math.max(colWidths[5], String.valueOf(digitalService.getSeasonalDiscount()).length());
                colWidths[6] = Math.max(colWidths[6], digitalService.getProductType().length());
                colWidths[7] = Math.max(colWidths[7], String.valueOf(digitalService.priceAfterTax()).length());
                colWidths[8] = Math.max(colWidths[8], String.valueOf(digitalService.priceAfterDiscount()).length());
                colWidths[9] = Math.max(colWidths[9],
                        String.valueOf(digitalService.priceAfterTaxAndDiscount()).length());
            }
        }

        // Build format string
        String format = String.format(
                "%%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds  %%-%ds%n",
                colWidths[0], colWidths[1], colWidths[2], colWidths[3], colWidths[4], colWidths[5], colWidths[6],
                colWidths[7], colWidths[8], colWidths[9], colWidths[10]);

        // Print header
        System.out.printf(format, (Object[]) headers);
        System.out.println();

        // Print rows
        for (Product product : productsList) {
            if (product instanceof PhysicalProduct) {
                PhysicalProduct physicalProduct = (PhysicalProduct) product;
                System.out.printf(format,
                        String.valueOf(physicalProduct.getProductId()),
                        physicalProduct.getName(),
                        physicalProduct.getCategory(),
                        String.valueOf(physicalProduct.getBasePrice()),
                        String.valueOf(physicalProduct.getTaxRate()),
                        String.valueOf(physicalProduct.getSeasonalDiscount()),
                        physicalProduct.getProductType(),
                        String.valueOf(physicalProduct.priceAfterTax()),
                        String.valueOf(physicalProduct.priceAfterDiscount()),
                        String.valueOf(physicalProduct.priceAfterTaxAndDiscount()),
                        String.valueOf(physicalProduct.getStockQuantity()),
                        String.valueOf(physicalProduct.getReorderLevel()),
                        physicalProduct.getSupplierName(),
                        String.valueOf(physicalProduct.getSupplierContact()));
            } else {
                DigitalService digitalService = (DigitalService) product;
                System.out.printf(format,
                        String.valueOf(digitalService.getProductId()),
                        digitalService.getName(),
                        digitalService.getCategory(),
                        String.valueOf(digitalService.getBasePrice()),
                        String.valueOf(digitalService.getTaxRate()),
                        String.valueOf(digitalService.getSeasonalDiscount()),
                        digitalService.getProductType(),
                        String.valueOf(digitalService.priceAfterTax()),
                        String.valueOf(digitalService.priceAfterDiscount()),
                        String.valueOf(digitalService.priceAfterTaxAndDiscount()),
                        "-", "-", "-", "-");
            }
        }
        System.out.println("\n===================================== END PRODUCT LIST =============================\n");
    }

    public void searchProductByID() {
        System.out.println("========================== SEARCH PRODUCT =============================");
        System.out.print("Enter Product ID: ");
        int productId = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
        List<Product> product = this.products.stream().filter(p -> p.getProductId() == productId)
                .collect(Collectors.toList());
        if (product.size() == 0) {
            System.out.println("Product not found");
            System.out.println("=====================================");
            return;
        }
        showProductList(product);
    }

    public void searchProductByName() {
        System.out.println("========================== SEARCH PRODUCT =============================");
        System.out.print("Enter Product Name: ");
        String name = this.scanner.nextLine().split("\\s+")[0];
        List<Product> product = this.products.stream().filter(p -> p.getName().equals(name))
                .collect(Collectors.toList());
        if (product.size() == 0) {
            System.out.println("Product not found");
            System.out.println("=====================================");
            return;
        }
        showProductList(product);
    }

    public void searchProductByCategory() {
        System.out.println("========================== SEARCH PRODUCT =============================");
        System.out.print("Enter Product Category: ");
        String category = this.scanner.nextLine().split("\\s+")[0];
        List<Product> product = this.products.stream()
                .filter(p -> p.getCategory().equals(ProductCategory.valueOf(category)))
                .collect(Collectors.toList());
        if (product.size() == 0) {
            System.out.println("Product not found");
            System.out.println("=====================================");
            return;
        }
        showProductList(product);
    }

    public void updateProduct() {
        System.out.println("========================== UPDATE PRODUCT =============================");

        System.out.print("Enter Product ID (mandatory): ");
        int productId = Integer.parseInt(this.scanner.nextLine().split("\\s+")[0]);
        int productIndex = IntStream.range(0, this.products.size())
                .filter(i -> this.products.get(i).getProductId() == productId)
                .findFirst().orElse(-1);
        if (productIndex == -1) {
            System.out.println("Product not found");
            System.out.println("=====================================");
            return;
        }
        Product product = this.products.get(productIndex);

        System.out.print("Enter the new name (current name: " + product.getName()
                + ") (press enter to skip - min 15 characters): ");
        String name = this.scanner.nextLine().split("\\s+")[0];
        if (!name.isEmpty() && name.length() < 15) {
            System.out.println("Product name is not valid");
            return;
        }
        if (!name.isEmpty()) {
            product.setName(name);
        }

        while (true) {
            System.out.print("Enter new Product Category (Electronics, Clothing, Books, Food) (current category: "
                    + product.getCategory() + ") (press enter to skip): ");
            String cat = this.scanner.nextLine().split("\\s+")[0];
            if (!cat.isEmpty()) {
                if (!cat.equals(ProductCategory.Electronics.name()) && !cat.equals(ProductCategory.Clothing.name())
                        && !cat.equals(ProductCategory.Books.name()) && !cat.equals(ProductCategory.Food.name())) {
                    System.out.println("Invalid category. Please try again.");
                    continue;
                } else {
                    product.setCategory(ProductCategory.valueOf(cat));
                }
            }

            break;
        }

        System.out.print(
                "Enter the new base price (current price: " + product.getBasePrice() + ") (press enter to skip): ");
        String basePrice = this.scanner.nextLine().split("\\s+")[0];
        if (!basePrice.isEmpty() && Long.parseLong(basePrice) <= 0) {
            System.out.println("Base price is invalid. Please try again.");
            return;
        }
        if (!basePrice.isEmpty()) {
            product.setBasePrice(Long.parseLong(basePrice));
        }

        System.out.print("Enter the new tax rate (current rate: " + product.getTaxRate() + ") (press enter to skip): ");
        String taxRate = this.scanner.nextLine().split("\\s+")[0];
        if (!taxRate.isEmpty() && Integer.parseInt(taxRate) < 0) {
            System.out.println("Tax rate is invalid. Please try again.");
            return;
        }
        if (!taxRate.isEmpty()) {
            product.setTaxRate(Integer.parseInt(taxRate));
        }

        System.out.print("Enter the new seasonal discount (current discount: " + product.getSeasonalDiscount()
                + ") (press enter to skip): ");
        String seasonalDiscount = this.scanner.nextLine().split("\\s+")[0];
        if (!seasonalDiscount.isEmpty() && Integer.parseInt(seasonalDiscount) < 0) {
            System.out.println("Seasonal discount is invalid. Please try again.");
            return;
        }
        if (!seasonalDiscount.isEmpty()) {
            product.setSeasonalDiscount(Integer.parseInt(seasonalDiscount));
        }

        if (product instanceof PhysicalProduct) {
            PhysicalProduct physicalProduct = (PhysicalProduct) product;
            System.out.print("Enter the new stock quantity (current quantity: " + physicalProduct.getStockQuantity()
                    + ") (press enter to skip): ");
            String stockQuantity = this.scanner.nextLine().split("\\s+")[0];
            if (!stockQuantity.isEmpty() && Integer.parseInt(stockQuantity) < 0) {
                System.out.println("Stock quantity is invalid. Please try again.");
                return;
            }
            if (!stockQuantity.isEmpty()) {
                physicalProduct.setStockQuantity(Integer.parseInt(stockQuantity));
            }

            System.out.print("Enter the new reorder level (current level: " + physicalProduct.getReorderLevel()
                    + ") (press enter to skip): ");
            String reorderLevel = this.scanner.nextLine().split("\\s+")[0];
            if (!reorderLevel.isEmpty() && Integer.parseInt(reorderLevel) < 0) {
                System.out.println("Reorder level is invalid. Please try again.");
                return;
            }
            if (!reorderLevel.isEmpty()) {
                physicalProduct.setReorderLevel(Integer.parseInt(reorderLevel));
            }

            System.out.print("Enter the new supplier name (current name: " + physicalProduct.getSupplierName()
                    + ") (press enter to skip): ");
            String supplierName = this.scanner.nextLine().split("\\s+")[0];
            if (!supplierName.isEmpty() && supplierName.length() > 15) {
                System.out.println("Supplier name is invalid. Please try again.");
                return;
            }
            if (!supplierName.isEmpty()) {
                physicalProduct.setSupplierName(supplierName);
            }

            System.out.print("Enter the new supplier contact (current contact: " + physicalProduct.getSupplierContact()
                    + ") (press enter to skip): ");
            String supplierContact = this.scanner.nextLine().split("\\s+")[0];
            if (!supplierContact.isEmpty() && String.valueOf(supplierContact).length() != 10) {
                System.out.println("Supplier contact must be 10 digits");
                return;
            }
            if (!supplierContact.isEmpty()) {
                physicalProduct.setSupplierContact(Long.parseLong(supplierContact));
            }
        }
        this.products.set(productIndex, product);
        utils.saveData("./db/products.txt", this.products);
        System.out.println("Product updated successfully");
    }

    public Product getProductById(int productId) {
        return this.products.stream().filter(p -> p.getProductId() == productId).findFirst().orElse(null);
    }
}
