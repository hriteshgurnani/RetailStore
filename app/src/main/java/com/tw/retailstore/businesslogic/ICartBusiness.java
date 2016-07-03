package com.tw.retailstore.businesslogic;

import com.tw.retailstore.models.Product;

/**
 * Business API Interface for Cart.
 * Examples include Fetch/Add/Update/Remove and Persist Products.
 * Created by hritesh on 29/06/16.
 */

public interface ICartBusiness {


    /**
     * Add Product to a Cart.
     *
     * @param product       Product to be added.
     * @param finalQuantity Quantity of the product to be added.
     */
    void addProduct(Product product, int finalQuantity);

    /**
     * Update Product existing in a Cart.
     *
     * @param productId     Product ID of the product to be updated.
     * @param finalQuantity New finalQuantity to update to.
     */
    void updateProductToQuantity(int productId, int finalQuantity) throws ProductNotInCartException;

    /**
     * Remove product from the Cart.
     *
     * @param productId Product ID of the product to be deleted.
     */
    void removeProduct(int productId) throws ProductNotInCartException;

    /**
     * Get Cart Info for current Cart.
     * Empty Cart Info if no cart exists.
     *
     * @return {@link ICartInfo} for current Cart.
     */
    ICartInfo getCartInfo();

    /**
     * Persist cart into Persistence layer i.e. Database.
     */
    void persistCart();

    /**
     * Delete cart when All Products are removed from it.
     */
    void deleteCart();
}
