package com.hgsoft.mylibrary.image;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 图片加载工具初始化，一般在application初始化
 */
public class ImageLoadUtil {

    public void init(Context context) {
        DisplayImageOptions.Builder defaultOptions = new DisplayImageOptions.Builder();
        defaultOptions.cacheInMemory(false);
        defaultOptions.cacheOnDisk(true);
//        defaultOptions.showImageOnLoading(R.mipmap.tupian_wu);
//        defaultOptions.showImageForEmptyUri(R.mipmap.tupian_wu);
//        defaultOptions.showImageOnFail(R.mipmap.tupian_wu);

        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.defaultDisplayImageOptions(defaultOptions.build());
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

}
