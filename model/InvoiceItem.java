package model;

public class InvoiceItem {
    private int productId;
    private int quantity;
    private long price;
    private Product productDetail;

    public InvoiceItem(int productId, int quantity, long price, Product product) {
        this.productDetail = product;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProductDetail() {
        return productDetail;
    }

    public long getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return this.getProductId() + " " + this.getQuantity() + " " + this.getPrice();
    }
}
