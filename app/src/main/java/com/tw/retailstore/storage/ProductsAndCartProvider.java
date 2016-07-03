package com.tw.retailstore.storage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Content provider for accessing User Carts and Store Products information.
 * Created by hritesh on 29/06/16.
 */

public class ProductsAndCartProvider extends ContentProvider {

    private SQLiteDatabase db;

    private static final int CART = 1;

    private static final int CART_PRODUCTS = 2;

    private static final int PRODUCTS = 3;

    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(ProductAndCartDatabaseContract.AUTHORITY, ProductAndCartDatabaseContract.TABLE_CART, CART);
        sUriMatcher.addURI(ProductAndCartDatabaseContract.AUTHORITY, ProductAndCartDatabaseContract.TABLE_CART_PRODUCTS, CART_PRODUCTS);
        sUriMatcher.addURI(ProductAndCartDatabaseContract.AUTHORITY, ProductAndCartDatabaseContract.TABLE_PRODUCTS, PRODUCTS);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public boolean onCreate() {
        db = ProductAndCartDbHelper.getInstance(getContext()).getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = sUriMatcher.match(uri);
        String tableName;
        switch (match) {
            case CART:
                tableName = ProductAndCartDatabaseContract.TABLE_CART;
                break;
            case CART_PRODUCTS:
                tableName = ProductAndCartDatabaseContract.TABLE_CART_PRODUCTS;
                break;
            case PRODUCTS:
                tableName = ProductAndCartDatabaseContract.TABLE_PRODUCTS;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }
        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        cursor.getCount();
        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = sUriMatcher.match(uri);
        String tableName;
        switch (match) {
            case CART:
                tableName = ProductAndCartDatabaseContract.TABLE_CART;
                break;
            case CART_PRODUCTS:
                tableName = ProductAndCartDatabaseContract.TABLE_CART_PRODUCTS;
                break;
            case PRODUCTS:
                tableName = ProductAndCartDatabaseContract.TABLE_PRODUCTS;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }

        long rowId = db.insert(tableName, null, values);
        Uri rowUri = ContentUris.withAppendedId(uri, rowId);
        if (rowId > 0) {
            getContext().getContentResolver().notifyChange(rowUri, null);
        }
        return rowUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        String tableName;
        int match = sUriMatcher.match(uri);
        int numberOfRows = 0;
        switch (match) {
            case CART:
                tableName = ProductAndCartDatabaseContract.TABLE_CART;
                break;
            case CART_PRODUCTS:
                tableName = ProductAndCartDatabaseContract.TABLE_CART_PRODUCTS;
                break;
            case PRODUCTS:
                tableName = ProductAndCartDatabaseContract.TABLE_PRODUCTS;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                if (db.insert(tableName, null, value) != -1) {
                    numberOfRows++;
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            if (numberOfRows > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return numberOfRows;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        int numberOfRows;
        switch (match) {
            case CART:
                numberOfRows = db.delete(ProductAndCartDatabaseContract.TABLE_CART, selection, selectionArgs);
                break;
            case CART_PRODUCTS:
                numberOfRows = db.delete(ProductAndCartDatabaseContract.TABLE_CART_PRODUCTS, selection, selectionArgs);
                break;
            case PRODUCTS:
                numberOfRows = db.delete(ProductAndCartDatabaseContract.TABLE_PRODUCTS, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }
        if (numberOfRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberOfRows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        int numberOfRows;
        switch (match) {
            case CART:
                numberOfRows = db.update(ProductAndCartDatabaseContract.TABLE_CART, values, selection, selectionArgs);
                break;
            case CART_PRODUCTS:
                numberOfRows = db.update(ProductAndCartDatabaseContract.TABLE_CART_PRODUCTS, values, selection, selectionArgs);
                break;
            case PRODUCTS:
                numberOfRows = db.update(ProductAndCartDatabaseContract.TABLE_PRODUCTS, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }
        if (numberOfRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberOfRows;
    }
}
