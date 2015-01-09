package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.api.models.CaptureCommentAttributes;
import com.delectable.mobile.api.models.HashtagResult;
import com.delectable.mobile.api.models.SearchHit;
import com.delectable.mobile.util.FontEnum;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.ArrowKeyMovementMethod;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Filter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ChipsMultiAutoCompleteTextView extends MultiAutoCompleteTextView
        implements MenuItem.OnMenuItemClickListener {

    private static final String TAG = ChipsMultiAutoCompleteTextView.class.getSimpleName();

    public static final char SYMBOL_HASHTAG = '#';

    public static final char SYMBOL_MENTION = '@';

    public static final int AUTO_COMPLETE_THRESHOLD = 1;

    private ActionsHandler mActionsHandler;

    private Tokenizer mTokenizer;

    private Filter mHashtagFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint == null || (constraint != null && (constraint.length() < 2))) {
                return null;
            } else if (SYMBOL_HASHTAG != constraint.charAt(0)) {
                return null;
            } else {
                Log.d(TAG, "HASHTAG query: " + constraint
                        .subSequence(1, constraint.length()));
                if (mActionsHandler != null) {
                    mActionsHandler.queryHashtag(
                            constraint.subSequence(1, constraint.length()).toString());
                }
                return null;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // nothing to do here
        }
    };

    private Filter mMentionFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint == null || (constraint != null && (constraint.length() < 2))) {
                return null;
            } else if (SYMBOL_MENTION != constraint.charAt(0)) {
                return null;
            } else {
                Log.d(TAG, "MENTION query: " + constraint
                        .subSequence(1, constraint.length()));
                if (mActionsHandler != null) {
                    mActionsHandler.queryMention(
                            constraint.subSequence(1, constraint.length()).toString());
                }
                return null;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // nothing to do here
        }
    };

    private SearchAutoCompleteAdapter<HashtagResult>
            mHashtagAdapter = new SearchAutoCompleteAdapter<HashtagResult>(
            getContext(), mHashtagFilter);

    private SearchAutoCompleteAdapter<AccountSearch>
            mMentionAdapter = new SearchAutoCompleteAdapter<AccountSearch>(
            getContext(), mMentionFilter);


    private boolean mIsDropdownShown;

    public ChipsMultiAutoCompleteTextView(Context context) {
        super(context);
        init(context, null);
    }

    public ChipsMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChipsMultiAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontTextView);
            int fontIntValue = a.getInt(R.styleable.FontTextView_fontName,
                    FontEnum.WHITNEY_BLACK.ordinal());
            a.recycle();

            FontEnum font = FontEnum.values()[fontIntValue];
            setTypeface(font);
        }

        mTokenizer = new HashtagMentionTokenizer();
        setTokenizer(mTokenizer);
