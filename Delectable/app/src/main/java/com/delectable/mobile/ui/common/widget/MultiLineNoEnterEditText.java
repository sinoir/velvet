package com.delectable.mobile.ui.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
 * Multi Line edit text without enter key
 */
public class MultiLineNoEnterEditText extends EditText {

    public MultiLineNoEnterEditText(Context context) {
        super(context);
    }

    public MultiLineNoEnterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiLineNoEnterEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // Private method from Android's TextView
    private static boolean isMultilineInputType(int type) {
        return (type & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE)) ==
                (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection ic = super.onCreateInputConnection(outAttrs);
        // Must revert the NO ENTER Action flag for multi line text editing with an action button
        if (onCheckIsTextEditor() && isEnabled()) {
            if (isMultilineInputType(outAttrs.inputType)) {
                outAttrs.imeOptions -= EditorInfo.IME_FLAG_NO_ENTER_ACTION;
            }
        }
        return ic;
    }
}
