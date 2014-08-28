package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.TaggeeContact;
import com.delectable.mobile.ui.common.widget.FontTextView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class InviteContactRow extends RelativeLayout {

    @InjectView(R.id.name)
    protected FontTextView mName;

    @InjectView(R.id.invite_button)
    protected TextView mInviteButton;

    private TaggeeContact mContact;

    private FollowActionsHandler mActionsHandler;

    public InviteContactRow(Context context) {
        this(context, null);
    }

    public InviteContactRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InviteContactRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.row_invite_contact, this);
        ButterKnife.inject(this);
    }

    public void setActionsHandler(FollowActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    private void updateData(String name) {
        mName.setText(name);
    }

    public void updateData(TaggeeContact contact) {
        mContact = contact;
        updateData(mContact.getFullName());
    }

    @OnClick(R.id.invite_button)
    protected void onInviteButtonClicked(View v) {
        if (mActionsHandler != null) {
            mActionsHandler.inviteContact(mContact);
        }
    }
}
