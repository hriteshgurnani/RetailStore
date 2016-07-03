package com.tw.retailstore.ui.products;

import com.tw.retailstore.businesslogic.ProductNotInCartException;
import com.tw.retailstore.models.Product;

/**
 * Contract between {@link ProductDetailActivity} and {@link ProductDetailPresenter}.
 * <p>
 * ProductDetailActivity is responsible for Product Details UI Display(Image/Quantity/Unit Price)
 * while ProductDetailPresenter is responsible to delegate Business functionality
 * to {@link com.tw.retailstore.businesslogic.IProductsBusiness} and {@link com.tw.retailstore.businesslogic.ICartBusiness} appropriately.
 * Created by hritesh on 29/06/16.
 */

interface ProductDetailViewContract {

    /**
     * Contract that UI component showing Product Detail should adhere to.
     * Implemented by {@link ProductDetailActivity}
     */
    interface ProductView {
        /**
         * Shows details of the Product in Product Detail UI.
         *
         * @param product Product whose details should be displayed to the User.
         */
        void showProductDetail(Product product);
    }

    /**
     * Interface to achieve Product Detail Display on UI.
     */
    interface UserAction {

        /**
         * Get Product Details of a particular Product.
         *
         * @param productId Product ID of which details should be fetched.
         */
        void getProductDetail(int productId);

        /**
         * Add or Update this Product to Cart.
         *
         * @param product         Product to add/update to Cart.
         * @param currentQuantity Quantity of this product to be added.
         */
        void addOrUpdateProduct(Product product, int currentQuantity);

        /**
         * Remove Product from Cart.
         *
         * @param productId Product ID of the Product to be removed.
         */
        void removeProduct(int productId) throws ProductNotInCartException;

        /**
         * Quantity of this Product that is in the Cart.
         * Returns 0 if the Product is not in the Cart or No Cart Exists.
         *
         * @param productId Product ID whose Shopped Quantity is required.
         * @return Shopped Quantity Number of a particular Product.
         */
        int getShoppedQuantity(int productId);

        /**
         * Number of Products in a Cart.
         * Returns 0 if no Cart exists.
         *
         * @return Product Number Count
         */
        int getNumberOfProductsInCart();
    }
}
