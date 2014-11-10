package com.delectable.mobile.ui.common.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class WebViewFragment extends BaseFragment {

    public static final String TAG = WebViewFragment.class.getSimpleName();

    private static final String URL = "url";

    private String mUrl;

    public static WebViewFragment newInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args == null) {
            throw new RuntimeException(TAG + " needs to be instantiated with a url");
        }

        mUrl = args.getString(URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        WebView view = (WebView) inflater.inflate(R.layout.fragment_webview, container, false);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(mUrl);
        return view;
    }

}
