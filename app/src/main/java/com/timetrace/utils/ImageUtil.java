package com.timetrace.utils;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.timetrace.monitor.Monitor;

import java.io.File;

/**
 * Created by Atomu on 2014/11/22.
 */
public class ImageUtil extends BaseUtil {
    private static ImageLoader imageLoader;
    private static ImageLoaderConfiguration configuration;

    public static void init() {
        ImageUtil.monitor = Monitor.getApplication();

        configuration = new ImageLoaderConfiguration.Builder(monitor)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiscCache(new File(AppUtil.getCacheDir())))
                .build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    /**
     * 涨姿势了, 发现的一篇计算采样频率来减小bitmap内存分配的帖子, 频率计算如下
     */
    public static int computeSampleSize(int resId, int minSideLength, int maxNumofPixels) {
        int initialSize = computeInitialSampleSize(resId, minSideLength, maxNumofPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    public static int computeSampleSize(String pathName, int minSideLength, int maxNumofPixels) {
        int initialSize = computeInitialSampleSize(pathName, minSideLength, maxNumofPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(int resId, int minSideLength, int maxNumofPixels) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(monitor.getResources(), resId, options);

        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumofPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumofPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumofPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    private static int computeInitialSampleSize(String pathName, int minSideLength, int maxNumofPixels) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumofPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumofPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumofPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
