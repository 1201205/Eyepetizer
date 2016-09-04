package com.hyc.eyepetizer.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Administrator on 2016/9/1.
 */
public class AnimateTextView extends CustomTextView {

    private static final int COUNT = 15;
    private static final int DELAY = 25;
    private static final int START_ANIM = 2;
    private int mCurrentIndex;
    private CharSequence mCharSequence;
    private StringBuilder mBuilder;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == START_ANIM) {
                if (mCurrentIndex < mCharSequence.length()) {
                    setText(mBuilder.append(mCharSequence.charAt(mCurrentIndex)).toString());
                    mCurrentIndex++;
                    mHandler.sendEmptyMessageDelayed(START_ANIM, DELAY);
                } else {
                    mHandler.removeCallbacksAndMessages(null);
                }
            }
        }
    };


    public AnimateTextView(Context context) {
        this(context, null);
    }


    public AnimateTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public AnimateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBuilder = new StringBuilder();
    }


    public void animateChar(long delay) {
        int builderLength = mBuilder.length();
        if (builderLength > 0) {
            mBuilder.delete(0, builderLength);
        }
        int length = mCharSequence.length();
        if (length == 0) {
            return;
        }
        if (length <= COUNT) {
            mCurrentIndex = 0;
            mHandler.sendEmptyMessageDelayed(START_ANIM, (COUNT - length) * DELAY + delay);
        } else {
            mCurrentIndex = length - COUNT - 1;
            mBuilder.append(mCharSequence.subSequence(0, mCurrentIndex));
            mHandler.sendEmptyMessageDelayed(START_ANIM, delay);
        }
    }


    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animateChar(200);
    }


    public void animateChar() {
        animateChar(0);
    }


    public void setAnimText(String text) {
        mCharSequence = text;
        if (TextUtils.isEmpty(text)) {
            return;
        } else {
            if (text.length() > COUNT) {
                Log.e("hyc-test", text.subSequence(0, text.length() - COUNT) + "_--");
                setText(text.subSequence(0, text.length() - COUNT));
            }
        }
    }


    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }
}
