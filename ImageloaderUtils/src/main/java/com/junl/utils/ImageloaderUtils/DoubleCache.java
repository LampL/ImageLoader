package com.junl.utils.ImageloaderUtils;

import android.graphics.Bitmap;

/**
 * 双缓存，获取图片时先从内存缓存中获取图片，如果没有，再从 SD 卡中获取
 * 缓存图片也是在内存和sd卡中都缓存一份
 */
public class DoubleCache implements ImageCache{
    ImageCache mMemoryCache = new MemoryCache();
    ImageCache mDiskCache = new DiskCache();

    //先从内存缓存中获取图片，如果没有，再从 SD 卡中获取
    public Bitmap get(String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap == null) {
            bitmap = mDiskCache.get(url);
        }
        return bitmap;
    }

    //将图片缓存到内存和 SD 卡中
    public void put(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
        mDiskCache.put(url, bitmap);
    }
}
