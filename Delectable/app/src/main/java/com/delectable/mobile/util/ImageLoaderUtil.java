package com.delectable.mobile.util;

import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageLoaderUtil {

    private static Picasso sPicasso = null;

    public static void loadImageIntoView(Context context, int imageResource, ImageView imageView) {
        if (sPicasso == null) {
            // Use custom Downloader to handle 302 redirected images
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttpDownloader(context.getApplicationContext()));
            sPicasso = builder.build();
        }

        if (imageView instanceof CircleImageView) {
            sPicasso.with(context).load(imageResource)
                    .into(((CircleImageView) imageView).getPicassoTarget());
        } else {
            sPicasso.with(context).load(imageResource).into(imageView);
        }
    }

    public static void loadImageIntoView(Context context, String imageUrl, ImageView imageView) {
        if (sPicasso == null) {
            // Use custom Downloader to handle 302 redirected images
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttpDownloader(context.getApplicationContext()));
            sPicasso = builder.build();
        }
        // Don't load image if imageUrl is null or ""
        if (imageUrl == null || imageUrl == "") {
            // TODO: Reset imageView to a default image?
            return;
        }

        if (imageView instanceof CircleImageView) {
            sPicasso.with(context).load(imageUrl)
                    .into(((CircleImageView) imageView).getPicassoTarget());
        } else {
            sPicasso.with(context).load(imageUrl).into(imageView);
        }
    }

    public static void loadBlurredImageIntoView(Context context, String imageUrl,
            ImageView imageView, final int blurRadius) {
        if (sPicasso == null) {
            // Use custom Downloader to handle 302 redirected images
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttpDownloader(context.getApplicationContext()));
            sPicasso = builder.build();
        }
        // Don't load image if imageUrl is null or ""
        if (imageUrl == null || imageUrl == "") {
            // TODO: Reset imageView to a default image?
            return;
        }

        Transformation blurTransformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap result = CameraUtil.blurImage(source, blurRadius);
                if (result != source) {
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "blur()";
            }
        };

        if (imageView instanceof CircleImageView) {
            sPicasso.with(context)
                    .load(imageUrl)
                    .transform(blurTransformation)
                    .into(((CircleImageView) imageView).getPicassoTarget());
        } else {
            sPicasso.with(context)
                    .load(imageUrl)
                    .transform(blurTransformation)
                    .into(imageView);
        }
    }
}
