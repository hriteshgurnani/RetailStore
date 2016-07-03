package com.tw.retailstore.ui.products;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tw.retailstore.R;
import com.tw.retailstore.businesslogic.BusinessFactory;
import com.tw.retailstore.businesslogic.ICartBusiness;
import com.tw.retailstore.businesslogic.IProductsBusiness;
import com.tw.retailstore.businesslogic.ProductNotInCartException;
import com.tw.retailstore.models.Product;
import com.tw.retailstore.utils.BaseAppCompactActivity;
import com.tw.retailstore.utils.CurrencyUtils;
import com.tw.retailstore.utils.CustomImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Activity that displays Product Details to the User.
 * Supports Addition/Update and Removal of this Product from the Cart.
 * Created by hritesh on 29/06/16.
 */

public class ProductDetailActivity extends BaseAppCompactActivity implements ProductDetailViewContract.ProductView {

    public static final String EXTRA_PRODUCT_DETAIL = "extra_product_key";
    @Bind(R.id.productNameTextView)
    TextView productNameTextView;
    @Bind(R.id.productImageView)
    CustomImageView productImageView;
    @Bind(R.id.quantityTextView)
    TextView quantityTextView;
    @Bind(R.id.plusImageView)
    ImageView plusImageView;
    @Bind(R.id.minusImageView)
    ImageView minusImageView;
    @Bind(R.id.unitPriceTextView)
    TextView unitPriceTextView;
    private ProductDetailPresenter productDetailsPresenter;
    private TextView actionBarProductCountTextView;
    private Product product;
    private int productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setActionBarTitle("Product Details");
        ButterKnife.bind(this);
        ICartBusiness cartBusiness = BusinessFactory.getInstance().getCartBusiness();
        IProductsBusiness productsBusiness = BusinessFactory.getInstance().getProductsBusiness();
        this.productId = getIntent().getIntExtra(EXTRA_PRODUCT_DETAIL, -1);
        if (productId == -1) {
            throw new IllegalArgumentException("Invalid product id");
        }
        productDetailsPresenter = new ProductDetailPresenter(productsBusiness, cartBusiness);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        productDetailsPresenter.attachView(this);
        productDetailsPresenter.getProductDetail(productId);
    }

    @Override
    public void showProductDetail(final Product product) {
        this.product = product;
        productNameTextView.setText(product.getProductName());
        unitPriceTextView.setText(getResources().getString(R.string.unit_price, CurrencyUtils.formatAsCurrency(product.getMrp())));
        plusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = Integer.parseInt(quantityTextView.getText().toString());
                quantity++;
                productDetailsPresenter.addOrUpdateProduct(product, quantity);
                updateQuantityTextView();
                updateActionBarProductCount();
            }
        });
        minusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = Integer.parseInt(quantityTextView.getText().toString());
                quantity--;
                if (quantity > 0) {
                    productDetailsPresenter.addOrUpdateProduct(product, quantity);
                } else if (quantity == 0) {
                    try {
                        productDetailsPresenter.removeProduct(product.getProductId());
                    } catch (ProductNotInCartException e) {
                        throw new IllegalStateException("Not possible.");
                    }
                }
                updateQuantityTextView();
                updateActionBarProductCount();
            }
        });

        productImageView.loadBitmap(product.getProductImageName());
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductImageDialog();
            }
        });
        updateQuantityTextView();
    }

    private void updateQuantityTextView() {
        quantityTextView.setText(String.valueOf(productDetailsPresenter.getShoppedQuantity(product.getProductId())));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateQuantityTextView();
        updateActionBarProductCount();
    }

    private void updateActionBarProductCount() {
        if (actionBarProductCountTextView == null) {
            return;
        }
        actionBarProductCountTextView.setText(String.valueOf(productDetailsPresenter.getNumberOfProductsInCart()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        View view = menu.findItem(R.id.action_cart).getActionView();
        View cartView = view.findViewById(R.id.layout_cart_container);
        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberOfProducts = productDetailsPresenter.getNumberOfProductsInCart();
                if (numberOfProducts > 0) {
                    showCartActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "No Products in Cart", Toast.LENGTH_LONG).show();
                }
            }
        });
        actionBarProductCountTextView = (TextView) view.findViewById(R.id.actionBarProductCountTextView);
        updateActionBarProductCount();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        productDetailsPresenter.detachView();
    }

    @Override
    protected boolean isDrawerIndicatorDisabled() {
        return true;
    }


    private void showProductImageDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_image);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        CustomImageView productImageView = (CustomImageView) dialog.findViewById(R.id.productImageView);
        productImageView.loadBitmap(product.getProductImageName());
    }
}
