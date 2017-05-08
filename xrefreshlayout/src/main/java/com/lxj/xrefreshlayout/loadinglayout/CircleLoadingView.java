package com.lxj.xrefreshlayout.loadinglayout;

import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lxj.xrefreshlayout.util.DensityUtil;

/**
 * Created by dance on 2017/5/8.
 */

public class CircleLoadingView extends View {
    private int progressColor = Color.BLUE;
    private int progressBorderWidth = 4;
    private float progress = 0;
    private float start = 270;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    IntEvaluator intEval = new IntEvaluator();
    ArgbEvaluator argbEval = new ArgbEvaluator();
    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        progressBorderWidth = DensityUtil.dip2px(context,4);
        setPaint();
    }

    private void setPaint() {
        paint.setColor(progressColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(progressBorderWidth);
    }

    RectF rect = null;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(rect==null){
            rect = new RectF(progressBorderWidth,progressBorderWidth,getWidth()-progressBorderWidth,
                    getHeight()-progressBorderWidth);
        }
        paint.setAlpha(intEval.evaluate(progress,30,255));
        float p = 360*progress;
        canvas.drawArc(rect, start, p, true, paint);
    }

    /**
     * 设置起始角度
     * @param start
     */
    public void setStart(float start){
        this.start = start;
    }

    /**
     * 进度为0-1
     * @param progress
     */
    public void setProgress(float progress){
        this.progress = progress;
        invalidate();
    }

    /**
     * 默认为Color.RED
     * @param color
     */
    public void setProgressColor(int color){
        this.progressColor = color;
        paint.setColor(progressColor);
    }

    public void setProgressBorderWidth(int borderWidth){
        this.progressBorderWidth = borderWidth;
    }
}
