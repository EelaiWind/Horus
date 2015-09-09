package com.eelaiwind.horus.timeChart;

import java.io.Serializable;

/**
 * 時間圖表的資料單位
 * timeCategory : 紀錄時間種類
 * minute : 持續的時間(millisecond)
 */

public class TimeChartData implements Serializable{

    public static final int MILLISECOND_A_DAY = 86400000;
    private  TimeCategory timeCategory;
    private int deltaMillisecond;

    public TimeChartData(TimeCategory timeCategory, int minute) {
        this.timeCategory = timeCategory;
        this.deltaMillisecond = minute;
    }

    public TimeCategory getTimeCategory() {
        return timeCategory;
    }

    public int getMillisecond() {
        return deltaMillisecond;
    }

    public void setTimeCategory(TimeCategory timeCategory) {
        this.timeCategory = timeCategory;
    }

    public void setMillisecond(int minute) {
        this.deltaMillisecond = minute;
    }

}
