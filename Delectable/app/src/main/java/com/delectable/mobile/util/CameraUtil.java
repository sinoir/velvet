package com.delectable.mobile.util;

import com.delectable.mobile.App;
import com.delectable.mobile.ui.BaseFragment;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CameraUtil {

    public static final int MAX_SIZE_PENDING = 1280;

    public static final int MAX_SIZE_INSTANT = 300;

    public static final int MAX_SIZE_PROFILE_IMAGE = 640;

    public static final int JPEG_QUALITY = 80;

    public static final int JPEG_QUALITY_INSTANT = 75;

    public static final String TAG = "CameraUtil";

    public static Camera getCameraInstance(int cameraId) {
        Camera camera = null;
        try {
            camera = Camera.open(cameraId);
            setCameraParameters(camera);
        } catch (Exception e) {
            Log.e(TAG, "Failed to open Camera", e);
        }
        return camera;
    }

    public static void setCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        // Picture Size
        int maxWidth = 0;
        int maxHeight = 0;
        for (Camera.Size size : parameters.getSupportedPictureSizes()) {
            if (size.width > maxWidth) {
                maxWidth = size.width;
                maxHeight = size.height;
            }
        }
        parameters.setPictureSize(maxWidth, maxHeight);
        Log.d(TAG, "pictureSize: " + maxWidth + "x" + maxHeight);

        // Preview Size with matching aspect ratio
        Camera.Size previewSize = getOptimalPreviewSize(0, maxWidth, maxHeight, parameters);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        Log.d(TAG, "previewSize: " + previewSize.width + "x" + previewSize.height);

        // Continous Auto Focus
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        // White Balance
        List<String> whiteBalance = parameters.getSupportedWhiteBalance();
        if (whiteBalance.contains(Camera.Parameters.WHITE_BALANCE_AUTO)) {
            parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        }

        // Anti Banding
        List<String> antiBanding = parameters.getSupportedAntibanding();
        if (antiBanding.contains(Camera.Parameters.ANTIBANDING_AUTO)) {
            parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
        }

        camera.setParameters(parameters);
    }

    public static boolean checkSystemHasCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static boolean checkSystemHasFlash(Context context) {
        return checkSystemHasCameraHardware(context) &&
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public static boolean checkSystemHasFocus(Context context) {
        return checkSystemHasCameraHardware(context) &&
                context.getPackageManager()
                        .hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }

    public static void setCameraDisplayOrientation(int cameraId, Camera camera) {
        int rotationDegrees = getCameraRotationInDegrees(cameraId);
        camera.setDisplayOrientation(rotationDegrees);
    }

    public static Bitmap rotateScaleAndCropImage(byte[] imageData, int cameraId) {

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        int rotationDegrees = getCameraRotationInDegrees(cameraId);
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationDegrees);
        float scaleFactor = CameraUtil.MAX_SIZE_PENDING / (float) bitmap.getHeight();
        matrix.postScale(scaleFactor, scaleFactor);

        Bitmap finalBitmap = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.getHeight(),
                bitmap.getHeight(),
                matrix,
                true);

        bitmap.recycle();

        return finalBitmap;
    }

    public static void blurImageAsync(final Bitmap source, final int radius,
            final BaseFragment context, final SafeAsyncTask.Callback<Bitmap> callback) {
        new SafeAsyncTask<Bitmap>(context) {
            @Override
            protected Bitmap safeDoInBackground(Void[] params) {
                return blurImage(source, radius);
            }

            @Override
            protected void safeOnPostExecute(Bitmap result) {
                if (callback != null) {
                    callback.onResult(result);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static Bitmap blurImage(Bitmap source, int radius) {
        if (source == null) {
            return null;
        }

        radius = MathUtil.clamp(radius, 0, 25);

        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = source.copy(source.getConfig(), true);

            final RenderScript rs = RenderScript.create(App.getInstance());
            final Allocation input = Allocation
                    .createFromBitmap(rs, source, Allocation.MipmapControl.MIPMAP_NONE,
                            Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap = source.copy(source.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return bitmap;
    }

    public static int getCameraRotationInDegrees(int cameraId) {
        WindowManager windowManager = (WindowManager) App.getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        Log.d(TAG, "rotation: " + degrees);

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int result;
        // Compensate for Front facing camera mirror
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    public static float getCameraPreviewAspectRatio(Camera camera) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size previewSize = parameters.getPreviewSize();
            return (float) previewSize.width / (float) previewSize.height;
        }
        return 1.0f;
    }

    public static Camera.Area getFocusAreaFromFrameBounds(PointF focusPoint, RectF bounds) {
        int focusAreaSize = 50;
        // Focus area has a width and height of 2000, 2000, ranging from -1000 to 1000
        int maxFocusSize = 2000;
        int adjustedX = (int) (((focusPoint.x * maxFocusSize) / bounds.width()) - 1000);
        int adjustedY = (int) (((focusPoint.y * maxFocusSize) / bounds.height()) - 1000);

        int left = adjustedX - (focusAreaSize / 2);
        int top = adjustedY - (focusAreaSize / 2);

        // Left and right must be within -1000 and 1000
        Rect focusArea = new Rect(
                Math.max(left, -1000),
                Math.max(top, -1000),
                Math.min(left + focusAreaSize, 1000),
                Math.min(top + focusAreaSize, 1000));

        return new Camera.Area(focusArea, focusAreaSize);
    }

    public static Camera.Size getOptimalPreviewSize(int displayOrientation, int width, int height,
            Camera.Parameters parameters) {
        double targetRatio = (double) width / height;
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = height;

        if (displayOrientation == 90 || displayOrientation == 270) {
            targetRatio = (double) height / width;
        }

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;

            if (Math.abs(ratio - targetRatio) <= 0.1) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        // Cannot find the one match the aspect ratio, ignore
        // the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;

            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return (optimalSize);
    }

    public static Bitmap loadBitmapFromUri(Uri imageUri, int maxSize)
            throws Exception {

        Bitmap bitmap = BitmapFactory.decodeStream(App.getInstance().getContentResolver().openInputStream(imageUri));

        int rotationDegrees = getExifRotationInDegrees(imageUri);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        boolean isPortrait = height > width;

        int shorterEdge = (isPortrait) ? width : height;
        int longerEdge = (isPortrait) ? height : width;
        int offset = (longerEdge - shorterEdge) / 2;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationDegrees);

        if (bitmap.getHeight() > maxSize) {
            float scaleFactor = maxSize / (float) shorterEdge;
            matrix.postScale(scaleFactor, scaleFactor);
        }

        Bitmap finalBitmap = Bitmap.createBitmap(
                bitmap,
                isPortrait ? 0 : offset,
                isPortrait ? offset : 0,
                shorterEdge,
                shorterEdge,
                matrix,
                true);

        return finalBitmap;
    }

    public static int getExifRotationInDegrees(Uri imageUri)
            throws Exception {

        // Make sure this is a JPEG
        String mimeType = App.getInstance().getContentResolver().getType(imageUri);
        if (!"image/jpeg".equalsIgnoreCase(mimeType)) {
            return 0;
        }

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
}
