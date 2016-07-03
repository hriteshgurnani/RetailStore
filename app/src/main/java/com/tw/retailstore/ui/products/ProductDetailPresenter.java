package com.tw.retailstore.ui.products;

import com.tw.retailstore.businesslogic.ICartBusiness;
import com.tw.retailstore.businesslogic.ICartInfo;
import com.tw.retailstore.businesslogic.IProductsBusiness;
import com.tw.retailstore.businesslogic.ProductNotInCartException;
import com.tw.retailstore.models.Product;
import com.tw.retailstore.utils.BasePresenter;

/**
 * Presenter which interacts with {@link IProductsBusiness} to provide required
 * Product Info to display on UI.
 * Handles User Actions performed on Product and appropriately delegates it to {@link ICartBusiness}
 * Created by hritesh on 29/06/16.
 */

class ProductDetailPresenter extends BasePresenter<ProductDetailViewContract.ProductView> implements ProductDetailViewContract.UserAction {

    private final ICartBusiness cartBusiness;
    private final IProductsBusiness productsBusiness;

    ProductDetailPresenter(IProductsBusiness productsBusiness, ICartBusiness cartBusiness) {
        this.productsBusiness = productsBusiness;
        this.cartBusiness = cartBusiness;
    }

    @Override
    public void getProductDetail(int productId) {
        Product product = productsBusiness.getProductDetails(productId);
        viewContract.showProductDetail(product);
    }

    @Override
    public void addOrUpdateProduct(Product product, int currentQuantity) {
        cartBusiness.addProduct(product, currentQuantity);
    }

    @Override
    public void removeProduct(int productId) throws ProductNotInCartException {
        cartBusiness.removeProduct(productId);
    }

    @Override
    public int getShoppedQuantity(int productId) {
        ICartInfo cartInfo = cartBusiness.getCartInfo();
        return cartInfo.getCartProductQuantity(productId);
    }

    @Override
    public int getNumberOfProductsInCart() {
        ICartInfo cartInfo = cartBusiness.getCartInfo();
        if (cartInfo == null) {
            return 0;
        }
        return cartInfo.getNumberOfProducts();
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        cartBusiness.persistCart();
    }

}
