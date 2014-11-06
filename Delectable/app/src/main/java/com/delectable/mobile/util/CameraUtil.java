package com.delectable.mobile.util;

import com.delectable.mobile.App;
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

        return finalBitmap;
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
        int cropSize = (bitmap.getWidth() > bitmap.getHeight())
                ? bitmap.getHeight()
                : bitmap.getWidth();

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationDegrees);

        if (bitmap.getHeight() > maxSize) {
            float scaleFactor = maxSize / (float) cropSize;
            matrix.postScale(scaleFactor, scaleFactor);
        }

        // TODO center crop
        Bitmap finalBitmap = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                cropSize,
                cropSize,
                matrix,
                true);

        return finalBitmap;
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
}
