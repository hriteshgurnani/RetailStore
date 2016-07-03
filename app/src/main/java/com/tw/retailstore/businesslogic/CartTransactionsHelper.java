package com.tw.retailstore.businesslogic;

import com.tw.retailstore.models.CartKey;
import com.tw.retailstore.models.CartProduct;
import com.tw.retailstore.storage.CartPersistenceHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Transaction Logger for all Production Additions/Updates/Remove operations done on Cart Products.
 * Persists these transactions to Persistence Layer when required.
 * Created by hritesh on 29/06/16.
 */

class CartTransactionsHelper {

    private final CartKey cartKey;

    private final Set<Integer> addTransactionSet = new HashSet<>();

    private final Set<Integer> deleteTransactionSet = new HashSet<>();

    private final Set<Integer> updateTransactionSet = new HashSet<>();

    private final CartPersistenceHelper persistenceHelper;

    CartTransactionsHelper(CartKey cartKey, CartPersistenceHelper persistenceHelper) {
        this.cartKey = cartKey;
        this.persistenceHelper = persistenceHelper;
    }

    void addToPendingInsertTransactions(int productId) {
        addTransactionSet.add(productId);
    }

    void addToPendingDeleteTransactions(int productId) {
        if (addTransactionSet.contains(productId)) {
            addTransactionSet.remove(productId);
            return;
        }

        if (updateTransactionSet.contains(productId)) {
            updateTransactionSet.remove(productId);
        }

        deleteTransactionSet.add(productId);
    }

    private void clearTransactions() {
        addTransactionSet.clear();
        updateTransactionSet.clear();
        deleteTransactionSet.clear();
    }

    void addToPendingUpdateTransactions(int productId) {
        if (addTransactionSet.contains(productId)) {
            return;
        }
        updateTransactionSet.add(productId);
    }

    void persistTransactions(ICartInfo cartInfo) {
        if (addTransactionSet.size() > 0) {
            List<CartProduct> addedList = new ArrayList<>();
            for (Integer productId : addTransactionSet) {
                addedList.add(cartInfo.getCartProduct(productId));
            }
            persistenceHelper.insertProducts(this.cartKey, addedList);
        }
        if (updateTransactionSet.size() > 0) {
            for (Integer productId : updateTransactionSet) {
                persistenceHelper.updateProduct(this.cartKey, cartInfo.getCartProduct(productId));
            }
        }
        if (deleteTransactionSet.size() > 0) {
            for (Integer productId : deleteTransactionSet) {
                persistenceHelper.deleteProduct(this.cartKey, productId);
            }
        }
        clearTransactions();
    }

    Set<Integer> getAddTransactionSet() {
        return addTransactionSet;
    }

    Set<Integer> getDeleteTransactionSet() {
        return deleteTransactionSet;
    }

    Set<Integer> getUpdateTransactionSet() {
        return updateTransactionSet;
    }
}
