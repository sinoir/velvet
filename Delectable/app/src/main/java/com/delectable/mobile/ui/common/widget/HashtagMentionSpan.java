package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureCommentAttributes;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

public class HashtagMentionSpan extends ClickableSpan {

    private static enum Type {HASHTAG, MENTION}

    private static final int TEXT_COLOR = App.getInstance().getResources().getColor(
            R.color.chestnut_to_chestnut_pressed);

    private String mTag;

    private String mLink;

    private Type mType;

    private String mKey;

    public HashtagMentionSpan(String tag, String link, String type) {
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
            // TODO launch user profile
            Toast.makeText(App.getInstance(), mTag + " / id=" + mKey + " link=" + mLink,
                    Toast.LENGTH_SHORT).show();
        }
    }

}
