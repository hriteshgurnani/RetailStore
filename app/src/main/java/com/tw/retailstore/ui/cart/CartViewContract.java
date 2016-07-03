package com.tw.retailstore.ui.cart;

import com.tw.retailstore.businesslogic.ICartInfo;
import com.tw.retailstore.businesslogic.ProductNotInCartException;

/**
 * Contract between {@link CartMainActivity} and {@link CartPresenter}.
 *
 * CartMainActivity is responsible for Cart UI Display while CartPresenter is responsible to delegate Business functionality
 * to {@link com.tw.retailstore.businesslogic.ICartBusiness}
 * Created by hritesh on 29/06/16.
 */

interface CartViewContract {

    /**
     * Currently a Marker Interface but useful when Product Update Operations
     * should be asynchronous etc.
     */
    interface CartView {

    }

    /**
     * Interface for Actions that can be performed on Cart UI.
     */
    interface UserAction {

        /**
         * Update Product Quantity User Action Performed.
         * @param productId Product ID of the Product updated.
         * @param quantity Quantity selected.
         */
        void updateProduct(int productId, int quantity) throws ProductNotInCartException;

        /**
         * Remove Product from Cart.
         * @param productId Product ID of product to be removed.
         */
        void removeProduct(int productId) throws ProductNotInCartException;

        /**
         * Get Cart Details for User.
         * @return {@link ICartInfo} as result.
         */
        ICartInfo getCartInfo();

        /**
         * Delete Cart when last Product is Removed.
         */
        void deleteCart();
    }
}
