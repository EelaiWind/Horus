package com.eelaiwind.horus.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.eelaiwind.horus.R;
import com.eelaiwind.horus.timeChart.CircleTimeChart;
import com.eelaiwind.horus.timeChart.TimeCategory;
import com.eelaiwind.horus.timeChart.TimeChartData;

import java.util.ArrayList;
import java.util.Arrays;


public class HomePage extends Activity {
    private final TimeCategory[] testTimeCategory = {TimeCategory.BUSY,TimeCategory.FREE,TimeCategory.OFF,TimeCategory.FREE,TimeCategory.OFF,TimeCategory.BUSY,TimeCategory.FREE,TimeCategory.BUSY,TimeCategory.OFF,TimeCategory.FREE,TimeCategory.BUSY,TimeCategory.OFF};
    private final int[] testTimeInterval = {135,65,70,205,10,90,85,60,200,40,70,60};
    private TimeChartData[] timeChartData;

    public static final String EXTRA_CHART_DATA = "com.eelaiwind.horus.CHART_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        try {
            timeChartData = TimeChartData.converTimeChartData(testTimeCategory,testTimeInterval);
            ((CircleTimeChart) findViewById(R.id.circle_time_chart)).setDrawingDatas(timeChartData);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        switch (v.getId()){
            case R.id.btn_record:
                Intent intent = new Intent(this,RecordPage.class);
                intent.putExtra("LENGTH",timeChartData.length);
                intent.putParcelableArrayListExtra(EXTRA_CHART_DATA, new ArrayList<>(Arrays.asList(timeChartData)));
                startActivity(intent);
                break;
        }
    }
}
