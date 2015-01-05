package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureCommentAttributes;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

public class HashtagMentionSpan extends ClickableSpan {

    private static enum Type {HASHTAG, MENTION}

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
            // TODO launch custom feed activity
            Toast.makeText(App.getInstance(), mTag + " / key=" + mKey + " link=" + mLink,
                    Toast.LENGTH_SHORT).show();
        } else if (mType == Type.MENTION) {
            // launch user profile
            Intent intent = UserProfileActivity
                    .newIntent(mContext, mKey);
            mContext.startActivity(intent);
//            Toast.makeText(App.getInstance(), mTag + " / id=" + mKey + " link=" + mLink,
//                    Toast.LENGTH_SHORT).show();
        }
    }

}
