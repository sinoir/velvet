package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This view has a picture of the wine capture, as well as the producer and wine name in the bottom
 * left hand corner. Used in the Wine Profile screen, Wine Capture screen, and also in the Follower
 * Feed screen.
 */
public class WineBannerView extends RelativeLayout {

    boolean mShowTriangleMask;

    private int mTriangleCenterPosition;

    private ImageView mWineImage;

    private TextView mProducerName;

    private TextView mWineName;

    public WineBannerView(Context context) {
        this(context, null);
    }

    public WineBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WineBannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WineBannerView);

        mShowTriangleMask = a.getBoolean(R.styleable.WineBannerView_showTriangleMask, false);

        int defaultCenterPos = context.getResources().getDimensionPixelSize(R.dimen.wine_banner_triangle_center_position);
        mTriangleCenterPosition = a.getDimensionPixelSize(R.styleable.WineBannerView_triangleCenterPosition, defaultCenterPos);

        a.recycle();

        View.inflate(context, R.layout.wine_banner_view, this);

        mWineImage = (ImageView) findViewById(R.id.wine_image);
        mProducerName = (TextView) findViewById(R.id.producer_name);
        mWineName = (TextView) findViewById(R.id.wine_name);
    }

    /**
     * Will include the vintage with the wine name in the view as well.
     */
    //Follower Feed ListView items use this method to update their views.
    public void updateData(CaptureDetails captureDetails) {
        String wineImageUrl = captureDetails.getPhoto().getUrl();
        String producerName = captureDetails.getDisplayTitle();
        String wineName = captureDetails.getDisplayDescription() +
                " " + captureDetails.getWineProfile().getVintage();

        updateViewWithData(wineImageUrl, producerName, wineName);
    }

    /**
     * @param capturePhotoHash include the {@link com.delectable.mobile.api.models.CaptureDetails
     *                         CaptureDetails}' {@link PhotoHash} if you want the view to show the
     *                         capture photo. Passing in {@code null} will use {@link WineProfile}'s
     *                         photo.
     * @param includeVintage   {@code true} to show the vintage year.
     */
    //Wine Profile screen (accessed from Follower Feed) uses this method to update their WineBannerView
    //Wine Profile screen (accessed from Wishlist) also uses this method, but also shows the vintage.
    public void updateData(WineProfile wineProfile, PhotoHash capturePhotoHash,
            boolean includeVintage) {

        String wineImageUrl;
        if (capturePhotoHash != null) {
            wineImageUrl = capturePhotoHash.getUrl();
        } else {
            wineImageUrl = wineProfile.getPhoto().getUrl();
        }
        String producerName = wineProfile.getProducerName();
        String wineName = wineProfile.getName();

        if (includeVintage) {
            wineName += " " + wineProfile.getVintage();
        }

        updateViewWithData(wineImageUrl, producerName, wineName);
    }

    //Wine Profile coming from Search Results screen uses this method
    public void updateData(BaseWine baseWine) {
        String wineImageUrl = baseWine.getPhoto().getUrl();
        String producerName = baseWine.getProducerName();
        String wineName = baseWine.getName();

        updateViewWithData(wineImageUrl, producerName, wineName);
    }

    private void updateViewWithData(String wineImageUrl, String producerName, String wineName) {
        ImageLoaderUtil.loadImageIntoView(getContext(), wineImageUrl, mWineImage);
        mProducerName.setText(producerName);
        mWineName.setText(wineName);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShowTriangleMask) {
            drawTriangle(canvas);
        }
    }

    //TODO will need to adjust the bottom bounds of this view by the height of our triangle 
    private void drawTriangle(Canvas canvas) {
        Paint paint = new Paint();

        paint.setStrokeWidth(0);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        int triangleWidth = getResources()
                .getDimensionPixelOffset(R.dimen.wine_banner_triangle_width);
        int triangleHeight = getResources()
                .getDimensionPixelOffset(R.dimen.wine_banner_triangle_height);

        int yTopBoundary = canvas.getHeight() - triangleHeight;
        int yBottomBoundary = canvas.getHeight();

        //left edge corners
        int xBottomLeftCorner = 0;
        int yBottomLeftCorner = yBottomBoundary;

        int xTopLeftCorner = 0;
        int yTopLeftCorner = yTopBoundary;

        //triangle corners
        int xTriangleLeft = mTriangleCenterPosition - triangleWidth / 2;
        int yTriangleLeft = yTopBoundary;

        int xTriangleBottom = xTriangleLeft + triangleWidth / 2;
        int yTriangleBottom = yBottomBoundary;

        int xTriangleRight = xTriangleBottom + triangleWidth / 2;
        int yTriangleRight = yTopBoundary;

        //right edge corners
        int xTopRightCorner = canvas.getWidth();
        int yTopRightCorner = yTopBoundary;

        int xBottomRightCorner = canvas.getWidth();
        int yBottomRightCorner = yBottomBoundary;

        Point a = new Point(xBottomLeftCorner, yBottomLeftCorner);
        Point b = new Point(xTopLeftCorner, yTopLeftCorner);
        Point c = new Point(xTriangleLeft, yTriangleLeft);
        Point d = new Point(xTriangleBottom, yTriangleBottom);
        Point e = new Point(xTriangleRight, yTriangleRight);
        Point f = new Point(xTopRightCorner, yTopRightCorner);
        Point g = new Point(xBottomRightCorner, yBottomRightCorner);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        //drawing left side trapezoid
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(d.x, d.y);
        path.close();

        //drawing right side trapezoid
        path.moveTo(d.x, d.y);
        path.lineTo(e.x, e.y);
        path.lineTo(f.x, f.y);
        path.lineTo(g.x, g.y);
        path.close();

        canvas.drawPath(path, paint);
    }


}