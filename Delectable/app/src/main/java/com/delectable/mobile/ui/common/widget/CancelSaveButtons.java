package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CancelSaveButtons extends LinearLayout {

    @InjectView(R.id.cancel_button)
    protected View mCancelButton;

    @InjectView(R.id.save_button)
    protected View mSaveButton;

    private ActionsHandler mActionsHandler;

    public CancelSaveButtons(Context context) {
        this(context, null);
    }

    public CancelSaveButtons(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CancelSaveButtons(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.cancel_save_buttons, this);
        ButterKnife.inject(this);

        setOrientation(LinearLayout.HORIZONTAL);
    }

    public ActionsHandler getActionsHandler() {
        return mActionsHandler;
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    @OnClick(R.id.cancel_button)
    public void onCancelClicked() {
        if (mActionsHandler != null) {
            mActionsHandler.onCancelClicked();
        }
    }

    @OnClick(R.id.save_button)
    public void onSaveClicked() {
        if (mActionsHandler != null) {
            mActionsHandler.onSaveClicked();
        }
    }

    public interface ActionsHandler {

        public void onCancelClicked();

        public void onSaveClicked();
    }
}
