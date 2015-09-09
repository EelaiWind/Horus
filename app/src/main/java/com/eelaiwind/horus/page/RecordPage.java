package com.eelaiwind.horus.page;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.eelaiwind.horus.notification.RunningNotification;
import com.eelaiwind.horus.PreferenceData;
import com.eelaiwind.horus.R;
import com.eelaiwind.horus.customView.LinearTimeChart;
import com.eelaiwind.horus.timeChart.TimeCategory;
import com.eelaiwind.horus.timeChart.TimeChartData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class RecordPage extends Activity{

    private long lastDataTime;
    private SharedPreferences preferences;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_page);

        preferences = getSharedPreferences(PreferenceData.FILE_NAME,MODE_PRIVATE);
        editText = (EditText) findViewById(R.id.show_message);
        lastDataTime = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.contains(PreferenceData.LAST_DATA_TIME)){

            if (lastDataTime == preferences.getLong(PreferenceData.LAST_DATA_TIME,0))
                return;

            lastDataTime = preferences.getLong(PreferenceData.LAST_DATA_TIME,0);
            try {
                ObjectInputStream in = new ObjectInputStream(openFileInput(RunningNotification.LAST_DATA_FILE_NAME));
                ArrayList<TimeChartData> timeChartDatas = (ArrayList<TimeChartData>) in.readObject();
                in.close();

                ((LinearTimeChart) findViewById(R.id.linear_time_chart)).setDrawingDatas(timeChartDatas);
                StringBuilder sb = new StringBuilder();
                for (TimeChartData data : timeChartDatas){
                    sb.append(data.getTimeCategory().getName()).append(' ').append(data.getMillisecond()).append(" \n");
                }
                editText.setText(sb.toString());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        else{
            TimeChartData[] data = new TimeChartData[1];
            data[0] = new TimeChartData(TimeCategory.OFF,1440);
            ((LinearTimeChart) findViewById(R.id.linear_time_chart)).setDrawingDatas(data);
            editText.setText("Last time chart data doesn't exist");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record_page, menu);
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
