package model;

public class InvoiceItem {
    private int productId;
    private int quantity;
    private Product productDetail;

    public InvoiceItem(int productId, int quantity, Product product) {
        this.productDetail = product;
        this.productId = productId;
        this.quantity = quantity;
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
}
