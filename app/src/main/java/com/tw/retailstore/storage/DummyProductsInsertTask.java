package com.tw.retailstore.storage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.tw.retailstore.businesslogic.IProductsBusiness;
import com.tw.retailstore.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Async Task to Insert hardcoded set of Products into Local Database.
 * Created by hritesh on 29/06/16.
 */
class DummyProductsInsertTask extends AsyncTask<Void, Void, Void> {

    private static final ContentValues[] productContentValues;

    private static final String CATEGORY_ELECTRONICS = "Electronics";

    private static final String CATEGORY_FURNITURE = "Furniture";

    private static final String PRODUCT_OVEN = "Microwave Oven";

    private static final String PRODUCT_TELEVISION = "Television";

    private static final String PRODUCT_VACUUM_CLEANER = "Vacuum Cleaner";

    private static final String PRODUCT_TABLE = "Table";

    private static final String PRODUCT_ALMIRAH = "Almirah";

    private static final String PRODUCT_CHAIR = "Chair";

    private static final String LOG_TAG = DummyProductsInsertTask.class.getSimpleName();

    static {
        List<Product> productList = new ArrayList<>();
        Product.Builder builder = new Product.Builder();
        builder.setProductId(1);
        builder.setProductCategory(CATEGORY_ELECTRONICS);
        builder.setProductImageName("oven.png");
        builder.setProductName(PRODUCT_OVEN);
        builder.setMrp(5000);
        productList.add(builder.build());

        builder.setProductId(2);
        builder.setProductCategory(CATEGORY_ELECTRONICS);
        builder.setProductImageName("television.png");
        builder.setProductName(PRODUCT_TELEVISION);
        builder.setMrp(20000);
        productList.add(builder.build());

        builder.setProductId(3);
        builder.setProductCategory(CATEGORY_ELECTRONICS);
        builder.setProductImageName("vcleaner.png");
        builder.setProductName(PRODUCT_VACUUM_CLEANER);
        builder.setMrp(2500);
        productList.add(builder.build());

        builder.setProductId(4);
        builder.setProductCategory(CATEGORY_FURNITURE);
        builder.setProductImageName("table.png");
        builder.setProductName(PRODUCT_TABLE);
        builder.setMrp(1000);
        productList.add(builder.build());

        builder.setProductId(5);
        builder.setProductCategory(CATEGORY_FURNITURE);
        builder.setProductImageName("almirah.png");
        builder.setProductName(PRODUCT_ALMIRAH);
        builder.setMrp(10000);
        productList.add(builder.build());


        builder.setProductId(6);
        builder.setProductCategory(CATEGORY_FURNITURE);
        builder.setProductImageName("chair.png");
        builder.setProductName(PRODUCT_CHAIR);
        builder.setMrp(500);
        productList.add(builder.build());

        productContentValues = ProductsPersistenceHelper.getProductContentValues(productList);
    }

    private final ContentResolver contentResolver;

    private final IProductsBusiness.IProductInsertCallback callback;

    DummyProductsInsertTask(ContentResolver contentResolver, IProductsBusiness.IProductInsertCallback callback) {
        this.contentResolver = contentResolver;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        int rows = contentResolver.bulkInsert(ProductAndCartDatabaseContract.CONTENT_URI_PRODUCTS, productContentValues);
        Log.d(LOG_TAG, "doInBackground() " + rows);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.onCompletion();
    }

}
