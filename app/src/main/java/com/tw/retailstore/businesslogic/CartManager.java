package com.tw.retailstore.businesslogic;

import android.os.AsyncTask;

import com.tw.retailstore.models.CartKey;
import com.tw.retailstore.models.CartProduct;
import com.tw.retailstore.models.Product;
import com.tw.retailstore.storage.CartDao;
import com.tw.retailstore.storage.CartPersistenceHelper;
import com.tw.retailstore.storage.CartProductDao;
import com.tw.retailstore.storage.StorageFactory;
import com.tw.retailstore.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Business Implementation to manage Carts.
 * Main idea of this manager is to allow supporting of Multiple Carts if required.
 * Also Entry point for all Cart Business functionality to the UI layer.
 * Created by hritesh on 29/06/16.
 */

class CartManager implements ICartBusiness {

    /**
     * Empty CartInfo if No CartSession is Active.
     */
    private static final ICartInfo EMPTY_CART_INFO = new ICartInfo() {
        @Override
        public List<CartProduct> getCartProducts() {
            return new ArrayList<>(0);
        }

        @Override
        public int getCartProductQuantity(int productId) {
            return 0;
        }

        @Override
        public double getTotalPrice() {
            return 0;
        }

        @Override
        public int getNumberOfProducts() {
            return 0;
        }

        @Override
        public CartProduct getCartProduct(int productId) {
            return null;
        }
    };

    private CartSession cartSession;

    private CartManager() {
        init();
    }

    private void init() {
        ReadCartsFromPersistenceTask readTask = new ReadCartsFromPersistenceTask();
        readTask.execute();
    }

    static synchronized ICartBusiness newInstance() {
        return new CartManager();
    }

    private CartSession createCartIfNotExists() {
        if (cartSession == null) {
            CartKey cartKey = new CartKey();
            cartSession = new CartSession(cartKey, StorageFactory.getInstance().getPersistenceHelper(), false);
        }
        return cartSession;
    }

    private CartSession createCartWithId(String cartId) {
        CartKey cartKey = new CartKey(cartId);
        return new CartSession(cartKey, StorageFactory.getInstance().getPersistenceHelper(), true);
    }

    @Override
    public void addProduct(Product product, int finalQuantity) {
        CartSession cartSession = createCartIfNotExists();
        cartSession.addProduct(product, finalQuantity);
    }

    @Override
    public void updateProductToQuantity(int productId, int finalQuantity) throws ProductNotInCartException {
        Preconditions.checkNotNull(cartSession, "updateProductToQuantity() cart cannot be null");
        cartSession.updateProduct(productId, finalQuantity);
    }

    @Override
    public void removeProduct(int productId) throws ProductNotInCartException {
        Preconditions.checkNotNull(cartSession, "removeProduct() cart cannot be null");
        cartSession.removeProduct(productId);
    }

    @Override
    public ICartInfo getCartInfo() {
        if (cartSession == null) {
            return EMPTY_CART_INFO;
        }
        return cartSession;
    }

    @Override
    public void persistCart() {
        if (cartSession != null) {
            cartSession.persistCart();
        }
    }

    @Override
    public void deleteCart() {
        Preconditions.checkNotNull(cartSession, "deleteCart() cart cannot be null");
        cartSession.close();
        cartSession = null;
    }

    private class ReadCartsFromPersistenceTask extends AsyncTask<Void, Void, CartSession> {

        @Override
        protected CartSession doInBackground(Void... voids) {
            CartPersistenceHelper persistenceHelper = StorageFactory.getInstance().getPersistenceHelper();
            List<CartDao> cartDaoList = persistenceHelper.getAllCartsFromPersistence();
            if (cartDaoList.isEmpty()) {
                return null;
            }
            if (cartDaoList.size() > 1) {
                throw new IllegalArgumentException("This case is not possible currently");
            }
            CartDao cartDao = cartDaoList.get(0);
            List<CartProductDao> cartProductDaoList = persistenceHelper.getCartProductsForCart(cartDao.getCartId());
            CartSession cartSession = CartManager.this.createCartWithId(cartDao.getCartId());
            cartSession.initialiseFromPersistence(cartProductDaoList);
            return cartSession;
        }

        @Override
        protected void onPostExecute(CartSession cartSession) {
            CartManager.this.notifyDatabaseReadDone(cartSession);
        }
    }

    private void notifyDatabaseReadDone(CartSession cartSession) {
        this.cartSession = cartSession;
    }

}
