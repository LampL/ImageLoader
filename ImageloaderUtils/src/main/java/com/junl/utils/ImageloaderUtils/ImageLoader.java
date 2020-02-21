package com.junl.utils.ImageloaderUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    //内存缓存
    ImageCache mImageCache = new MemoryCache();
    //线程池，线程池数量为 CPU 的数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    //UI Handler
    Handler mUIHandler = new Handler(Looper.getMainLooper());

    private void updateImageView(final ImageView imageView, final Bitmap bitmap) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    //注入缓存实现
    public void setImageCache(ImageCache cache) {
        mImageCache = cache;
    }

    public void displayImage(String url, ImageView imageView) {
        Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

        submitLoadRequest(url, imageView);
    }

    private void submitLoadRequest(final String iamgeUrl, final ImageView imageView) {
        imageView.setTag(iamgeUrl);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(iamgeUrl);
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(iamgeUrl)) {
                    updateImageView(imageView, bitmap);
                }
                mImageCache.put(iamgeUrl, bitmap);
            }
        });
    }

    public Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
