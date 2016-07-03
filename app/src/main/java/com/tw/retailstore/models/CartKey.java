package com.tw.retailstore.models;

import java.util.UUID;

/**
 * Cart Session Key to Identify a Particular Cart.
 * Required when Multiple Carts for a Single Store/Single Cart for Multiple Stores need to be supported.
 * Uses a Random Number as the ID for the first time and is re-instantiated from the Database later.
 * Created by hritesh on 29/06/16.
 */

public class CartKey {

    private final String cartId;

    public CartKey() {
        this.cartId = UUID.randomUUID().toString();
    }

    public CartKey(String cartId) {
        this.cartId = cartId;
    }

    public String getCartId() {
        return cartId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartKey cartKey = (CartKey) o;

        return cartId.equals(cartKey.cartId);

    }

    @Override
    public int hashCode() {
        return cartId.hashCode();
    }

    @Override
    public String toString() {
        return "CartKey{" +
                "cartId='" + cartId + '\'' +
                '}';
    }
}
