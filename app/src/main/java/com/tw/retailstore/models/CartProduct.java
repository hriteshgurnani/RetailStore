package com.tw.retailstore.models;


/**
 * Cart Product Business Object holding a Product and its corresponding Shopped Quantity/ Total Price.
 * Used in {@link com.tw.retailstore.businesslogic.CartSession} to hold list of Products in the CartSession.
 * Created by hritesh on 29/06/16.
 */

public class CartProduct {

    /**
     * Product added to the Cart.
     */
    private Product product;

    /**
     * Quantity of the Product added.
     */
    private int cartQuantity = 0;

    /**
     * Total MRP of the quantity added.
     */
    private double totalMrpPrice = 0L;

    private CartProduct(Builder builder) {
        this.product = builder.product;
        this.cartQuantity = builder.cartQuantity;
        this.totalMrpPrice = builder.totalMrpPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartProduct that = (CartProduct) o;

        return product != null ? product.equals(that.product) : that.product == null;

    }

    @Override
    public String toString() {
        return "CartProduct{" +
                "product=" + product +
                ", cartQuantity=" + cartQuantity +
                ", totalMrpPrice=" + totalMrpPrice +
                '}';
    }

    @Override
    public int hashCode() {
        return product != null ? product.hashCode() : 0;
    }

    public int getCartQuantity() {
        return cartQuantity;
    }

    public String getProductName() {
        return product.getProductName();
    }

    public double getMrp() {
        return product.getMrp();
    }

    public String getProductCategoryName() {
        return product.getProductCategory();
    }

    public int getProductId() {
        return product.getProductId();
    }

    public String getProductImage() {
        return product.getProductImageName();
    }

    void setProduct(Product product) {
        this.product = product;
    }

    public void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public void setTotalMrpPrice(double totalMrpPrice) {
        this.totalMrpPrice = totalMrpPrice;
    }

    public double getTotalMrpPrice() {
        return totalMrpPrice;
    }

    public static class Builder {

        private Product product;

        private int cartQuantity = 0;

        private double totalMrpPrice;

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder setCartQuantity(int cartQuantity) {
            this.cartQuantity = cartQuantity;
            return this;
        }

        public Builder setTotalMrpPrice(double totalMrpPrice) {
            this.totalMrpPrice = totalMrpPrice;
            return this;
        }

        public CartProduct build() {
            return new CartProduct(this);
        }
    }
}
