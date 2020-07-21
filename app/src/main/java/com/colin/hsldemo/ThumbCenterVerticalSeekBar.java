package com.colin.hsldemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.AbsSeekBar;

import java.lang.reflect.Field;

/**
 * 低版本的进度条，thumb的默认位置是对齐至top的，不拉伸，
 * 做修正后，thumb上下居中对齐。
 *
 * @author <a href="mailto:610028256@qq.com">PanDengke</a>
 * @version v1.0
 * @create 2016/2/2 10:44
 * @update 2016/2/2 10:44
 * @since v1.0
 */
public class ThumbCenterVerticalSeekBar extends android.support.v7.widget.AppCompatSeekBar {
    public ThumbCenterVerticalSeekBar(Context context) {
        this(context, null);
    }

    public ThumbCenterVerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbCenterVerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Drawable getFixedThumb() {
        Drawable thumb = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            thumb = getThumb();
        } else {
            try {
                Field f = AbsSeekBar.class.getDeclaredField("mThumb");
                f.setAccessible(true);
                thumb = (Drawable) f.get(this);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return thumb;
    }

    public void fixThumbRect() {
        Drawable thumb = getFixedThumb();
        if (thumb != null) {
            int avaliable = getHeight() - getPaddingBottom() - getPaddingTop();
            int drawHeight = thumb.getIntrinsicHeight();

            if(thumb instanceof StateListDrawable){
                StateListDrawable slDrawable = (StateListDrawable) thumb;
                thumb = slDrawable.getCurrent();
            }

            Rect bounds = thumb.getBounds();
            bounds.top = (avaliable - drawHeight) / 2;
            bounds.bottom = (avaliable + drawHeight) / 2;
            thumb.setBounds(bounds);
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        fixThumbRect();
        super.onDraw(canvas);
    }
}
