package com.tw.retailstore.businesslogic;

import com.tw.retailstore.models.Product;
import com.tw.retailstore.storage.ProductsPersistenceHelper;
import com.tw.retailstore.storage.StorageFactory;

import java.util.List;

/**
 * Business Implementation to fetch Products of the Store.
 * Fetches Products locally from a SQLite Database and eventually maintains an In Memory List of Products.
 * Supports Dummy Insert operation for Application testing.
 * Created by hritesh on 29/06/16.
 */
class ProductsBusiness implements IProductsBusiness {

    private List<Product> inMemoryProductList;

    synchronized static ProductsBusiness newInstance() {
        return new ProductsBusiness();
    }

    @Override
    public List<Product> getProductsList() {
        if (inMemoryProductList == null || inMemoryProductList.isEmpty()) {
            ProductsPersistenceHelper productsPersistence = StorageFactory.getInstance().getProductsPersistenceHelper();
            inMemoryProductList = productsPersistence.getProductsFromPersistence();
        }
        return inMemoryProductList;
    }

    @Override
    public Product getProductDetails(int productId) {
        for (Product product : inMemoryProductList) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        throw new IllegalStateException("Not possible for current app");
    }

    @Override
    public void insertDummyProducts(IProductInsertCallback callback) {
        ProductsPersistenceHelper productsPersistence = StorageFactory.getInstance().getProductsPersistenceHelper();
        productsPersistence.insertDummyProducts(callback);
    }
}
