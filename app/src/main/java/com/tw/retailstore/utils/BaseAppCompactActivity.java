package com.tw.retailstore.utils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.tw.retailstore.R;
import com.tw.retailstore.ui.cart.CartMainActivity;

/**
 * Base Activity for Retail Store App.
 * Created by hritesh on 29/06/16.
 */

public abstract class BaseAppCompactActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(R.layout.activity_navigation_drawer, null);
        FrameLayout contentLayout = (FrameLayout) view.findViewById(R.id.contentView);
        getLayoutInflater().inflate(layoutResID, contentLayout, true);
        super.setContentView(view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isDrawerIndicatorDisabled());
        getSupportActionBar().setDisplayShowHomeEnabled(isDrawerIndicatorDisabled());
    }

    protected abstract boolean isDrawerIndicatorDisabled();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showCartActivity() {
        Intent intent = new Intent(this, CartMainActivity.class);
        startActivity(intent);
    }


    protected void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
