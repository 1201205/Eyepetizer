package com.hyc.eyepetizer.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.hyc.eyepetizer.utils.AppUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ray on 16/8/28.
 */
public class LoadingAnimView extends View {
    private static final int MIN_HEIGHT = 115;
    private static final int MIN_WIDTH = 110;
    private Paint mPaint;
    private float mArc;
    private ValueAnimator mAnimator;
    private Path mPath;
    private float mSpaceWidth;
    private float mMinHeight;
    private float mMinWidth;
    private float mHeight;
    private float mWidth;
    private List<PointF> mPoints;
    private boolean mIsLoading;

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        float width=mWidth = MeasureSpec.getSize(widthMeasureSpec);
        float height=mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mMinHeight= AppUtil.dip2px(MIN_HEIGHT);
        mMinWidth= AppUtil.dip2px(MIN_WIDTH);

        if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = Math.min(width, mMinWidth);
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = Math.min(height, mMinHeight);
        }

        //选择基准
        if (mHeight / MIN_HEIGHT > mWidth / MIN_WIDTH) {
            mSpaceWidth=mWidth/22;
            mHeight=mSpaceWidth*23;
        } else {
            mSpaceWidth=mHeight/23;
            mWidth=mSpaceWidth*22;
        }
        setMeasuredDimension((int)mWidth, (int)mHeight);
        //Log.e("hyc--test"+toString(),mWidth+"----"+mHeight+"---"+mSpaceWidth);
    }


    private void initPoints() {
        if (mPoints != null) {
            mPoints.clear();
        } else {
            mPoints = new ArrayList<>();
        }
        mPoints.add(new PointF(mSpaceWidth * 2, mSpaceWidth * 10));
        mPoints.add(new PointF(mSpaceWidth * 11, 1 * mSpaceWidth));
        mPoints.add(new PointF(mSpaceWidth * 20, mSpaceWidth * 10));
        mPoints.add(new PointF(mSpaceWidth * 21.5f, mSpaceWidth * 11.5f));
        mPoints.add(new PointF(mSpaceWidth * 20, mSpaceWidth * 13));
        mPoints.add(new PointF(mSpaceWidth * 11, mSpaceWidth * 22));
        mPoints.add(new PointF(mSpaceWidth * 2, mSpaceWidth * 13));
        mPoints.add(new PointF(mSpaceWidth / 2, mSpaceWidth * 11.5f));
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight=h;
        mWidth = w;
        initPoints();
        //Log.e("hyc-test2"+toString(),mWidth+"_----"+mHeight);
    }


    public void round(int round) {
        mArc += round;
        invalidate();
    }
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        //canvas.drawPath();
//        mPaint.setColor(Color.BLACK);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPath.moveTo(20, 50);
//        mPath.quadTo(110, -40, 200, 50);
//        mPath.quadTo(215, 65, 200, 80);
//        mPath.quadTo(110, 170, 20, 80);
//        mPath.quadTo(5, 65, 20, 50);
//        mPath.moveTo(20, 100);
//        mPath.quadTo(110, 10, 200, 100);
//        mPath.quadTo(215, 115, 200, 130);
//        mPath.quadTo(110, 220, 20, 130);
//        mPath.quadTo(5, 115, 20, 100);
//        canvas.drawPath(mPath, mPaint);
//        mPaint.setColor(Color.WHITE);
//        mPaint.setStrokeWidth(10);
//        mPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(110, 115, 30, mPaint);
        //        canvas.drawCircle((float) (110 + 20 * Math.sin(mArc * Math.PI / 180)),
        //                (float) (110 + 20 * Math.cos(mArc * Math.PI / 180)), 5, mPaint);
        mPath.moveTo(mPoints.get(0).x, mPoints.get(0).y);
        mPath.quadTo(mPoints.get(1).x, mPoints.get(1).y, mPoints.get(2).x, mPoints.get(2).y);
        mPath.quadTo(mPoints.get(3).x, mPoints.get(3).y, mPoints.get(4).x, mPoints.get(4).y);
        mPath.quadTo(mPoints.get(5).x, mPoints.get(5).y, mPoints.get(6).x, mPoints.get(6).y);
        mPath.quadTo(mPoints.get(7).x, mPoints.get(7).y, mPoints.get(0).x, mPoints.get(0).y);
        canvas.drawPath(mPath, mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mSpaceWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mSpaceWidth * 11, mSpaceWidth * 11.5f, mSpaceWidth * 3, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(
            (float) (mSpaceWidth * 11 + mSpaceWidth * 2 * Math.sin(mArc * Math.PI / 180)),
            (float) (mSpaceWidth * 11.15 + mSpaceWidth * 2 * Math.cos(mArc * Math.PI / 180)),
            mSpaceWidth, mPaint);
        //canvas.drawArc(10,10,210,110,0f,180,true,mPaint);
        //canvas.drawArc(0,0,200,100,-180,180,true,mPaint);
        mPath.reset();
    }


    public void startAnim() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0, 1);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mArc = -360 * valueAnimator.getAnimatedFraction();
                    postInvalidate();
                }
            });
            mAnimator.setTarget(this);
            mAnimator.setDuration(800);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        }
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.start();
        mIsLoading = true;
    }


    public void stopAnim() {
        if (mAnimator != null) {
            mAnimator.end();
        }
        mIsLoading = false;
    }


    public boolean isLoading() {
        return mIsLoading;
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null) {
            mAnimator.removeAllUpdateListeners();
        }
    }
}
