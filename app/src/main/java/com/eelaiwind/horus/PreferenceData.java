package com.eelaiwind.horus;

import android.content.SharedPreferences;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 記錄所有 sharedPreference 的 key
 */
public final class PreferenceData {
    public final static String FILE_NAME ="com.eelaiwind.horus.fileName";
    public final static String TOTAL_SWITCH = "com.eelaiwind.horus.totalSwitch";
    public final static String TODAY_TIME = "com.eelaiwind.todayTime";
    public final static String PRE_TIME = "com.eelaiwind.preTime";
    public final static String PRE_STATE = "com.eelaiwind.preState";
    public final static String LAST_DATA_TIME = "com.eelaiwind.lastDataTime";
    public final static String SLEEP_TIME_HOUR = "com.eelaiwind.sleepTimeHour";
    public final static String SLEEP_TIME_MINUTE = "com.eelaiwind.sleepTimeMinute";
    public final static String SLEEP_TIME = "com.eelaiwind.sleepTime";
    public final static String WAKE_TIME_HOUR = "com.eelaiwind.wakeTimeHour";
    public final static String WAKE_TIME_MINUTE = "com.eelaiwind.wakeTimeMinute";
    public final static String WAKE_TIME ="com.eelaiwind.wakeTime";

    public static void UpdateSleepAlarmTime(SharedPreferences pref, int sleepHour, int sleepMinute, int wakeHour, int wakeMinute){
        Calendar sleepTime = Calendar.getInstance();
        sleepTime.set(Calendar.HOUR_OF_DAY,sleepHour);
        sleepTime.set(Calendar.MINUTE,sleepMinute);
        sleepTime.set(Calendar.SECOND,0);
        sleepTime.set(Calendar.MILLISECOND, 0);

        Calendar wakeTime = Calendar.getInstance();
        wakeTime.set(Calendar.HOUR_OF_DAY,wakeHour);
        wakeTime.set(Calendar.MINUTE,wakeMinute);
        wakeTime.set(Calendar.SECOND,0);
        wakeTime.set(Calendar.MILLISECOND,0);

        if (wakeTime.before(sleepTime)){
            wakeTime.roll(Calendar.DATE,1);
        }

        if (wakeTime.before(Calendar.getInstance())){
            sleepTime.roll(Calendar.DATE,1);
            wakeTime.roll(Calendar.DATE,1);
        }

        DateFormat format = SimpleDateFormat.getDateTimeInstance();
        Log.d("EELAI_WIND", "下次睡覺時間 : " + format.format(sleepTime.getTime()));
        Log.d("EELAI_WIND", "下次起床時間 : " + format.format(wakeTime.getTime()));
        pref.edit().
                putInt(PreferenceData.WAKE_TIME_HOUR,wakeHour).
                putInt(PreferenceData.WAKE_TIME_MINUTE,wakeMinute).
                putInt(PreferenceData.SLEEP_TIME_HOUR,sleepHour).
                putInt(PreferenceData.SLEEP_TIME_MINUTE,sleepMinute).
                putLong(PreferenceData.SLEEP_TIME,sleepTime.getTimeInMillis()).
                putLong(PreferenceData.WAKE_TIME,wakeTime.getTimeInMillis()).
                apply();
    }
}
