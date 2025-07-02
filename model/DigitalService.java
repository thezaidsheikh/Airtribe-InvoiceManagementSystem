package model;

import common.ProductCategory;

public class DigitalService extends Product {
    public DigitalService(int productId, String name, ProductCategory category, long price, int taxRate,
            int seasonalDiscount) {
        super(productId, name, category, price, taxRate, seasonalDiscount, "DigitalService");
    }

    public boolean isLowStock() {
        return false;
    }
}
