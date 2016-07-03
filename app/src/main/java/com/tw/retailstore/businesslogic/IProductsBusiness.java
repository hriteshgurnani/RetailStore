package com.tw.retailstore.businesslogic;

import com.tw.retailstore.models.Product;

import java.util.List;

/**
 * Business API Interface to fetch Products of the Store.
 * Also provides API to do a Dummy insert of Products for temporary Application testing.
 * Created by hritesh on 29/06/16.
 */

public interface IProductsBusiness {

    /**
     * Task Completion callback.
     */
    interface IProductInsertCallback {
        void onCompletion();
    }

    /**
     * Fetch Products of the Store.
     *
     * @return List of products of the Store.
     */
    List<Product> getProductsList();

    /**
     * Get Product Details for a Particular Product of the Store.
     *
     * @param productId Product ID of the product to fetch.
     * @return Product details of requested product.
     */
    Product getProductDetails(int productId);

    /**
     * Dummy method to allow current Application testing as all data should be maintained locally.
     *
     * @param callback Callback to notify completion of operation.
     */
    void insertDummyProducts(IProductInsertCallback callback);
}
