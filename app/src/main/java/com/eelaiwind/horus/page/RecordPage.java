package com.eelaiwind.horus.page;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.eelaiwind.horus.R;
import com.eelaiwind.horus.timeChart.LinearTimeChart;
import com.eelaiwind.horus.timeChart.TimeCategory;
import com.eelaiwind.horus.timeChart.TimeChartData;

import java.util.ArrayList;
import java.util.Objects;


public class RecordPage extends Activity{
    private final TimeCategory[] testTimeCategory = {TimeCategory.BUSY,TimeCategory.FREE,TimeCategory.OFF,TimeCategory.FREE,TimeCategory.OFF,TimeCategory.BUSY,TimeCategory.FREE,TimeCategory.BUSY,TimeCategory.OFF,TimeCategory.FREE,TimeCategory.BUSY,TimeCategory.OFF};
    private final int[] testTimeInterval = {135,65,70,205,10,90,85,60,200,40,70,60};

    private TimeChartData[] timeChartDatas = new TimeChartData[0];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_page);

        try {
            timeChartDatas = TimeChartData.converTimeChartData(testTimeCategory,testTimeInterval);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((LinearTimeChart) findViewById(R.id.linear_time_chart)).setDrawingDatas(timeChartDatas);
        EditText editText = (EditText) findViewById(R.id.show_message);
        editText.setText("");
        for (TimeChartData data : timeChartDatas){
            editText.append(data.getTimeCategory().getName()+' '+data.getMinute()+'\n');
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
