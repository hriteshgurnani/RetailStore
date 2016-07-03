package com.tw.retailstore.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.tw.retailstore.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Async Task to load Bitmap of a Product Image from Assets and load it to an Image View.
 * Adds this Bitmap to LRUCache on successful loading.
 * Created by hritesh on 30/06/16.
 */

class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private final AssetManager assets;
    private String imageName;

    BitmapWorkerTask(ImageView imageView, AssetManager assets) {
        this.imageViewReference = new WeakReference<>(imageView);
        this.assets = assets;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        this.imageName = params[0];
        Bitmap bitmap = null;
        try {
            InputStream inputStream = getInputStreamFromAssets();
            try {
                bitmap = BitmapFactory.decodeStream(inputStream);
                BitmapLruCache.getInstance().addBitmapToMemoryCache(imageName, bitmap);
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private InputStream getInputStreamFromAssets() throws IOException {
        return assets.open(imageName);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.drawable.noimage);
                }
            }
        }
    }

    String getImageName() {
        return imageName;
    }
}