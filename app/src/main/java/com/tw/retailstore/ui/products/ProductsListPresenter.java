package com.tw.retailstore.ui.products;

import com.tw.retailstore.businesslogic.ICartBusiness;
import com.tw.retailstore.businesslogic.ICartInfo;
import com.tw.retailstore.businesslogic.IProductsBusiness;
import com.tw.retailstore.models.Product;
import com.tw.retailstore.ui.products.ProductListViewContract;
import com.tw.retailstore.utils.BasePresenter;

import java.util.List;

/**
 * Presenter which interacts with {@link IProductsBusiness} to provide List of Products to display on UI.
 * Also interacts with {@link ICartBusiness} to check for existence a Cart and its product count.
 * Created by hritesh on 29/06/16.
 */

class ProductsListPresenter extends BasePresenter<ProductListViewContract.ProductView> implements ProductListViewContract.UserAction {

    private final IProductsBusiness productsFetcher;
    private final ICartBusiness cartBusiness;

    ProductsListPresenter(IProductsBusiness productsFetcher,
                                 ICartBusiness cartBusiness) {
        this.productsFetcher = productsFetcher;
        this.cartBusiness = cartBusiness;
    }

    @Override
    public void getAllProducts() {
        List<Product> products = productsFetcher.getProductsList();
        viewContract.showProducts(products);
    }

    @Override
    public int getNumberOfProductsInCart() {
        ICartInfo cartInfo = cartBusiness.getCartInfo();
        if (cartInfo == null) {
            return 0;
        }
        return cartInfo.getNumberOfProducts();
    }

}
