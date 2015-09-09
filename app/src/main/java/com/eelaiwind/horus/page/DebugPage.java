package com.eelaiwind.horus.page;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.eelaiwind.horus.notification.RunningNotification;
import com.eelaiwind.horus.PreferenceData;
import com.eelaiwind.horus.R;
import com.eelaiwind.horus.timeChart.TimeCategory;
import com.eelaiwind.horus.timeChart.TimeChartData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DebugPage extends Activity {

    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_page);
        preferences = getSharedPreferences(PreferenceData.FILE_NAME, MODE_PRIVATE);

        debug();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
       }

    private void debug() {
        StringBuilder sb = new StringBuilder();
        DateFormat format = SimpleDateFormat.getDateTimeInstance();

        if (preferences.contains(PreferenceData.TOTAL_SWITCH)) {
            sb.append("total switch : ").append(preferences.getBoolean(PreferenceData.TOTAL_SWITCH, false)).append('\n')
                    .append("today time : ").append(format.format(new Date(preferences.getLong(PreferenceData.TODAY_TIME, 0)))).append('\n')
                    .append("pre time : ").append(format.format(new Date(preferences.getLong(PreferenceData.PRE_TIME, 0)))).append('\n');
            try {
                sb.append("pre state : ").append(TimeCategory.getTimCategory(preferences.getInt(PreferenceData.PRE_STATE, 0),TimeCategory.UNKNOWN).getName()).append('\n');
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            sb.append("preference doesn't exist\n");
        }
        if (preferences.contains(PreferenceData.LAST_DATA_TIME)) {
            sb.append("Last data time : ").append(format.format(new Date(preferences.getLong(PreferenceData.LAST_DATA_TIME, 0)))).append('\n');
        }

        try {
            ObjectInputStream in = new ObjectInputStream(openFileInput(RunningNotification.DATA_FILE_NAME));
            ArrayList<TimeChartData> dataList = (ArrayList<TimeChartData>) in.readObject();
            for (TimeChartData item : dataList)
            sb.append(item.getTimeCategory().getName()).append(" ").append(item.getMillisecond()).append('\n');
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Log.d("EELAI_WIND",sb.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debug_page, menu);
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
}
