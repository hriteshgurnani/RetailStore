package com.tw.retailstore.ui.cart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tw.retailstore.R;
import com.tw.retailstore.businesslogic.BusinessFactory;
import com.tw.retailstore.businesslogic.ICartBusiness;
import com.tw.retailstore.businesslogic.ICartInfo;
import com.tw.retailstore.businesslogic.ProductNotInCartException;
import com.tw.retailstore.models.CartProduct;
import com.tw.retailstore.ui.products.ProductDetailActivity;
import com.tw.retailstore.utils.BaseAppCompactActivity;
import com.tw.retailstore.utils.CurrencyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Activity that displays Cart to the User.
 * Created by hritesh on 29/06/16.
 */

    public class CartMainActivity extends BaseAppCompactActivity implements CartViewContract.CartView {

    @Bind(R.id.totalPriceTextView)
    TextView totalPriceTextView;
    @Bind(R.id.totalItemsTextView)
    TextView totalItemsTextView;
    @Bind(R.id.productsListView)
    RecyclerView productsListView;
    private CartPresenter cartPresenter;
    private CartProductsListRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_main);
        setActionBarTitle("Cart");
        ButterKnife.bind(this);
        ICartBusiness cartBusiness = BusinessFactory.getInstance().getCartBusiness();
        cartPresenter = new CartPresenter(cartBusiness);
    }

    private void initCartProducts() {
        ICartInfo cartInfo = cartPresenter.getCartInfo();
        updateCartLevelViews(cartInfo);

        adapter = new CartProductsListRecyclerAdapter(new ArrayList<>(cartInfo.getCartProducts()), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        productsListView.setLayoutManager(layoutManager);
        productsListView.setItemAnimator(new DefaultItemAnimator());
        productsListView.setAdapter(adapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        cartPresenter.attachView(this);
        initCartProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cartPresenter.detachView();
    }

    private void updateViews() {
        ICartInfo cartInfo = cartPresenter.getCartInfo();
        updateCartLevelViews(cartInfo);
        updateProductViews(cartInfo);
    }

    private void updateCartLevelViews(ICartInfo cartInfo) {
        totalPriceTextView.setText(getResources().getString(R.string.cart_value, CurrencyUtils.formatAsCurrency(cartInfo.getTotalPrice())));
        totalItemsTextView.setText(getResources().getString(R.string.number_of_items, cartInfo.getNumberOfProducts()));
    }

    private void updateProductViews(ICartInfo cartInfo) {
        adapter.notifyDataSetChanged(new ArrayList<>(cartInfo.getCartProducts()));
    }

    @Override
    protected boolean isDrawerIndicatorDisabled() {
        return true;
    }

    class CartProductsListRecyclerAdapter extends RecyclerView.Adapter<CartProductsListRecyclerAdapter.ViewHolder> {

        private final List<CartProduct> cartProductList;
        private final LayoutInflater layoutInflater;

        private CartProductsListRecyclerAdapter(List<CartProduct> cartProductList, Context context) {
            this.cartProductList = cartProductList;
            this.layoutInflater = LayoutInflater.from(context);
        }

        private void notifyDataSetChanged(List<CartProduct> cartProductList) {
            this.cartProductList.clear();
            this.cartProductList.addAll(cartProductList);
            notifyDataSetChanged();
        }

        private Object getItem(int position) {
            return cartProductList.get(position);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.layout_cart_product_row,
                    parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final CartProduct cartProduct = (CartProduct) getItem(position);
            holder.totalPriceTextView.setText(CurrencyUtils.formatAsNumber(cartProduct.getTotalMrpPrice()));
            final TextView quantityTextView = holder.quantityTextView;
            holder.quantityTextView.setText(String.valueOf(cartProduct.getCartQuantity()));
            holder.productNameTextView.setText(cartProduct.getProductName());
            holder.productNameTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            holder.productNameTextView.setSelected(true);
            holder.unitPriceTextView.setText(getResources().getString(R.string.unit, CurrencyUtils.formatAsCurrency(cartProduct.getMrp())));
            holder.unitPriceTextView.setSelected(true);
            holder.plusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer quantity = Integer.parseInt(quantityTextView.getText().toString());
                    quantity++;
                    updateProduct(cartProduct.getProductId(), quantity);
                }
            });
            holder.minusImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer quantity = Integer.parseInt(quantityTextView.getText().toString());
                    quantity--;
                    updateProduct(cartProduct.getProductId(), quantity);
                }
            });
            holder.productDetailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProductDetailActivity(cartProduct);
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return cartProductList.size();
        }

        private void updateProduct(int productId, int quantity) {
            if (quantity > 0) {
                try {
                    cartPresenter.updateProduct(productId, quantity);
                    updateViews();
                } catch (ProductNotInCartException e) {
                    throw new IllegalStateException("Not possible");
                }
            } else if (quantity == 0) {
                showConfirmationDialogToRemoveProduct(productId);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.productNameTextView)
            TextView productNameTextView;
            @Bind(R.id.unitPriceTextView)
            TextView unitPriceTextView;
            @Bind(R.id.quantityTextView)
            TextView quantityTextView;
            @Bind(R.id.totalPriceTextView)
            TextView totalPriceTextView;
            @Bind(R.id.minusImageView)
            ImageView minusImageView;
            @Bind(R.id.plusImageView)
            ImageView plusImageView;
            @Bind(R.id.productDetailView)
            View productDetailView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }


    private void showConfirmationDialogToRemoveProduct(final int productId) {
        int numberOfProducts = cartPresenter.getCartInfo().getNumberOfProducts();
        String title;
        DialogInterface.OnClickListener listener;
        if (numberOfProducts == 1) {
            title = "Are you sure you want to Delete this Product and Exit the Cart?";
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cartPresenter.deleteCart();
                    finish();
                }
            };
        } else {
            title = "Are you sure you want to Delete this Product?";
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        cartPresenter.removeProduct(productId);
                        updateViews();
                    } catch (ProductNotInCartException e) {
                        throw new IllegalStateException("Not possible.");
                    }
                }
            };
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private void showProductDetailActivity(CartProduct cartProduct) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_DETAIL, cartProduct.getProductId());
        startActivity(intent);
    }
}
