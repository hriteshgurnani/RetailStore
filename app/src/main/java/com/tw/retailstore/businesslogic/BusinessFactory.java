package com.tw.retailstore.businesslogic;

/**
 * Factory to provide Business logic instances for Store Products and User Carts.
 * Created by hritesh on 29/06/16.
 */
public class BusinessFactory {

    private static BusinessFactory instance;

    private ICartBusiness cartManager;

    private IProductsBusiness productsBusiness;

    public static synchronized BusinessFactory getInstance() {
        if (instance == null) {
            instance = new BusinessFactory();
        }
        return instance;
    }

    private BusinessFactory() {
    }

    public ICartBusiness getCartBusiness() {
        if (cartManager == null) {
            cartManager = CartManager.newInstance();
        }
        return cartManager;
    }

    public IProductsBusiness getProductsBusiness() {
        if (productsBusiness == null) {
            productsBusiness = ProductsBusiness.newInstance();
        }
        return productsBusiness;
    }

}
