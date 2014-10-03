package com.delectable.mobile.util;

import com.delectable.mobile.App;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class PhotoUtil {

    public static Bitmap loadBitmapFromUri(Uri imageUri) throws IOException {
        Matrix matrix = new Matrix();
        int fixedRotation = getExifRotationInDegrees(imageUri);

        if (fixedRotation != 0) {
            matrix.preRotate(fixedRotation);
        }

        Bitmap originalBitmap = MediaStore.Images.Media
                .getBitmap(App.getInstance().getContentResolver(), imageUri);
        Bitmap adjustedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(),
                originalBitmap.getHeight(), matrix, true);

        return adjustedBitmap;
    }

    public static int getExifRotationInDegrees(Uri imageUri) throws IOException {
        ExifInterface exif = new ExifInterface(imageUri.getPath());
        int exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

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

}
