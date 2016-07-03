package com.tw.retailstore.ui.products;

import com.tw.retailstore.models.Product;

import java.util.List;

/**
 * Contract between {@link ProductsListActivity} and {@link ProductsListPresenter}.
 * <p>
 * ProductsListActivity is responsible for displaying Products of the Retail Store.
 * while ProductsListPresenter is responsible to delegate Business functionality
 * to {@link com.tw.retailstore.businesslogic.IProductsBusiness} and {@link com.tw.retailstore.businesslogic.ICartBusiness}
 * to fetch all Products and Number Of Products in Current Cart if it exists.
 * Created by hritesh on 29/06/16.
 */

interface ProductListViewContract {

    /**
     * Contract that UI component showing Product List should adhere to.
     * Implemented by {@link ProductsListActivity}
     */
    interface ProductView {
        /**
         * Shows Input List of Products to User.
         *
         * @param productList List of Products of Retail Store to display to User.
         */
        void showProducts(List<Product> productList);
    }

    /**
     * Interface to achieve Product List Display on UI.
     */
    interface UserAction {
        /**
         * Request All Products of the Store.
         */
        void getAllProducts();

        /**
         * Number of Products in a Cart.
         * Returns 0 if no Cart exists.
         *
         * @return Product Number Count
         */
        int getNumberOfProductsInCart();
    }
}
