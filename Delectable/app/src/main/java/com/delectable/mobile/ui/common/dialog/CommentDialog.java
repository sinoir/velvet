package com.delectable.mobile.ui.common.dialog;

import com.delectable.mobile.R;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class CommentDialog extends DialogWithCancelButton {

    EditText mCommentEditText;

    View mContentView;

    CommentDialogCallback mCallback;

    public CommentDialog(Context context, CommentDialogCallback callback) {
        super(context);
        mCallback = callback;
        mContentView = getLayoutInflater().inflate(R.layout.dialog_comment, null);
        setView(mContentView);
        setTitle(R.string.dialog_comment_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommentEditText = (EditText) mContentView.findViewById(R.id.comment_edit_text);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mCommentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND && mCallback != null) {
                    finishWritingComment();
                }
                return false;
            }
        });
    }

    private void finishWritingComment() {
        String comment = mCommentEditText.getText().toString();
        // TODO: Validate comment / show error?
        if (comment.length() > 0 && mCallback != null) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            mCallback.onFinishWritingComment(comment);
            dismiss();
        }
    }

    public static interface CommentDialogCallback {

        public void onFinishWritingComment(String comment);
    }
}
