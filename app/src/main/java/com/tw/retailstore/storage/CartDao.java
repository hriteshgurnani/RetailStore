package com.tw.retailstore.storage;

/**
 * Data Access Object Model for a Cart in SQLiteDatabase.
 * Created by hritesh on 29/06/16.
 */

public class CartDao {

    /**
     * Identifier for a Cart.
     */
    private final String cartId;

    CartDao(String cartId) {
        this.cartId = cartId;
    }

    public String getCartId() {
        return cartId;
    }

}
