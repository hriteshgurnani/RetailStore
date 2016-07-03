package com.tw.retailstore.storage;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Helper class defining contract for Cart, CartProducts and Products storage in Database.
 */
class ProductAndCartDatabaseContract {

    private static final String SCHEME = "content://";
    private static final String PATH_SEPARATOR = "/";

    static final String AUTHORITY = "com.tw.retailstore.storage";
    static final String TABLE_CART = "Cart";
    static final String TABLE_CART_PRODUCTS = "CartProducts";
    static final String TABLE_PRODUCTS = "Products";

    static final Uri CONTENT_URI_CART = Uri.parse(SCHEME + AUTHORITY + PATH_SEPARATOR + TABLE_CART);
    static final Uri CONTENT_URI_CART_PRODUCTS = Uri.parse(SCHEME + AUTHORITY + PATH_SEPARATOR + TABLE_CART_PRODUCTS);
    static final Uri CONTENT_URI_PRODUCTS = Uri.parse(SCHEME + AUTHORITY + PATH_SEPARATOR + TABLE_PRODUCTS);

    interface CartColumns extends BaseColumns {
        String CART_ID = "CART_ID";
    }

    interface CartProductColumns extends BaseColumns {
        String CART_ID = "CART_ID";
        String CART_PRODUCT_ORDER_QUANTITY = "CART_PRODUCT_ORDER_QUANTITY";
        String CART_PRODUCT_MRP = "CART_PRODUCT_MRP";
        String CART_PRODUCT_ID = "CART_PRODUCT_ID";
        String CART_PRODUCT_IMAGE = "CART_PRODUCT_IMAGE";
        String CART_PRODUCT_CATEGORY_NAME = "CART_PRODUCT_CATEGORY_NAME";
        String CART_PRODUCT_NAME = "CART_PRODUCT_NAME";
    }

    interface ProductColumns extends BaseColumns {
        String PRODUCT_ID = "PRODUCT_ID";
        String PRODUCT_MRP = "PRODUCT_MRP";
        String PRODUCT_IMAGE = "PRODUCT_IMAGE";
        String PRODUCT_CATEGORY_NAME = "PRODUCT_CATEGORY_NAME";
        String PRODUCT_NAME = "PRODUCT_NAME";
    }
}