//        setTokenizer(new ChipsTokenizer());
        setThreshold(AUTO_COMPLETE_THRESHOLD);
        setMovementMethod(new ChipsArrowKeyMovementMethod());
        setAdapter(mHashtagAdapter);
    }

    public void setActionsHandler(ActionsHandler handler) {
        mActionsHandler = handler;
    }

    /**
     * Convenience method that calls {@link #setTypeface(android.graphics.Typeface)}
     */
    public void setTypeface(FontEnum font) {
        Typeface typeface = Typeface
                .createFromAsset(getContext().getAssets(), "fonts/" + font.getFileName());
        setTypeface(typeface);
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        Editable e = getText();
        ChipSpan[] chips = e.getSpans(0, e.length(), ChipSpan.class);
        for (ChipSpan chipSpan : chips) {
            if (chipSpan.isSelected()) {
                MenuItem item = menu.add(getResources().getString(R.string.delete));
                item.setOnMenuItemClickListener(this);
                break;
            }
        }
        super.onCreateContextMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // delete spans
        Editable e = getText();
        ChipSpan[] chips = e.getSpans(0, e.length(), ChipSpan.class);
        for (ChipSpan chipSpan : chips) {
            if (chipSpan.isSelected()) {
                int start = e.getSpanStart(chipSpan);
                int end = e.getSpanEnd(chipSpan);
                if (end < e.length() && e.charAt(end) == ' ') {
                    end++;
                }
                e.delete(start, end);
            }
        }
        return true;
    }

    @Override
    public void showDropDown() {
        mIsDropdownShown = true;
        super.showDropDown();
        mIsDropdownShown = false;
    }

    /**
     * <p>Converts the selected item from the drop down list into a sequence of character that can
     * be used in the edit box.</p>
     *
     * @param selectedItem the item selected by the user for completion
     * @return a sequence of characters representing the selected suggestion
     */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        if (!mIsDropdownShown) {
            SearchHit hit = (SearchHit) selectedItem;
            ChipSpan span = null;
            if (hit.getObject() instanceof HashtagResult) {
                HashtagResult result = (HashtagResult) hit.getObject();
                span = new ChipSpan(result.getTag(), result.getTag(),
                        CaptureCommentAttributes.TYPE_HASHTAG);
            } else if (hit.getObject() instanceof AccountSearch) {
                AccountSearch result = (AccountSearch) hit.getObject();
                span = new ChipSpan(result.getFullName(), result.getId(),
                        CaptureCommentAttributes.TYPE_MENTION);
            }
            // replace with single character while in edit mode (allows for deleting chips with a single backspace), then replace later with real text (same for parsing when editing a comment)
            SpannableString ss = null;
            ss = new SpannableString("x");
            ss.setSpan(span, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        }
        return super.convertSelectionToString(selectedItem);
    }

    /**
     * <p>Performs the text completion by replacing the range from {@link
     * android.widget.MultiAutoCompleteTextView.Tokenizer#findTokenStart} to {@link
     * #getSelectionEnd} by the the result of passing <code>text</code> through {@link
     * android.widget.MultiAutoCompleteTextView.Tokenizer#terminateToken}. In addition, the replaced
     * region will be marked as an AutoText substition so that if the user immediately presses DEL,
     * the completion will be undone. Subclasses may override this method to do some different
     * insertion of the content into the edit box.</p>
     *
     * @param text the selected suggestion in the drop down list
     */
    @Override
    protected void replaceText(CharSequence text) {
        Log.d(TAG, "replaceText: " + text);
        super.replaceText(text);

//        clearComposingText();
//
//        int end = getSelectionEnd();
//        int start = mTokenizer.findTokenStart(getText(), end);
//
//        Editable editable = getText();
//        String original = TextUtils.substring(editable, start, end);
//
//        QwertyKeyListener.markAsReplaced(editable, start, end, original);
//        editable.replace(start, end, mTokenizer.terminateToken(text));
    }

    /**
     * Instead of filtering whenever the total length of the text exceeds the threshhold, this
     * subclass filters only when the length of the range from {@link android.widget.MultiAutoCompleteTextView.Tokenizer#findTokenStart}
     * to {@link #getSelectionEnd} meets or exceeds {@link #getThreshold}.
     */
    @Override
    public boolean enoughToFilter() {
        int end = getSelectionEnd();
        if (end <= 0 || mTokenizer == null) {
            return false;
        }

        int start = mTokenizer.findTokenStart(getText(), end);
        if (SYMBOL_HASHTAG == getText().charAt(start)) {
            setAdapter(mHashtagAdapter);
            return true;
        } else if (SYMBOL_MENTION == getText().charAt(start)) {
            setAdapter(mMentionAdapter);
            return true;
        } else if (end - start >= getThreshold()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Starts filtering the content of the drop down list. The filtering pattern is the specified
     * range of text from the edit box. Subclasses may override this method to filter with a
     * different pattern, for instance a smaller substring of <code>text</code>.</p>
     */
    @Override
    protected void performFiltering(CharSequence text, int start, int end, int keyCode) {
        super.performFiltering(text, start, end, keyCode);
    }

    public ArrayList<ChipSpan> getSpans() {
        ChipSpan[] chips = getText().getSpans(0, getText().length(), ChipSpan.class);
        return new ArrayList<>(Arrays.asList(chips));
    }

    public static boolean isTagPrefix(char c) {
        return (c == SYMBOL_HASHTAG || c == SYMBOL_MENTION);
    }

    public static class ChipSpan extends ReplacementSpan {

        private static DisplayMetrics sDisplayMetrics = App.getInstance().getResources()
                .getDisplayMetrics();

        private static int sSpanHeight = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, sDisplayMetrics);

        private static int sPadding = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, sDisplayMetrics);

        private String mSpanText;

        private String mId;

        private String mType;

        private int mLength;

        private boolean mSelected;

        public ChipSpan(String spanText, String id, String type) {
            mType = type;
            mId = id;
            mSpanText = (CaptureCommentAttributes.TYPE_HASHTAG.equals(type)
                    ? SYMBOL_HASHTAG
                    : SYMBOL_MENTION)
                    + spanText;
        }

        /**
         * Returns the list key for hashtags or the account id for mentions
         */
        public String getId() {
            return mId;
        }

        public String getType() {
            return mType;
        }

        public String getReplacedText() {
            return mSpanText;
        }

        public void toggleSelection() {
            mSelected = !mSelected;
        }

        public boolean isSelected() {
            return mSelected;
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end,
                Paint.FontMetricsInt fm) {
            mLength = (int) paint.measureText(mSpanText) + 2 * sPadding;
            if (fm != null) {
                fm.ascent = -sSpanHeight;
                fm.descent = 0;
                fm.top = fm.ascent;
                fm.bottom = 0;
            }
            // 4 (2 + 2) is for cursor at both ends
            return mLength + 4;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top,
                int y, int bottom, Paint paint) {
//            x += 2;
//            paint.setColor(0xffcccccc);
//            canvas.drawRect(x, top, x + length, bottom, paint);
            paint.setColor(HashtagMentionSpan.HASHTAG_COLOR);
            canvas.drawText(mSpanText, x + sPadding, y, paint);

            if (mSelected) {
                paint.setColor(0x33ff0000);
                canvas.drawRect(x, top, x + mLength, bottom, paint);
            }
        }
    }

    class ChipsArrowKeyMovementMethod extends ArrowKeyMovementMethod {

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();
                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);
                Editable e = getText();
                ChipSpan[] spans = e.getSpans(off, off, ChipSpan.class);
                if (spans.length == 1) {
                    spans[0].toggleSelection();
                    invalidate();
                }
            }
            return super.onTouchEvent(widget, buffer, event);
        }
    }

