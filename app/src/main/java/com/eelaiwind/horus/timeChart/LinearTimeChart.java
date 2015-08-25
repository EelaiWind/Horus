package com.eelaiwind.horus.timeChart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Eelai Wind on 2015/8/24.
 */
public class LinearTimeChart extends View{

    private DrawingData[] drawingDatas = new DrawingData[0];
    private TimeChartData[] timeChartDatas;
    private int width, height;
    private Paint linePaint;
    public LinearTimeChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.FILL);
    }

    public void setDrawingDatas(TimeChartData[] timeChartDatas) {
        this.timeChartDatas = timeChartDatas;
        drawingDatas = new DrawingData[timeChartDatas.length];
        updateDrawingData();
        invalidate();
    }

    private void updateDrawingData(){
        float cumulativeSum = getPaddingLeft();
        for (int i = 0 ; i < timeChartDatas.length ; i++){
            drawingDatas[i] = new DrawingData();
            drawingDatas[i].color = timeChartDatas[i].getTimeCategory().getColor();
            drawingDatas[i].left = cumulativeSum;
            cumulativeSum += ((float)(this.width * timeChartDatas[i].getMinute()))/1440;
            drawingDatas[i].right = cumulativeSum;
            drawingDatas[i].top = getPaddingTop();
            drawingDatas[i].bottom = getPaddingTop()+height;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w - getPaddingLeft() - getPaddingRight();
        height = h - getPaddingTop() - getPaddingBottom();
        updateDrawingData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (DrawingData data : drawingDatas){
            linePaint.setColor(data.color);
            canvas.drawRect(data.left,data.top,data.right,data.bottom,linePaint);
        }
    }

    private class DrawingData{
        int color;
        float left,top,right,bottom;
    }
}
