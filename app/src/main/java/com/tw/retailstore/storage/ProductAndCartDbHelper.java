
package com.tw.retailstore.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class handling Creation and Deletion of Tables.
 */
class ProductAndCartDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "retail_store.db";

    private static final int DB_VERSION = 1;

    private static ProductAndCartDbHelper instance;

    private static final String CREATE_TABLE_CART = "CREATE TABLE `" + ProductAndCartDatabaseContract.TABLE_CART + "` " +
            "( " +
            " `" + ProductAndCartDatabaseContract.CartColumns.CART_ID + "` TEXT NOT NULL, " +
            "   UNIQUE (" + ProductAndCartDatabaseContract.CartColumns.CART_ID + ")" +
            ");";

    private static final String DROP_TABLE_CART = "DROP TABLE IF EXISTS " + ProductAndCartDatabaseContract.TABLE_CART + ";";

    private static final String CREATE_TABLE_CART_PRODUCTS = "CREATE TABLE `" + ProductAndCartDatabaseContract.TABLE_CART_PRODUCTS + "` " +
            "( " +
            " `" + ProductAndCartDatabaseContract.CartProductColumns._ID + "` INTEGER PRIMARY KEY AUTOINCREMENT," +
            " `" + ProductAndCartDatabaseContract.CartProductColumns.CART_ID + "` TEXT NOT NULL," +
            " `" + ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_CATEGORY_NAME + "` TEXT NOT NULL," +
            " `" + ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_ID + "` INTEGER NOT NULL," +
            " `" + ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_IMAGE + "` TEXT," +
            " `" + ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_MRP + "` REAL NOT NULL," +
            " `" + ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_NAME + "` TEXT NOT NULL," +
            " `" + ProductAndCartDatabaseContract.CartProductColumns.CART_PRODUCT_ORDER_QUANTITY + "` INTEGER NOT NULL" +
            ");";

    private static final String DROP_TABLE_CART_PRODUCTS = "DROP TABLE IF EXISTS " + ProductAndCartDatabaseContract.TABLE_CART_PRODUCTS + ";";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE `" + ProductAndCartDatabaseContract.TABLE_PRODUCTS + "` " +
            "( " +
            " `" + ProductAndCartDatabaseContract.ProductColumns._ID + "` INTEGER PRIMARY KEY AUTOINCREMENT," +
            " `" + ProductAndCartDatabaseContract.ProductColumns.PRODUCT_ID + "` INTEGER NOT NULL," +
            " `" + ProductAndCartDatabaseContract.ProductColumns.PRODUCT_CATEGORY_NAME + "` TEXT NOT NULL," +
            " `" + ProductAndCartDatabaseContract.ProductColumns.PRODUCT_IMAGE + "` TEXT," +
            " `" + ProductAndCartDatabaseContract.ProductColumns.PRODUCT_MRP + "` REAL NOT NULL," +
            " `" + ProductAndCartDatabaseContract.ProductColumns.PRODUCT_NAME + "` TEXT NOT NULL," +
            "   UNIQUE (" + ProductAndCartDatabaseContract.ProductColumns.PRODUCT_ID + ")" +
            ");";

    private static final String DROP_TABLE_PRODUCTS = "DROP TABLE IF EXISTS " + ProductAndCartDatabaseContract.TABLE_PRODUCTS + ";";

    static synchronized ProductAndCartDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ProductAndCartDbHelper(context, DB_NAME, null, DB_VERSION);
        }
        return instance;
    }

    private ProductAndCartDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_CART_PRODUCTS);
        db.execSQL(CREATE_TABLE_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CART);
        db.execSQL(DROP_TABLE_CART_PRODUCTS);
        db.execSQL(DROP_TABLE_PRODUCTS);
        onCreate(db);
    }
}
