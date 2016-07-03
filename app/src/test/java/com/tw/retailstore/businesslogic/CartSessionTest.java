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
 * Unit Tests for {@link CartSession}
 * Created by hritesh on 03/07/16.
 */

public class CartSessionTest {

    private static final Product mockProductOne;

    private static final Product mockProductTwo;

    static {
        Product.Builder builder = new Product.Builder();

        mockProductOne = builder.setProductImageName(null)
                .setProductCategory("Food")
                .setMrp(10)
                .setProductId(1)
                .setProductName("First product")
                .build();

        mockProductTwo = builder.setProductImageName(null)
                .setProductCategory("Food")
                .setMrp(20)
                .setProductId(2)
                .setProductName("Second product")
                .build();
    }

    @Mock
    CartKey cartKey;

    @Mock
    CartPersistenceHelper cartPersistenceHelper;

    private CartSession cartSession;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cartSession = new CartSession(cartKey, cartPersistenceHelper, false);
    }

    @After
    public void tearDown() {
        cartSession = null;
    }

    @Test
    public void addNewProductsToCartUpdatesCountAndPrice() {
        //Add First product
        cartSession.addProduct(mockProductOne, 5); //Price is 5*10 = 50
        //Add Second product
        cartSession.addProduct(mockProductTwo, 10);//Price is 10*20 = 200

        Assert.assertEquals(2, cartSession.getNumberOfProducts());
        Assert.assertEquals(250.00, cartSession.getTotalPrice(), 0);
    }

    @Test
    public void addSameProductTwiceUpdatesCountOnlyOnceAndPriceWithLatestQuantity() {
        //Add First product
        cartSession.addProduct(mockProductOne, 1); //Price is 1*10 = 10
        //Add First product again
        cartSession.addProduct(mockProductOne, 2);//Price is 2*10 = 20

        Assert.assertEquals(1, cartSession.getNumberOfProducts());
        Assert.assertEquals(20.00, cartSession.getTotalPrice(), 0);
    }

    @Test
    public void updateExistingProductToCartUpdatesPriceAndNotCount() throws ProductNotInCartException {
        //Add First product with Quantity 1
        cartSession.addProduct(mockProductOne, 1);

        //Update First product with Quantity 2
        cartSession.updateProduct(mockProductOne.getProductId(), 2);

        Assert.assertEquals(1, cartSession.getNumberOfProducts());
        Assert.assertEquals(20.00, cartSession.getTotalPrice(), 0);
    }

    @Test(expected = ProductNotInCartException.class)
    public void updateNonExistingProductToCartThrowsException() throws ProductNotInCartException {
        //Update First product with Quantity 2
        cartSession.updateProduct(mockProductOne.getProductId(), 2);
    }


    @Test
    public void removeAddedProductFromCartReturnsZeroCountAndPrice() throws ProductNotInCartException {
        //Add First product with Quantity 1
        cartSession.addProduct(mockProductOne, 1);

        //Remove First product
        cartSession.removeProduct(mockProductOne.getProductId());

        Assert.assertEquals(0, cartSession.getNumberOfProducts());
        Assert.assertEquals(0.00, cartSession.getTotalPrice(), 0);
    }

    @Test
    public void removeAddedAndUpdatedProductFromCartReturnsZeroCountAndPrice() throws ProductNotInCartException {
        //Add First product with Quantity 1
        cartSession.addProduct(mockProductOne, 1);

        //Update First product with Quantity 2
        cartSession.updateProduct(mockProductOne.getProductId(), 2);

        //Remove First product
        cartSession.removeProduct(mockProductOne.getProductId());

        Assert.assertEquals(0, cartSession.getNumberOfProducts());
        Assert.assertEquals(0.00, cartSession.getTotalPrice(), 0);
    }

    @Test(expected = ProductNotInCartException.class)
    public void removeNonExistingProduct() throws ProductNotInCartException {
        cartSession.removeProduct(mockProductOne.getProductId());
    }

    @Test
    public void removeOneProductFromTwoCartReturnsSingleCount() throws ProductNotInCartException {
        //Add First product with Quantity 1
        cartSession.addProduct(mockProductOne, 1);

        //Add Second product with Quantity 1
        cartSession.addProduct(mockProductTwo, 1);//1*20

        //Remove First product
        cartSession.removeProduct(mockProductOne.getProductId());

        Assert.assertEquals(1, cartSession.getNumberOfProducts());
        Assert.assertEquals(20.00, cartSession.getTotalPrice(), 0);
    }
}
