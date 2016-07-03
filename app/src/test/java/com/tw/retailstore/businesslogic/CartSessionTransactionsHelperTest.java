package com.tw.retailstore.businesslogic;

import com.tw.retailstore.models.CartKey;
import com.tw.retailstore.models.Product;
import com.tw.retailstore.storage.CartPersistenceHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit Tests for {@link CartTransactionsHelper}
 * Created by hritesh on 03/07/16.
 */

public class CartSessionTransactionsHelperTest {

    private static final Product mockProductOne;

    static {
        Product.Builder builder = new Product.Builder();

        mockProductOne = builder.setProductImageName(null)
                .setProductCategory("Food")
                .setMrp(10)
                .setProductId(1)
                .setProductName("First product")
                .build();
    }

    @Mock
    CartKey cartKey;

    @Mock
    CartPersistenceHelper cartPersistenceHelper;

    private CartTransactionsHelper cartTransactionsHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cartTransactionsHelper = new CartTransactionsHelper(cartKey, cartPersistenceHelper);
    }

    @After
    public void tearDown() {
        cartTransactionsHelper = null;
    }

    @Test
    public void addToPendingInsertTransactions() {
        cartTransactionsHelper.addToPendingInsertTransactions(mockProductOne.getProductId());

        Assert.assertTrue(cartTransactionsHelper.getAddTransactionSet().contains(mockProductOne.getProductId()));

    }

    @Test
    public void addToPendingUpdateTransactions() {
        cartTransactionsHelper.addToPendingUpdateTransactions(mockProductOne.getProductId());

        Assert.assertTrue(cartTransactionsHelper.getUpdateTransactionSet().contains(mockProductOne.getProductId()));

    }

    @Test
    public void addToPendingRemoveTransactions() {
        cartTransactionsHelper.addToPendingDeleteTransactions(mockProductOne.getProductId());

        Assert.assertTrue(cartTransactionsHelper.getDeleteTransactionSet().contains(mockProductOne.getProductId()));
    }

    @Test
    public void alreadyPresentInAdd_DoesNotAddItToUpdateTransactions() {
        cartTransactionsHelper.addToPendingInsertTransactions(mockProductOne.getProductId());
        cartTransactionsHelper.addToPendingUpdateTransactions(mockProductOne.getProductId());

        Assert.assertTrue(cartTransactionsHelper.getAddTransactionSet().contains(mockProductOne.getProductId()));
        Assert.assertTrue(!cartTransactionsHelper.getUpdateTransactionSet().contains(mockProductOne.getProductId()));
    }

    @Test
    public void alreadyPresentInAdd_OnDeleteTransaction_GetsRemovedFromAdd_And_NotAddedToDeleteTransaction() {
        cartTransactionsHelper.addToPendingInsertTransactions(mockProductOne.getProductId());
        cartTransactionsHelper.addToPendingDeleteTransactions(mockProductOne.getProductId());

        Assert.assertTrue(!cartTransactionsHelper.getAddTransactionSet().contains(mockProductOne.getProductId()));
        Assert.assertTrue(!cartTransactionsHelper.getDeleteTransactionSet().contains(mockProductOne.getProductId()));
    }

    @Test
    public void alreadyPresentInUpdate_OnDeleteTransaction_GetsRemovedFromUpdate_And_AddedToDeleteTransaction() {
        cartTransactionsHelper.addToPendingUpdateTransactions(mockProductOne.getProductId());
        cartTransactionsHelper.addToPendingDeleteTransactions(mockProductOne.getProductId());

        Assert.assertTrue(!cartTransactionsHelper.getUpdateTransactionSet().contains(mockProductOne.getProductId()));
        Assert.assertTrue(cartTransactionsHelper.getDeleteTransactionSet().contains(mockProductOne.getProductId()));
    }
}
