package com.tw.retailstore.businesslogic;

import com.tw.retailstore.models.CartKey;
import com.tw.retailstore.models.CartProduct;
import com.tw.retailstore.models.Product;
import com.tw.retailstore.storage.CartPersistenceHelper;
import com.tw.retailstore.storage.CartProductDao;
import com.tw.retailstore.utils.CurrencyUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Business Implementation of a Cart.
 * Holds Products, Price and Count for a Cart.
 * Created by hritesh on 29/06/16.
 */

class CartSession implements ICartInfo {

    private final CartKey cartKey;

    private final CartPersistenceHelper persistenceHelper;

    private final CartTransactionsHelper cartTransactionsHelper;

    private final Map<Integer, CartProduct> cartProductMap = new HashMap<>();

    private int productCount;

    private BigDecimal totalCartPrice = new BigDecimal(0);

    private boolean alreadyPersisted;

    CartSession(CartKey cartKey, CartPersistenceHelper persistenceHelper, boolean alreadyPersisted) {
        this.cartKey = cartKey;
        this.persistenceHelper = persistenceHelper;
        this.alreadyPersisted = alreadyPersisted;
        this.cartTransactionsHelper = new CartTransactionsHelper(cartKey, persistenceHelper);
    }

    @Override
    public int getCartProductQuantity(int productId) {
        if (cartProductMap.containsKey(productId)) {
            return cartProductMap.get(productId).getCartQuantity();
        }
        return 0;
    }

    @Override
    public double getTotalPrice() {
        return totalCartPrice.doubleValue();
    }

    @Override
    public int getNumberOfProducts() {
        return productCount;
    }

    @Override
    public List<CartProduct> getCartProducts() {
        return new ArrayList<>(cartProductMap.values());
    }

    @Override
    public CartProduct getCartProduct(int productId) {
        return cartProductMap.get(productId);
    }

    private void updateParamsOnAddition(double productTotalMrpPrice) {
        productCount++;
        this.totalCartPrice = CurrencyUtils.add(this.totalCartPrice, productTotalMrpPrice);
    }

    private void updateParamsOnDeletion(CartProduct cartProduct) {
        productCount--;
        this.totalCartPrice = CurrencyUtils.subtract(this.totalCartPrice, cartProduct.getTotalMrpPrice());
    }

    void addProduct(Product product, int quantity) {
        boolean isExisting = cartProductMap.containsKey(product.getProductId());
        if (isExisting) {
            try {
                updateProduct(product.getProductId(), quantity);
            } catch (ProductNotInCartException e) {
                throw new IllegalStateException("Not possible.");
            }
        } else {
            addProductInternal(product, quantity);
            cartTransactionsHelper.addToPendingInsertTransactions(product.getProductId());
        }
    }

    void updateProduct(int productId, int quantity) throws ProductNotInCartException {
        if (!cartProductMap.containsKey(productId)) {
            throw new ProductNotInCartException("Product doesn't exist in Cart");
        }
        CartProduct cartProduct = cartProductMap.get(productId);
        int netQuantityChange = cartProduct.getCartQuantity() - quantity;
        if (netQuantityChange == 0) {
            return;
        }
        updateProductInternal(productId, quantity);
        cartTransactionsHelper.addToPendingUpdateTransactions(productId);
    }

    void removeProduct(int productId) throws ProductNotInCartException {
        if (!cartProductMap.containsKey(productId)) {
            throw new ProductNotInCartException("Product doesn't exist in Cart");
        }
        CartProduct cartProduct = cartProductMap.remove(productId);
        updateParamsOnDeletion(cartProduct);
        cartTransactionsHelper.addToPendingDeleteTransactions(productId);
    }

    private void addProductInternal(Product product, int quantity) {
        double productTotalMrpPrice = CurrencyUtils.buildPrice(product.getMrp(), quantity);
        CartProduct cartProduct = new CartProduct.Builder()
                .setProduct(product)
                .setCartQuantity(quantity)
                .setTotalMrpPrice(productTotalMrpPrice)
                .build();
        cartProductMap.put(product.getProductId(), cartProduct);
        updateParamsOnAddition(productTotalMrpPrice);
    }

    private void updateProductInternal(int productId, int quantity) {
        CartProduct cartProduct = cartProductMap.get(productId);
        int netQuantityChange = cartProduct.getCartQuantity() - quantity;
        if (netQuantityChange == 0) {
            return;
        }
        updateParamsOnDeletion(cartProduct);
        double productTotalMrpPrice = CurrencyUtils.buildPrice(cartProduct.getMrp(), quantity);
        cartProduct.setCartQuantity(quantity);
        cartProduct.setTotalMrpPrice(productTotalMrpPrice);
        updateParamsOnAddition(productTotalMrpPrice);
    }

    void persistCart() {
        if (getNumberOfProducts() <= 0) {
            return;
        }
        if (!alreadyPersisted) {
            persistenceHelper.insertCart(cartKey.getCartId());
            alreadyPersisted = true;
        }
        persistProducts();
    }

    private void persistProducts() {
        cartTransactionsHelper.persistTransactions(this);
    }

    void initialiseFromPersistence(List<CartProductDao> cartProductDaoList) {
        for (CartProductDao cartProductDao : cartProductDaoList) {
            Product product = new Product.Builder()
                    .initFromCartProductDao(cartProductDao)
                    .build();
            addProductInternal(product, cartProductDao.getCartQuantity());
        }
    }

    void close() {
        if (alreadyPersisted) {
            persistenceHelper.deleteCart(cartKey.getCartId());
        }
    }
}
