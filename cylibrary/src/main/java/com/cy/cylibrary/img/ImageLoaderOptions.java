package com.cy.cylibrary.img;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;


public class ImageLoaderOptions {
    private int placeHolder=-1; //当没有成功加载的时候显示的图片
    private ImageReSize size=null; //重新设定容器宽高
    private int errorDrawable=-1;  //加载错误的时候显示的drawable
    private boolean isCrossFade=false; //是否渐变平滑的显示图片
    private boolean isCenterCrop=false;
    private  boolean isSkipMemoryCache = false; //是否跳过内存缓存
    private   ViewPropertyAnimation.Animator animator = null; // 图片加载动画
    private BitmapTransformation transformation = null;


    private ImageLoaderOptions(ImageReSize resize, int placeHolder, int errorDrawable, boolean isCrossFade, boolean isSkipMemoryCache, ViewPropertyAnimation.Animator animator,BitmapTransformation transformation,boolean isCenterCrop){
        this.placeHolder=placeHolder;
        this.size=resize;
        this.errorDrawable=errorDrawable;
        this.isCrossFade=isCrossFade;
        this.isSkipMemoryCache=isSkipMemoryCache;
        this.animator=animator;
        this.transformation = transformation;
        this.isCenterCrop = isCenterCrop;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(int placeHolder) {
        this.placeHolder = placeHolder;
    }

    public ImageReSize getSize() {
        return size;
    }

    public void setSize(ImageReSize size) {
        this.size = size;
    }

    public int getErrorDrawable() {
        return errorDrawable;
    }

    public void setErrorDrawable(int errorDrawable) {
        this.errorDrawable = errorDrawable;
    }

    public boolean isCrossFade() {
        return isCrossFade;
    }

    public void setCrossFade(boolean crossFade) {
        isCrossFade = crossFade;
    }

    public boolean isSkipMemoryCache() {
        return isSkipMemoryCache;
    }

    public void setSkipMemoryCache(boolean skipMemoryCache) {
        isSkipMemoryCache = skipMemoryCache;
    }

    public ViewPropertyAnimation.Animator getAnimator() {
        return animator;
    }

    public void setAnimator(ViewPropertyAnimation.Animator animator) {
        this.animator = animator;
    }

    public BitmapTransformation getTransformation() {
        return transformation;
    }

    public void setTransformation(BitmapTransformation transformation) {
        this.transformation = transformation;
    }

    public boolean isCenterCrop() {
        return isCenterCrop;
    }

    public void setCenterCrop(boolean centerCrop) {
        isCenterCrop = centerCrop;
    }

    public static final class Builder {
        private int placeHolder = -1;
        private ImageReSize size = null;
        private int errorDrawable = -1;
        private boolean isCrossFade = false;
        private boolean isCenterCrop = false;
        private boolean isSkipMemoryCache = false;
        private ViewPropertyAnimation.Animator animator = null;
        private BitmapTransformation transformation = null;

        public Builder() {

        }

        public Builder placeHolder(int drawable) {
            this.placeHolder = drawable;
            return this;
        }

        public Builder reSize(ImageReSize size) {
            this.size = size;
            return this;
        }

        public Builder anmiator(ViewPropertyAnimation.Animator animator) {
            this.animator = animator;
            return this;
        }

        public Builder errorDrawable(int errorDrawable) {
            this.errorDrawable = errorDrawable;
            return this;
        }

        public Builder isCrossFade(boolean isCrossFade) {
            this.isCrossFade = isCrossFade;
            return this;
        }

        public Builder isCenterCrop(boolean isCenterCrop) {
            this.isCenterCrop = isCenterCrop;
            return this;
        }

        public Builder isSkipMemoryCache(boolean isSkipMemoryCache) {
            this.isSkipMemoryCache = isSkipMemoryCache;
            return this;
        }

        public Builder transformationShape(BitmapTransformation transformation){
            this.transformation = transformation;
            return this;
        }

        public ImageLoaderOptions build() {
            return new ImageLoaderOptions(this.size, this.placeHolder, this.errorDrawable, this.isCrossFade, this.isSkipMemoryCache, this.animator,this.transformation,this.isCenterCrop);
        }
    }
}
