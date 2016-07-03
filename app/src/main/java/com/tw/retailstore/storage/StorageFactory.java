package com.tw.retailstore.storage;

import com.tw.retailstore.RetailStoreApp;

/**
 * Factory to provide Business logic instances which internally support CRUD operations
 * for Store Products and User Carts on a local Database.
 * Created by hritesh on 29/06/16.
 */

public class StorageFactory {

    public static StorageFactory getInstance() {
        return new StorageFactory();
    }

    private StorageFactory() {
    }

    public CartPersistenceHelper getPersistenceHelper() {
        return CartPersistenceHelper.newInstance(RetailStoreApp.getInstance());
    }

    public ProductsPersistenceHelper getProductsPersistenceHelper() {
        return ProductsPersistenceHelper.newInstance(RetailStoreApp.getInstance());
    }
}
