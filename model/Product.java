package model;

import common.ProductCategory;

public class Product {
    protected int productId;
    protected String name;
    protected long basePrice;
    protected int seasonalDiscount = 0;
    protected int taxRate = 18;
    protected ProductCategory category;
    protected String productType;

    public Product() {
    }

    public Product(int productId, String name, ProductCategory category, long basePrice, int taxRate,
            int seasonalDiscount,
            String productType) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.basePrice = basePrice;
        this.taxRate = taxRate;
        this.seasonalDiscount = seasonalDiscount;
        this.productType = productType;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductType() {
        return this.productType;
    }

    public final long getTaxAmount() {
        return this.getBasePrice() * this.getTaxRate() / 100;
    };

    public final long getSeasonalDiscountAmount() {
        return this.getBasePrice() * this.getSeasonalDiscount() / 100;
    };

    public final long priceAfterTax() {
        return this.getBasePrice() + this.getTaxAmount();
    }

    public final long priceAfterDiscount() {
        return this.getBasePrice() - this.getSeasonalDiscountAmount();
    }

    public final long priceAfterTaxAndDiscount() {
        return (this.getBasePrice() + this.getTaxAmount()) - this.getSeasonalDiscountAmount();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public long getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(long basePrice) {
        this.basePrice = basePrice;
    }

    public int getSeasonalDiscount() {
        return seasonalDiscount;
    }

    public void setSeasonalDiscount(int seasonalDiscount) {
        this.seasonalDiscount = seasonalDiscount;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public String toString() {
        return this.productId + " " + this.name + " " + this.category + " " + this.basePrice + " " + this.taxRate
                + " " + this.seasonalDiscount + " " + this.productType + " " + this.priceAfterTax() + " "
                + this.priceAfterDiscount() + " " + this.priceAfterTaxAndDiscount();
    }

}
