package com.hyc.eyepetizer.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by ray on 16/8/28.
 */
public class LoadingAnimView extends View {
    private Paint mPaint;
    private float arc;
    private ValueAnimator mAnimator;
    private Path mPath;


    public LoadingAnimView(Context context) {
        this(context, null);
    }


    public LoadingAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LoadingAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
    }


    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawPath();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPath.moveTo(20, 100);
        mPath.quadTo(110, 10, 200, 100);
        mPath.quadTo(215, 115, 200, 130);
        mPath.quadTo(110, 220, 20, 130);
        mPath.quadTo(5, 115, 20, 100);
        canvas.drawPath(mPath, mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(110, 115, 30, mPaint);
        canvas.drawCircle((float) (110 + 20 * Math.sin(arc * Math.PI / 180)),
            (float) (110 + 20 * Math.cos(arc * Math.PI / 180)), 5, mPaint);
        //canvas.drawArc(10,10,210,110,0f,180,true,mPaint);
        //canvas.drawArc(0,0,200,100,-180,180,true,mPaint);
        mPath.reset();
    }


    public void startAnim() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0, 1);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    arc = -360 * valueAnimator.getAnimatedFraction();
                    postInvalidate();
                }
            });
            mAnimator.setTarget(this);
            mAnimator.setDuration(800);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        }
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
    }


    public void stopAnim() {
        if (mAnimator != null) {
            mAnimator.end();
        }
    }


    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimator.removeAllUpdateListeners();
    }
}
