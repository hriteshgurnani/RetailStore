package com.tw.retailstore.storage;

/**
 * Data Access Object Model for a Product in a Cart in SQLiteDatabase.
 * Created by hritesh on 29/06/16.
 */

public class CartProductDao {

    private int productId;

    private String productName;

    private String productImageUrl;

    private String productCategory;

    private double mrp;

    private int cartQuantity;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductCategory() {
        return productCategory;
    }

    void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public double getMrp() {
        return mrp;
    }

    void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public int getCartQuantity() {
        return cartQuantity;
    }

    void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }
}
