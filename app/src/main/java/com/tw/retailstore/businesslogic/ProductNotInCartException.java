package com.tw.retailstore.businesslogic;

/**
 * Exception thrown if Update/Remove operation is performed on a Product
 * which doesn't exist in the Cart.
 * Created by hritesh on 03/07/16.
 */
public class ProductNotInCartException extends Throwable {

    public ProductNotInCartException(String s) {
        super(s);
    }
}
