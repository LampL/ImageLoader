package com.junl.utils.ImageloaderUtils;

import android.graphics.Bitmap;
import android.util.LruCache;

public interface ImageCache {
    //    // 图片 LRU 缓存
//    LruCache<String, Bitmap> mImageCache;
//
//    public ImageCache() {
//        initImageCache();
//    }
//
//    private void initImageCache() {
//        //计算可使用的最大内存
//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        //取四分之一的可用内存作为缓存
//        final int cacheSize = maxMemory / 4;
//
//        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, Bitmap bitmap) {
//                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
//            }
//        };
//    }
//
//    public void put(String url, Bitmap bitmap) {
//        mImageCache.put(url, bitmap);
//    }
//
//    public Bitmap get(String url) {
//        return mImageCache.get(url);
//    }
    public Bitmap get(String url);

    public void put(String url, Bitmap bitmap);
}
