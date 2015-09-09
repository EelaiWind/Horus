package com.eelaiwind.horus.page;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eelaiwind.horus.notification.RunningNotification;
import com.eelaiwind.horus.PreferenceData;
import com.eelaiwind.horus.R;
import com.eelaiwind.horus.customView.CircleTimeChart;
import com.eelaiwind.horus.timeChart.TimeCategory;
import com.eelaiwind.horus.timeChart.TimeChartData;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class HomePage extends Activity {

    private final static String ON = "Turn ON", OFF = "Turn OFF";
    private SharedPreferences preference;
    private Button totalSwitch;
    private CircleTimeChart circleTimeChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        preference = getSharedPreferences(PreferenceData.FILE_NAME,MODE_PRIVATE);
        totalSwitch = (Button) findViewById(R.id.btn_total);
        circleTimeChart = ((CircleTimeChart) findViewById(R.id.circle_time_chart));

        if (preference.getBoolean(PreferenceData.TOTAL_SWITCH,false)){
            turnOnTotalSwitch();
        }
        else{
            turnOffTotalSwitch();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCircleTimeChart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changePage(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.btn_record:
                intent = new Intent(this,RecordPage.class);
                startActivity(intent);
                break;
            case R.id.btn_debug:
                intent = new Intent(this,DebugPage.class);
                startActivity(intent);
                break;
            case  R.id.btn_setting:
                intent = new Intent(this,SettingPage.class);
                startActivity(intent);
                break;
        }
    }

    public void reset(View v){
        turnOffTotalSwitch();
        getSharedPreferences(PreferenceData.FILE_NAME,MODE_PRIVATE).edit().clear().apply();
        File dataFile = new File(getFilesDir(),RunningNotification.DATA_FILE_NAME);
        if (dataFile.exists()){
            dataFile.delete();
        }
    }

    public void totalSwitch(View v){
      if (isTotalSwitchOn())
          turnOffTotalSwitch();
        else
          turnOnTotalSwitch();
    }

    private boolean isTotalSwitchOn(){
       return preference.getBoolean(PreferenceData.TOTAL_SWITCH,false);
    }

    private void turnOnTotalSwitch(){
        Intent intent = new Intent(this, RunningNotification.class);
        startService(intent);
        totalSwitch.setText(OFF);
        preference.edit().putBoolean(PreferenceData.TOTAL_SWITCH, true).apply();
        updateCircleTimeChart();
    }

    private void turnOffTotalSwitch(){
        Intent intent = new Intent(this, RunningNotification.class);
        stopService(intent);
        totalSwitch.setText(ON);
        preference.edit().putBoolean(PreferenceData.TOTAL_SWITCH,false).apply();
    }

    private ServiceConnection timeChartDataConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(HomePage.this,"onServiceConnected",Toast.LENGTH_SHORT).show();
            ArrayList<TimeChartData> dataList = ((RunningNotification.MyBinder) service).getTimeChartDatas();
            TimeCategory preState = TimeCategory.getTimCategory(preference.getInt(PreferenceData.PRE_STATE, 0), TimeCategory.UNKNOWN);
            int deltaTime = (int)(System.currentTimeMillis() - preference.getLong(PreferenceData.PRE_TIME,0));
            dataList.add(new TimeChartData(preState,deltaTime));
            circleTimeChart.setDrawingDatas(dataList);
            unbindService(this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(HomePage.this, "onServiceDisconnected", Toast.LENGTH_SHORT).show();
        }
    };

    private void updateCircleTimeChart(){

        if (isTotalSwitchOn()) {
            Intent intent = new Intent(this, RunningNotification.class);
            bindService(intent, timeChartDataConnection, BIND_AUTO_CREATE);
        }
        else{
            try {
                ObjectInputStream in = new ObjectInputStream(openFileInput(RunningNotification.DATA_FILE_NAME));
                ArrayList<TimeChartData> dataList = (ArrayList<TimeChartData>) in.readObject();
                TimeCategory preState = TimeCategory.getTimCategory(preference.getInt(PreferenceData.PRE_STATE, 0), TimeCategory.UNKNOWN);
                int deltaTime = (int)(System.currentTimeMillis() - preference.getLong(PreferenceData.PRE_TIME,0));
                dataList.add(new TimeChartData(preState,deltaTime));
                circleTimeChart.setDrawingDatas(dataList);
                in.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
