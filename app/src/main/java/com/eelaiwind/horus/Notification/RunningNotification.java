package com.eelaiwind.horus.Notification;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.eelaiwind.horus.R;

/**
 * Created by Eelai Wind on 2015/8/26.
 */
public class RunningNotification extends Service{

    //ID 不要用 0 否則不會顯示通知
    private final static int ID = 1;
    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(ID, makeNotification());
        Toast.makeText(this,"RunningNotification : onCreate()",Toast.LENGTH_SHORT).show();
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        Toast.makeText(this,"RunningNotification : onDestroy()",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
