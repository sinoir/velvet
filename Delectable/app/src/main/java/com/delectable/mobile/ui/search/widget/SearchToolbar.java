package com.delectable.mobile.ui.search.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.util.FontEnum;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A custom toolbar view to be used instead of the default v7 searchview, which is not beautiful
 * like the search seen in google apps. The implemenation simply uses a textwatcher and a done
 * button listener on an edittext field, while still using the {@link
 * SearchView.OnQueryTextListener} interface to communicate search queries back to the implementing
 * class.
 */
public class SearchToolbar extends RelativeLayout {

    public static final String TAG = SearchToolbar.class.getSimpleName();

    @InjectView(R.id.search_field)
    protected EditText mSearchField;

    @InjectView(R.id.close_button)
    protected ImageButton mCloseButton;

    private SearchView.OnQueryTextListener mOnQueryTextListener;

    public SearchToolbar(Context context) {
        super(context);
        init();
    }

    public SearchToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchToolbar(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.toolbar_search, this);
        ButterKnife.inject(this);

        //need to manually set typeface instead of using FontEditText because of support library limitations with subclassing EditText
        //though, we don't current use the default underline for the edittext here
        Typeface type = FontEnum.WHITNEY_MEDIUM.getTypeface(getContext());
        mSearchField.setTypeface(type);
        mSearchField.addTextChangedListener(SearchTextWatcher);
        mSearchField.setOnEditorActionListener(DoneActionListener);

        mCloseButton.setVisibility(View.INVISIBLE);
    }

    public void setOnQueryTextListener(SearchView.OnQueryTextListener l) {
        mOnQueryTextListener = l;
    }

    @OnClick(R.id.close_button)
    protected void onClearButtonClick() {
        mSearchField.setText("");
        showKeyboard();
    }

    public void showKeyboard() {
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                toggleSoftInput(InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private TextWatcher SearchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            //hide the clear button if query string is fully deleted
            int v = s.length() > 0 ? View.VISIBLE : View.INVISIBLE;
            mCloseButton.setVisibility(v);

            if (mOnQueryTextListener != null) {
                mOnQueryTextListener.onQueryTextChange(s.toString());
            }
        }
    };

    /**
     * Listens for done button on soft keyboard.
     */
    protected TextView.OnEditorActionListener DoneActionListener
            = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (mOnQueryTextListener != null) {
                    mOnQueryTextListener.onQueryTextSubmit(mSearchField.getText().toString());
                }
                //hide keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(0, 0);
                return true;
            }
            return false;
        }
    };

}
