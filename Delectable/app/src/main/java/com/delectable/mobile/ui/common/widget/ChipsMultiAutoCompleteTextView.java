package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.util.FontEnum;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.io.InputStream;

/**
 * Inspired by https://gist.github.com/pskink/5fe9c0bb4677c1debc5e
 */
public class ChipsMultiAutoCompleteTextView extends MultiAutoCompleteTextView
        implements MenuItem.OnMenuItemClickListener {

    private final static String TAG = ChipsMultiAutoCompleteTextView.class.getSimpleName();

    private boolean mIsDropdownShown;

    private int mSpanHeight;

    private int mPadding;

    private Drawable mDefaultUser;

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

        Resources res = getResources();
        mDefaultUser = res.getDrawable(R.drawable.ic_avatar);
        DisplayMetrics metrics = res.getDisplayMetrics();
        mSpanHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, metrics);
        mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, metrics);

        setTokenizer(new HashtagMentionTokenizer());
//        setTokenizer(new ChipsTokenizer());
        setThreshold(1);
        setMovementMethod(new ChipsArrowKeyMovementMethod());

        String[] from = {ContactsContract.Contacts.DISPLAY_NAME};
        int[] to = {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
                android.R.layout.simple_dropdown_item_1line, null, from, to);

        SimpleCursorAdapter.ViewBinder viewBinder = new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View v, Cursor c, int index) {
                TextView tv = (TextView) v;
                int nameIdx = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                int emailIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                tv.setText(c.getString(nameIdx) + " " + c.getString(emailIdx));
                return true;
            }
        };
        adapter.setViewBinder(viewBinder);

        FilterQueryProvider provider = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if (constraint == null) {
                    return null;
                }
                Uri uri = Uri
                        .withAppendedPath(ContactsContract.CommonDataKinds.Email.CONTENT_FILTER_URI,
                                constraint.toString());
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
                MenuItem item = menu.add("Delete Selected Chips");
                item.setOnMenuItemClickListener(this);
                break;
            }
        }
        super.onCreateContextMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
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

    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        //Log.d(TAG, "convertSelectionToString " + mIsDropdownShown);
        if (!mIsDropdownShown) {
            Cursor c = (Cursor) selectedItem;
            SpannableString ss = new SpannableString("x");
            int nameIdx = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int emailIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            int contactIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID);
            Object what = new ChipSpan(c.getString(nameIdx), c.getString(emailIdx),
                    c.getLong(contactIdx));
            ss.setSpan(what, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        }
        return super.convertSelectionToString(selectedItem);
    }

    class ChipSpan extends ReplacementSpan {

        private String name;

        private String email;

        private int length;

        private boolean selected;

        private Drawable drawable;

        public ChipSpan(String name, String email, long contactId) {
            this.name = name;
            this.email = email;
            Uri contactUri = ContentUris
                    .withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
            InputStream stream = ContactsContract.Contacts
                    .openContactPhotoInputStream(getContext().getContentResolver(), contactUri);
            drawable = stream != null ? new BitmapDrawable(stream) : mDefaultUser;
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
            length = (int) paint.measureText(name) + mSpanHeight + 3 * mPadding;
            if (fm != null) {
                fm.ascent = -mSpanHeight;
                fm.descent = 0;
                fm.top = fm.ascent;
                fm.bottom = 0;
            }
            // 4 (2 + 2) is for cursor at both ends
            return length + 4;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top,
                int y, int bottom, Paint paint) {
            x += 2;
            paint.setColor(0xffcccccc);
            canvas.drawRect(x, top, x + length, bottom, paint);
            paint.setColor(0xff000000);
            canvas.drawText(name, x + mPadding, y, paint);

            int right = (int) x + length - mPadding;
            drawable.setBounds(right - mSpanHeight, top, right, bottom);
            drawable.draw(canvas);

            if (selected) {
                paint.setColor(0x66ff0000);
                canvas.drawRect(x, top, x + length, bottom, paint);
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

    public static class ChipsTokenizer implements Tokenizer {

        @Override
        public int findTokenStart(CharSequence text, int cursor) {
            Log.d(TAG, "findTokenStart [" + text + "] " + cursor);
            int i = cursor;
            while (i > 0 && text.charAt(i - 1) != ' ') {
                i--;
            }
            return i;
        }

        @Override
        public int findTokenEnd(CharSequence text, int cursor) {
            Log.d(TAG, "findTokenEnd [" + text + "] " + cursor);
            return text.length();
        }

        @Override
        public CharSequence terminateToken(CharSequence text) {
            Log.d(TAG, "terminateToken [" + text + "]");
            if (text instanceof Spanned) {
                SpannableStringBuilder s = new SpannableStringBuilder(text);
                s.append(" ");
                return s;
            }
            return text + " ";
        }
    }

    /**
     * Tokenizer to support completion of @user and #hashtag
     */
    public static class HashtagMentionTokenizer implements Tokenizer {

        @Override
        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;

            while (i > 0 && (text.charAt(i - 1) != '@') && text.charAt(i - 1) != '#') {
                i--;
            }
//            if (i>0)
//                return i-1;
//            else
            return i;
        }

        @Override
        public int findTokenEnd(CharSequence text, int cursor) {
            Log.d(TAG, "fte:" + text + "," + cursor);
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (text.charAt(i) == '@') {
                    return i;
                } else {
                    i++;
                }
            }

            return len;

        }

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