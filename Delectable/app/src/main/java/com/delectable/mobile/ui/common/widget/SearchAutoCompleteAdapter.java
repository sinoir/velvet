package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.api.models.HashtagResult;
import com.delectable.mobile.api.models.SearchHit;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchAutoCompleteAdapter<T> extends BaseAdapter implements Filterable {

    public static final String TAG = SearchAutoCompleteAdapter.class.getSimpleName();

    private Context mContext;

    protected ArrayList<SearchHit<T>> mItems = new ArrayList<>();

    private Filter mFilter;

    public SearchAutoCompleteAdapter(Context context, Filter filter) {
        mContext = context;
        mFilter = filter;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row = view;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(R.layout.row_autocomplete_search, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.textView = (TextView) row.findViewById(R.id.row_text);
            holder.imageView = (ImageView) row.findViewById(R.id.row_image);
            row.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) row.getTag();
        SearchHit<T> hit = (SearchHit<T>) getItem(position);
        if (hit.getObject() instanceof HashtagResult) {
            holder.textView.setText(
                    ChipsMultiAutoCompleteTextView.SYMBOL_HASHTAG + ((HashtagResult) hit
                            .getObject()).getTag());
        } else if (hit.getObject() instanceof AccountSearch) {
            AccountSearch account = (AccountSearch) hit.getObject();
            holder.textView.setText(
                    ChipsMultiAutoCompleteTextView.SYMBOL_MENTION + account.getFullName());
            String profileImageUrl = account.getPhoto().getBestThumb();
            ImageLoaderUtil.loadImageIntoView(mContext, profileImageUrl, holder.imageView);
            holder.imageView.setVisibility(View.VISIBLE);
        }

        return row;
    }

    private static class ViewHolder {

        public TextView textView;

        public ImageView imageView;
    }

    public ArrayList<SearchHit<T>> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<SearchHit<T>> items) {
        mItems = items;
    }

    /**
     * <p>Returns a filter that can be used to constrain data with a filtering pattern.</p>
     *
     * <p>This method is usually implemented by {@link android.widget.Adapter} classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        return mFilter;
    }

}
