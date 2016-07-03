package com.tw.retailstore.ui.cart;

import com.tw.retailstore.businesslogic.ICartBusiness;
import com.tw.retailstore.businesslogic.ICartInfo;
import com.tw.retailstore.businesslogic.ProductNotInCartException;
import com.tw.retailstore.utils.BasePresenter;

/**
 * Presenter which interacts with {@link ICartBusiness} to provide required
 * Cart Business Info to Cart UI.
 * Handles User Actions performed on Cart Activity and appropriately delegates it to {@link ICartBusiness}
 * Created by hritesh on 29/06/16.
 */

class CartPresenter extends BasePresenter<CartViewContract.CartView> implements CartViewContract.UserAction {

    private final ICartBusiness cartBusiness;

    CartPresenter(ICartBusiness cartBusiness) {
        this.cartBusiness = cartBusiness;
    }

    @Override
    public void updateProduct(int productId, int quantity) throws ProductNotInCartException {
        cartBusiness.updateProductToQuantity(productId, quantity);
    }

    @Override
    public void removeProduct(int productId) throws ProductNotInCartException {
        cartBusiness.removeProduct(productId);
    }

    @Override
    public ICartInfo getCartInfo() {
        return cartBusiness.getCartInfo();
    }

    @Override
    public void deleteCart() {
        cartBusiness.deleteCart();
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        cartBusiness.persistCart();
    }
}
