package com.rajeshjadav.android.mvputilityapp.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import java.lang.ref.WeakReference;

/**
 * Use:
 */
public class DelayAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 500;
    private final DelayHandler mHandler = new DelayHandler(this);
    private int mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAutoCompleteDelay(int autoCompleteDelay) {
        mAutoCompleteDelay = autoCompleteDelay;
    }

    protected void filter(CharSequence text, int keyCode) {
        DelayAutoCompleteTextView.super.performFiltering(text, keyCode);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), mAutoCompleteDelay);
    }

    @Override
    public void onFilterComplete(int count) {
        super.onFilterComplete(count);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(this);
    }

    private static class DelayHandler extends Handler {
        private final WeakReference<DelayAutoCompleteTextView> delayAutoCompleteTextViewWeakReference;

        DelayHandler(DelayAutoCompleteTextView activity) {
            delayAutoCompleteTextViewWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DelayAutoCompleteTextView delayAutoCompleteTextView = delayAutoCompleteTextViewWeakReference.get();
            if (delayAutoCompleteTextView != null && msg != null) {
                delayAutoCompleteTextView.filter((CharSequence) msg.obj, msg.arg1);
            }
        }
    }

}

