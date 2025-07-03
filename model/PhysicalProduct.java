package model;

import common.ProductCategory;

public class PhysicalProduct extends Product {
    private String supplierName;
    private long supplierContact;
    private int stockQuantity;
    private int reorderLevel;

    public PhysicalProduct(int productId, String name, ProductCategory category, long price, int taxRate,
            int seasonalDiscount, int stockQuantity, int reorderLevel, String supplierName, long supplierContact) {
        super(productId, name, category, price, taxRate, seasonalDiscount, "PhysicalProduct");
        this.supplierName = supplierName;
        this.supplierContact = supplierContact;
        this.stockQuantity = stockQuantity;
        this.reorderLevel = reorderLevel;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public long getSupplierContact() {
        return this.supplierContact;
    }

    public void setSupplierContact(long supplierContact) {
        this.supplierContact = supplierContact;
    }

    // Inventory management
    public boolean isLowStock() {
        return stockQuantity <= reorderLevel;
    }

    @Override
    public String toString() {
        return this.productId + " " + this.name + " " + this.category + " " + this.basePrice + " " + this.taxRate
                + " " + this.seasonalDiscount + " " + this.productType + " " + this.priceAfterTax() + " "
                + this.priceAfterDiscount() + " " + this.priceAfterTaxAndDiscount() + " " + this.stockQuantity + " "
                + this.reorderLevel + " " + this.supplierName + " " + this.supplierContact;
    }
}
