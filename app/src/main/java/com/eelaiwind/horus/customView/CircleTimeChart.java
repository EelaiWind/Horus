package com.eelaiwind.horus.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.eelaiwind.horus.R;
import com.eelaiwind.horus.timeChart.TimeChartData;

import java.util.ArrayList;

/**
 * 圓形時間圖表
 * [attribute]
 *     radius : 圓形半徑
 *
 * 用 setDrawingDatas(...) 設定時間圖表資料
 */
public class CircleTimeChart extends View {
    private int STROKE_WIDTH = 10;
    private Paint arcPaint;
    private DrawingData[] drawingDatas = new DrawingData[0];

    private RectF rectf = new RectF();
    private int radius;

    public CircleTimeChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleTimeChart,0,0);
        radius =  typeArray.getDimensionPixelOffset(R.styleable.CircleTimeChart_radius, 10);
        STROKE_WIDTH = radius/10;
        typeArray.recycle();
        init();

    }

    public void setDrawingDatas(ArrayList<TimeChartData> timeChartData) {

        float cumulativeSum = -90;
        drawingDatas = new DrawingData[timeChartData.size()];
        for (int i = 0 ; i < timeChartData.size() ; i++){
            drawingDatas[i] = new DrawingData();
            drawingDatas[i].color = timeChartData.get(i).getTimeCategory().getColor();
            drawingDatas[i].startDegree = cumulativeSum;
            float deltaDegree = 360f*timeChartData.get(i).getMillisecond()/TimeChartData.MILLISECOND_A_DAY;
            drawingDatas[i].deltaDegree = deltaDegree;
            cumulativeSum += deltaDegree;
        }
        invalidate();
    }

    public void setDrawingDatas(TimeChartData[] timeChartData) {

        float cumulativeSum = -90;
        drawingDatas = new DrawingData[timeChartData.length];
        for (int i = 0 ; i < timeChartData.length ; i++){
            drawingDatas[i] = new DrawingData();
            drawingDatas[i].color = timeChartData[i].getTimeCategory().getColor();
            drawingDatas[i].startDegree = cumulativeSum;
            float deltaDegree = 360f*timeChartData[i].getMillisecond()/TimeChartData.MILLISECOND_A_DAY;
            drawingDatas[i].deltaDegree = deltaDegree;
            cumulativeSum += deltaDegree;
        }
        invalidate();
    }

    private void init(){
        arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(STROKE_WIDTH);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w - getPaddingLeft() - getPaddingRight();
        int height = h - getPaddingTop() - getPaddingBottom();

        rectf.set(getPaddingLeft() + STROKE_WIDTH / 2, getPaddingTop() + STROKE_WIDTH / 2, getPaddingLeft() + width - STROKE_WIDTH / 2, getPaddingTop() + height - STROKE_WIDTH / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int circleRadius = 2* (radius + STROKE_WIDTH);
        int minWidth = getPaddingLeft() + getPaddingRight() + circleRadius;
        int minHeight = getPaddingTop() + getPaddingBottom() + circleRadius;

        int w = resolveSizeAndState(minWidth,widthMeasureSpec,0);
        int h = resolveSizeAndState(minHeight,heightMeasureSpec,0);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (DrawingData data : drawingDatas){
            arcPaint.setColor(data.color);
            canvas.drawArc(rectf,data.startDegree,data.deltaDegree,false,arcPaint);
        }
    }

    private class DrawingData{
        int color;
        float startDegree, deltaDegree;
    }
}
