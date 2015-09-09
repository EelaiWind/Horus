package com.eelaiwind.horus.page;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.eelaiwind.horus.PreferenceData;
import com.eelaiwind.horus.R;

public class SettingPage extends Activity {

    private TextView sleepTime, wakeTime;
    private SharedPreferences preferences;
    private int sleepHour, sleepMinute, wakeHour, wakeMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);

        sleepTime = (TextView) findViewById(R.id.txt_sleep_time);
        wakeTime = (TextView) findViewById(R.id.txt_wake_time);

        preferences = getSharedPreferences(PreferenceData.FILE_NAME,MODE_PRIVATE);

        sleepHour = preferences.getInt(PreferenceData.SLEEP_TIME_HOUR,23);
        sleepMinute = preferences.getInt(PreferenceData.SLEEP_TIME_MINUTE,0);
        wakeHour = preferences.getInt(PreferenceData.WAKE_TIME_HOUR,7);
        wakeMinute = preferences.getInt(PreferenceData.WAKE_TIME_MINUTE,0);
        sleepTime.setText(String.format("%02d:%02d",sleepHour,sleepMinute));
        wakeTime.setText(String.format("%02d:%02d",wakeHour,wakeMinute));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting_page, menu);
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

    public void setTime(final View v){

        TimePickerDialog.OnTimeSetListener onTimeSetListener;

        if (v.getId() == R.id.txt_sleep_time) {
            onTimeSetListener = new TimePickerDialog.OnTimeSetListener(){

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    sleepHour = hourOfDay;
                    sleepMinute = minute;
                    sleepTime.setText(String.format("%02d:%02d",hourOfDay,minute));
                }
            };
            new TimePickerDialog(this,onTimeSetListener, sleepHour, sleepMinute, true).show();
        }
        else if (v.getId() == R.id.txt_wake_time){
            onTimeSetListener = new TimePickerDialog.OnTimeSetListener(){

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    wakeHour = hourOfDay;
                    wakeMinute = minute;
                    wakeTime.setText(String.format("%02d:%02d",hourOfDay,minute));
                }
            };
            new TimePickerDialog(this,onTimeSetListener, wakeHour, wakeMinute, true).show();
        }
    }

    public void saveSetting(View v){
        PreferenceData.UpdateSleepAlarmTime(preferences,sleepHour,sleepMinute,wakeHour,wakeMinute);
        finish();
    }
}
