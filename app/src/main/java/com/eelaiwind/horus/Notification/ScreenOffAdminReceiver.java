package com.eelaiwind.horus.notification;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Eelai Wind on 2015/9/10.
 */
public class ScreenOffAdminReceiver extends DeviceAdminReceiver {
    private void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context,"admin_receiver_status_ENabled");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context,"admin_receiver_status_DISabled");
    }

}
