package com.cy.cylibrary.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cy.cylibrary.R;
import com.cy.cylibrary.utils.DisplayUtil;
import com.cy.cylibrary.view.TextDrawable;

import java.util.HashMap;
import java.util.Random;

/**
 * 图片加载工具类
 */
public class ImageLoader{
    private static String[] colors = new String[]{"#80deea", "#ff9776", "#b6e3f7", "#a5d6a7"};
    private static HashMap<String, String> noneUserIcon = new HashMap<>();//用户无头像时候的颜色映射
    private static String httpHeaderRefererUrl = null;//文件域名头。例如："http://www.91zhanghu.com"


    /*头像大小-大头像(50X50)*/
    public static final int ICON_SIZE_BIG = 0;
    /*头像大小-普通头像(40X40)*/
    public static final int ICON_SIZE_NORMAL = 1;
    /*头像大小-小头像(30X30)*/
    public static final int ICON_SIZE_SMALL = 2;

    /**
     * 设置图片文件域名
     * @param url
     */
    public static void setHttpHeaderReferUrl(String url){
        ImageLoader.httpHeaderRefererUrl = url;
    }




    /**
     * 加载用户头像
     */
    public static void showUserIcon(Context context , ImageView imageView, String url , int defaultImgRes) {
        showUserIcon(context, imageView, url, "", 0 , defaultImgRes);
    }

    public static void showUserIcon(Context context, ImageView imageView, String url, String userName, int imageType ,int defaultImgRes) {

        if(defaultImgRes <= 0){
            defaultImgRes = R.drawable.icon_default_header;
        }


        if (TextUtils.isEmpty(url)) {
            if (TextUtils.isEmpty(userName)) {
                imageView.setImageResource(defaultImgRes);
                return;
            }

            String colorBg = "#EF9A9A";
            if (userName.length() > 2) {
                userName = userName.substring(userName.length() - 2, userName.length());
            }
            if (noneUserIcon.containsKey(userName)) {
                colorBg = noneUserIcon.get(userName);
            } else {
                int index = new Random().nextInt(4);
                colorBg = colors[index];
                noneUserIcon.put(userName, colorBg);
            }

            int fontSize = 12;
            if (imageType == ICON_SIZE_BIG) {
                fontSize = 17;
            } else if (imageType == ICON_SIZE_SMALL) {
                fontSize = 8;
            } else {
                fontSize = 12;
            }

            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .fontSize(DisplayUtil.sp2px(context , fontSize))
//                    .withBorder(5) //边框
                    .textColor(Color.WHITE)
                    .endConfig()
                    .buildRound(userName, Color.parseColor(colorBg));
            imageView.setImageDrawable(drawable);

        } else {
            String urlAppend = "";
            ImageLoaderOptions  options =  new ImageLoaderOptions.Builder().transformationShape(new GlideCircleTransform(imageView.getContext())).placeHolder(defaultImgRes).errorDrawable(defaultImgRes).build();
            if(url.startsWith("http")||url.startsWith("www")){
                urlAppend += "?x-oss-process=image/resize,m_mfit,h_200,w_200";//将图自动裁剪成宽度为200，高度为200的效果图
                GlideUrl glideUrl = new GlideUrl(url+urlAppend,new GlideHeader(httpHeaderRefererUrl));
                DrawableTypeRequest dtr = Glide.with(imageView.getContext()).load(glideUrl);
                loadOptions(dtr, options).into(imageView);
            }else{
                //装配基本的参数
                DrawableTypeRequest dtr = Glide.with(imageView.getContext()).load(url);
                //装配附加参数
                loadOptions(dtr, options).into(imageView);
            }
        }
    }

    public static void showImage(ImageView imageView, String url, ImageLoaderOptions options , int defaultImgRes , int errorImgRes) {

        if(defaultImgRes <= 0){
            defaultImgRes = R.drawable.icon_normal_default;
        }

        if(errorImgRes <= 0){
            errorImgRes = R.drawable.icon_normal_error;
        }


        String urlAppend = "";
        if(url.startsWith("http")||url.startsWith("www")){
            urlAppend += "?x-oss-process=image/resize,m_mfit,h_200,w_200";//将图自动裁剪成宽度为200，高度为200的效果图
//            if (url.contains("-scale")) {
//            } else {
//                urlAppend += "?x-oss-process=image/indexcrop,y_375,i_0";//裁剪图左上角200x200的大小
//            }
            GlideUrl glideUrl = new GlideUrl(url+urlAppend,new GlideHeader(httpHeaderRefererUrl));
            DrawableTypeRequest dtr = Glide.with(imageView.getContext()).load(glideUrl);
            if(options==null){
                options = new ImageLoaderOptions.Builder().placeHolder(defaultImgRes).errorDrawable(errorImgRes).build();
            }
            loadOptions(dtr, options).into(imageView);
        }else{
            //装配基本的参数
            DrawableTypeRequest dtr = Glide.with(imageView.getContext()).load(url);
            //装配附加参数
            if(options==null){
                options = new ImageLoaderOptions.Builder().placeHolder(defaultImgRes).errorDrawable(errorImgRes).build();
            }
            loadOptions(dtr, options).into(imageView);
        }
    }

