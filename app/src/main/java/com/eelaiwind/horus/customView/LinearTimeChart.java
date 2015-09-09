package com.eelaiwind.horus.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.eelaiwind.horus.timeChart.TimeChartData;

import java.util.ArrayList;

/**
 * 線型時間圖表
 *
 * 用 setDrawingDatas(...) 設定時間圖表資料
 */
public class LinearTimeChart extends View{

    private DrawingData[] drawingDatas;
    private TimeChartData[] timeChartDatas;
    private int width, height;
    private Paint linePaint;
    public LinearTimeChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.FILL);
        timeChartDatas = new TimeChartData[0];
        drawingDatas = new DrawingData[0];
    }

    public void setDrawingDatas(TimeChartData[] timeChartDatas) {
        this.timeChartDatas = timeChartDatas;
        drawingDatas = new DrawingData[timeChartDatas.length];
        updateDrawingData();
        invalidate();
    }

    public void setDrawingDatas(ArrayList<TimeChartData> timeChartDatas) {
        this.timeChartDatas = new TimeChartData[timeChartDatas.size()];
        this.timeChartDatas = timeChartDatas.toArray(this.timeChartDatas);
        drawingDatas = new DrawingData[timeChartDatas.size()];
        updateDrawingData();
        invalidate();
    }

    private void updateDrawingData(){
        float cumulativeSum = getPaddingLeft();
        for (int i = 0 ; i < timeChartDatas.length ; i++){
            drawingDatas[i] = new DrawingData();
            drawingDatas[i].color = timeChartDatas[i].getTimeCategory().getColor();
            drawingDatas[i].left = cumulativeSum;
            cumulativeSum += ((float)(this.width * timeChartDatas[i].getMillisecond()))/TimeChartData.MILLISECOND_A_DAY;
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
