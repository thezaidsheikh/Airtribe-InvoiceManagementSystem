package model;

import common.ProductCategory;
import common.utils;

public class Product {
    protected int productId;
    protected String name;
    protected long basePrice;
    protected int seasonalDiscount = 0;
    protected int taxRate = 18;
    protected ProductCategory category;
    protected String productType;

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

    private final long tax() {
        return this.getBasePrice() * this.getTaxRate() / 100;
    };

    private final long discount() {
        return this.getBasePrice() * this.getSeasonalDiscount() / 100;
    };

    public long getRegularPrice() {
        long price = (long) (this.getBasePrice() + this.tax()) - this.discount();
        return price;
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
                + " " + this.seasonalDiscount + " " + this.productType;
    }

}
