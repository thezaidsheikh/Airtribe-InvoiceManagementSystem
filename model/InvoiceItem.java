package model;

public class InvoiceItem extends Product {
    private int productId;
    private int quantity;

    public InvoiceItem(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