//    public static class ChipsTokenizer implements Tokenizer {
//
//        @Override
//        public int findTokenStart(CharSequence text, int cursor) {
//            Log.d(TAG, "findTokenStart [" + text + "] " + cursor);
//            int i = cursor;
//            while (i > 0 && text.charAt(i - 1) != ' ') {
//                i--;
//            }
//            return i;
//        }
//
//        @Override
//        public int findTokenEnd(CharSequence text, int cursor) {
//            Log.d(TAG, "findTokenEnd [" + text + "] " + cursor);
//            return text.length();
//        }
//
//        @Override
//        public CharSequence terminateToken(CharSequence text) {
//            Log.d(TAG, "terminateToken [" + text + "]");
//            if (text instanceof Spanned) {
//                SpannableStringBuilder s = new SpannableStringBuilder(text);
//                s.append(" ");
//                return s;
//            }
//            return text + " ";
//        }
//    }

    /**
     * Tokenizer to support completion of @user and #hashtag
     */
    public static class HashtagMentionTokenizer implements Tokenizer {

        /**
         * Returns the start of the token that ends at offset <code>cursor within text.
         */
        @Override
        public int findTokenStart(CharSequence text, int cursor) {
            if (text.length() == 0) {
                return 0;
            }

            int i = cursor - 1; // start on last character

            // move cursor back until a tag prefix is found
            while (i > 0 && !isTagPrefix(text.charAt(i))) {
                i--;
            }

//            Log.d(TAG, "tokenStart: " + i + " / " + text.charAt(i));
            return i;
        }

        /**
         * Returns the end of the token (minus trailing punctuation) that begins at offset
         * <code>cursor within text.
         */
        @Override
        public int findTokenEnd(CharSequence text, int cursor) {
            Log.d(TAG, "fte:" + text + "," + cursor);

            for (int i = cursor; i < text.length(); i++) {
                if (isTagPrefix(text.charAt(i))) {
                    Log.d(TAG, "tokenEnd: " + i + " / " + text.charAt(i));
                    return i;
                }
            }

            Log.d(TAG, "tokenEnd: " + text.length() + " / " + text.charAt(text.length()));
            return text.length();
        }

        /**
         * Returns <code>text, modified, if necessary, to ensure that it ends with a token
         * terminator (for example a space or comma).
         */
        @Override
        public CharSequence terminateToken(CharSequence text) {
//            Log.d(TAG, "tto:" + text );
//            String s = text.toString();
////        if (s.startsWith("@")) {
////            return s.substring(0, s.indexOf(',')) + " ";
////        }
////        return text + " ";
//            return s;
            Log.d(TAG, "terminateToken [" + text + "]");
            if (text instanceof Spanned) {
                SpannableStringBuilder s = new SpannableStringBuilder(text);
                s.append(" ");
                return s;
            }
            return text + " ";
        }
    }

    public void updateHashtagResults(ArrayList<SearchHit<HashtagResult>> results) {
        mHashtagAdapter.setItems(results);
        mHashtagAdapter.notifyDataSetChanged();
    }

    public void updateMentionResults(ArrayList<SearchHit<AccountSearch>> results) {
        mMentionAdapter.setItems(results);
        mMentionAdapter.notifyDataSetChanged();
    }

    public interface ActionsHandler {

        public void queryHashtag(String query);

        public void queryMention(String query);
    }

}