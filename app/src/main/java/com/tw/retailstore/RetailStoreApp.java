package com.tw.retailstore;

import android.app.Application;

/**
 * Application Class which initialises Business and Storage Factories.
 * Created by hritesh on 29/06/16.
 */

public class RetailStoreApp extends Application {

    private static RetailStoreApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static RetailStoreApp getInstance() {
        return sInstance;
    }
}
