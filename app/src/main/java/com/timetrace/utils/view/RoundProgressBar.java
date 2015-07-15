package com.timetrace.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.timetrace.app.R;

/**
 * Created by Atomu on 2014/11/22.
 */
public class RoundProgressBar extends View {
    public static final int STROKE = 0;
    public static final int FILL = 1;

    private static final String TAG = "roundProgressbar";

    private Paint paint;
    private int roundColor;
    private int roundProgressColor;
    private int textColor;
    private float textSize;
    private float roundWidth;
    private int max = 100;
    private int progress;
    private boolean textIsDisplayable;
    private int style;

    public RoundProgressBar(Context context) {
        super(context);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        roundColor = typedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.BLUE);
        roundProgressColor = typedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        textColor = typedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GRAY);
        textSize = typedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        roundWidth = typedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        max = typedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        progress = typedArray.getInteger(R.styleable.RoundProgressBar_progress, 0);
        textIsDisplayable = typedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, false);
        style = typedArray.getInt(R.styleable.RoundProgressBar_style, 0);

        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();

        int center = getWidth() / 2;
        int radius = (int) (center - roundWidth / 2.0);
        paint.setColor(roundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roundWidth);
        paint.setAntiAlias(true);
        canvas.drawCircle(center, center, radius, paint);

        Log.i(TAG, "" + center);

        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT);
        int percent = (int) (progress * 1.0 / max * 100);
        float textWidth = paint.measureText(percent + " %");

        if (textIsDisplayable && percent != 0 && style == STROKE) {
            canvas.drawText(percent + " %", center - textWidth / 2, center + textSize / 2, paint);
        }

        paint.setStrokeWidth(roundWidth);
        paint.setColor(roundProgressColor);
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);

        switch (style) {
            case STROKE:
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, 360 * progress / max, false, paint);
                break;
            case FILL:
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0) {
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint);
                }
                break;
        }
    }

    public synchronized int getMax() {
        return max;
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max no less than 0");
        }
        this.max = max;
    }

    public synchronized int getProgress() {
        return progress;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress no less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

}
