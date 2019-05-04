package com.urfa.ui.custom;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class DotView extends RelativeLayout {
    private Paint mPaint;
    private int radius = 3;
    private float gapLength = 4.65f;
    private NotDrawableZone nonDrawableZone = NotDrawableZone.NONE;

    public DotView(Context context) {
        super(context);
        init();
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#DFDFDF"));
        mPaint.setStyle(Paint.Style.FILL);
        this.setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (nonDrawableZone == NotDrawableZone.ALL)
            return;
        int startPosition = radius;
        int endPosition = getHeight();

        View child = null;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            break;
        }

        if (child != null && nonDrawableZone != NotDrawableZone.NONE) {
            if (nonDrawableZone == NotDrawableZone.TOP) {
                startPosition = child.getBottom();
            } else {
                endPosition = child.getTop();
            }
        }

        int x = getWidth() / 2;
        for (int i = 0; i + ((int) gapLength) < endPosition; i = i + ((int) gapLength)) {
            canvas.drawCircle(x, startPosition + i * gapLength, radius, mPaint);
        }
    }

    public void setNonDrawableZone(NotDrawableZone other) {
        this.nonDrawableZone = other;
        invalidate();
    }

    public enum NotDrawableZone {
        NONE, TOP, BOTTOM, ALL
    }

}