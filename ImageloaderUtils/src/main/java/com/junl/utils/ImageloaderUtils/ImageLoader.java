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
    ImageCache mImageCache = new ImageCache();
    //SD 卡缓存
    DiskCache mDiskcache = new DiskCache();
    // 双缓存
    DoubleCache mDoubleCache = new DoubleCache();
    //是否使用 sd 卡缓存
    boolean isUseDiskCache = false;
    // 使用双缓存
    boolean isUseDoubleCache = false;
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

    public void displayImage(final String url, final ImageView imageView) {
        Bitmap bitmap = null;
        if (isUseDoubleCache) {
            bitmap = mDoubleCache.get(url);
        } else if (isUseDiskCache) {
            bitmap = mDiskcache.get(url);
        } else {
            bitmap = mImageCache.get(url);
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    updateImageView(imageView, bitmap);
                }
                mImageCache.put(url, bitmap);
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

    public void useDiskCache(boolean useDiskCache) {
        isUseDiskCache = useDiskCache;
    }

    public void useDoubleCache(boolean useDoubleCache) {
        isUseDoubleCache = useDoubleCache;
    }
}
