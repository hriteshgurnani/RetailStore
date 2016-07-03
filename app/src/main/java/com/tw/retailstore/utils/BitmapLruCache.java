package com.tw.retailstore.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * LRU Cache to hold Bitmaps of Product Images.
 * Created by hritesh on 30/06/16.
 */

class BitmapLruCache {

    private final LruCache<String, Bitmap> mMemoryCache;

    private static BitmapLruCache instance;

    public static synchronized BitmapLruCache getInstance() {
        if (instance == null) {
            instance = new BitmapLruCache();
        }
        return instance;
    }

    private BitmapLruCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
