package com.hyc.eyepetizer.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/9/1.
 */
public class AnimateTextView extends CustomTextView {

    private static final int COUNT = 10;
    private static final int DELAY = 16;
    private static final int TIME = 600;
    private static final int START_ANIM = 2;
    private float mCurrentIndex;
    private CharSequence mCharSequence;
    private long delayTime;
    private float addtionCount;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == START_ANIM) {
                if (mCurrentIndex < mCharSequence.length()) {
                    setText(mCharSequence.subSequence(0, (int) mCurrentIndex));
                    mCurrentIndex += addtionCount;
                    mHandler.sendEmptyMessageDelayed(START_ANIM, delayTime);
                } else {
                    setText(mCharSequence);
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
    }


    public void animateChar(long delay) {
        int length = mCharSequence.length();
        if (length == 0) {
            return;
        }
            mCurrentIndex = 0;
            mHandler.sendEmptyMessageDelayed(START_ANIM, delay);
    }


    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //animateChar(200);
    }


    public void animateChar() {
        animateChar(0);
    }


    public void setAnimText(String text) {
        mCharSequence = text;
        if (TextUtils.isEmpty(text)) {
            return;
        } else {
            int length = text.length();
            if (TIME / length < 16) {
                addtionCount = length / (TIME * 1f / 16);
                delayTime = 16;
            } else {
                delayTime = TIME / length;
                addtionCount = 1;
            }
        }
    }


    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }
}
