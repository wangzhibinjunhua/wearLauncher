package com.sczn.wearlauncher.activity.card;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.view.card.ChartView;

import android.app.Activity;
import android.os.Bundle;


public class PressureActivity extends Activity {

    private ChartView mDataLineChartView;

    private final static float[] defValues = {1158.0f, 253.7f, 1382.0f, 786.0f, 899.5f, 1455.0f, 783.0f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_air_pressure);
        mDataLineChartView = (ChartView) findViewById(R.id.chart_line);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        //获取数据后，绘制图形
        mDataLineChartView.setValues(defValues);
    }
}
