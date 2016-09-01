package com.hyc.eyepetizer.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/1.
 */
public class AnimateTextView extends TextView{

    private static final int COUNT=15;
    private static final int DELAY=25;
    private static final int START_ANIM=2;
    private int mCurrentIndex;
    private CharSequence mCharSequence;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==START_ANIM) {
                if (mCurrentIndex < mCharSequence.length()) {
                    setText((getText().toString() + mCharSequence.charAt(mCurrentIndex)));
                    mCurrentIndex++;
                   mHandler.sendEmptyMessageDelayed(START_ANIM,DELAY);
                } else {
                    mHandler.removeCallbacksAndMessages(null);
                }
            }
        }
    };

    public AnimateTextView(Context context) {
        this(context,null);
    }

    public AnimateTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AnimateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void animateChar(){
        int length=getText().length();
        if (length==0) {
            return;
        }
        if (length < COUNT) {
            mCurrentIndex = 0;
            mHandler.sendEmptyMessageDelayed(START_ANIM, (COUNT - length) * DELAY);
        } else {
            mCurrentIndex=length-COUNT-1;
            mHandler.sendEmptyMessage(START_ANIM);
        }
    }

    public void setAnimText(String text){
        mCharSequence=text;
        if (TextUtils.isEmpty(text)) {
            return;
        } else {
            if (text.length()>COUNT) {
                setText(text.subSequence(0,text.length()-COUNT));
            }
        }
    }
    @Override
    public void setText(CharSequence text, BufferType type) {
        mCharSequence=text;
        if (TextUtils.isEmpty(text)) {
            return;
        } else {
            if (text.length()>COUNT) {
                super.setText(text.subSequence(0,text.length()-COUNT), type);
            }
        }
    }
}
