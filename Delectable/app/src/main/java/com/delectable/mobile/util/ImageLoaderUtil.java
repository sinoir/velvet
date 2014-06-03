package com.delectable.mobile.util;

import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.widget.ImageView;

public class ImageLoaderUtil {

    private static Picasso sPicasso = null;

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
}
