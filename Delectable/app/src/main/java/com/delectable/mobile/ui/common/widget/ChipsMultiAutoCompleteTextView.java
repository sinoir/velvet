package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.util.FontEnum;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
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
import android.view.View;
import android.widget.FilterQueryProvider;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Inspired by https://gist.github.com/pskink/5fe9c0bb4677c1debc5e
 */
public class ChipsMultiAutoCompleteTextView extends MultiAutoCompleteTextView
        implements MenuItem.OnMenuItemClickListener {

    private static final String TAG = ChipsMultiAutoCompleteTextView.class.getSimpleName();

    public static final char SYMBOL_HASHTAG = '#';

    public static final char SYMBOL_MENTION = '@';

    public static final int AUTO_COMPLETE_THRESHOLD = 1;

    private Tokenizer mTokenizer;

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

        String[] from = {ContactsContract.Contacts.DISPLAY_NAME};
        int[] to = {android.R.id.text1};
        // TODO custom row layout that allows for user images
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
                android.R.layout.simple_dropdown_item_1line, null, from, to);

        SimpleCursorAdapter.ViewBinder viewBinder = new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View v, Cursor c, int index) {
                TextView tv = (TextView) v;
                int nameIdx = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                int emailIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                tv.setText(c.getString(nameIdx) + " (" + c.getString(emailIdx) + ")");
                return true;
            }
        };
        adapter.setViewBinder(viewBinder);

        FilterQueryProvider provider = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if (constraint == null || (constraint != null && constraint.length() == 0)) {
                    return null;
                }
                Log.d(TAG, "query constraint: " + constraint);
                Uri uri = Uri
                        .withAppendedPath(ContactsContract.CommonDataKinds.Email.CONTENT_FILTER_URI,
                                constraint.subSequence(1, constraint.length())
                                        .toString()); // skip first character (#, @)
                Log.d(TAG, "query uri: " + uri.toString());
                String[] proj = {BaseColumns._ID, ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Email.DATA,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID};
                return getContext().getContentResolver().query(uri, proj, null, null, null);
            }
        };
        adapter.setFilterQueryProvider(provider);
        setAdapter(adapter);
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
        //Log.d(TAG, "convertSelectionToString " + mIsDropdownShown);
        if (!mIsDropdownShown) {
            Cursor c = (Cursor) selectedItem;
            int nameIdx = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int emailIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            int contactIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
            // TODO determine correct replacement text (needs to be @User Name or #hashtag)
            SpannableString ss = new SpannableString(c.getString(nameIdx));
            // TODO determine what was selected and then set hashtag or mention span accordingly
            ChipSpan span = new MentionChipSpan(c.getString(nameIdx), c.getString(emailIdx));
//            ChipSpan span = new ChipSpan(c.getString(nameIdx),
//                    c.getLong(contactIdx));
            ss.setSpan(span, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        }
        return super.convertSelectionToString(selectedItem);
    }

    /**
     * <p>Performs the text completion by replacing the range from {@link android.widget.MultiAutoCompleteTextView.Tokenizer#findTokenStart}
     * to {@link #getSelectionEnd} by the the result of passing <code>text</code> through {@link
     * android.widget.MultiAutoCompleteTextView.Tokenizer#terminateToken}. In addition, the replaced region will be marked as an AutoText
     * substition so that if the user immediately presses DEL, the completion will be undone.
     * Subclasses may override this method to do some different insertion of the content into the
     * edit box.</p>
     *
     * @param text the selected suggestion in the drop down list
     */
    @Override
    protected void replaceText(CharSequence text) {
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
//        return super.enoughToFilter();
        Editable text = getText();

        int end = getSelectionEnd();
        if (end <= 0 || mTokenizer == null) {
            return false;
        }

        int start = mTokenizer.findTokenStart(text, end);
        if (!isTagPrefix(text.charAt(start))) {
            return false;
        }

        if (end - start >= getThreshold()) {
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

    public static boolean isTagPrefix(char c) {
        return (c == SYMBOL_HASHTAG || c == SYMBOL_MENTION);
    }

    public ArrayList<ChipSpan> getSpans() {
        ChipSpan[] chips = getText().getSpans(0, getText().length(), ChipSpan.class);
        return (ArrayList<ChipSpan>) Arrays.asList(chips);
    }

    public static class ChipSpan extends ReplacementSpan {

        private static DisplayMetrics sDisplayMetrics = App.getInstance().getResources()
                .getDisplayMetrics();

        private static int sSpanHeight = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, sDisplayMetrics);

        private static int sPadding = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, sDisplayMetrics);

        private String mSpanText;

        private int mLength;

        private boolean selected;

        public ChipSpan(String spanText, long contactId) {
            mSpanText = spanText;
            Uri contactUri = ContentUris
                    .withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        }

        public void toggleSelection() {
            selected = !selected;
        }

        public boolean isSelected() {
            return selected;
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

            if (selected) {
                paint.setColor(0x33ff0000);
                canvas.drawRect(x, top, x + mLength, bottom, paint);
            }
        }
    }

    public static class HashtagChipSpan extends ChipSpan {

        public String listKey;

        public HashtagChipSpan(String hashtag, String listKey) {
            super(SYMBOL_HASHTAG + hashtag, 0);
            this.listKey = listKey;
        }

    }

    public static class MentionChipSpan extends ChipSpan {

        public String accountId;

        public MentionChipSpan(String userName, String accountId) {
            super(SYMBOL_MENTION + userName, 0);
            this.accountId = accountId;
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

            Log.d(TAG, "tokenStart: " + i + " / " + text.charAt(i));
            return i;
        }

        /**
         * Returns the end of the token (minus trailing punctuation)
         * that begins at offset <code>cursor within text.
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
         * Returns <code>text, modified, if necessary, to ensure that
         * it ends with a token terminator (for example a space or comma).
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
}