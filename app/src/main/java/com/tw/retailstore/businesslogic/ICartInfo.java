package com.tw.retailstore.businesslogic;

import com.tw.retailstore.models.CartProduct;

import java.util.List;

/**
 * Business API Interface to fetch details of a Cart.
 * Created by hritesh on 29/06/16.
 */

public interface ICartInfo {

    /**
     * Get Products in a Cart.
     * @return Collection of Products in the Cart.
     */
    List<CartProduct> getCartProducts();

    /**
     * Quantity of Items in the Cart for a particular Product
     * @param productId Product ID of the Product.
     * @return Quantity Number
     */
    int getCartProductQuantity(int productId);

    /**
     * Get Total Value of the Cart.
     * @return double value representing total value of the Cart.
     */
    double getTotalPrice();

    /**
     * Get Total Number of Products in the Cart.
     * @return Product count.
     */
    int getNumberOfProducts();

    /**
     * Get Details of a Particular Product in the Cart.
     * @param productId Product ID of the Product.
     * @return Detailed Info of the Product in the Cart.
     */
    CartProduct getCartProduct(int productId);
}
