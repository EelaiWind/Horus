package com.eelaiwind.horus.timeChart;

import android.graphics.Paint;

import java.io.Serializable;

/**
 * Created by Eelai Wind on 2015/8/24.
 */
public enum TimeCategory{
    BUSY(0,"busy",0xffFF0000),
    FREE(1,"free",0xff00FF00),
    OFF(2,"off",0xffAAAAAA);

    private int index, color;
    private String name;

    TimeCategory(int index,String name,int color){
        this.color = color;
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public TimeCategory getTimCategory(int index) throws Exception{
        for (TimeCategory temp : TimeCategory.values()){
            if (temp.getIndex() == index){
                return temp;
            }
        }
        throw new Exception(String.valueOf(index)+" is not an index in TimeCategory");
    }
}
