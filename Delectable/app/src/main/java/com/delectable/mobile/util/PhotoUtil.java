package com.delectable.mobile.util;

import com.delectable.mobile.App;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PhotoUtil {

    private static final String TAG = PhotoUtil.class.getSimpleName();

    public static Bitmap loadBitmapFromUri(Uri imageUri, int maxSize)
            throws Exception {

        // Load Sampled/Resized Bitmap (Prevents out of memory errors)
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(App.getInstance().getContentResolver().openInputStream(imageUri),
                null, options);
        options.inSampleSize = PhotoUtil.calculateInSampleSize(options, maxSize, maxSize);
        options.inJustDecodeBounds = false;

        // Matrix to Rotate Bitmap based on Exif data
        Matrix matrix = new Matrix();
        int fixedRotation = getExifRotationInDegrees(imageUri);

        if (fixedRotation != 0) {
            matrix.preRotate(fixedRotation);
        }

        Bitmap originalBitmap = BitmapFactory.decodeStream(
                App.getInstance().getContentResolver().openInputStream(imageUri), null, options);

        Bitmap adjustedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(),
                originalBitmap.getHeight(), matrix, true);

        return adjustedBitmap;
    }

    public static int getExifRotationInDegrees(Uri imageUri)
            throws Exception {
        int exifRotation = 0;

        InputStream imageIs = null;

        try {
            imageIs = App.getInstance().getContentResolver().openInputStream(imageUri);
            BufferedInputStream imageBis = new BufferedInputStream(imageIs);
            Metadata metadata = ImageMetadataReader.readMetadata(imageBis, false);

            ExifIFD0Directory exifIFD0Directory = metadata.getDirectory(ExifIFD0Directory.class);

            if (exifIFD0Directory != null && exifIFD0Directory
                    .containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                exifRotation = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (imageIs != null) {
                    imageIs.close();
                }
            } catch (IOException io) {
                // no-op
            }
        }

        if (exifRotation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        }
        if (exifRotation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        }
        if (exifRotation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    /**
     * Helper to calculate sample size based on the max width / height From:
     * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html#load-bitmap
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
