package com.eelaiwind.horus.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.eelaiwind.horus.R;

/**
 *      繪製進度條的View，可以設定顏色 、最大值、目前數值
 */
public class ColorBar extends View {
    private Paint backgroundPaint, barPaint, textPaint;
    private int width, height;
    private int color, value,max;
    private static final float strokeWidth = 3f;
    private RectF backgroundBar, valueBar;
    private Rect textBound = new Rect();
    public ColorBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorBar,0,0);
        color = typeArray.getColor(R.styleable.ColorBar_barColor,0xffff0000);
        max = typeArray.getInt(R.styleable.ColorBar_maxValue, 100);
        value = typeArray.getInt(R.styleable.ColorBar_nowValue,50);
        typeArray.recycle();
        init();
    }
    private void init(){
        backgroundBar = new RectF();
        valueBar = new RectF();

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setColor(0xff000000);

        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setColor(color);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xff000000);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        width = w - getPaddingLeft() - getPaddingRight();
        height = h - getPaddingTop() - getPaddingBottom();
        float progress = (width * value / max);
        valueBar.set(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + progress, getPaddingTop() + height);
        backgroundBar.set(getPaddingLeft()+strokeWidth/2,getPaddingTop()+strokeWidth/2,getPaddingLeft()+width-strokeWidth/2,getPaddingTop()+height-strokeWidth/2);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawRoundRect(valueBar,height/2,height/2, barPaint);
        canvas.drawRoundRect(backgroundBar, height / 2, height / 2, backgroundPaint);
        String str = String.valueOf(value)+"/"+String.valueOf(max);
        textPaint.setTextSize(height*0.6f);
        textPaint.getTextBounds(str,0,str.length(),textBound);
        canvas.drawText(str,width/2,getPaddingTop()+(height+textBound.height())/2,textPaint);
    }
}


