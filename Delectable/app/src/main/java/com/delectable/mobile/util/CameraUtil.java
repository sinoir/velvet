package com.delectable.mobile.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

public class CameraUtil {

    public static final String TAG = "CameraUtil";

    public static Camera getCameraInstance(int cameraId) {
        Camera camera = null;
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            Log.e(TAG, "Failed to open Camera", e);
        }
        return camera;
    }

    public static boolean checkSystemHasFrontCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

    public static boolean checkSystemHasFlash(Context context) {
        return checkSystemHasFrontCameraHardware(context) &&
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public static boolean checkSystemHasFocus(Context context) {
        return checkSystemHasFrontCameraHardware(context) &&
                context.getPackageManager()
                        .hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }

    public static void setCameraDisplayOrientation(Context context, int cameraId,
            Camera camera) {
        int rotationDegrees = getCameraRotationFixInDegrees(context, cameraId);
        camera.setDisplayOrientation(rotationDegrees);
    }

    public static Bitmap getRotatedBitmapTakenFromCamera(
            Context context,
            byte[] imageData,
            int cameraId) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        int rotationDegrees = getCameraRotationFixInDegrees(context, cameraId);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationDegrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix,
                false);

        return rotatedBitmap;
    }

    public static int getCameraRotationFixInDegrees(Context context, int cameraId) {
        WindowManager windowManager = (WindowManager) context
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
}
