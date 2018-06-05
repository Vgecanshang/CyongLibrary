package com.cy.cylibrary.img.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;


import java.io.File;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created on : June 18, 2016
 * Author     : zetbaitsu
 * Name       : Zetra
 * Email      : zetra@mail.ugm.ac.id
 * GitHub     : https://github.com/zetbaitsu
 * LinkedIn   : https://id.linkedin.com/in/zetbaitsu
 *
 //                File compressedImage = new Compressor.Builder(mActivity)
 //                        .setMaxWidth(1024)
 //                        .setMaxHeight(1024)
 //                        .setQuality(40)
 //                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
 //                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
 //                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
 //                        .build()
 //                        .compressToFile(new File(tempList.get(i).getPath()));
 //
 //
 */
public class Compressor {
    private static volatile Compressor INSTANCE;
    private Context context;
    //max width and height values of the compressed image is taken as 612x816
    private float maxWidth = 1024.0f;
    private float maxHeight = 1024.0f;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;
    private int quality = 80;
    private String destinationDirectoryPath;
    private String fileNamePrefix;
    private String fileName;

    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath() + "//";

    private static String fileCachePath = SD_PATH+"filesCache";


    private Compressor(Context context) {
        this.context = context;
//        destinationDirectoryPath = JYFileUtil.splitPath(PathUtil.IMG_CACHE);
        destinationDirectoryPath = fileCachePath;
    }

    public static void setFileCachePath (String cacheFileName){
        fileCachePath = SD_PATH + cacheFileName ;
    }


    public static Compressor getDefault(Context context) {
        if (INSTANCE == null) {
            synchronized (Compressor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Compressor(context);
                }
            }
        }
        return INSTANCE;
    }

    public File compressToFile(File file) {
        return ImageUtil.compressImage(context, Uri.fromFile(file), maxWidth, maxHeight,
            compressFormat, bitmapConfig, quality, destinationDirectoryPath,
            fileNamePrefix, fileName);
    }

    public Bitmap compressToBitmap(File file) {
        return ImageUtil.getScaledBitmap(context, Uri.fromFile(file), maxWidth, maxHeight, bitmapConfig);
    }

    public Observable<File> compressToFileAsObservable(final File file) {
        return Observable.defer(new Func0<Observable<File>>() {
            @Override
            public Observable<File> call() {
                return Observable.just(compressToFile(file));
            }
        });
    }

    public Observable<Bitmap> compressToBitmapAsObservable(final File file) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                return Observable.just(compressToBitmap(file));
            }
        });
    }

    public static class Builder {
        private Compressor compressor;

        public Builder(Context context) {
            compressor = new Compressor(context);
        }

        public Builder setMaxWidth(float maxWidth) {
            compressor.maxWidth = maxWidth;
            return this;
        }

        public Builder setMaxHeight(float maxHeight) {
            compressor.maxHeight = maxHeight;
            return this;
        }

        public Builder setCompressFormat(Bitmap.CompressFormat compressFormat) {
            compressor.compressFormat = compressFormat;
            return this;
        }

        public Builder setBitmapConfig(Bitmap.Config bitmapConfig) {
            compressor.bitmapConfig = bitmapConfig;
            return this;
        }

        public Builder setQuality(int quality) {
            compressor.quality = quality;
            return this;
        }

        public Builder setDestinationDirectoryPath(String destinationDirectoryPath) {
            compressor.destinationDirectoryPath = destinationDirectoryPath;
            return this;
        }

        public Builder setFileNamePrefix(String prefix) {
            compressor.fileNamePrefix = prefix;
            return this;
        }

        public Builder setFileName(String fileName) {
            compressor.fileName = fileName;
            return this;
        }

        public Compressor build() {
            return compressor;
        }
    }
}
