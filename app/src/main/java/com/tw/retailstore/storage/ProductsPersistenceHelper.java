package com.tw.retailstore.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.tw.retailstore.businesslogic.IProductsBusiness;
import com.tw.retailstore.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper to perform Get and Insert operations for Products of a Store on a Database.
 * Created by hritesh on 29/06/16.
 */

public class ProductsPersistenceHelper {

    private final Context applicationContext;

    private ProductsPersistenceHelper(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    static ProductsPersistenceHelper newInstance(Context applicationContext) {
        return new ProductsPersistenceHelper(applicationContext);
    }

    /**
     * Gets Products of a Store from local Database.
     *
     * @return List of Products of that Store.
     */
    public List<Product> getProductsFromPersistence() {
        List<Product> cartProductList = new ArrayList<>();
        Cursor cursor = applicationContext.getContentResolver().query(ProductAndCartDatabaseContract.CONTENT_URI_PRODUCTS, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            throw new IllegalStateException("No products in database.");
        }

        cursor.moveToFirst();
        Product.Builder builder = new Product.Builder();
        while (!cursor.isAfterLast()) {
            builder.setMrp(cursor.getDouble(cursor.getColumnIndex(ProductAndCartDatabaseContract.ProductColumns.PRODUCT_MRP)));
            builder.setProductCategory(cursor.getString(cursor.getColumnIndex(ProductAndCartDatabaseContract.ProductColumns.PRODUCT_CATEGORY_NAME)));
            builder.setProductImageName(cursor.getString(cursor.getColumnIndex(ProductAndCartDatabaseContract.ProductColumns.PRODUCT_IMAGE)));
            builder.setProductId(cursor.getInt(cursor.getColumnIndex(ProductAndCartDatabaseContract.ProductColumns.PRODUCT_ID)));
            builder.setProductName(cursor.getString(cursor.getColumnIndex(ProductAndCartDatabaseContract.ProductColumns.PRODUCT_NAME)));
            builder.setMrp(cursor.getDouble(cursor.getColumnIndex(ProductAndCartDatabaseContract.ProductColumns.PRODUCT_MRP)));
            cartProductList.add(builder.build());
            cursor.moveToNext();
        }

        cursor.close();
        return cartProductList;
    }

    /**
     * Insert Dummy Products into local Database.
     *
     * @param callback Callback to notify Operation completion.
     */
    public void insertDummyProducts(IProductsBusiness.IProductInsertCallback callback) {
        new DummyProductsInsertTask(applicationContext.getContentResolver(), callback).execute();
    }

    static ContentValues[] getProductContentValues(List<Product> productList) {
        ContentValues[] contentValues = new ContentValues[productList.size()];
        int i = 0;
        for (Product product : productList) {
            ContentValues values = new ContentValues();
            values.put(ProductAndCartDatabaseContract.ProductColumns.PRODUCT_CATEGORY_NAME, product.getProductCategory());
            values.put(ProductAndCartDatabaseContract.ProductColumns.PRODUCT_ID, product.getProductId());
            values.put(ProductAndCartDatabaseContract.ProductColumns.PRODUCT_IMAGE, product.getProductImageName());
            values.put(ProductAndCartDatabaseContract.ProductColumns.PRODUCT_MRP, product.getMrp());
            values.put(ProductAndCartDatabaseContract.ProductColumns.PRODUCT_NAME, product.getProductName());
            contentValues[i] = values;
            i++;
        }
        return contentValues;
    }
}
