package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserCapturesAdapter extends BaseAdapter {

    private static final int sTypeSimpleListing = 0;

    private static final int sTypeDetailedListing = 1;

    private Activity mContext;

    private ArrayList<CaptureDetails> mData;

    private int mListingType;

    public UserCapturesAdapter(Activity context, ArrayList<CaptureDetails> data) {
        mListingType = 0;
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (mListingType == sTypeSimpleListing) {
            view = getSimpleListingView(position, convertView, parent);
        } else if (mListingType == sTypeDetailedListing) {
            view = getDetailedListingView(position, convertView, parent);
        }
        return view;
    }

    public View getSimpleListingView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        SimpleListingViewHolder viewHolder = null;
        CaptureDetails capture = mData.get(position);

        if (rowView == null || !(rowView.getTag() instanceof SimpleListingViewHolder)) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.row_simple_wine_detail, null);
            viewHolder = new SimpleListingViewHolder();
            viewHolder.wineImage = (ImageView) rowView.findViewById(R.id.image);
            viewHolder.producerName = (TextView) rowView.findViewById(R.id.producer_name);
            viewHolder.wineName = (TextView) rowView.findViewById(R.id.wine_name);
            viewHolder.ratingBarView = rowView.findViewById(R.id.rating_bar);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (SimpleListingViewHolder) rowView.getTag();
        }

        String producerName = "";
        String wineName = "";
        String wineImageUrl = "";
        if (capture.getWineProfile() != null) {
            producerName = capture.getWineProfile().getProducerName();
            wineName = capture.getWineProfile().getName();
            wineImageUrl = capture.getWineProfile().getPhoto().getThumbUrl();
        }
        viewHolder.producerName.setText(producerName);
        viewHolder.wineName.setText(wineName);
        Picasso.with(mContext).load(wineImageUrl).into(viewHolder.wineImage);

        return rowView;
    }

    public View getDetailedListingView(int position, View convertView, ViewGroup parent) {
        // TODO: Implement switching listing
        return null;
    }

    static class SimpleListingViewHolder {

        ImageView wineImage;

        TextView producerName;

        TextView wineName;

        // TODO: Custom Bar View for rating
        View ratingBarView;
    }

    static class DetailedListingViewHolder {
        // TODO: Possibly split out?
    }
}
