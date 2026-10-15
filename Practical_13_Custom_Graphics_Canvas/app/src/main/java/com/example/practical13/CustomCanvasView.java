package com.example.practical13;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Custom View demonstrating the Android 2D Canvas API.
 * Draws shapes (circles, rectangles, lines, arcs, text) using specialized Paint objects,
 * and handles user touch input with automatic view invalidation.
 */
public class CustomCanvasView extends View {

    private Paint paintBackground;
    private Paint paintRect;
    private Paint paintCircle;
    private Paint paintCircleOutline;
    private Paint paintLine;
    private Paint paintArc;
    private Paint paintText;
    private Paint paintTouchMarker;

    // Interactive touch coordinates
    private float touchX = -100;
    private float touchY = -100;
    private boolean isAlternateStyle = false;

    public CustomCanvasView(Context context) {
        super(context);
        init();
    }

    public CustomCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initializes all Paint objects once to avoid object allocation in onDraw().
     */
    private void init() {
        // 1. Grid/Background Paint
        paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBackground.setColor(Color.parseColor("#FFFDFD"));
        paintBackground.setStyle(Paint.Style.FILL);

        // 2. Rectangle Paint
        paintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintRect.setColor(Color.parseColor("#2196F3")); // Material Blue
        paintRect.setStyle(Paint.Style.FILL);

        // 3. Circle Paint (Solid)
        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(Color.parseColor("#E91E63")); // Material Pink
        paintCircle.setStyle(Paint.Style.FILL);

        // 4. Circle Paint (Stroked / Concentric)
        paintCircleOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircleOutline.setColor(Color.parseColor("#FFC107")); // Amber
        paintCircleOutline.setStyle(Paint.Style.STROKE);
        paintCircleOutline.setStrokeWidth(6f);

        // 5. Line Paint
        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setColor(Color.parseColor("#4CAF50")); // Green
        paintLine.setStrokeWidth(8f);
        paintLine.setStrokeCap(Paint.Cap.ROUND);

        // 6. Arc / Pie Paint
        paintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintArc.setColor(Color.parseColor("#FF9800")); // Orange
        paintArc.setStyle(Paint.Style.FILL);

        // 7. Text Paint
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.parseColor("#3700B3")); // Deep Purple
        paintText.setTextSize(36f);
        paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // 8. Interactive Touch Marker Paint
        paintTouchMarker = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTouchMarker.setColor(Color.parseColor("#6200EE"));
        paintTouchMarker.setStyle(Paint.Style.STROKE);
        paintTouchMarker.setStrokeWidth(5f);
    }

    /**
     * Toggles between standard and alternate color schemes.
     */
    public void toggleStyle() {
        isAlternateStyle = !isAlternateStyle;
        if (isAlternateStyle) {
            paintRect.setColor(Color.parseColor("#009688")); // Teal
            paintCircle.setColor(Color.parseColor("#9C27B0")); // Purple
            paintArc.setColor(Color.parseColor("#FF5722")); // Deep Orange
        } else {
            paintRect.setColor(Color.parseColor("#2196F3")); // Blue
            paintCircle.setColor(Color.parseColor("#E91E63")); // Pink
            paintArc.setColor(Color.parseColor("#FF9800")); // Orange
        }
        // Request system to redraw this view
        invalidate();
    }

    /**
     * Resets touch marker position.
     */
    public void resetCanvas() {
        touchX = -100;
        touchY = -100;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // 1. Draw Canvas Background
        canvas.drawRect(0, 0, width, height, paintBackground);

        // 2. Draw Title Text
        canvas.drawText("Android 2D Canvas Graphics", 40f, 60f, paintText);

        // 3. Draw Rounded Rectangle
        RectF roundRect = new RectF(40f, 100f, width * 0.45f, 280f);
        canvas.drawRoundRect(roundRect, 20f, 20f, paintRect);

        // 4. Draw Concentric Circles
        float circleCenterX = width * 0.72f;
        float circleCenterY = 190f;
        canvas.drawCircle(circleCenterX, circleCenterY, 70f, paintCircle);
        canvas.drawCircle(circleCenterX, circleCenterY, 85f, paintCircleOutline);

        // 5. Draw Diagonal Accent Line
        canvas.drawLine(40f, 320f, width - 40f, 320f, paintLine);

        // 6. Draw Arc / Pie Slice
        RectF arcBounds = new RectF(40f, 360f, 260f, 580f);
        // drawArc(bounds, startAngle, sweepAngle, useCenter, paint)
        canvas.drawArc(arcBounds, 0f, 270f, true, paintArc);

        // 7. Draw Descriptive Labels
        Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setColor(Color.DKGRAY);
        labelPaint.setTextSize(26f);

        canvas.drawText("drawRoundRect()", 50f, 200f, labelPaint);
        canvas.drawText("drawCircle()", circleCenterX - 60f, circleCenterY + 5f, labelPaint);
        canvas.drawText("drawArc() 270 deg", 280f, 480f, labelPaint);

        // 8. Draw Interactive Touch Indicator
        if (touchX > 0 && touchY > 0) {
            canvas.drawCircle(touchX, touchY, 35f, paintTouchMarker);
            Paint touchCore = new Paint(Paint.ANTI_ALIAS_FLAG);
            touchCore.setColor(Color.parseColor("#6200EE"));
            touchCore.setStyle(Paint.Style.FILL);
            canvas.drawCircle(touchX, touchY, 12f, touchCore);

            canvas.drawText("Touch: (" + (int) touchX + ", " + (int) touchY + ")",
                    Math.max(20f, touchX - 80f),
                    Math.max(40f, touchY - 45f),
                    labelPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                touchX = event.getX();
                touchY = event.getY();
                // Request Android rendering pipeline to invoke onDraw()
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                return true;
        }
        return super.onTouchEvent(event);
    }
}
