package com.colin.hsldemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;


/**
 * create by colin
 * 2020/5/27
 */
public class GradientSeekBar extends ThumbCenterVerticalSeekBar {
    private HSLColor.GradientModel gradientModel;
    private Paint mPaint;
    private Rect rect;
    private int gradientHeight;
    private Drawable mProgressDrawable;

    public GradientSeekBar(Context context) {
        this(context, null);
    }

    public GradientSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressDrawable = getProgressDrawable();
        init();
    }

    private void init() {
        if (gradientModel != null && rect != null) {
            setBackgroundColor(Color.TRANSPARENT);
            setProgressDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setIndeterminate(false);
        mPaint = new Paint();
        gradientHeight = getResources().getDimensionPixelSize(R.dimen.gradient_seek_height);
    }

    public void setGradientModel(@NonNull HSLColor.GradientModel gradientModel) {
        this.gradientModel = gradientModel;
        if (getWidth() == 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    innerSetGradient();
                }
            });
        } else {
            innerSetGradient();
        }
    }

    private void innerSetGradient() {
        int top = (getMeasuredHeight() - gradientHeight) / 2;
        rect = new Rect(getPaddingLeft(), top, getMeasuredWidth() - getPaddingRight(), top + gradientHeight);
        int[] colors;
        if (gradientModel.mid == 0) {
            colors = new int[]{gradientModel.start, gradientModel.end};
        } else {
            colors = new int[]{gradientModel.start, gradientModel.mid, gradientModel.end};
        }
        mPaint.setShader(new LinearGradient(rect.left, rect.top, rect.right, rect.bottom, colors,
                null, Shader.TileMode.CLAMP));
        setBackgroundColor(Color.TRANSPARENT);
        setProgressDrawable(new ColorDrawable(Color.TRANSPARENT));
        invalidate();
    }

    public void setNormalProgress() {
        this.gradientModel = null;
        this.rect = null;
        setProgressDrawable(mProgressDrawable);
        invalidate();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //绘制背景的渐变。
        if (gradientModel != null && rect != null) {
            canvas.drawRect(rect, mPaint);
        }
        super.onDraw(canvas);
    }
}
