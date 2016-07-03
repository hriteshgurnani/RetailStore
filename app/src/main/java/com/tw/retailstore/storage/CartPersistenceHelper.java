package com.tw.retailstore.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.tw.retailstore.models.CartKey;
import com.tw.retailstore.models.CartProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper to perform CRUD operations for a Cart and its Product to Persistence.(Database)
 * Created by hritesh on 29/06/16.
 */

public class CartPersistenceHelper {

    private final Context applicationContext;

    private CartPersistenceHelper(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    static CartPersistenceHelper newInstance(Context applicationContext) {
        return new CartPersistenceHelper(applicationContext);
    }

    /**
     * Insert Products for a particular Cart into Persistence
     *
     * @param cartKey         Key that identifies a Cart.
     * @param cartProductList List of Products in the Cart.
     */
    public void insertProducts(CartKey cartKey, List<CartProduct> cartProductList) {
        List<ContentValues> contentValuesList = new ArrayList<>();
        for (CartProduct cartProduct : cartProductList) {
            contentValuesList.add(getProductContentValues(cartKey, cartProduct));
        }
        applicationContext.getContentResolver().bulkInsert(ProductAndCartDatabaseContract.CONTENT_URI_CART_PRODUCTS, contentValuesList.toArray(new ContentValues[1]));
    }

    /**
     * Get Products in a particular Cart.
     *
     * @param cartId Key that identifies a Cart.
     * @return List of products in the Cart.
     */
    public List<CartProductDao> getCartProductsForCart(String cartId) {
        List<CartProductDao> cartProductList = new ArrayList<>();
        ContentResolver contentResolver = applicationContext.getContentResolver();
        Cursor cursor = contentResolver.query(ProductAndCartDatabaseContract.CONTENT_URI_CART_PRODUCTS, null, ProductAndCartDatabaseContract.CartProductColumns.CART_ID + " = ?", new String[]{cartId}, null);
        if (cursor == null) {
            return cartProductList;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CartProductDao cartProductDao = new CartProductDao();
                cartProductDao.setMrp(cursor.getDouble(cursor.getColumnIndex(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_MRP)));
                cartProductDao.setProductCategory(cursor.getString(cursor.getColumnIndex(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_CATEGORY_NAME)));
                cartProductDao.setProductImageUrl(cursor.getString(cursor.getColumnIndex(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_IMAGE)));
                cartProductDao.setProductId(cursor.getInt(cursor.getColumnIndex(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_ID)));
                cartProductDao.setProductName(cursor.getString(cursor.getColumnIndex(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_NAME)));
                cartProductDao.setCartQuantity(cursor.getInt(cursor.getColumnIndex(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_ORDER_QUANTITY)));
                cartProductList.add(cartProductDao);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return cartProductList;
    }

    /**
     * Update Product in a particular Cart.
     *
     * @param cartKey     Key that identifies a Cart.
     * @param cartProduct Product to update in the Cart.
     */
    public void updateProduct(CartKey cartKey, CartProduct cartProduct) {
        ContentValues values = new ContentValues();
        values.put(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_ORDER_QUANTITY, cartProduct.getCartQuantity());
        applicationContext.getContentResolver().update(ProductAndCartDatabaseContract.CONTENT_URI_CART_PRODUCTS, values, ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_ID + "= ? AND "
                        + ProductAndCartDatabaseContract.CartProductColumns.CART_ID + "=?",
                new String[]{Integer.toString(cartProduct.getProductId()), cartKey.getCartId()});
    }

    /**
     * Insert cartId into Cart table.
     *
     * @param cartId Key that identifies a Cart.
     */
    public void insertCart(String cartId) {
        ContentValues values = new ContentValues();
        values.put(ProductAndCartDatabaseContract.CartColumns.CART_ID, cartId);
        applicationContext.getContentResolver().insert(ProductAndCartDatabaseContract.CONTENT_URI_CART, values);
    }

    /**
     * Get all Carts from Cart table.
     *
     * @return List of all Carts in the database.
     */
    public List<CartDao> getAllCartsFromPersistence() {
        List<CartDao> cartDaoList = new ArrayList<>();
        ContentResolver contentResolver = applicationContext.getContentResolver();
        Cursor cursor = contentResolver.query(ProductAndCartDatabaseContract.CONTENT_URI_CART, null, null, null, null);
        if (cursor == null) {
            return cartDaoList;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String cartId = cursor.getString(cursor.getColumnIndex(ProductAndCartDatabaseContract.CartColumns.CART_ID));
                cartDaoList.add(new CartDao(cartId));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return cartDaoList;
    }

    /**
     * Delete Cart from Database.
     *
     * @param cartId Cart ID of Cart to delete.
     */
    public void deleteCart(String cartId) {
        applicationContext.getContentResolver().delete(ProductAndCartDatabaseContract.CONTENT_URI_CART, ProductAndCartDatabaseContract.CartColumns.CART_ID + " = ?", new String[]{cartId});
        applicationContext.getContentResolver().delete(ProductAndCartDatabaseContract.CONTENT_URI_CART_PRODUCTS, ProductAndCartDatabaseContract.CartProductColumns.CART_ID + " = ?", new String[]{cartId});
    }

    /**
     * Delete product from a particular Cart.
     *
     * @param cartKey   Key that identifies a Cart.
     * @param productId Product ID of the product to delete.
     */
    public void deleteProduct(CartKey cartKey, int productId) {
        applicationContext.getContentResolver().delete(ProductAndCartDatabaseContract.CONTENT_URI_CART_PRODUCTS,
                ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_ID + "=? AND " + ProductAndCartDatabaseContract.CartProductColumns.CART_ID + "=?",
                new String[]{Integer.toString(productId), cartKey.getCartId()});
    }

    private ContentValues getProductContentValues(CartKey cartKey, CartProduct cartProduct) {
        ContentValues values = new ContentValues();
        values.put(ProductAndCartDatabaseContract.CartProductColumns.CART_ID, cartKey.getCartId());
        values.put(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_CATEGORY_NAME, cartProduct.getProductCategoryName());
        values.put(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_ID, cartProduct.getProductId());
        values.put(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_IMAGE, cartProduct.getProductImage());
        values.put(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_MRP, cartProduct.getMrp());
        values.put(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_NAME, cartProduct.getProductName());
        values.put(ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_ORDER_QUANTITY, cartProduct.getCartQuantity());
        return values;
    }
}
