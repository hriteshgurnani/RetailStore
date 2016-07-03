package com.tw.retailstore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.tw.retailstore.businesslogic.BusinessFactory;
import com.tw.retailstore.businesslogic.IProductsBusiness;
import com.tw.retailstore.ui.products.ProductsListActivity;

/**
 * Main Launcher Activity.
 * Initialises the Application by inserting Dummy Products to Local Database if empty
 * and Navigates to {@link ProductsListActivity} on completion.
 * Created by hritesh on 30/06/16.
 */

public class SplashActivity extends Activity {

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        insertDummyProductsIfEmpty();
    }

    private void insertDummyProductsIfEmpty() {
        final IProductsBusiness productsBusiness = BusinessFactory.getInstance().getProductsBusiness();
        try {
            productsBusiness.getProductsList();
            showProductListActivity();
        } catch (IllegalStateException e) {
            showBusyDialog("Initialising");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    productsBusiness.insertDummyProducts(new IProductsBusiness.IProductInsertCallback() {
                        @Override
                        public void onCompletion() {
                            onDummyProductsInsertComplete();
                        }
                    });
                }
            }, 5000);
        }
    }

    private void showBusyDialog(String message) {
        if (dialog != null) {
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.layout_dialog, null);
        TextView messageTextView = (TextView) view.findViewById(R.id.messageTextView);
        if (message != null) {
            messageTextView.setText(message);
        } else {
            messageTextView.setText(null);
        }
        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void dismissBusyDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void onDummyProductsInsertComplete() {
        dismissBusyDialog();
        showProductListActivity();
    }

    private void showProductListActivity() {
        Intent intent = new Intent(this, ProductsListActivity.class);
        startActivity(intent);
        finish();
    }
}
