package com.delectable.mobile.ui.wineprofile.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.AccountsNetworkController;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.WineBannerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class WineProfileFragment extends BaseFragment {

    public static final String TAG = WineProfileFragment.class.getSimpleName();

    private static final String sArgsWineProfile = "wineProfile";

    private static final String sArgsPhotoHash = "photoHash";


    private BaseNetworkController mNetworkController;

    private WineProfile mWineProfile;

    private PhotoHash mCapturePhotoHash;


    private ArrayList<CaptureNote> mCaptureNotes = new ArrayList<CaptureNote>();

    /**
     * Returns fragment that uses the capture photo as it's wine image.
     *
     * @param capturePhotoHash {@link com.delectable.mobile.api.models.CaptureDetails
     *                         CaptureDetails}' PhotoHash. Pass in null to use WineProfile's image.
     */
    public static WineProfileFragment newInstance(WineProfile wineProfile,
            PhotoHash capturePhotoHash) {
        WineProfileFragment fragment = new WineProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(sArgsWineProfile, wineProfile);
        args.putParcelable(sArgsPhotoHash, capturePhotoHash);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkController = new AccountsNetworkController(getActivity());
        Bundle args = getArguments();
        if (args != null) {
            mWineProfile = args.getParcelable(sArgsWineProfile);
            mCapturePhotoHash = args.getParcelable(sArgsPhotoHash);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wine_profile, container, false);
        ListView listview = (ListView) view.findViewById(R.id.list_view);

        WineBannerView banner = new WineBannerView(getActivity());
        banner.updateData(mWineProfile, mCapturePhotoHash, false);

        listview.addHeaderView(banner);

        //TODO implement real adapter for listview, this is just a placeholder
        //String[] listItems = {"item 1", "item 2 ", "list", "android", "item 3", "foobar", "bar", };
        String[] listItems = {};
        BaseAdapter adapter = new ArrayAdapter(this.getActivity(),
                android.R.layout.simple_list_item_1, listItems);
        listview.setAdapter(adapter);
        return view;
    }
}
