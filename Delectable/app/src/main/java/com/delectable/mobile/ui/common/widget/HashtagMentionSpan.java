package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureCommentAttributes;
import com.delectable.mobile.api.models.CaptureFeed;
import com.delectable.mobile.ui.capture.activity.FeedActivity;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

public class HashtagMentionSpan extends ClickableSpan {

    private static enum Type {HASHTAG, MENTION}

    private static final String TAG = HashtagMentionSpan.class.getSimpleName();

    private static final int TEXT_COLOR = App.getInstance().getResources().getColor(
            R.color.chestnut_to_chestnut_pressed);

    private Context mContext;

    private String mTag;

    private String mLink;

    private Type mType;

    private String mKey;

    public HashtagMentionSpan(Context context, String tag, String link, String type) {
        mContext = context;
        mTag = tag;
        if (CaptureCommentAttributes.TYPE_HASHTAG.equals(type)) {
            mType = Type.HASHTAG;
        } else if (CaptureCommentAttributes.TYPE_MENTION.equals(type)) {
            mType = Type.MENTION;
        }
        mLink = link;
        if (link != null && !link.isEmpty()) {
            mKey = mLink.substring(mLink.lastIndexOf('=') + 1, mLink.length());
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(TEXT_COLOR);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        if (mType == Type.HASHTAG) {
            // launch custom feed activity
            Intent intent = FeedActivity.newIntent(mContext, mKey, CaptureFeed.CUSTOM, mTag);
            mContext.startActivity(intent);
            Log.d(TAG, mTag + " / key=" + mKey + " link=" + mLink);
        } else if (mType == Type.MENTION) {
            // launch user profile
            Intent intent = UserProfileActivity
                    .newIntent(mContext, mKey);
            mContext.startActivity(intent);
            Log.d(TAG, mTag + " / id=" + mKey + " link=" + mLink);
        }
    }

}
