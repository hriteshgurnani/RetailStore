package com.tw.retailstore.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Image View which displays Bitmaps from Assets.
 * Used for Product Image Display.
 * Created by hritesh on 29/06/16.
 */

public class CustomImageView extends ImageView {

    private WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadBitmap(String imageName) {
        final Bitmap bitmap = BitmapLruCache.getInstance().getBitmapFromMemCache(imageName);
        if (bitmap != null) {
            setImageBitmap(bitmap);
        } else {
            if (cancelPotentialWork(imageName)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(this, getContext().getAssets());
                bitmapWorkerTaskReference = new WeakReference<>(task);
                task.execute(imageName);
            }
        }
    }

    private boolean cancelPotentialWork(String imageName) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask();
        if (bitmapWorkerTask != null) {
            String currentImageName = bitmapWorkerTask.getImageName();
            if (currentImageName == null || !currentImageName.equals(imageName)) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    private BitmapWorkerTask getBitmapWorkerTask() {
        if (bitmapWorkerTaskReference != null) {
            return bitmapWorkerTaskReference.get();
        }
        return null;
    }
}
