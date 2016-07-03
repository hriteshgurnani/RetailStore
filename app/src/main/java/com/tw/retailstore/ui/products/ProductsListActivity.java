package com.tw.retailstore.ui.products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tw.retailstore.R;
import com.tw.retailstore.businesslogic.BusinessFactory;
import com.tw.retailstore.businesslogic.ICartBusiness;
import com.tw.retailstore.businesslogic.IProductsBusiness;
import com.tw.retailstore.models.Product;
import com.tw.retailstore.utils.BaseAppCompactActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Activity that displays List of Products in a Retails Store to the User.
 * Allows Navigation to Product Details on Selecting a Product and Navigation to Cart if it exists.
 * Created by hritesh on 29/06/16.
 */

public class ProductsListActivity extends BaseAppCompactActivity implements ProductListViewContract.ProductView {

    private static final String LOG_TAG = ProductsListActivity.class.getSimpleName();

    @Bind(R.id.productExpandableListView)
    ExpandableListView productExpandableListView;

    private ProductListAdapter productExpandableListAdapter;

    private ProductsListPresenter productsPresenter;

    private TextView actionBarProductCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setActionBarTitle("Retail Store");
        ButterKnife.bind(this);
        initialiseViews();
        IProductsBusiness productsFetcher = BusinessFactory.getInstance().getProductsBusiness();
        ICartBusiness cartBusiness = BusinessFactory.getInstance().getCartBusiness();
        productsPresenter = new ProductsListPresenter(productsFetcher, cartBusiness);
    }

    private void initialiseViews() {
        productExpandableListAdapter = new ProductListAdapter(LayoutInflater.from(this), this);
        productExpandableListView.setAdapter(productExpandableListAdapter);
        expandGroups();
    }

    private void fetchProducts() {
        productsPresenter.getAllProducts();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        productsPresenter.attachView(this);
        fetchProducts();
    }

    @Override
    public void showProducts(List<Product> productList) {
        Log.d(LOG_TAG, "showProducts() productList " + productList);
        List<ProductsBasedOnCategory> productsBasedOnCategoryList = formProductListBasedOnCategory(productList);
        Log.d(LOG_TAG, "showProducts() productsBasedOnCategoryList " + productsBasedOnCategoryList);
        productExpandableListAdapter.notifyDataSetChanged(productsBasedOnCategoryList);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                expandGroups();
            }
        });
    }

    private void expandGroups() {
        for (int i = 0; i < productExpandableListAdapter.getGroupCount(); i++) {
            productExpandableListView.expandGroup(i);
        }
    }

    private List<ProductsBasedOnCategory> formProductListBasedOnCategory(List<Product> productList) {
        Map<String, ProductsBasedOnCategory> productsBasedOnCategoryMap = new HashMap<>();
        for (Product product : productList) {
            ProductsBasedOnCategory productsBasedOnCategory;
            String productCategory = product.getProductCategory();
            if (productsBasedOnCategoryMap.containsKey(productCategory)) {
                productsBasedOnCategory = productsBasedOnCategoryMap.get(productCategory);
            } else {
                productsBasedOnCategory = new ProductsBasedOnCategory(productCategory);
            }
            productsBasedOnCategory.addProduct(product);
            productsBasedOnCategoryMap.put(productCategory, productsBasedOnCategory);
        }
        return new ArrayList<>(productsBasedOnCategoryMap.values());
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume()");
        super.onResume();
        updateActionBarProductCount();
    }

    private void updateActionBarProductCount() {
        if (actionBarProductCountTextView == null) {
            return;
        }
        actionBarProductCountTextView.setText(String.valueOf(productsPresenter.getNumberOfProductsInCart()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu()");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        View view = menu.findItem(R.id.action_cart).getActionView();
        View cartView = view.findViewById(R.id.layout_cart_container);
        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberOfProducts = productsPresenter.getNumberOfProductsInCart();
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
    protected boolean isDrawerIndicatorDisabled() {
        return false;
    }

    private boolean expandOrCollapseGroup(final int groupPosition) {
        boolean isExpanded = productExpandableListView.isGroupExpanded(groupPosition);
        if (isExpanded) {
            productExpandableListView.collapseGroup(groupPosition);
        } else {
            productExpandableListView.post(new Runnable() {

                @Override
                public void run() {
                    productExpandableListView.setSelectedGroup(groupPosition);
                }
            });
            productExpandableListView.expandGroup(groupPosition);
        }
        return isExpanded;
    }

    class ProductListAdapter extends BaseExpandableListAdapter {

        private final LayoutInflater layoutInflater;
        private final Context context;
        private final List<ProductsBasedOnCategory> productsBasedOnCategoryList = new ArrayList<>();

        private ProductListAdapter(LayoutInflater layoutInflater, Context context) {
            this.layoutInflater = layoutInflater;
            this.context = context;
        }

        @Override
        public int getGroupCount() {
            return productsBasedOnCategoryList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return productsBasedOnCategoryList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return productsBasedOnCategoryList.get(groupPosition).getProductAtPosition(childPosition);
        }

        private List<Product> getChildren(int groupPosition) {
            return productsBasedOnCategoryList.get(groupPosition).getProductList();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @SuppressWarnings("InflateParams")
        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = convertView;
            final GroupViewHolder viewHolder;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.layout_product_list_group_row, null);
                viewHolder = new GroupViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (GroupViewHolder) view.getTag();
            }
            ProductsBasedOnCategory productsBasedOnCategory = (ProductsBasedOnCategory) getGroup(groupPosition);
            viewHolder.categoryNameTextView.setText(productsBasedOnCategory.getCategoryName());

            final ImageView collapseImageView = viewHolder.collapseImageView;
            setImage(collapseImageView, isExpanded);
            collapseImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    boolean wasExpanded = expandOrCollapseGroup(groupPosition);
                    setImage(collapseImageView, !wasExpanded);
                }
            });
            return view;
        }

        private void setImage(ImageView imageView, boolean isExpanded) {
            if (isExpanded) {
                imageView.setImageResource(R.drawable.collapse_all_icon);
            } else {
                imageView.setImageResource(R.drawable.expand_all_icon);
            }
        }

        @SuppressWarnings("InflateParams")
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.layout_product_list_child_row, null);
            }
            GridView gridView = (GridView) view.findViewById(R.id.productsGridView);

            ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
            int numberOfColumns = context.getResources().getInteger(R.integer.numberOfColumnsInGrid);
            int childCount = getChildrenCount(groupPosition);
            int numOfFilledRows = childCount / numberOfColumns;
            int numOfUnfilledRows = childCount % numberOfColumns;

            int factor = numOfFilledRows == 0 ? 1 : (numOfUnfilledRows == 0 ? numOfFilledRows : numOfFilledRows + 1);
            float height = context.getResources().getDimension(R.dimen.gridProductHeight);
            float value = (height + 10) * (factor);
            layoutParams.height = Math.round(value);
            gridView.setLayoutParams(layoutParams);
            gridView.setAdapter(new ProductGridAdapter(this.context, this.layoutInflater, getChildren(groupPosition)));
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        void notifyDataSetChanged(List<ProductsBasedOnCategory> productsBasedOnCategoryList) {
            this.productsBasedOnCategoryList.clear();
            this.productsBasedOnCategoryList.addAll(productsBasedOnCategoryList);
            super.notifyDataSetChanged();
        }

        class GroupViewHolder {
            @Bind(R.id.productCategoryTextView)
            TextView categoryNameTextView;
            @Bind(R.id.collapseImageView)
            ImageView collapseImageView;
            GroupViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    static class ProductGridAdapter extends BaseAdapter {

        private final List<Product> productList;
        private final LayoutInflater layoutInflater;
        private final Context context;

        ProductGridAdapter(Context context, LayoutInflater layoutInflater, List<Product> productList) {
            this.context = context;
            this.layoutInflater = layoutInflater;
            this.productList = productList;
            Log.d(LOG_TAG, "ProductGridAdapter() productList " + productList.size());
        }

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Product getItem(int position) {
            return productList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            View view = convertView;
            final ViewHolder viewHolder;
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.layout_product_grid_child, null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            final Product product = getItem(position);
            viewHolder.productNameTextView.setText(product.getProductName());
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    showProductDetailActivity(product);
                }
            });
            return view;
        }


        private void showProductDetailActivity(Product product) {
            Intent intent = new Intent(this.context, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_DETAIL, product.getProductId());
            context.startActivity(intent);
        }

        static class ViewHolder {
            @Bind(R.id.productNameTextView)
            TextView productNameTextView;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }

    private static class ProductsBasedOnCategory {
        private final String categoryName;
        private List<Product> productList;

        ProductsBasedOnCategory(String productCategory) {
            if (productCategory == null) {
                throw new IllegalArgumentException("productCategory cannot be null");
            }
            this.categoryName = productCategory;
        }

        void addProduct(Product product) {
            if (productList == null) {
                productList = new ArrayList<>();
            }
            productList.add(product);
        }

        List<Product> getProductList() {
            return productList;
        }

        public String getCategoryName() {
            return categoryName;
        }

        Product getProductAtPosition(int index) {
            return productList.get(index);
        }

        @Override
        public String toString() {
            return "ProductsBasedOnCategory{" +
                    "categoryName='" + categoryName + '\'' +
                    ", productList=" + productList +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ProductsBasedOnCategory that = (ProductsBasedOnCategory) o;

            return categoryName.equals(that.categoryName);

        }

        @Override
        public int hashCode() {
            return categoryName.hashCode();
        }
    }
}