    public static void showBigImage(ImageView imageView, String url, ImageLoaderOptions options, RequestListener listener , int errorImgRes) {

        if(errorImgRes <= 0){
            errorImgRes = R.drawable.icon_normal_error;
        }

        //装配基本的参数
        GlideUrl glideUrl = new GlideUrl(url,new GlideHeader(httpHeaderRefererUrl));
        DrawableTypeRequest dtr = Glide.with(imageView.getContext()).load(glideUrl);
        //装配附加参数
        if(options==null){
            options = new ImageLoaderOptions.Builder().errorDrawable(errorImgRes).build();
        }
        String urlAppend = "";
        urlAppend += "?x-oss-process=image/resize,m_mfit,h_200,w_200";//将图自动裁剪成宽度为200，高度为200的效果图
//        if (url.contains("-scale")) {
//        } else {
//            urlAppend += "?x-oss-process=image/indexcrop,y_375,i_0";//裁剪图左上角200x200的大小
//        }
        GlideUrl thumbnailUrl = new GlideUrl(url + urlAppend, new GlideHeader(httpHeaderRefererUrl));
        DrawableTypeRequest thumbnailUrlReq = Glide.with(imageView.getContext()).load(thumbnailUrl);

        loadOptions(dtr, options).thumbnail(thumbnailUrlReq).listener(listener).dontAnimate().into(imageView);
    }

    public static void showSysImage(ImageView imageView, String url, ImageLoaderOptions options , int defaultImgRes , int errorImgRes) {


        if(defaultImgRes <= 0){
            defaultImgRes = R.drawable.icon_normal_default;
        }

        if(errorImgRes <= 0){
            errorImgRes = R.drawable.icon_normal_error;
        }

        //装配基本的参数
        String urlAppend = "?x-oss-process=image/resize,m_mfit,h_250,w_500";//将图自动裁剪成宽度为200，高度为200的效果图
        GlideUrl glideUrl = new GlideUrl(url+urlAppend,new GlideHeader(httpHeaderRefererUrl));
        DrawableTypeRequest dtr = Glide.with(imageView.getContext()).load(glideUrl);
        //装配附加参数
        if(options==null){
            options = new ImageLoaderOptions.Builder().placeHolder(defaultImgRes).errorDrawable(errorImgRes).build();
        }
        loadOptions(dtr, options).into(imageView);
    }

    public static void showImage(ImageView imageView, int drawable, ImageLoaderOptions options) {
        DrawableTypeRequest dtr = Glide.with(imageView.getContext()).load(drawable);
        loadOptions(dtr, options).into(imageView);
    }

    public static Bitmap getBitmapForUrl(Context context, String url) throws Exception {
        GlideUrl glideUrl = new GlideUrl(url,new GlideHeader(httpHeaderRefererUrl));
        Bitmap bitmap = Glide.with(context)
                .load(glideUrl)
                .asBitmap()
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
        return bitmap;
    }

    //清理磁盘缓存,需要在子线程中执行
    public static void clearImageDiskCache(Context mContext) {
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存,可以在UI主线程中进行
    public static void clearImageMemory(Context mContext) {
        Glide.get(mContext).clearMemory();
    }

    //这个方法用来装载由外部设置的参数
    private static DrawableTypeRequest loadOptions(DrawableTypeRequest dtr, ImageLoaderOptions options) {
        if (options == null) {
            return dtr;
        }
        if (options.getPlaceHolder() != -1) {
            dtr.placeholder(options.getPlaceHolder());
        }
        if (options.getErrorDrawable() != -1) {
            dtr.error(options.getErrorDrawable());
        }
        if (options.isCrossFade()) {
            dtr.crossFade();
        }
        if (options.isSkipMemoryCache()) {
            dtr.skipMemoryCache(options.isSkipMemoryCache());
        }
        if (options.getAnimator() != null) {
            dtr.animate(options.getAnimator());
        }
        if (options.getSize() != null) {
            dtr.override(options.getSize().reWidth, options.getSize().reHeight);
        }
        if (options.getTransformation() != null) {
            dtr.transform(options.getTransformation());
        }
        if (options.isCenterCrop()) {
            dtr.centerCrop();
        }
        return dtr;
    }
}
