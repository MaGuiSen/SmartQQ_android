package com.lib.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ma.qqmsg.R;

/**
 * Created by sunfusheng on 16/4/6.
 */
public class ImageUtil {
    //这里做测试
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    // 将资源ID转为Uri
    public static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

    // 加载网络图片
    public static void loadImage(Context context, String source, ImageView imageView) {
        loadImage(context, source, imageView, 0);
    }

    // 加载网络图片
    public static void loadImage(Context context, String source, ImageView imageView, int defaultResId) {
        try {
            Glide.with(context)
                    .load(source)
                    .placeholder(defaultResId)
                    .error(defaultResId)
                    .crossFade()
                    .centerCrop()
                    .into(imageView);
        } catch (Exception e) {

        }
    }

    // 加载drawable图片
    public static void loadResImage(Context context, int resId, ImageView imageView) {
        try {
            Glide.with(context)
                    .load(resourceIdToUri(context, resId))
                    .placeholder(resId)
                    .error(resId)
                    .crossFade()
                    .into(imageView);
        } catch (Exception e) {

        }
    }

//    // 加载本地图片
//    public static void loadLocalImage(Context context, String path, ImageView imageView) {
//        Log.e("path__",path+"");
//        Glide.with(context)
////                .load("file://" + path)//估计不需要file
//                .load(path+"")
//                .crossFade()
//                .into(imageView);
//    }
//
//    // 加载本地圆型图片
//    public static void loadCircleLocalImage(Context context, String path, ImageView imageView) {
//        Log.e("path__",path+"");
//        Glide.with(context)
//                .load("file://" + path)
//                .crossFade()
//                .transform(new GlideCircleTransform(context))
//                .into(imageView);
//    }

    // 加载drawable圆型图片
    public static void loadCircleResImage(Context context, int resId, ImageView imageView) {
        try {
            Glide.with(context)
                    .load(resourceIdToUri(context, resId))
                    .placeholder(resId)
                    .error(resId)
                    .crossFade()
                    .transform(new GlideCircleTransform(context))
                    .into(imageView);
        } catch (Exception e) {

        }
    }

    // 加载圆形网络图片
    public static void loadAvatarImage(Context context, String url, ImageView imageView) {
        loadCircleImage(context, url, imageView, R.mipmap.ic_head_default_me);
    }

    // 加载圆形网络图片
    public static void loadCircleImage(Context context, String url, ImageView imageView, int defaultResId) {
        try {
            Glide.with(context)
                    .load(url)
                    .placeholder(defaultResId)
                    .error(defaultResId)
                    .crossFade()
                    .transform(new GlideCircleTransform(context))
                    .into(imageView);
        } catch (Exception e) {

        }
    }

}
