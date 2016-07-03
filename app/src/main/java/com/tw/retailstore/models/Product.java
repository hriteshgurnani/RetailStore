package com.tw.retailstore.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.tw.retailstore.storage.CartProductDao;

/**
 * Product Model representing Product of a Retail Store.
 * Created by hritesh on 29/06/16.
 */

public class Product implements Parcelable {

    private final int productId;

    private final String productName;

    private final String productImageName;

    private final String productCategory;

    private final double mrp;

    private Product(Builder builder) {
        this.productId = builder.productId;
        this.productName = builder.productName;
        this.productImageName = builder.productImageName;
        this.productCategory = builder.productCategory;
        this.mrp = builder.mrp;
    }

    protected Product(Parcel in) {
        productId = in.readInt();
        productName = in.readString();
        productImageName = in.readString();
        productCategory = in.readString();
        mrp = in.readDouble();
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productImageName='" + productImageName + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", mrp=" + mrp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return productId == product.productId;

    }

    @Override
    public int hashCode() {
        return productId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageName() {
        return productImageName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public double getMrp() {
        return mrp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeString(productName);
        dest.writeString(productImageName);
        dest.writeString(productCategory);
        dest.writeDouble(mrp);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public static class Builder {
        private int productId;
        private String productName;
        private String productImageName;
        private String productCategory;
        private double mrp;

        public Builder setProductId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder setProductImageName(String productImageName) {
            this.productImageName = productImageName;
            return this;
        }

        public Builder setProductCategory(String productCategory) {
            this.productCategory = productCategory;
            return this;
        }

        public Builder setMrp(double mrp) {
            this.mrp = mrp;
            return this;
        }

        public Product build() {
            return new Product(this);
        }

        public Builder initFromCartProductDao(CartProductDao cartProductDao) {
            this.productId = cartProductDao.getProductId();
            this.productName = cartProductDao.getProductName();
            this.productImageName = cartProductDao.getProductImageUrl();
            this.productCategory = cartProductDao.getProductCategory();
            this.mrp = cartProductDao.getMrp();
            return this;
        }
    }
}
