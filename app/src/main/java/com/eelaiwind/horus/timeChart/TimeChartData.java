package com.eelaiwind.horus.timeChart;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Eelai Wind on 2015/8/24.
 */
public class TimeChartData implements Parcelable{
    TimeCategory timeCategory;
    int minute;

    public TimeChartData(){

    }

    public TimeChartData(TimeCategory timeCategory, int minute) {
        this.timeCategory = timeCategory;
        this.minute = minute;
    }

    public TimeCategory getTimeCategory() {
        return timeCategory;
    }

    public int getMinute() {
        return minute;
    }

    public void setTimeCategory(TimeCategory timeCategory) {
        this.timeCategory = timeCategory;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public static TimeChartData[] converTimeChartData(TimeCategory[] timeCatagory, int[] timeInterval) throws Exception {
        if (timeCatagory.length != timeInterval.length){
            throw new Exception("time chart data arrays' length are different");
        }
        TimeChartData[] timeChartData = new TimeChartData[timeCatagory.length];
        for (int i = 0 ; i < timeCatagory.length ; i++){
            timeChartData[i] = new TimeChartData();
            timeChartData[i].timeCategory = timeCatagory[i];
            timeChartData[i].minute = timeInterval[i];
        }
        return timeChartData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(timeCategory);
        dest.writeInt(minute);
    }

    public static final Parcelable.Creator<TimeChartData> CREATOR
            = new Parcelable.Creator<TimeChartData>() {
        public TimeChartData createFromParcel(Parcel in) {
            return new TimeChartData(in);
        }

        public TimeChartData[] newArray(int size) {
            return new TimeChartData[size];
        }
    };

    private TimeChartData(Parcel in) {
        timeCategory = (TimeCategory) in.readSerializable();
        minute = in.readInt();
    }
}
