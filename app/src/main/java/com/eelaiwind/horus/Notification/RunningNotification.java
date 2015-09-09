package com.eelaiwind.horus.Notification;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import com.eelaiwind.horus.PreferenceData;
import com.eelaiwind.horus.R;
import com.eelaiwind.horus.timeChart.TimeCategory;
import com.eelaiwind.horus.timeChart.TimeChartData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 監測螢幕開啟/關閉的背景service，會把結果記錄在 timeChartDatas 中，方便其他元件繪製長條圖
 * 今日的 timeChartDatas 存在 DATA_FILE_NAME 檔案中
 * 昨日的 timeChartDatas 存在 LAST_DATA_FILE_NAME 檔案中
 *
 * 其他service可以用bindService方式取得 timeChartDatas 資料
 */
public class RunningNotification extends Service{

    //ID 不要用 0 否則不會顯示通知
    private final static int ID = 1;

    public static final String DATA_FILE_NAME = "timeChartData";
    public static final String LAST_DATA_FILE_NAME = "lastTimeChartData";

    private SharedPreferences preferences;
    private long todayTime, preTime;
    private TimeCategory preState;
    private ArrayList<TimeChartData> timeChartDatas;

    private boolean isScreenON;

    private MyBinder myBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"RunningNotification : onCreate()",Toast.LENGTH_SHORT).show();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver screenReceiver = new ScreenReceiver();
        preferences = getSharedPreferences(PreferenceData.FILE_NAME,MODE_PRIVATE);

        startForeground(ID, makeNotification());

        isScreenON = true;
        initData();
        registerReceiver(screenReceiver, filter);
    }

    private void initData(){
        if  (preferences.contains(PreferenceData.TODAY_TIME) &&
                preferences.contains(PreferenceData.PRE_TIME) &&
                preferences.contains(PreferenceData.PRE_STATE)){
            Toast.makeText(this,"todayTime, preTime, preState 資料存在",Toast.LENGTH_SHORT).show();
            todayTime = preferences.getLong(PreferenceData.TODAY_TIME,0);
            preTime = preferences.getLong(PreferenceData.PRE_TIME, 0);
            try {
                preState = TimeCategory.getTimCategory(preferences.getInt(PreferenceData.PRE_STATE,0),TimeCategory.OFF);
            } catch (Exception e) {
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            File file = new File(getFilesDir(), DATA_FILE_NAME);
            if (file.exists()){
                Toast.makeText(this,"資料檔案存在",Toast.LENGTH_SHORT).show();
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                    Object obj = in.readObject();
                    timeChartDatas = (ArrayList<TimeChartData>)obj;
                    in.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                timeChartDatas = new ArrayList<>();
            }
        }
        else{
            Toast.makeText(this,"初始化 todayTime, preTime, preState",Toast.LENGTH_SHORT).show();
            DateFormat format = SimpleDateFormat.getDateInstance();
            try {
                setTodayTime(format.parse(format.format(new Date())).getTime());
            } catch (ParseException e) {
                Toast.makeText(this,"Init today time failed",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            preTime = todayTime;
            preState = TimeCategory.OFF;
            preferences.edit().
                    putLong(PreferenceData.PRE_TIME, preTime).
                    putInt(PreferenceData.PRE_STATE,preState.getIndex()).
                    apply();

            timeChartDatas = new ArrayList<>();
        }
    }

    private Notification makeNotification(){
        return new Notification.Builder(this)
                .setSmallIcon(R.drawable.eye)
                .setContentTitle("Horus 正在運行")
                .setContentText("一般模式")
                .setContentInfo("test")
                //.setDefaults(Notification.DEFAULT_VIBRATE)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"RunningNotification : onStartCommand()",Toast.LENGTH_SHORT).show();
        if (isScreenON){
            updateState(TimeCategory.BUSY);
        }
        else{
            updateState(TimeCategory.FREE);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        updateState(TimeCategory.OFF);
        stopForeground(true);
        Toast.makeText(this,"RunningNotification : onDestroy()",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    private void setTodayTime(long todayTime){
        this.todayTime = todayTime;
        preferences.edit().putLong(PreferenceData.TODAY_TIME,todayTime).apply();
    }

    private void updateState(TimeCategory nextState){

        long nextTime = System.currentTimeMillis();
        long nextDay = todayTime + 86400000;

        if (nextTime >= nextDay){
            // not in the same day
            int deltaMillisecond = (int)(nextDay - preTime);
            timeChartDatas.add(new TimeChartData(preState, deltaMillisecond));

            try {
                ObjectOutputStream out = new ObjectOutputStream(openFileOutput(LAST_DATA_FILE_NAME, MODE_PRIVATE));
                out.writeObject(timeChartDatas);
                out.close();
                preferences.edit().putLong(PreferenceData.LAST_DATA_TIME, todayTime).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                DateFormat format = SimpleDateFormat.getDateInstance();
                setTodayTime(format.parse(format.format(new Date())).getTime());
                preTime = todayTime;
                preferences.edit().putLong(PreferenceData.PRE_TIME, preTime).apply();
                timeChartDatas.clear();
            } catch (ParseException e) {
                Toast.makeText(this, "Init today time failed", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        if (nextState == preState)
            return;

        int deltaMillisecond = (int)(nextTime - preTime);
        timeChartDatas.add(new TimeChartData(preState, deltaMillisecond));

        preTime = nextTime;
        preState = nextState;

        preferences.edit().
                putLong(PreferenceData.PRE_TIME, preTime).
                putInt(PreferenceData.PRE_STATE, preState.getIndex()).
                apply();

        try {
            ObjectOutputStream out = new ObjectOutputStream(openFileOutput(DATA_FILE_NAME, MODE_PRIVATE));
            out.writeObject(timeChartDatas);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MyBinder extends Binder {

        public ArrayList<TimeChartData> getTimeChartDatas(){
            return timeChartDatas;
        }
    }

    private class ScreenReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                isScreenON = true;
                updateState(TimeCategory.BUSY);
            }
            else{
                isScreenON = false;
                updateState(TimeCategory.FREE);
            }
        }
    }
}
